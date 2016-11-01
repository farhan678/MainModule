package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.Model.AddLabourDetailModelClass;
import com.androidbelieve.drawerwithswipetabs.Model.ProjectData;
import com.androidbelieve.drawerwithswipetabs.Model.VendorData;
import com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter.cAddLabourDetailAdapter;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class ViewLabourActivity extends AppCompatActivity {


    EditText mDescription;
    EditText NoOfLabour,Remark;
    public Button Cancle, Add;
    ImageView mPhotoImageUrl;
    RecyclerView recyclerView;
    cAddLabourDetailAdapter cAddLabourDetailAdapterr;
    ArrayList<AddLabourDetailModelClass> cAddDetailArray = new ArrayList<>();
    boolean isClickFormList = false;
    int currListIndex;
    String projectValue = "", vendorValue = "", contractValue="", descValue="";
    private ProgressDialog pDialog;
    ApiCall mApiCall;
    int description_Num_Index=0;
    CustomDialogClass customDialogClass = null;
    Button cClose;
    ArrayList<String> Description_Id = new ArrayList<>();
    ArrayList<String> Description_name = new ArrayList<>();
    EditText edtBatch_Id,edtreported_By,edtReported_Date;
    TextView mEditRemark,mProjectNumber,mContractorNumber,mContractNumber;
    String batch_id,imageWebURL;
    ImageView mImageDisplay;
   // LinearLayout linearImage;
    boolean isImageAdded =false;
    CheckBox estimationFlag;
    TextView messageText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_labour);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView ActionBarTitle = (TextView) findViewById(R.id.actionbartitle);
        ActionBarTitle.setText("Labour (View)");


        mApiCall = new ApiCall(this);
        pDialog = new ProgressDialog(ViewLabourActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        messageText = new TextView(ViewLabourActivity.this);
        mProjectNumber = (TextView) findViewById(R.id.mProjectNumber);
        mContractorNumber = (TextView) findViewById(R.id.mContractorNumber);
        mContractNumber = (TextView) findViewById(R.id.mContractNumber);
        mProjectNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ViewLabourActivity.this);

                // set title
               // alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));

                String MsgStr = mProjectNumber.getText().toString();
                //  MsgStr = MsgStr.replace("")
                // set dialog message
                alertDialogBuilder
                        .setMessage(MsgStr)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.show();
                messageText = (TextView)alertDialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                alertDialog.show();
            }

        });
        mContractorNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ViewLabourActivity.this);

                // set title
              //  alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));

                String MsgStr = mContractorNumber.getText().toString();
                //  MsgStr = MsgStr.replace("")
                // set dialog message
                alertDialogBuilder
                        .setMessage(MsgStr)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.show();
                messageText = (TextView)alertDialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                alertDialog.show();

            }
        });
        mContractNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ViewLabourActivity.this);

                // set title
              //  alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));

                String MsgStr = mContractNumber.getText().toString();
                //  MsgStr = MsgStr.replace("")
                // set dialog message
                alertDialogBuilder
                        .setMessage(MsgStr)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });


                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.show();
                messageText = (TextView)alertDialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                alertDialog.show();

            }
        });
        cClose = (Button) findViewById(R.id.mBtnClose);
        cClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cAddLabourDetailAdapterr = new cAddLabourDetailAdapter(cAddDetailArray);
        recyclerView.setAdapter(cAddLabourDetailAdapterr);

        /**
         * code for adding recycler view in add details
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                isClickFormList = true;
                currListIndex = position;
                customDialogClass = new CustomDialogClass(ViewLabourActivity.this);
                customDialogClass.show();

            }
        }));
        //end
        mPhotoImageUrl = (ImageView) findViewById(R.id.mImgAttachment);
        edtBatch_Id = (EditText) findViewById(R.id.mBatchId);

        mEditRemark = (TextView) findViewById(R.id.mEditRemark);
        mEditRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ViewLabourActivity.this);

                // set title
             //   alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));

                String MsgStr = mEditRemark.getText().toString();
                //  MsgStr = MsgStr.replace("")
                // set dialog message
                alertDialogBuilder
                        .setMessage(MsgStr)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.show();
                messageText = (TextView)alertDialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                alertDialog.show();



            }
        });
         batch_id = getIntent().getStringExtra("Batch_Id");
         edtreported_By = (EditText) findViewById(R.id.edtreportedby);
        edtReported_Date = (EditText) findViewById(R.id.edtReportedate);
        mImageDisplay = (ImageView) findViewById(R.id.btnImage);
        mImageDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImageAdded == true) {
                    CustomImagePopupClass customImagePopupClass = new CustomImagePopupClass(ViewLabourActivity.this);
                    customImagePopupClass.show();
                }else {
                    Toast.makeText(getApplicationContext(),"No document for this record",Toast.LENGTH_LONG).show();
                }
            }
        });
      //  linearImage = (LinearLayout) findViewById(R.id.linearImage);
        // edtBatch_Id.setText(batch_id.toString());
        estimationFlag = (CheckBox) findViewById(R.id.mChkEstimationFlag);
         new mGetViewLabourDetailTask().execute(batch_id);
         new mGetReported_ByTask().execute();
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

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.customdialog_view);
            Cancle = (Button) findViewById(R.id.btncancle);
            mDescription = (EditText) findViewById(R.id.viewDesc);
            int widthScreen = getWidth(getApplicationContext());
            ViewGroup.LayoutParams params = getWindow().getAttributes();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = widthScreen;
            getWindow().setAttributes((WindowManager.LayoutParams) params);


            // ArrayAdapter<String> adapterDescriptionNumber = new ArrayAdapter<String>(ViewLabourActivity.this, android.R.layout.simple_spinner_item, Description_name); //selected item will look like a spinner set from XML
         //   adapterDescriptionNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          //  mDescription.setAdapter(adapterDescriptionNumber);
          /*  Log.d("Desc","Desc : "+cAddDetailArray.get(currListIndex).getcDescription());
            for (int i=0; i<Description_Id.size(); i++){
                if (Description_name.get(i).equals(cAddDetailArray.get(currListIndex).getcDescription())){
                    description_Num_Index = i;
                }
            }
            mSpinnerDescription.setSelection(description_Num_Index);

            mSpinnerDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Spinner","Spinner");
                    description_Num_Index = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

            NoOfLabour = (EditText) findViewById(R.id.edtlabour);
            Remark = (EditText) findViewById(R.id.edtremark);

            if (isClickFormList){
                Cancle.setText("Exit");
                mDescription.setText(cAddDetailArray.get(currListIndex).getcDescription());
                NoOfLabour.setText(cAddDetailArray.get(currListIndex).getcNoOfLabour());
                Remark.setText(cAddDetailArray.get(currListIndex).getcRemark());

            }
            else {
                NoOfLabour.setText("");
                Remark.setText("");
                mDescription.setText("");
            }

            Cancle.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                default:
                    break;
            }
            dismiss();
        }

        /*public void AddLabourDetail(int flag){

            String nooflabour = NoOfLabour.getText().toString();
            String remark = Remark.getText().toString();

            if (flag == 0){
                Log.v("test","FLAG IFFFFFF");
                AddLabourDetailModelClass object = new AddLabourDetailModelClass(Description_name.get(description_Num_Index),nooflabour,remark,Description_Id.get(description_Num_Index),cAddDetailArray.get(currListIndex).getSeqs_No());
                cAddDetailArray.set(currListIndex,object);
            }
            else {
                Log.v("test","FLAG ELSEEEEE");
                if (Description_Id.size()>0)
                    cAddDetailArray.add(new AddLabourDetailModelClass(Description_name.get(description_Num_Index),nooflabour,remark,Description_Id.get(description_Num_Index),""+ (description_Num_Index+1)));
                else
                    cAddDetailArray.add(new AddLabourDetailModelClass("",nooflabour,remark,"",""+ (description_Num_Index+1)));
            }
            cAddLabourDetailAdapterr.notifyDataSetChanged();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        }*/
    } // end custom dialog

    public class CustomImagePopupClass extends Dialog implements
            View.OnClickListener {


        public Activity c;
        ImageView mImgeFull;

        public CustomImagePopupClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.image_popup);
            Cancle = (Button) findViewById(R.id.btncancle);
            mImgeFull = (ImageView) findViewById(R.id.mImgeFull);
            Picasso.with(ViewLabourActivity.this)
                    .load(imageWebURL)
                    .into(mImgeFull);
            Cancle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btncancle:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

      @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            Log.v("Path","Path"+images.get(0).getPath());
            mPhotoImageUrl.setImageURI(Uri.parse(images.get(0).getPath()));
            setPic(images.get(0).getPath());
        }
    }
    private void setPic(String path) {
        // Get the dimensions of the View
        int targetW = mPhotoImageUrl.getWidth();
        int targetH = mPhotoImageUrl.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        mPhotoImageUrl.setImageBitmap(bitmap);

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

                Log.v("Result","Result Reported : "+ result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    if (jsonLabourData.getString("status").equals("1")) {
                        JSONArray  jsonArray= jsonLabourData.getJSONArray("result");
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

    // ========================= API CALL ===================================

    private class mGetViewLabourDetailTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = mApiCall.doGetViewLabourDetail(batch_id);
            return result;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            if (result != null) {

                Log.v("Result","Result View Labour Details: "+ result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    if (jsonLabourData.getString("status").equals("1")){
                        JSONObject jsonLabourObject = jsonLabourData.getJSONObject("result");
                        projectValue = jsonLabourObject.getString("ttt_prj_id");
                        contractValue = jsonLabourObject.getString("ttt_con_id");
                        fetchVendorValue();
                        mEditRemark.setText(jsonLabourObject.getString("ttt_hdr_remark"));
                        mContractNumber.setText(contractValue);
                        mProjectNumber.setText(projectValue);
                        mContractorNumber.setText(vendorValue);
                        edtBatch_Id.setText(jsonLabourObject.getString("batch_id"));
                     //   edtreported_By.setText(jsonLabourObject.getString("ttt_rpt_by"));
                        edtReported_Date.setText(jsonLabourObject.getString("ttt_rpt_dt"));
                        if (jsonLabourObject.getString("ttt_inv_doc").length()> 0) {
                            isImageAdded = true;
                           // linearImage.setVisibility(View.VISIBLE);
                            imageWebURL = jsonLabourObject.getString("ttt_inv_doc");
                            Picasso.with(ViewLabourActivity.this)
                                    .load(imageWebURL)
                                    .into(mImageDisplay);
                        }
                        if (jsonLabourObject.getString("ttt_esti_flg").equals("Y")){
                            estimationFlag.setChecked(true);
                        }
                        else {
                            estimationFlag.setChecked(false);
                        }
                        JSONArray jsonLabourDetailArray = jsonLabourObject.getJSONArray("labours");
                        for (int i=0; i<jsonLabourDetailArray.length(); i++){
                            JSONObject jsonLabourDetailObject = (JSONObject) jsonLabourDetailArray.get(i);
                            String cDesc = jsonLabourDetailObject.getString("ttt_hdr_id")+":"+
                                    jsonLabourDetailObject.getString("ttt_floor")+":"+
                                    jsonLabourDetailObject.getString("ttt_rate");
                            cAddDetailArray.add(new AddLabourDetailModelClass(jsonLabourDetailObject.getString("ttt_hdr_name"),
                                    jsonLabourDetailObject.getString("ttt_company_cnt"),jsonLabourDetailObject.getString("ttt_remark"),
                                    cDesc,
                                    jsonLabourDetailObject.getString("ttt_hdr_seqno")));

                        }

                        cAddLabourDetailAdapterr.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //=======vendor value======================
    /*private String fetchVendorValue(String projectValue, String contractValue){
        String vendorValue="";
        if(FetchLabourInfoService.projectInfo!=null && !FetchLabourInfoService.projectInfo.isEmpty() ){

                ProjectData pd = (ProjectData)FetchLabourInfoService.projectInfo.get(projectValue);


                    Map vendorMap = pd.getVendor();
                    for(   Object vKey : vendorMap.keySet()){
                        VendorData vd = (VendorData)vendorMap.get(vKey.toString());
                        if(vd.getContract().containsKey(contractValue)){
                            vendorValue = vKey.toString();
                            for(Object cKey : vd.getContract().keySet()){

                                if(vd.getContract().get(cKey.toString()).equals(contractValue)){
                                    contractValue=cKey.toString();

                                    break;
                                }

                            }

                        }
                    }






        }
        return vendorValue;
    }*/
    //=======vendor value======================
    private void fetchVendorValue(){
        if(FetchLabourInfoService.projectInfo!=null && !FetchLabourInfoService.projectInfo.isEmpty() ){
            for(Object prjKey:FetchLabourInfoService.projectInfo.keySet()){
                ProjectData pd = (ProjectData)FetchLabourInfoService.projectInfo.get(prjKey.toString());
                if(pd.getPrjId().equals(projectValue)){
                    projectValue = prjKey.toString();
                    Map vendorMap = pd.getVendor();
                    for(   Object vKey : vendorMap.keySet()){
                        VendorData vd = (VendorData)vendorMap.get(vKey.toString());
                        if(vd.getContract().containsValue(contractValue)){
                            vendorValue = vKey.toString();
                            for(Object cKey : vd.getContract().keySet()){

                                if(vd.getContract().get(cKey.toString()).equals(contractValue)){
                                    contractValue=cKey.toString();
                                    break;
                                }

                            }

                        }
                    }
                }



            }

        }
    }
    public static int getWidth(Context mContext){
        int width=0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if(Build.VERSION.SDK_INT>12){
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        }
        else{
            width = display.getWidth();  // Deprecated
        }
        return width;
    }

}
