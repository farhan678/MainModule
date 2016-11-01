package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.library.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import urllink.Url;

public class LocalActivity extends Activity {


    SharedPreferences shoepreferences, sharedpreferences;
    String usertype, id;
    StringBuilder menunamelocalsave = new StringBuilder();
    StringBuilder msmprogramvalue = new StringBuilder();
    StringBuilder menunamecode = new StringBuilder();
    SharedPreferences localvalue;
    JSONObject json, jsons;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date valuecfirst, valuesecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // check the main menu value stored in shared prefences or not yes available forward the main page else getting the value in server

        localvalue = getSharedPreferences("localvalue", Context.MODE_PRIVATE);

        new JSONParse().execute();

/*        if (localvalue.contains("menu")) {

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        } else {


            // calling the server clsaa
            new JSONParse().execute();

        }

*/
        shoepreferences = getSharedPreferences("cms", Context.MODE_PRIVATE);


        if (shoepreferences.contains("usersubid")) {

            id = shoepreferences.getString("usersubid", "");
            usertype = shoepreferences.getString("usertype", "");


        }


    }


    // calling the server
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialog = new ProgressDialog(LocalActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();
            Log.i("value server sub id", usertype + id);


            try {

                json = jParser.getJSONFromUrl(Url.url + "usr_user_type=" + usertype + "&sub_id=" + id);
                Log.d("json", json.toString());


            } catch (Exception e) {


            }

            return json;


        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();


            try {


                if (json != null) {


                    JSONArray posts = json.optJSONArray("storeList");
                    // Getting JSON Array from URL

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);


                        String ver = post.optString("msm_menu_name");


                        String name = post.optString("msm_menu_id");
                        ;
                        String msm_prg_name = post.optString("msm_prg_name");
                        ;


                        String a = "";
                        if (a.equals(msm_prg_name)) {


                            msm_prg_name = "menu";


                        }


                        menunamelocalsave.append(ver + ",");
                        menunamecode.append(name + ",");

                        msmprogramvalue.append(msm_prg_name + ",");


                    }


                    savevaluemenu(menunamelocalsave, msmprogramvalue);
                    savevaluemenucode(menunamecode);
                    for (String ret : menunamelocalsave.toString().split(",", 0)) {


                        if (ret != null) {
                            Log.i("splitvalue", ret);

                        }

                    }

                } else {

                    Toast.makeText(LocalActivity.this, "Datas not found or Internet Issue", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

// store the main menu value in shared prefetances

    public void savevaluemenu(StringBuilder menu, StringBuilder msmpg) {

        localvalue = getSharedPreferences("localvalue", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = localvalue.edit();

        editor.putString("menu", menu.toString());
        editor.putString("msmpg", msmpg.toString());

        editor.commit();


    }


// store the main menu code value in shared prefetances

    public void savevaluemenucode(StringBuilder code) {

        localvalue = getSharedPreferences("localvaluecode", Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = localvalue.edit();

        editor.putString("code", code.toString());

        editor.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }


}
