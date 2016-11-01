package com.androidbelieve.drawerwithswipetabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.androidbelieve.drawerwithswipetabs.library.JSONParser;


import org.json.JSONObject;


import urllink.*;
public class ForgotActivity extends AppCompatActivity {



    String Sucess,textmessage;


    String subcode,username;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        Bundle bundle=getIntent().getExtras();


        username=bundle.getString("username");
        subcode=bundle.getString("code");







// call the server
                    new JSONParse().execute();




    }


    // call the server

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pDialog = new ProgressDialog(ForgotActivity.this);
            pDialog.setMessage("Password Sending To Email ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            JSONObject json=null;


try{

     json = jParser.getJSONFromUrl(Url.ForgotPassword+"code=" + subcode+"&username=" + username );


}

    catch(Exception e){




    }



            return json;



        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();





            try {


                if(json!=null) {


                    JSONObject json_data =json;

                    Sucess=(json_data.getString("result"));
                    textmessage=(json_data.getString("text"));

                    if( Sucess.equals("success"))
                    {



                        showAlert("Password Send your Email" );


                    }
                    else {

                        showAlert(textmessage);


                    }




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
                            Intent i = new Intent(ForgotActivity.this, Getstarted.class);


                            startActivity(i);

                        }

                        else {







                        }






                    }



                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
