package com.androidbelieve.drawerwithswipetabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;

import android.preference.PreferenceManager;
import android.provider.Settings;

import android.support.v7.app.AppCompatActivity;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import urllink.*;
public class Getstarted extends AppCompatActivity {


    EditText Userid,Username, Password;
    Button loginButton;
    Bean bean=new Bean();

    String ipAddress;
    String android_id;
    InputStream is=null;
    String result=null;
    String line=null;
    String textmessage = null;
    String Sucess,strDate;
    String subid,usernamevalue;
    String type,userid;
    int i=1;
    int increment;
    SharedPreferences sharedpreferences;
    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_getstarted);
        clearAllData();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
         ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
         android_id = Settings.Secure.getString(this.getContentResolver(),
                 Settings.Secure.ANDROID_ID);

        Log.d("Android", "Android ID : " + android_id+ipAddress);
        Date dNow = new Date( );


        SimpleDateFormat ft =
                new SimpleDateFormat("yyyy-MM-dd ");



         strDate = ft.format(dNow);


        loginButton = (Button)findViewById(R.id.btn_login);
        Username = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        Userid = (EditText)findViewById(R.id.editText);
        forgot=(TextView)findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (Userid.getText().toString().length() == 3) {

                    if (Username.getText().toString().length() != 0) {


                        Intent i=new Intent(Getstarted.this,ForgotActivity.class);
                        i.putExtra("username",Username.getText().toString());
                        i.putExtra("code",Userid.getText().toString());

                        startActivity(i);
             }
                    else {

                       Toast.makeText(getApplicationContext(), "Enter The User Name", Toast.LENGTH_LONG).show();
                   }

                }
               else{

                    Toast.makeText(getApplicationContext(), "Enter The Subcode", Toast.LENGTH_LONG).show();
               }
          }





        });





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Userid.getText().toString().length() == 3) {

                    if (Username.getText().toString().length() != 0) {


                        if (Password.getText().toString().length() != 0) {

                           //Calling function for  check username and password in server
                            insert();


                            usernamevalue=Username.getText().toString();
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "Enter The Password", Toast.LENGTH_LONG).show();
                        }

                    }
                    else{

                        Toast.makeText(getApplicationContext(), "Enter The User Name", Toast.LENGTH_LONG).show();
                    }
                }



                else{


                    Toast.makeText(getApplicationContext(), "SubCode Accept Three Char Only!", Toast.LENGTH_LONG).show();
                }

            }
        });



    }




// check username and password in server

    public void insert ()
    {


        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


        nameValuePairs.add(new BasicNameValuePair("code",Userid.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("username", Username.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("password",Password.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("address",ipAddress));
        nameValuePairs.add(new BasicNameValuePair("deviceid",android_id));


        try
        {
            HttpClient httpclient = new DefaultHttpClient();


            HttpPost httppost = new HttpPost(Url.Loginurl);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Please check the internet connection",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                // bar.dismiss();
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        try
        {

            Log.i("result",result);
            JSONObject json_data = new JSONObject(result);
            String position="15";


            Sucess=(json_data.getString("result"));
            textmessage=(json_data.getString("text"));

            if( Sucess.equals("success"))
            {


                sharedpreferences = getSharedPreferences("cms", Context.MODE_PRIVATE);
                subid=(json_data.getString("sub_id"));
                type=(json_data.getString("user_type"));
                bean.setSub_id(subid);
                bean.setUser_type(type);

                userid=(json_data.getString("userid"));
                bean.setUserid(userid);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("code",Userid.getText().toString());
                editor.putString("subid",userid);
                editor.putString("usertype",type);
                editor.putString("username", Username.getText().toString());
                editor.putString("s","-");

                editor.putString("usersubid",subid);
                editor.putString("ip",ipAddress);
                editor.putString("device",android_id);
                editor.putString("loginidofuser",position);
                editor.putString("logintime",strDate);
                editor.commit();
                showAlert("Login Sucess");


            }
            else {

                showAlert(textmessage);


            }



        }
        catch(Exception e)
        {
            Log.e("Fail 3", e.toString());

        }
    }
    private void showAlert(String result) {


        final String msg=result;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(result).setTitle("Result")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (Sucess.equals("success")) {
                            Intent i = new Intent(Getstarted.this, LocalActivity.class);

                            i.putExtra("subid", subid);
                            i.putExtra("usertype", type);

                            startActivity(i);




                        } else {
                            if(msg.equals("Please check the password!")){

                                increment=i++;

                            }


                            if(increment==5){

                                Log.i("increment", increment + "");
                                Intent i=new Intent(Getstarted.this,ActiveActivity.class);
                                i.putExtra("username",usernamevalue);
                                startActivity(i);



                            }






                        }


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    // disable the back button

    public void onBackPressed() {


    }
    public void clearAllData()
    {
        SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());; // here you get your prefrences by either of two methods
        Editor editor = prefs.edit();
        //Toast.makeText(getApplicationContext(),"Already : "+prefs.getAll().toString(),Toast.LENGTH_LONG).show();
        editor.clear();
        editor.commit();
     //   Toast.makeText(getApplicationContext(), ""+getApplicationContext().getSharedPreferences("submenutitile", Context.MODE_PRIVATE).getAll().toString(),Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(),"Cleared"+prefs.getAll().toString(),Toast.LENGTH_LONG).show();
    }

}
