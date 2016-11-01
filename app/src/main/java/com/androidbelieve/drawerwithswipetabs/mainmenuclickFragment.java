package com.androidbelieve.drawerwithswipetabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class mainmenuclickFragment extends android.support.v4.app.Fragment {


    public mainmenuclickFragment() {
        // Required empty public constructor
    }
SharedPreferences submenuvalue;
    String servervalue,masterpage;

    String methodname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_mainmenuclick, container, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



        TextView programname=(TextView)rootView.findViewById(R.id.prg);





        submenuvalue =getActivity(). getSharedPreferences("menuprgromname", Context.MODE_PRIVATE);

try {

    if (submenuvalue.contains("menuprgromname")) {

        servervalue = submenuvalue.getString("menuprgromname", "");


    }

    String replacedString = servervalue.replace(".", ",");
    String[] menunamevalue = replacedString.split(",", 0);
     masterpage = menunamevalue[0];

    methodname = menunamevalue[1];
}
catch(Exception e){

    Toast.makeText(getActivity(), "Enter Method Name Proper Format", Toast.LENGTH_LONG).show();

}

        Class noparams[] = {};


        Class[] paramString = new Class[1];
        paramString[0] = String.class;


        Class[] paramInt = new Class[1];
        paramInt[0] = Integer.TYPE;

        try{

            Class cls = Class.forName("response." + masterpage);
            Object obj = cls.newInstance();

            //call the  method
            Method method = cls.getDeclaredMethod(methodname, noparams);

            String str1 = (String) method.invoke(obj, new Object[]{});
            System.out.println("getString1 returned: " + str1);
            programname.setText(str1);





        }catch(Exception ex){


            programname.setText("1Check Your Class And Method Name");
            Toast.makeText(getActivity(), "2Check Your Class And Method Name", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }


}
