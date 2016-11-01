package com.androidbelieve.drawerwithswipetabs.library;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.R;
import com.androidbelieve.drawerwithswipetabs.SentFragment;
import com.androidbelieve.drawerwithswipetabs.Test;
import com.androidbelieve.drawerwithswipetabs.UserDatabaseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import urllink.*;
/**
 * A simple {@link Fragment} subclass.
 */
public class Changepassword_layout extends android.support.v4.app.Fragment {


    public Changepassword_layout() {
        // Required empty public constructor
    }

    SharedPreferences shoepreferences;

    EditText oldpass,newpass,confirmpass;



    String oldpasswordvalue,userid,newpasswordvalue;


    String textmessage = null;
    String Sucess;
    Button save,btncancel;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Test t;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_changepassword_layout, container, false);




        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


t=new Test();
        Test.check();
        t.obj(getActivity());




        shoepreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);

        if (shoepreferences.contains("subid")) {

            userid = shoepreferences.getString("subid", "");





        }

        oldpass=(EditText)rootView.findViewById(R.id.old);
        newpass=(EditText)rootView.findViewById(R.id.newpassword);
        confirmpass=(EditText)rootView.findViewById(R.id.confimpassword);
        save=(Button)rootView. findViewById(R.id.button);
        btncancel=(Button)rootView.findViewById(R.id.cancel);





        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.check();
                t.obj(getActivity());
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpasswordvalue = oldpass.getText().toString();
                newpasswordvalue = newpass.getText().toString();
                Test.check();
                t.obj(getActivity());


                if (oldpasswordvalue.length() != 0) {


                    if (newpasswordvalue.length() != 0) {

                        if (confirmpass.getText().toString().length() != 0) {


                            if (newpasswordvalue.equals(confirmpass.getText().toString())) {

                                new JSONParse().execute();

                            } else {

                                Toast.makeText(getActivity(), "New Password and Confirm Password not Same",
                                        Toast.LENGTH_LONG).show();

                            }


                        } else {
                            Toast.makeText(getActivity(), "Enter the Confirm Password",
                                    Toast.LENGTH_LONG).show();
                        }


                    } else {

                        Toast.makeText(getActivity(), "Enter the New Password",
                                Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(getActivity(), "Enter the Old Password",
                            Toast.LENGTH_LONG).show();

                }


            }
        });












        return rootView;
    }







    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updte The Password ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            JSONObject json=null;


            try {
                json = jParser.getJSONFromUrl(Url.PasswordReset + "user_id=" + userid + "&new_password=" + newpasswordvalue + "&old_password=" + oldpasswordvalue);
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



                        showAlert(textmessage);


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
                        } else {


                        }


                    }


                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
