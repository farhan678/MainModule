package com.androidbelieve.drawerwithswipetabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.library.JSONParser;

import org.json.JSONObject;
import urllink.*;
public class PopActivity extends AppCompatActivity {
SharedPreferences sharedpreferences;
    String code,ipAddtress,deviceid,usersubid;

    String Sucess,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        SharedPreferences settings;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pDialog = new ProgressDialog(PopActivity.this);
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
     json = jParser.getJSONFromUrl(Url.Logout+"address=" + ipAddtress + "&deviceid=" +deviceid+"&sub_id="+usersubid+"&usr_id="+code+"&opr="+"SessionTimeOut");
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
                    settings = getSharedPreferences("userdetail", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                    settings = getSharedPreferences("cms", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                    settings = getSharedPreferences("localvalue", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                    settings = getSharedPreferences("localvaluecode", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();

                    settings = getSharedPreferences("msm_prg_name", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                    Sucess=(json_data.getString("result"));
                    clearAllData();

                    if( Sucess.equals("success"))
                    {



                        showAlert("Your Seesion Time Out  " );


                    }
                    else {

                        showAlert("Your Seesion Time Out ");


                    }







                }




                else{

                    Toast.makeText(PopActivity.this, "Datas not found or Internet Issue", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
    }





    private void showAlert(String result) {


        final String msg=result;

        AlertDialog.Builder builder = new AlertDialog.Builder(PopActivity.this);
        builder.setMessage(result).setTitle("Result")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (Sucess.equals("success")) {
                            Intent i = new Intent(PopActivity.this, Getstarted.class);


                            startActivity(i);

                        } else {

                            Intent i = new Intent(PopActivity.this, Getstarted.class);


                            startActivity(i);

                        }


                    }


                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void clearAllData()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());; // here you get your prefrences by either of two methods
        SharedPreferences.Editor editor = prefs.edit();
        //Toast.makeText(getApplicationContext(),"Already : "+prefs.getAll().toString(),Toast.LENGTH_LONG).show();
        editor.clear();
        editor.commit();
        //   Toast.makeText(getApplicationContext(), ""+getApplicationContext().getSharedPreferences("submenutitile", Context.MODE_PRIVATE).getAll().toString(),Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(),"Cleared"+prefs.getAll().toString(),Toast.LENGTH_LONG).show();
    }
}
