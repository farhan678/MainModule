package com.androidbelieve.drawerwithswipetabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.library.JSONParser;

import org.json.JSONObject;
import urllink.*;
public class ActiveActivity extends AppCompatActivity {
    String Sucess,username;
    SharedPreferences sharedpreferences;
    String code,ipAddtress,deviceid,usersubid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);



        Bundle bundle=getIntent().getExtras();


        username=bundle.getString("username");



        sharedpreferences = getSharedPreferences("cms", Context.MODE_PRIVATE);



        if (sharedpreferences.contains("usertype")) {


            code= sharedpreferences.getString("subid", "");
            ipAddtress= sharedpreferences.getString("ip", "");
            deviceid= sharedpreferences.getString("device", "");
            usersubid= sharedpreferences.getString("usersubid", "");





        }


// call the server


        new JSONParse().execute();

    }








// call the server


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pDialog = new ProgressDialog(ActiveActivity.this);
            pDialog.setMessage("Your Account Is Processing ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            JSONObject json=null;


            try {
                json = jParser.getJSONFromUrl(Url.account + "address=" + ipAddtress + "&deviceid=" + deviceid + "&user_name=" + username + "&staus=" + "Inactive");

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


                    if( Sucess.equals("success"))
                    {



                        showAlert("Your Account is InActive " );


                    }
                    else {

                        showAlert("Your Account Active");


                    }







                }




                else{

                    Toast.makeText(getApplicationContext(), "Datas not found or Internet Issue", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }


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
                            Intent i = new Intent(ActiveActivity.this, Getstarted.class);


                            startActivity(i);

                        } else {

                            Intent i = new Intent(ActiveActivity.this, Getstarted.class);


                            startActivity(i);

                        }


                    }


                });
        AlertDialog alert = builder.create();
        alert.show();
    }



}
