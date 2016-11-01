package com.androidbelieve.drawerwithswipetabs;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.Model.Labour;
import com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter.cLabourAdapter;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubmenuclickFragment extends android.support.v4.app.Fragment {


    public SubmenuclickFragment() {
        // Required empty public constructor
    }

    Class tClass;
    String methodname,masterpage;
    String servervalue;
    SharedPreferences submenuvalue,sharedpreferences;
    Date valuecfirst,valuesecond;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    RecyclerView recyclerView;
    cLabourAdapter cLabourAdapterr;
    ArrayList<Labour> labourArrayList = new ArrayList<>();
    private ProgressDialog pDialog;
    public static Activity activity;



    FloatingActionButton mBtnAddLabour;
    ApiCall mApiCall;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_submenuclick, container, false);

        TextView programname=(TextView)rootView.findViewById(R.id.prg);

        programname.setText("Now you are in Labour Activity Fragment");

//
//try {
//
//
//
//
//
//
//
//
//    submenuvalue = getActivity().getSharedPreferences("msm_prg_name", Context.MODE_PRIVATE);
//
//    if (submenuvalue.contains("msm_prg_name")) {
//
//        servervalue = submenuvalue.getString("msm_prg_name", "");
//        Log.i("servervalue", servervalue);
//
//    }
//
//
//    String replacedString = servervalue.replace(".", ",");
//    String[] menunamevalue = replacedString.split(",", 0);
//
//
//
//    Log.i("class",replacedString);
//     masterpage = menunamevalue[0];
//
//    methodname = menunamevalue[1];
//}
//    catch(Exception e){
//
//        Toast.makeText(getActivity(), "Enter Method Name Proper Format", Toast.LENGTH_LONG).show();
//
//    }
//
//
//        Class noparams[] = {};
//
//        //String parameter
//        Class[] paramString = new Class[1];
//        paramString[0] = String.class;
//
//        //int parameter
//        Class[] paramInt = new Class[1];
//        paramInt[0] = Integer.TYPE;
//
//        try{
//
//            sharedpreferences = getActivity().getSharedPreferences("userdetail", Context.MODE_PRIVATE);
//
//            String first = sharedpreferences.getString("loginidofuser", "");
//
//            String second= sharedpreferences.getString("logintime", "");
//
//            valuecfirst = sdf.parse(first);
//            valuesecond = sdf.parse(second);
//
//
//
//
//
//
//                Class cls = Class.forName("response." + masterpage);
//                Object obj = cls.newInstance();
//
//                //call the printIt method
//                Method method = cls.getDeclaredMethod(methodname, noparams);
//
//                String str1 = (String) method.invoke(obj, new Object[]{});
//                System.out.println("getString1 returned: " + str1);
//                programname.setText(str1);
//
//
//
//
//
//        }catch(Exception ex){
//
//            programname.setText("ye3Check Your Class And Method Name");
//            Toast.makeText(getActivity(), "4Check Your Class And Method Name", Toast.LENGTH_LONG).show();
//        }

        return rootView;

                                }


                            }
