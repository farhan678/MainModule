package com.androidbelieve.drawerwithswipetabs;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.library.JSONParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import urllink.*;
/**
 * A simple {@link Fragment} subclass.
 */
public class changeemail_layout extends android.support.v4.app.Fragment {


    public changeemail_layout() {
        // Required empty public constructor
    }
    EditText emailbutton;
    String textmessage = null;
    String Sucess,userid,emailvalue,serverusername;

    SharedPreferences shoepreferences;
    Button savebutton;
    Button btncancel;
    ImageButton logout;
    TextView username;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Test t;
    String name,deleteid;
    InputStream is = null;
    String result1 = null;
    String line = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.changeemail_layout, container, false);

        savebutton=(Button)rootView.findViewById(R.id.button);
        emailbutton=(EditText)rootView.findViewById(R.id.email);
        btncancel=(Button)rootView.findViewById(R.id.btncancel);
        t =new Test();

        Test.check();
        t.obj(getActivity());

        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        shoepreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);

        if (shoepreferences.contains("subid")) {

            userid = shoepreferences.getString("subid", "");

            serverusername= shoepreferences.getString("username", "");

        }
        email();

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.check();
                t.obj(getActivity());
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();

            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Test.check();
                t.obj(getActivity());
                emailvalue=emailbutton.getText().toString();
                if(emailvalue.length()!= 0 &&isValidEmail(emailvalue))

                {
                    new JSONParse().execute();
                }
                else{


                    Toast.makeText(getActivity(), "Enter The Valid Email id",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


        return rootView;
    }
    private boolean isValidEmail(String email) {
        // TODO Auto-generated method stub
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    // call the server
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updte The Email ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            JSONObject json=null;

            try {

                json = jParser.getJSONFromUrl(Url.Email + "user_id=" + userid + "&email=" + emailvalue);
            }


            catch(Exception e){


            }


            return json;



        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();





            try {


                if(json!=null){




                    JSONObject json_data =json;

                    Sucess=(json_data.getString("result"));
                    textmessage=(json_data.getString("text"));

                    if( Sucess.equals("success"))
                    {



                        showAlert("Success fully updated The Email" );


                    }
                    else {

                        showAlert(textmessage);


                    }







                }




                else{

                    Toast.makeText(getActivity(), "Datas not found or Internet Issue", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }








    }





    private void showAlert(String result) {


        final String msg=result;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(result).setTitle("Result")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (Sucess.equals("success")) {
                            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();

                        }

                        else {







                        }






                    }



                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void email(){


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("usr_id", userid));
Log.i("serverusername",userid);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Url.Profile);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        } catch (Exception es) {
            Log.e("Fail 1", es.toString());
            Toast.makeText(getActivity(), "InterNet Connection Problem",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result1 = sb.toString();
            Log.e("pass 2", "connection success ");
        } catch (Exception es) {
            Log.e("Fail 2", es.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result1);


            String result=(json_data.getString("result"));

if(result.equals("success")) {

    String mail = (json_data.getString("usr_Email"));
    // Log.d("Reading: ", name);


    emailbutton.setText(mail);

}

        } catch (Exception es) {
            Log.e("Fail 3", es.toString());
        }


    }


}
