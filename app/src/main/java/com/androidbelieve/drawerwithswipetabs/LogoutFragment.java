package com.androidbelieve.drawerwithswipetabs;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.library.JSONParser;

import org.json.JSONObject;

import java.util.List;

import urllink.Url;

// logout action tag
public class LogoutFragment extends android.support.v4.app.Fragment {

    UserDatabaseHandler dbsub;
    SharedPreferences settings, sharedpreferences;
    String code, ipAddtress, deviceid, usersubid;

    String Sucess, username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
        sharedpreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);

       // Test.logfinal();

        if (sharedpreferences.contains("usertype")) {


            code = sharedpreferences.getString("subid", "");
            ipAddtress = sharedpreferences.getString("ip", "");
            deviceid = sharedpreferences.getString("device", "");
            usersubid = sharedpreferences.getString("usersubid", "");


        }


        dbsub = new UserDatabaseHandler(getActivity());

        // delete the local sharedprefences values


        settings = getActivity().getSharedPreferences("userdetail", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        settings = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        settings = getActivity().getSharedPreferences("localvalue", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        settings = getActivity().getSharedPreferences("localvaluecode", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        settings = getActivity().getSharedPreferences("msm_prg_name", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        try {
            List<UserInformation> contacts = dbsub.getAllUserInformation();

            for (UserInformation u : contacts) {


                String tablename = u.getName();
                settings = getActivity().getSharedPreferences(tablename, Context.MODE_PRIVATE);
                settings.edit().clear().commit();


                Log.i("delete", "delete");
            }

        } catch (Exception e) {


        }


        new JSONParse().execute();

        return rootView;
    }


// call the server

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Your Account Is Processing ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            JSONObject json = null;
            try {

                json = jParser.getJSONFromUrl(Url.Logout + "address=" + ipAddtress + "&deviceid=" + deviceid + "&sub_id=" + usersubid + "&usr_id=" + code + "&opr=" + "Logout");

            } catch (Exception e) {


            }

            return json;


        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();


            try {


                if (json != null) {


                    JSONObject json_data = json;

                    Sucess = (json_data.getString("result"));


                    if (Sucess.equals("success")) {


                        showAlert("Your Account Logout ");


                    } else {

                        showAlert("Your Account Not Logout");


                    }


                } else {

                    Toast.makeText(getActivity(), "Datas not found or Internet Issue", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    private void showAlert(String result) {


        final String msg = result;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your Account Logout ").setTitle("Result")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (Sucess.equals("success")) {
                            Intent i = new Intent(getActivity(), Getstarted.class);


                            startActivity(i);

                        } else {

                            Intent i = new Intent(getActivity(), Getstarted.class);


                            startActivity(i);

                        }


                    }


                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
