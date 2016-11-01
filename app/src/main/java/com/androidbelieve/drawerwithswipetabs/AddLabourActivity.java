package com.androidbelieve.drawerwithswipetabs;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.androidbelieve.drawerwithswipetabs.Model.AddLabourDetailModelClass;
import com.androidbelieve.drawerwithswipetabs.Model.ProjectData;
import com.androidbelieve.drawerwithswipetabs.Model.VendorData;
import com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter.cAddLabourDetailAdapter;
import com.androidbelieve.drawerwithswipetabs.VolleyClass.AppHelper;
import com.androidbelieve.drawerwithswipetabs.VolleyClass.VolleyMultipartRequest;
import com.androidbelieve.drawerwithswipetabs.VolleyClass.VolleySingleton;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import urllink.Url;

public class AddLabourActivity extends AppCompatActivity {
    int contractSpinnerPosition;
    private static final int CAMERA_REQUEST = 1888;
    Spinner mSpinnerProjectNumber, mSpinnerContractNumber, mSpinnerContractorNumber;
    TextView cAddDetail;
    CustomDialogClass customDialogClass = null;
    Spinner mSpinnerDescription;
    EditText NoOfLabour, Remark;
    public Button Cancle, Add;
    ImageView mPhotoImageUrl, editPickDate, VImgAttachment;
    RecyclerView recyclerView;
    cAddLabourDetailAdapter cAddLabourDetailAdapterr;
    ArrayList<AddLabourDetailModelClass> cAddDetailArray = new ArrayList<>();
    boolean isClickFormList = false;
    int currListIndex;
    private ProgressDialog pDialog;
    ApiCall mApiCall;
    /*ArrayList<String> project_id = new ArrayList<>();
    ArrayList<String> project_number = new ArrayList<>();
    ArrayList<String> vendor_id = new ArrayList<>();
    ArrayList<String> vendor_number = new ArrayList<>();
    ArrayList<String> contract_id = new ArrayList<>();
    ArrayList<String> contract_number = new ArrayList<>();*/

    //int description_Num_Index=0;
    String projectValue = "", vendorValue = "", contractValue = "", descValue = "";
    Button cSave, cClose;
    /*ArrayList<String> Description_Id = new ArrayList<>();
    ArrayList<String> Description_name = new ArrayList<>();*/
    Map descriptionMap = new HashMap();
    EditText mEditRemark, edtReported_Date, edtReported_By;
    CheckBox estimationFlag;
    ImageView mImageTempFull;
    TextView messageText = null;
    boolean isImageAdded = false;
    NumberFormat formatter = new DecimalFormat("#0.00");
    // Custom_Spinner_Adapter custom_spinner_adapter;
    String mCurrentPhotoPath;
    //Point size = new Point();
    // Display display = getWindowManager().getDefaultDisplay();
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_labour);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView ActionBarTitle = (TextView) findViewById(R.id.actionbartitle);
        ActionBarTitle.setText("Labour (New)");


        mApiCall = new ApiCall(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        mSpinnerProjectNumber = (Spinner) findViewById(R.id.mSpinnerProjectNumber);
        mSpinnerContractorNumber = (Spinner) findViewById(R.id.mSpinnerContracterNumber);
        mSpinnerContractNumber = (Spinner) findViewById(R.id.mSpinnerContractNumber);
        // mSpinnerProjectNumber.setLayoutParams(params);
        mSpinnerProjectNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner", "Spinner");
                projectValue = mSpinnerProjectNumber.getSelectedItem().toString();
                Log.v("Result", " projectValue " + projectValue + "pstion" + mSpinnerProjectNumber.getSelectedItemPosition());
                vendorValue = "";
                contractValue = "";
                //  if(mSpinnerProjectNumber.getSelectedItemPosition()!=0){
                if (!cAddDetailArray.isEmpty() || cAddDetailArray != null || cAddDetailArray.size() > 0) {
                    recyclerView.removeAllViews();
                    cAddDetailArray.clear();
                    //Toast.makeText(getApplicationContext(), "" + cAddDetailArray.size(), Toast.LENGTH_LONG).show();
                    recyclerView.setAdapter(new cAddLabourDetailAdapter(cAddDetailArray));
                }
                getVendorListing(projectValue);
                //   }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerContractorNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner", "Spinner");
                vendorValue = mSpinnerContractorNumber.getSelectedItem().toString();
                contractValue = "";
                if (!cAddDetailArray.isEmpty() || cAddDetailArray != null || cAddDetailArray.size() > 0) {
                    recyclerView.removeAllViews();
                    cAddDetailArray.clear();
                    //Toast.makeText(getApplicationContext(), "" + cAddDetailArray.size(), Toast.LENGTH_LONG).show();
                    recyclerView.setAdapter(new cAddLabourDetailAdapter(cAddDetailArray));
                }
                getContractListing(projectValue, vendorValue);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerContractNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner", "Spinner");
                contractValue = mSpinnerContractNumber.getSelectedItem().toString();
                if (mSpinnerContractNumber.getSelectedItemPosition() != 0) {
                    ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(projectValue);
                    Map vendor = pd.getVendor();
                    VendorData vd = (VendorData) vendor.get(vendorValue);
                    Map contractInfo = vd.getContract();
                    if (!cAddDetailArray.isEmpty() || cAddDetailArray != null || cAddDetailArray.size() > 0) {
                        recyclerView.removeAllViews();
                        cAddDetailArray.clear();
                        //Toast.makeText(getApplicationContext(), "" + cAddDetailArray.size(), Toast.LENGTH_LONG).show();
                        recyclerView.setAdapter(new cAddLabourDetailAdapter(cAddDetailArray));
                    }

                    new mGeDescriptionsListingTask().execute(pd.getPrjId(), contractInfo.get(contractValue).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cSave = (Button) findViewById(R.id.mBtnSave);
        cSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cAddDetailArray.size() > 0 &&
                        ApiCall.sub_id.length() > 0 &&
                        projectValue.length() > 0 &&
                        vendorValue.length() > 0 &&
                        edtReported_Date.getText().toString().length() > 0 &&
                        mEditRemark.getText().toString().length() > 0 &&
                        ApiCall.user_type.length() > 0) {
                    pDialog.show();
                    doGetNewLabourDetail();
                } else {
                    //Toast.makeText(AddLabourActivity.this, "At least one product details is required", Toast.LENGTH_LONG).show();
                    Toast.makeText(AddLabourActivity.this, "Please provide complete data", Toast.LENGTH_LONG).show();
                }
            }
        });

        cClose = (Button) findViewById(R.id.mBtnClose);
        cClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getProjectListing();


        cAddDetail = (TextView) findViewById(R.id.txtaddDetail);
        cAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickFormList = false;
                customDialogClass = new CustomDialogClass(AddLabourActivity.this);
                // customDialogClass.getWindow().setLayout(getWidth(getApplicationContext()), ViewGroup.LayoutParams.WRAP_CONTENT);
                customDialogClass.show();

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddLabourActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cAddLabourDetailAdapterr = new cAddLabourDetailAdapter(cAddDetailArray);
        recyclerView.setAdapter(cAddLabourDetailAdapterr);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(AddLabourActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                isClickFormList = true;
                currListIndex = position;
                customDialogClass = new CustomDialogClass(AddLabourActivity.this);
                customDialogClass.show();

            }
        }));

        mPhotoImageUrl = (ImageView) findViewById(R.id.mImgAttachment);
        mImageTempFull = (ImageView) findViewById(R.id.mImageTempFull);

        mPhotoImageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                // dispatchTakePictureIntent();
            }
        });


        mEditRemark = (EditText) findViewById(R.id.mEditRemark);
        estimationFlag = (CheckBox) findViewById(R.id.mChkEstimationFlag);
        edtReported_By = (EditText) findViewById(R.id.edtreportedby);
        edtReported_Date = (EditText) findViewById(R.id.edtReportedate);
        edtReported_Date.setText(getCurrentDate());
        editPickDate = (ImageView) findViewById(R.id.edtPicdate);
        mEditRemark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {

                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        new mGetReported_ByTask().execute();

        //setupUI(AddLabourActivity.this);

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddLabourActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            userChoosenTask = "Take Photo";
                            int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                            // Toast.makeText(getApplicationContext(), "Res  = "+result+" ... Manager  = "+PackageManager.PERMISSION_GRANTED, Toast.LENGTH_LONG).show();
                            if (getApplicationContext().checkCallingOrSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddLabourActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        Utility.MY_PERMISSIONS_REQUEST_CAMERA);
                            } else if (ContextCompat.checkSelfPermission(AddLabourActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddLabourActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            } else {
                                captureImaege();
                            }
                            //boolean resultCamera = Utility.checkPermissionCamera(AddLabourActivity.this);
                        } else if (items[item].equals("Choose from Library")) {
                            // boolean result = Utility.checkPermissionReadWrite(AddLabourActivity.this);
                            userChoosenTask = "Choose from Library";
                            if (ContextCompat.checkSelfPermission(AddLabourActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AddLabourActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            } else {
                                galleryIntent();
                            }


                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();

                        }
                    }
                }

        );
        builder.show();
    }

    private void captureImaege() {

        try {
            createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //      cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCurrentPhotoPath)));
        startActivityForResult(cameraIntent, REQUEST_CAMERA);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cmsfit.labour",
                        photoFile);

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                //onSelectFromGalleryResult(data);
                isImageAdded = true;
                mPhotoImageUrl.setImageURI(Crop.getOutput(data));
                beginCrop(data.getData());
            } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                isImageAdded = true;
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + ".png";
                mPhotoImageUrl.setImageURI(Crop.getOutput(data));
                beginCrop(data.getData());

            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            mPhotoImageUrl.setImageURI(Crop.getOutput(result));
            //Toast.makeText(this, Crop.getOutput(result).toString() + ".....", Toast.LENGTH_SHORT).show();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        captureImaege();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {

                }
                break;

            case Utility.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        captureImaege();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {

                }
                break;
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File direct = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!direct.exists()) {
            File wallpaperDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            wallpaperDirectory.mkdirs();
        }

        File file = new File(direct, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {

            FileOutputStream out = new FileOutputStream(file);
            int count = imageToSave.getByteCount();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageToSave.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            out.write(bitmapdata);
            out.flush();
            out.close();
            // Toast.makeText(getApplicationContext(), "PATH = " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(data.getData().getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                mPhotoImageUrl.setImageBitmap(bmRotated);
                mImageTempFull.setImageBitmap(bmRotated);
                isImageAdded = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
//        mCurrentPhotoPath = storageDir.getAbsolutePath();
        //Toast.makeText(getApplicationContext(), "Image Path = " + mCurrentPhotoPath, Toast.LENGTH_LONG).show();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        setPic();
    }

    private void setPic() {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        // Get the dimensions of the View
        int targetW = mPhotoImageUrl.getWidth();
        int targetH = mPhotoImageUrl.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Bitmap bmRotated = rotateBitmap(bitmap, orientation);
        mPhotoImageUrl.setImageBitmap(bmRotated);
        isImageAdded = true;
        setPicFull();
    }

    private void setPicFull() {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        // Get the dimensions of the View
        int targetW = 300;
        int targetH = 450;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        Bitmap bmRotated = rotateBitmap(bitmap, orientation);
        mImageTempFull.setImageBitmap(bmRotated);
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void showDatePickerDialog(View view) {
        showDialog(100);

    }


    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(this, myDateListener, year, month, day);


    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        edtReported_Date.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }


    public class CustomDialogClass extends Dialog implements
            View.OnClickListener {


        public Activity c;
        ArrayAdapter<String> adapterDescriptionNumber;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.customdialog);
            Cancle = (Button) findViewById(R.id.btncancle);
            Add = (Button) findViewById(R.id.btnAdd);
            int widthScreen = getWidth(getApplicationContext());
            LayoutParams params = getWindow().getAttributes();
            params.height = LayoutParams.WRAP_CONTENT;
            params.width = widthScreen;
            getWindow().setAttributes((WindowManager.LayoutParams) params);
            NoOfLabour = (EditText) findViewById(R.id.edtlabour);
            Remark = (EditText) findViewById(R.id.edtremark);
             NoOfLabour.setFilters(new InputFilter[]{ new MinMaxInputFilter(Double.parseDouble("1.00"), Double.parseDouble("999.00"))});
            mSpinnerDescription = (Spinner) findViewById(R.id.mSpinnerDescription);
            mSpinnerDescription.setDropDownWidth(widthScreen);
            ArrayList descriptionArray = new ArrayList(descriptionMap.keySet());
            descriptionArray.add(0, "Select Description");
            adapterDescriptionNumber = new ArrayAdapter<String>(AddLabourActivity.this, android.R.layout.simple_spinner_item, descriptionArray); //selected item will look like a spinner set from XML
            adapterDescriptionNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerDescription.setAdapter(adapterDescriptionNumber);


            mSpinnerDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Spinner", "Spinner");
                    //description_Num_Index = position;
                    descValue = mSpinnerDescription.getSelectedItem().toString();
                    NoOfLabour.setText("");
                    Remark.setText("");
                    if (position > 0 && cAddDetailArray.size() > 0) {
                        if (position == adapterDescriptionNumber.getPosition(cAddDetailArray.get(currListIndex).getcDescription().toString())) {
                            if (cAddDetailArray.get(currListIndex).getcNoOfLabour().toString() != null) {
                                NoOfLabour.setText("" + cAddDetailArray.get(currListIndex).getcNoOfLabour().toString());
                            }
                            if (cAddDetailArray.get(currListIndex).getcRemark().toString() != null) {
                                Remark.setText("" + cAddDetailArray.get(currListIndex).getcRemark().toString());
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            NoOfLabour = (EditText) findViewById(R.id.edtlabour);
            NoOfLabour.setFilters(new InputFilter[]{ new MinMaxInputFilter(Double.parseDouble("1.00"), Double.parseDouble("999.00"))});
            Remark = (EditText) findViewById(R.id.edtremark);

            if (isClickFormList) {
                Cancle.setText("Delete");
                Add.setText("Update");
                NoOfLabour.setText(cAddDetailArray.get(currListIndex).getcNoOfLabour());
                Remark.setText(cAddDetailArray.get(currListIndex).getcRemark());
                int position = adapterDescriptionNumber.getPosition(cAddDetailArray.get(currListIndex).getcDescription());
                mSpinnerDescription.setSelection(position);

            } else {
                NoOfLabour.setText("");
                Remark.setText("");
                mSpinnerDescription.setSelection(0);
            }

            Cancle.setOnClickListener(this);
            Add.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btncancle:
                    if (isClickFormList) {
                        cAddDetailArray.remove(currListIndex);
                        cAddLabourDetailAdapterr.notifyDataSetChanged();
                    }

                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                    dismiss();
                    break;
                case R.id.btnAdd:
                    if (isClickFormList) {


                        if (NoOfLabour.getText().toString().equals("") || NoOfLabour.getText().length() > 6) {
                            NoOfLabour.setError(getResources().getString(R.string.Labour));
                        } else if (mSpinnerDescription.getSelectedItemPosition() == 0) {
                            Log.v("description", "description else if");
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) mSpinnerDescription.getAdapter();
                            TextView selectedTextView = (TextView) mSpinnerDescription.getSelectedView();
                            selectedTextView.setError("Please select a value");


                        } else {
                            if (Double.parseDouble(NoOfLabour.getText().toString()) > 0 && Double.parseDouble(NoOfLabour.getText().toString()) < 1000) {
                                cAddDetailArray.remove(currListIndex);
                                AddLabourDetail();
                                dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Count should be greater than 0 and less than 1000", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        if (NoOfLabour.getText().toString().equals("") || NoOfLabour.getText().length() > 6) {
                            Log.v("labour", "labour if");
                            NoOfLabour.setError(getResources().getString(R.string.Labour));

                        } else if (mSpinnerDescription.getSelectedItemPosition() == 0) {
                            Log.v("description", "description else if");
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) mSpinnerDescription.getAdapter();
                            TextView selectedTextView = (TextView) mSpinnerDescription.getSelectedView();
                            selectedTextView.setError("Please select a value");


                        } else {
                            if (Double.parseDouble(NoOfLabour.getText().toString()) > 0 && Double.parseDouble(NoOfLabour.getText().toString()) < 1000) {
                                Log.v("labour", "labour else length:" + NoOfLabour.getText().length());
                                AddLabourDetail();
                                dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Count should be greater than 0 and less than 1000", Toast.LENGTH_LONG).show();

                            }
                        }

                    }

                    break;
                default:
                    break;
            }
            // dismiss();
        }
    }

    public void AddLabourDetail() {
        if (Double.parseDouble(NoOfLabour.getText().toString()) > 0 && Double.parseDouble(NoOfLabour.getText().toString()) < 1000) {

            String nooflabour = formatter.format(Double.parseDouble(NoOfLabour.getText().toString()));
            //String nooflabour = String.valueOf(Double.parseDouble(NoOfLabour.getText().toString()));
            String remark = Remark.getText().toString();

            if (descValue != "") {
                cAddDetailArray.add(new AddLabourDetailModelClass(descValue, nooflabour, remark, descriptionMap.get(descValue).toString(), "" + (cAddDetailArray.size() + 1)));
            } else {
                cAddDetailArray.add(new AddLabourDetailModelClass("", nooflabour, remark, "", "" + (cAddDetailArray.size() + 1)));
            }
            cAddLabourDetailAdapterr.notifyDataSetChanged();

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Count should be greater than 0 and less than 1000", Toast.LENGTH_LONG).show();
        }

    }

    // ========================= API CALL ===================================
    private void getProjectListing() {
        pDialog.hide();
        if (FetchLabourInfoService.projectInfo != null && !FetchLabourInfoService.projectInfo.isEmpty()) {
            ArrayList projectNoArray = new ArrayList(FetchLabourInfoService.projectInfo.keySet());
            projectNoArray.add(0, "Select Project");
            ArrayAdapter<String> adapterProjectNumber = new ArrayAdapter<String>(AddLabourActivity.this, R.layout.spinner_item, projectNoArray); //selected item will look like a spinner set from XML
            adapterProjectNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerProjectNumber.setAdapter(adapterProjectNumber);
            mSpinnerProjectNumber.setDropDownWidth(getWidth(getApplicationContext()));
            /*Custom_Spinner_Adapter custom_spinner_adapter=new Custom_Spinner_Adapter(AddLabourActivity.this,projectNoArray);
                        mSpinnerProjectNumber.setAdapter(custom_spinner_adapter);*/
            if (mSpinnerProjectNumber.getSelectedItemPosition() != 0) {
                if (!projectValue.isEmpty()) {
                    Log.v("Result", " projectValue " + projectValue + "pstin  " + mSpinnerProjectNumber.getSelectedItemPosition());

                    getVendorListing(projectValue);
                } else {
                    Log.v("Result", " FetchLabourInfoService.projectInfo.keySet().toArray()[0].toString() " + FetchLabourInfoService.projectInfo.keySet().toArray()[0].toString() + "pstin  " + mSpinnerProjectNumber.getSelectedItemPosition());
                    getVendorListing(FetchLabourInfoService.projectInfo.keySet().toArray()[0].toString());
                }
            } else {
                getVendorListing("");
            }


        } else {
            Log.v("Result", "Result Project listing empty ");
        }
    }

    private void getVendorListing(String prjNo) {
        pDialog.hide();
        if (!FetchLabourInfoService.projectInfo.isEmpty()) {
            if (mSpinnerProjectNumber.getSelectedItemPosition() != 0) {
                ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(prjNo);
                Map vendor = pd.getVendor();
                ArrayList vendorList = new ArrayList(vendor.keySet());
                Collections.sort(vendorList, String.CASE_INSENSITIVE_ORDER);
                // Collections.reverse(vendorList);
                vendorList.add(0, "Select Vendor");
                ArrayAdapter<String> adapterVendorNumber = new ArrayAdapter<String>(AddLabourActivity.this, R.layout.spinner_item, vendorList); //selected item will look like a spinner set from XML
                adapterVendorNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
                //display.getSize(size);
                /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        size.x , size.y / 2);*/
                mSpinnerContractorNumber.setDropDownWidth(getWidth(getApplicationContext()));
                mSpinnerContractorNumber.setAdapter(adapterVendorNumber);
                if (mSpinnerContractorNumber.getSelectedItemPosition() != 0) {
                    if (!projectValue.isEmpty() && !vendorValue.isEmpty()) {
                        Log.v("Result", "vendor.get(vendorValue).toString()" + vendorValue);
                        getContractListing(prjNo, vendor.get(vendorValue).toString());
                    } else {
                        Log.v("Result", " pd.getPrjId(),vendor.keySet().toArray()[0].toString() " + vendor.keySet().toArray()[0].toString());

                        getContractListing(prjNo, vendor.keySet().toArray()[0].toString());
                    }
                } else {
                    getContractListing("", "");
                }

            } else {
                ArrayList vendorList = new ArrayList();
                vendorList.add(0, "Select Vendor");
                ArrayAdapter<String> adapterVendorNumber = new ArrayAdapter<String>(AddLabourActivity.this, R.layout.spinner_item, vendorList); //selected item will look like a spinner set from XML
                adapterVendorNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);

                mSpinnerContractorNumber.setDropDownWidth(getWidth(getApplicationContext()));

                mSpinnerContractorNumber.setAdapter(adapterVendorNumber);
                getContractListing("", "");

            }


        } else {
            Log.v("Result", "Result Project listing empty ");
        }
    }

    private void getContractListing(String prjNo, String venNo) {
        pDialog.hide();
        if (!FetchLabourInfoService.projectInfo.isEmpty()) {
            if (mSpinnerProjectNumber.getSelectedItemPosition() != 0 && mSpinnerContractorNumber.getSelectedItemPosition() != 0) {
                ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(prjNo);
                Map vendor = pd.getVendor();
                VendorData vd = (VendorData) vendor.get(venNo);
                ArrayList contractList = new ArrayList(vd.getContract().keySet());
                Collections.sort(contractList, String.CASE_INSENSITIVE_ORDER);
                // Collections.reverse(contractList);
                contractList.add(0, "Select Contract");
                ArrayAdapter<String> adapterContractNumber = new ArrayAdapter<String>(AddLabourActivity.this, R.layout.spinner_item, contractList); //selected item will look like a spinner set from XML
                adapterContractNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
                /*Custom_Spinner_Adapter custom_spinner_adapter=new Custom_Spinner_Adapter(AddLabourActivity.this,contractList);
                        mSpinnerContractorNumber.setAdapter(custom_spinner_adapter);*/


                Map contractInfo = vd.getContract();

                mSpinnerContractNumber.setAdapter(adapterContractNumber);
                mSpinnerContractNumber.setDropDownWidth(getWidth(getApplicationContext()));
                //mSpinnerContractNumber.setAdapter(custom_spinner_adapter);
                if (mSpinnerContractNumber.getSelectedItemPosition() != 0) {
                    if (!projectValue.isEmpty() && !contractValue.isEmpty()) {
                        Log.v("Result", " contractInfo.get(contractValue).toString() " + contractInfo.get(contractValue).toString());
                        new mGeDescriptionsListingTask().execute(pd.getPrjId(), contractInfo.get(contractValue).toString());
                    } else {
                        Log.v("Result", " vd.getContract().keySet().toArray()[0].toString() " + vd.getContract().keySet().toArray()[0].toString());

                        new mGeDescriptionsListingTask().execute(pd.getPrjId(), vd.getContract().values().toArray()[0].toString());
                    }
                }


            } else {
                ArrayList contractList = new ArrayList();
                contractList.add(0, "Select Contract");
                ArrayAdapter<String> adapterContractNumber = new ArrayAdapter<String>(AddLabourActivity.this, R.layout.spinner_item, contractList); //selected item will look like a spinner set from XML
                adapterContractNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerContractNumber.setDropDownWidth(getWidth(getApplicationContext()));
                mSpinnerContractNumber.setAdapter(adapterContractNumber);
            }


        } else {
            Log.v("Result", "Result Project listing empty ");
        }
    }


// ========================= API CALL ===================================

    private class mGeDescriptionsListingTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = mApiCall.doGetDescriptionstListing(params[0], params[1]);
            return result;
        }

        protected void onPostExecute(String result) {
            pDialog.hide();
            if (result != null) {

                Log.v("Result", "Result DEst: " + result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    if (jsonLabourData.getString("status").equals("1")) {
                        JSONArray jsonLabourArray = jsonLabourData.getJSONArray("result");
                        // if (jsonLabourArray.length()>0){
                        descriptionMap.clear();
                        descValue = "";
                        // }
                        for (int i = 0; i < jsonLabourArray.length(); i++) {
                            JSONObject jsonLabourObject = (JSONObject) jsonLabourArray.get(i);
                            descriptionMap.put(jsonLabourObject.getString("descr"), jsonLabourObject.getString("cor_hdr_id") + ":"
                                    + jsonLabourObject.getString("lvl") + ":" + jsonLabourObject.getString("rate"));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //=============Create New Labour API With Image =========================

    public String doGetNewLabourDetail() {

        String url = Url.server + "?action=add-labour";

        Log.v("test", "VOLLEY url :" + url);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                pDialog.hide();
                Log.d("POST1", "Post VollyResponce");

                String resultResponse = new String(response.data);
                Log.v("test", "VOLLEY resultResponse :" + resultResponse);

               /* LabourActivity.activity.finish();
                Intent intent = new Intent(AddLabourActivity.this,LabourActivity.class);
                startActivity(intent);
                finish();
*/
                try {
                    JSONObject responsejsonObject = new JSONObject(resultResponse);
                    if (responsejsonObject.getString("status").equals("1")) {
                        //  Toast.makeText(getApplicationContext(), responsejsonObject.getString("0"), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                AddLabourActivity.this);

                        // set title
                        //   alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));


                        String MsgStr = responsejsonObject.getString("0");
                        //  MsgStr = MsgStr.replace("")
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Labour Record\n" + responsejsonObject.getString("0"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        LabourActivity.activity.finish();
                                        Intent intent = new Intent(AddLabourActivity.this, LabourActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.show();
                        messageText = (TextView) alertDialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);

                        // show it
                        alertDialog.show();
                    } else {

                        Toast.makeText(AddLabourActivity.this, "Labour not created", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                error.printStackTrace();
                Log.v("test", "VOLLEY Error :" + error.getMessage());
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    Toast.makeText(AddLabourActivity.this, "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = error.getClass().getSimpleName();
                    if (!TextUtils.isEmpty(errorMessage)) {
                        Toast.makeText(AddLabourActivity.this, "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //sub_id, user_id, prj_id, con_id, rpt_date, hdr_remark, user_type
                params.put("sub_id", ApiCall.sub_id);
                params.put("user_id", ApiCall.user_id);
                ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(projectValue);
                Map vendor = pd.getVendor();
                VendorData vd = (VendorData) vendor.get(vendorValue);
                params.put("prj_id", pd.getPrjId());
                params.put("con_id", vd.getContract().get(contractValue));
                params.put("rpt_date", edtReported_Date.getText().toString());
                params.put("hdr_remark", mEditRemark.getText().toString());
                params.put("user_type", ApiCall.user_type);
                if (estimationFlag.isChecked()) {
                    params.put("esti_flag", "Y");
                } else
                    params.put("esti_flag", "N");
                // params.put("inv_doc","FILEOBJECT");


                for (int i = 0; i < cAddDetailArray.size(); i++) {
                    // String skuStr = "product_post[product_post_m_skus_attributes[" + i + "][sku_code]";
                    // Map<String, String> paramsDetail = new HashMap<>();
                    try {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("company_cnt", cAddDetailArray.get(i).getcNoOfLabour());
                        if (descValue != "") {
                            jsonObject.put("hdr_id", cAddDetailArray.get(i).getcDescriptionId());
                            jsonObject.put("rate", cAddDetailArray.get(i).getRate());
                            jsonObject.put("floor", cAddDetailArray.get(i).getLvl());
                        }

                        jsonObject.put("hdr_seqno", "" + (i + 1));
                        jsonObject.put("remark", cAddDetailArray.get(i).getcRemark());


                        params.put("labours[" + i + "]", jsonObject.toString());
                    } catch (Exception e) {

                    }

                }


                Log.v("test", "VOLLEY PARAMS : " + params.toString());

                Log.d("POST1", "Post VollyMap");

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (isImageAdded == true) {
                    params.put("inv_doc", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(AddLabourActivity.this, mPhotoImageUrl.getDrawable()), "image/jpeg"));
                    Log.d("POST1", "Post VollyMapImage");
                }
                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(AddLabourActivity.this).addToRequestQueue(multipartRequest);
        return "";
    }

    private class mGetReported_ByTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = mApiCall.doGetReportedName();
            return result;
        }

        protected void onPostExecute(String result) {
            pDialog.hide();
            if (result != null) {

                Log.v("Result", "Result Reported : " + result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    if (jsonLabourData.getString("status").equals("1")) {
                        JSONArray jsonArray = jsonLabourData.getJSONArray("result");
                        //  if (jsonLabourArray.length()>0){
                        JSONObject reported_By = jsonArray.getJSONObject(0);
                        edtReported_By.setText(reported_By.getString("usr_usernm"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String getCurrentDate() {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        return dateString;
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    public static int getWidth(Context mContext) {
        int width = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();  // Deprecated
        }
        return width;
    }

    public static int getHeight(Context mContext) {
        int height = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();  // Deprecated
        }
        return height;
    }


}
