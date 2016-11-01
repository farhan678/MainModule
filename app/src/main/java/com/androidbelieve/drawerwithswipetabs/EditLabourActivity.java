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
import android.view.ViewGroup;
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
import android.widget.TableLayout;
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
import com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter.Custom_Spinner_Adapter;
import com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter.cAddLabourDetailAdapter;
import com.androidbelieve.drawerwithswipetabs.VolleyClass.AppHelper;
import com.androidbelieve.drawerwithswipetabs.VolleyClass.VolleyMultipartRequest;
import com.androidbelieve.drawerwithswipetabs.VolleyClass.VolleySingleton;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class EditLabourActivity extends AppCompatActivity {

    Spinner mSpinnerProjectNumber, mSpinnerContractNumber, mSpinnerContractorNumber;
    TableLayout cAddLabourDetailTableLayout;
    TextView cAddDetail;
    CustomDialogClass customDialogClass = null;
    Spinner mSpinnerDescription;
    EditText NoOfLabour, Remark;
    public Button Cancle, Add, mChooseButtonPhoto, imgReplace;
    ImageView mPhotoImageUrl, editPickDate;
    RecyclerView recyclerView;
    cAddLabourDetailAdapter cAddLabourDetailAdapterr;
    ArrayList<AddLabourDetailModelClass> cAddDetailArray = new ArrayList<>();
    boolean isClickFormList = false;
    boolean prjCallFromWithIn, venCallFromWithIn, conCallFromWithIn;
    boolean imageEdit = false;
    int currListIndex;
    private ProgressDialog pDialog;
    ApiCall mApiCall;
    Map descriptionMap = new HashMap();
    Map seqMap = new HashMap();
    /*ArrayList<String> project_id = new ArrayList<>();
    ArrayList<String> project_number = new ArrayList<>();
    ArrayList<String> vendor_id = new ArrayList<>();
    ArrayList<String> vendor_number = new ArrayList<>();
    ArrayList<String> contract_id = new ArrayList<>();
    ArrayList<String> contract_number = new ArrayList<>();*/
    String projectValue = "", vendorValue = "", contractValue = "", descValue = "";
    //int project_Num_Index = 0,vendor_Num_Index = 0,contract_Num_Index = 0,description_Num_Index=0;
    Button cSave, cClose;
    /*ArrayList<String> Description_Id = new ArrayList<>();
    ArrayList<String> Description_name = new ArrayList<>();*/
    EditText mEditRemark, edtBatch_Id, edtreported_By, edtReported_Date;
    String batch_id;//response_proj_id,response_contract_id;
    CheckBox estimationFlag;
    String imageWebURL;
    TextView messageText = null;
    boolean isImageAdded = false;
    ImageView mImageTempFull;
    Button mDeleteImage;
    Custom_Spinner_Adapter custom_spinner_adapter;
    CustomImagePopupClass customImagePopupClass;

    String mCurrentPhotoPath;
    NumberFormat formatter = new DecimalFormat("#0.00");
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_labour);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView ActionBarTitle = (TextView) findViewById(R.id.actionbartitle);
        ActionBarTitle.setText("Labour (Edit)");


        mApiCall = new ApiCall(this);
        pDialog = new ProgressDialog(EditLabourActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        mSpinnerProjectNumber = (Spinner) findViewById(R.id.mSpinnerProjectNumber);
        mSpinnerContractorNumber = (Spinner) findViewById(R.id.mSpinnerContracterNumber);
        mSpinnerContractNumber = (Spinner) findViewById(R.id.mSpinnerContractNumber);


        mSpinnerProjectNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner", "Spinner");
                projectValue = mSpinnerProjectNumber.getSelectedItem().toString();
                Log.v("Result", " projectValue " + projectValue + "pstion" + mSpinnerProjectNumber.getSelectedItemPosition());
                if (prjCallFromWithIn) {
                    prjCallFromWithIn = false;
                } else {
                    vendorValue = "";
                    contractValue = "";
                    if (!cAddDetailArray.isEmpty() || cAddDetailArray != null || cAddDetailArray.size() > 0) {
                        recyclerView.removeAllViews();
                        cAddDetailArray.clear();
                        //Toast.makeText(getApplicationContext(), "" + cAddDetailArray.size(), Toast.LENGTH_LONG).show();
                        recyclerView.setAdapter(new cAddLabourDetailAdapter(cAddDetailArray));
                    }

                    mGetContractorListingTask(projectValue);
                }


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
                Log.v("Result", " vendorValue " + vendorValue + "pstion" + mSpinnerContractorNumber.getSelectedItemPosition());
                if (venCallFromWithIn) {
                    venCallFromWithIn = false;
                } else {
                    contractValue = "";
                    if (!cAddDetailArray.isEmpty() || cAddDetailArray != null || cAddDetailArray.size() > 0) {
                        recyclerView.removeAllViews();
                        cAddDetailArray.clear();
                        //Toast.makeText(getApplicationContext(), "" + cAddDetailArray.size(), Toast.LENGTH_LONG).show();
                        recyclerView.setAdapter(new cAddLabourDetailAdapter(cAddDetailArray));
                    }
                    mGetContractListingTask(projectValue, vendorValue);
                }


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
                Log.v("Result", " contractValue " + contractValue + "pstion" + mSpinnerContractNumber.getSelectedItemPosition());
                if (conCallFromWithIn) {
                    conCallFromWithIn = false;
                } else {
                    if (mSpinnerContractNumber.getSelectedItemPosition() != 0) {
                        ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(projectValue);
                        Map vendor = pd.getVendor();
                        VendorData vd = (VendorData) vendor.get(vendorValue);
                        Map contractInfo = vd.getContract();
                        if (!cAddDetailArray.isEmpty() || cAddDetailArray != null || cAddDetailArray.size() > 0) {
                            recyclerView.removeAllViews();
                            cAddDetailArray.clear();
                            descriptionMap.clear();
                            //Toast.makeText(getApplicationContext(), "" + cAddDetailArray.size(), Toast.LENGTH_LONG).show();
                            recyclerView.setAdapter(new cAddLabourDetailAdapter(cAddDetailArray));
                        }
                        new mGeDescriptionsListingTask().execute(pd.getPrjId(), contractInfo.get(contractValue).toString());
                    }
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
                    Toast.makeText(getApplicationContext(), "At least one product details is required", Toast.LENGTH_LONG).show();
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


        cAddDetail = (TextView) findViewById(R.id.txtaddDetail);
        cAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinnerContractNumber.getSelectedItemPosition() != 0) {
                    isClickFormList = false;
                    customDialogClass = new CustomDialogClass(EditLabourActivity.this);
                    customDialogClass.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select correct Contract Number", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cAddLabourDetailAdapterr = new cAddLabourDetailAdapter(cAddDetailArray);
        recyclerView.setAdapter(cAddLabourDetailAdapterr);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                isClickFormList = true;
                currListIndex = position;
                customDialogClass = new CustomDialogClass(EditLabourActivity.this);
                customDialogClass.show();

            }
        }));

        mPhotoImageUrl = (ImageView) findViewById(R.id.mImgAttachment);
        mImageTempFull = (ImageView) findViewById(R.id.mImageTempFull);


        mEditRemark = (EditText) findViewById(R.id.mEditRemark);
        batch_id = getIntent().getStringExtra("Batch_Id");
        edtBatch_Id = (EditText) findViewById(R.id.mBatchId);
        edtreported_By = (EditText) findViewById(R.id.edtreportedby);
        edtReported_Date = (EditText) findViewById(R.id.edtReportedate);
        edtReported_Date.setText(getCurrentDate());
        estimationFlag = (CheckBox) findViewById(R.id.mChkEstimationFlag);
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

        mPhotoImageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImageAdded == true) {
                    customImagePopupClass = new CustomImagePopupClass(EditLabourActivity.this);
                    customImagePopupClass.show();
                } else {
                    selectImage();
                }
            }
        });

        new mGetEditLabourDetailTask().execute(batch_id);
    }

    public void showDatePickerDialog(View view) {
        showDialog(100);
        //Toast.makeText(getApplicationContext(), "kanika", Toast.LENGTH_SHORT)
        //      .show();
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

    public class CustomImagePopupClass extends Dialog implements
            View.OnClickListener {


        public Context c;
        ImageView mImgeFull;

        public CustomImagePopupClass(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            this.c = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.image_edit_popup);
            ViewGroup.LayoutParams params = getWindow().getAttributes();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = getWidth(getApplicationContext());
            getWindow().setAttributes((WindowManager.LayoutParams) params);

            Cancle = (Button) findViewById(R.id.btncancle);
            imgReplace = (Button) findViewById(R.id.btnreplace);
            mImgeFull = (ImageView) findViewById(R.id.mImgeFull);
            mDeleteImage = (Button) findViewById(R.id.imageDelete);
            Log.v("kanika", "Path" + imageWebURL);
            /*if(imageEdit == false){
                Picasso.with(EditLabourActivity.this)
                        .load(imageWebURL)
                        .into(mImgeFull);
            }else{
                mImgeFull.setImageURI(Uri.parse(imageWebURL));
            }*/

            mImgeFull.setImageDrawable(mImageTempFull.getDrawable());

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customImagePopupClass.dismiss();
                    new mDeleteImageTask().execute();
                }
            });


            Cancle.setOnClickListener(this);
            imgReplace.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btncancle:
                    dismiss();
                    break;
                case R.id.btnreplace:
                    dismiss();
                    selectImage();
                    break;
                default:
                    break;
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditLabourActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                    // Toast.makeText(getApplicationContext(), "Res  = "+result+" ... Manager  = "+PackageManager.PERMISSION_GRANTED, Toast.LENGTH_LONG).show();
                    if (getApplicationContext().checkCallingOrSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditLabourActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                Utility.MY_PERMISSIONS_REQUEST_CAMERA);
                    } else if (ContextCompat.checkSelfPermission(EditLabourActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditLabourActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        captureImaege();
                    }
                    //boolean resultCamera = Utility.checkPermissionCamera(AddLabourActivity.this);
                } else if (items[item].equals("Choose from Library")) {
                    // boolean result = Utility.checkPermissionReadWrite(AddLabourActivity.this);
                    userChoosenTask = "Choose from Library";
                    if (ContextCompat.checkSelfPermission(EditLabourActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditLabourActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        galleryIntent();
                    }


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
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
            ViewGroup.LayoutParams params = getWindow().getAttributes();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = widthScreen;
            getWindow().setAttributes((WindowManager.LayoutParams) params);
            NoOfLabour = (EditText) findViewById(R.id.edtlabour);
            Remark = (EditText) findViewById(R.id.edtremark);
            NoOfLabour.setFilters(new InputFilter[]{ new MinMaxInputFilter(Double.parseDouble("1.00"), Double.parseDouble("999.00"))});
            mSpinnerDescription = (Spinner) findViewById(R.id.mSpinnerDescription);
            mSpinnerDescription.setDropDownWidth(widthScreen);
            ArrayList descList = new ArrayList(descriptionMap.keySet());
            descList.add(0, "Select Description");

            adapterDescriptionNumber = new ArrayAdapter<String>(EditLabourActivity.this, android.R.layout.simple_spinner_item, descList); //selected item will look like a spinner set from XML
            adapterDescriptionNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerDescription.setAdapter(adapterDescriptionNumber);
            if (isClickFormList) {
                Log.d("Desc", "Desc : " + cAddDetailArray.get(currListIndex).getcDescription());
                Log.d("Remark", "Remark : " + cAddDetailArray.get(currListIndex).getcRemark());
                Log.d("Labour", "Labour : " + cAddDetailArray.get(currListIndex).getcNoOfLabour());

            /*for (int i=0; i<Description_Id.size(); i++){
                if (Description_name.get(i).equals(cAddDetailArray.get(currListIndex).getcDescription())){
                    description_Num_Index = i;
                }
            }*/
                int position = adapterDescriptionNumber.getPosition(cAddDetailArray.get(currListIndex).getcDescription().toString());
                mSpinnerDescription.setSelection(position);

            } else {
                mSpinnerDescription.setSelection(0);
            }


            mSpinnerDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Spinner", "Spinner");
                    descValue = mSpinnerDescription.getSelectedItem().toString();
                    NoOfLabour.setText("");
                    Remark.setText("");
                    if(position>0 && cAddDetailArray.size()>0)
                    {
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
                int pos = adapterDescriptionNumber.getPosition(cAddDetailArray.get(currListIndex).getcDescription());
                mSpinnerDescription.setSelection(pos);
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
            Test.check();
            switch (v.getId()) {
                case R.id.btncancle:
                    if (isClickFormList) {
                        cAddDetailArray.remove(currListIndex);
                        cAddLabourDetailAdapterr.notifyDataSetChanged();
                        descValue = "";
                    }

                    dismiss();
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                    break;
                case R.id.btnAdd:
                    if (isClickFormList) {
                        // cAddDetailArray.remove(currListIndex);
                        if(Double.parseDouble(NoOfLabour.getText().toString())>0 && Double.parseDouble(NoOfLabour.getText().toString())<1000) {
                            AddLabourDetail(0);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Count should be greater than 0 and less than 1000", Toast.LENGTH_LONG).show();

                        }

                    } else {

                        if (NoOfLabour.getText().toString().equals("") || NoOfLabour.getText().length() > 6) {
                            Log.v("labour", "labour if");
                            NoOfLabour.setError(getResources().getString(R.string.Labour));

                        } else if (descValue.equals("") || mSpinnerDescription.getSelectedItemPosition() == 0) {
                            Log.v("description", "description else if");
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) mSpinnerDescription.getAdapter();
                            TextView selectedTextView = (TextView) mSpinnerDescription.getSelectedView();
                            selectedTextView.setError("Please select a value");

                        } else {
                            Log.v("labour", "labour else");
                            if(Double.parseDouble(NoOfLabour.getText().toString())>0 && Double.parseDouble(NoOfLabour.getText().toString())<1000) {
                                AddLabourDetail(1);
                                dismiss();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Count should be greater than 0 and less than 1000", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                    dismiss();
                    break;
                default:
                    break;
            }
            //dismiss();
        }


        public void AddLabourDetail(int flag) {
            if(Double.parseDouble(NoOfLabour.getText().toString())>0 && Double.parseDouble(NoOfLabour.getText().toString())<1000)
            {
                String nooflabour = formatter.format(Double.parseDouble(NoOfLabour.getText().toString()));
                String remark = Remark.getText().toString();

                if (flag == 0) {
                    Log.v("test", "FLAG IFFFFFF");
                    if (!descValue.isEmpty()) {
                       // AddLabourDetailModelClass object = new AddLabourDetailModelClass(descValue, nooflabour, remark, descriptionMap.get(descValue).toString(), cAddDetailArray.get(currListIndex).getSeqs_No());
                        AddLabourDetailModelClass object = new AddLabourDetailModelClass(descValue, nooflabour, remark, descriptionMap.get(descValue).toString(), seqMap.get(descValue).toString());
                        cAddDetailArray.set(currListIndex, object);
                    }
                } else {
                    Log.v("test", "FLAG ELSEEEEE");
                    if (!descValue.isEmpty())
//                        cAddDetailArray.add(new AddLabourDetailModelClass(descValue, nooflabour, remark, descriptionMap.get(descValue).toString(), "" + cAddDetailArray.size()));
                        cAddDetailArray.add(new AddLabourDetailModelClass(descValue, nooflabour, remark, descriptionMap.get(descValue).toString(), "" + seqMap.get(descValue).toString()));
           /* else
                cAddDetailArray.add(new AddLabourDetailModelClass("",nooflabour,remark,"",""+ (description_Num_Index+1)));*/
                }
                cAddLabourDetailAdapterr.notifyDataSetChanged();
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Count should be greater than 0 and less than 1000", Toast.LENGTH_LONG).show();
            }

        }
    } // end custom dialog

    // ========================= API CALL ===================================

    public class mGetEditLabourDetailTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = mApiCall.doGetEditLabourDetail(batch_id);
            return result;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            if (result != null) {

                Log.v("Result", "Result View Labour Details: " + result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    if (jsonLabourData.getString("status").equals("1")) {
                        JSONObject jsonLabourObject = jsonLabourData.getJSONObject("result");
                        mEditRemark.setText(jsonLabourObject.getString("ttt_hdr_remark"));
                        projectValue = jsonLabourObject.getString("ttt_prj_id");
                        contractValue = jsonLabourObject.getString("ttt_con_id");
                        edtBatch_Id.setText(jsonLabourObject.getString("batch_id"));
                        //  edtreported_By.setText(jsonLabourObject.getString("ttt_rpt_by"));
                        edtReported_Date.setText(jsonLabourObject.getString("ttt_rpt_dt"));
                        if (jsonLabourObject.getString("ttt_inv_doc").length() > 0) {
                            isImageAdded = true;
                            // linearImage.setVisibility(View.VISIBLE);
                            imageWebURL = jsonLabourObject.getString("ttt_inv_doc");
                            Picasso.with(EditLabourActivity.this)
                                    .load(imageWebURL)
                                    .into(mPhotoImageUrl);
                            Picasso.with(EditLabourActivity.this)
                                    .load(imageWebURL)
                                    .into(mImageTempFull);
                        }
                        if (jsonLabourObject.getString("ttt_esti_flg").equals("Y")) {
                            estimationFlag.setChecked(true);
                        } else {
                            estimationFlag.setChecked(false);
                        }

                        JSONArray jsonLabourDetailArray = jsonLabourObject.getJSONArray("labours");
                        for (int i = 0; i < jsonLabourDetailArray.length(); i++) {
                            JSONObject jsonLabourDetailObject = (JSONObject) jsonLabourDetailArray.get(i);
                            cAddDetailArray.add(new AddLabourDetailModelClass(jsonLabourDetailObject.getString("ttt_hdr_name"),
                                    jsonLabourDetailObject.getString("ttt_company_cnt"),
                                    jsonLabourDetailObject.getString("ttt_remark"),
                                    jsonLabourDetailObject.getString("ttt_hdr_id")+":"+
                                            jsonLabourDetailObject.getString("ttt_floor")+":"+
                                            jsonLabourDetailObject.getString("ttt_rate"),
                                    jsonLabourDetailObject.getString("ttt_hdr_seqno")
                            ));

                        }

                        cAddLabourDetailAdapterr.notifyDataSetChanged();
                        fetchVendorValue();
                        mGetProjectListingTask();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
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
                        edtreported_By.setText(reported_By.getString("usr_usernm"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //=======vendor value======================
    private void fetchVendorValue() {
        if (FetchLabourInfoService.projectInfo != null && !FetchLabourInfoService.projectInfo.isEmpty()) {
            for (Object prjKey : FetchLabourInfoService.projectInfo.keySet()) {
                ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(prjKey.toString());
                if (pd.getPrjId().equals(projectValue)) {
                    projectValue = prjKey.toString();
                    Map vendorMap = pd.getVendor();
                    for (Object vKey : vendorMap.keySet()) {
                        VendorData vd = (VendorData) vendorMap.get(vKey.toString());
                        if (vd.getContract().containsValue(contractValue)) {
                            vendorValue = vKey.toString();
                            for (Object cKey : vd.getContract().keySet()) {

                                if (vd.getContract().get(cKey.toString()).equals(contractValue)) {
                                    contractValue = cKey.toString();
                                    prjCallFromWithIn = true;
                                    venCallFromWithIn = true;
                                    conCallFromWithIn = true;
                                    break;
                                }

                            }

                        }
                    }
                }


            }

        }
    }

    // ========================= API CALL ===================================

    private void mGetProjectListingTask() {
        pDialog.hide();
        if (FetchLabourInfoService.projectInfo != null && !FetchLabourInfoService.projectInfo.isEmpty()) {
            try {
                ArrayList projectNoArray = new ArrayList(FetchLabourInfoService.projectInfo.keySet());
                ArrayAdapter<String> adapterProjectNumber = new ArrayAdapter<String>(EditLabourActivity.this, R.layout.spinner_item, projectNoArray); //selected item will look like a spinner set from XML
                adapterProjectNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerProjectNumber.setAdapter(adapterProjectNumber);
                int position = adapterProjectNumber.getPosition(projectValue);
                mSpinnerProjectNumber.setSelection(position);
                mGetContractorListingTask(projectValue);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.v("Result", "Result Project listing empty ");
        }


    }

    // ========================= API CALL ===================================

    private void mGetContractorListingTask(String prjNo) {
        pDialog.hide();
        Log.v("Data", "prjno in contrator Listing call " + prjNo);
        try {
            ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(prjNo);
            Map vendor = pd.getVendor();
            ArrayList vendorList = new ArrayList(vendor.keySet());
            Collections.sort(vendorList, String.CASE_INSENSITIVE_ORDER);
            vendorList.add(0, "Select Vendor");
            ArrayAdapter<String> adapterVendorNumber = new ArrayAdapter<String>(EditLabourActivity.this, R.layout.spinner_item, vendorList); //selected item will look like a spinner set from XML
            adapterVendorNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerContractorNumber.setAdapter(adapterVendorNumber);
            if (!vendorValue.isEmpty()) {
                Log.v("Data", "not empty contrator Listing call " + vendorValue);
                int position = adapterVendorNumber.getPosition(vendorValue);
                mSpinnerContractorNumber.setSelection(position);
            } else {
                Log.v("Data", " empty contrator Listing call ");
                mSpinnerContractorNumber.setSelection(0);
            }
            mGetContractListingTask(projectValue, vendorValue);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private class mDeleteImageTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog.show();
            //showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = mApiCall.doGetDeleteImage(batch_id);
            return result;
        }

        protected void onPostExecute(String result) {
            pDialog.hide();
            //hideProgressDialog();
            if (result != null) {

                Log.v("Result", "Result Delete: " + result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    String ServerMsg = jsonLabourData.getString("result");
                    if (jsonLabourData.getString("status").equals("1")) {
                        isImageAdded = false;
                        mPhotoImageUrl.setImageResource(android.R.drawable.ic_menu_camera);
                        Toast.makeText(getApplicationContext(), ServerMsg, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), ServerMsg, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.v("NOLOg", "No Log" + result);
            }
        }
    }

// ========================= API CALL ===================================

    private void mGetContractListingTask(String prjNo, String venNo) {
        pDialog.hide();
        try {
            ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(prjNo);
            Map vendor = pd.getVendor();
            VendorData vd = null;
            ArrayList contractList;
            if (venNo.isEmpty() || mSpinnerContractorNumber.getSelectedItemPosition() == 0) {
                contractList = new ArrayList();
            } else {
                vd = (VendorData) vendor.get(venNo);
                contractList = new ArrayList(vd.getContract().keySet());
            }
            Collections.sort(contractList, String.CASE_INSENSITIVE_ORDER);
            contractList.add(0, "Select Contract");
            ArrayAdapter<String> adapterContractNumber = new ArrayAdapter<String>(EditLabourActivity.this, R.layout.spinner_item, contractList); //selected item will look like a spinner set from XML
            adapterContractNumber.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerContractNumber.setAdapter(adapterContractNumber);
            if (!venNo.isEmpty() && !contractValue.isEmpty()) {
                int position = adapterContractNumber.getPosition(contractValue);
                mSpinnerContractNumber.setSelection(position);
            } else {
                mSpinnerContractNumber.setSelection(0);
            }

            if (!projectValue.isEmpty() && !contractValue.isEmpty() && mSpinnerContractNumber.getSelectedItemPosition() != 0)
                new mGeDescriptionsListingTask().execute(pd.getPrjId(), vd.getContract().get(contractValue));

        } catch (Exception e) {
            e.printStackTrace();
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
                        descValue = "";
                        // }
                        for (int i = 0; i < jsonLabourArray.length(); i++) {
                            JSONObject jsonLabourObject = (JSONObject) jsonLabourArray.get(i);
                            descriptionMap.put(jsonLabourObject.getString("descr"), jsonLabourObject.getString("cor_hdr_id") +":"
                                    +jsonLabourObject.getString("lvl")+":"+jsonLabourObject.getString("rate"));
                            seqMap.put(jsonLabourObject.getString("descr"), jsonLabourObject.getString("cor_hdr_seqno"));
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

        String url = Url.server + "?action=edit-labour";

        Log.v("test", "VOLLEY url :" + url);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                pDialog.hide();
                Log.d("POST1", "Post VollyResponce");

                String resultResponse = new String(response.data);
                Log.v("test", "VOLLEY resultResponse :" + resultResponse);
/*
                LabourActivity.activity.finish();
                Intent intent = new Intent(EditLabourActivity.this,LabourActivity.class);
                startActivity(intent);
                finish();*/

                try {
                    JSONObject responsejsonObject = new JSONObject(resultResponse);
                    if (responsejsonObject.getString("status").equals("1")) {
                        // Toast.makeText(getApplicationContext(), responsejsonObject.getString("0"), Toast.LENGTH_LONG).show();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                EditLabourActivity.this);

                        // set title
                        // alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Labour Record\n" + responsejsonObject.getString("0"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        LabourActivity.activity.finish();
                                        Intent intent = new Intent(EditLabourActivity.this, LabourActivity.class);
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
                    } else
                        Toast.makeText(getApplicationContext(), "Labour not updated successfully", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                } else {
                    String errorMessage = error.getClass().getSimpleName();
                    if (!TextUtils.isEmpty(errorMessage)) {
                        Toast.makeText(getApplicationContext(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                ProjectData pd = (ProjectData) FetchLabourInfoService.projectInfo.get(projectValue);
                Map vendor = pd.getVendor();
                VendorData vd = (VendorData) vendor.get(vendorValue);
                params.put("sub_id", ApiCall.sub_id);
                params.put("user_id", ApiCall.user_id);
                params.put("prj_id", pd.getPrjId());
                params.put("con_id", vd.getContract().get(contractValue));
                params.put("rpt_date", edtReported_Date.getText().toString());
                params.put("hdr_remark", mEditRemark.getText().toString());
                params.put("batch_id", batch_id);
                params.put("user_type", ApiCall.user_type);
                // params.put("inv_doc","FILEOBJECT");
                if (estimationFlag.isChecked()) {
                    params.put("esti_flag", "Y");
                } else
                    params.put("esti_flag", "N");


                for (int i = 0; i < cAddDetailArray.size(); i++) {
                    // String skuStr = "product_post[product_post_m_skus_attributes[" + i + "][sku_code]";
                    // Map<String, String> paramsDetail = new HashMap<>();
                    try {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("company_cnt", cAddDetailArray.get(i).getcNoOfLabour());

                        jsonObject.put("hdr_id", cAddDetailArray.get(i).getcDescriptionId());
                        jsonObject.put("rate", cAddDetailArray.get(i).getRate());
                        jsonObject.put("floor", cAddDetailArray.get(i).getLvl());

                        jsonObject.put("hdr_seqno", "" + cAddDetailArray.get(i).getSeqs_No());
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
                    params.put("inv_doc", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getApplicationContext(), mPhotoImageUrl.getDrawable()), "image/jpeg"));
                    Log.d("POST1", "Post VollyMapImage");
                }

                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
        return "";
    }


    public String getCurrentDate() {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        Log.v("date", "date" + dateString);
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

}
