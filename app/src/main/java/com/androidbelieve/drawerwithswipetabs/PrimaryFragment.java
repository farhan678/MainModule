package com.androidbelieve.drawerwithswipetabs;


import session.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ratan on 7/29/2015.
 */
public class PrimaryFragment extends Fragment {



String  user_subid,user_usertype,user_usersubid;



    String usertype,msm_menu_id;
    String id,msmpg;
    RelativeLayout mainLayout;
    String sharedpreferancevalue,menucode;
    Handler h;
    Runnable r;
    Date valuecfirst,valuesecond;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    JSONArray android = null;
    SharedPreferences submenuvalue;
    SharedPreferences sharedpreferences;
    SharedPreferences localvalue;
    ListView listView;
    Test t;
    ImageButton logout;
    UserDatabaseHandler dbsnamep;
    int positionvalue;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> oslist1 = new ArrayList<HashMap<String, String>>();
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.primary_layout, container, false);


        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        dbsnamep= new UserDatabaseHandler(getActivity());

        // call seesion time


         t=new Test();

        Test.check();

        t.obj(getActivity());
        sharedpreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("usertype")) {

            user_usertype = sharedpreferences.getString("usertype", "");

            user_usersubid = sharedpreferences.getString("usersubid", "");
            user_subid = sharedpreferences.getString("subid", "");

        }


//getting menu value from SharedPreferences

        localvalue = getActivity().getSharedPreferences("localvalue", Context.MODE_PRIVATE);

        if (localvalue.contains("menu")) {

                    id = localvalue.getString("menu", "");

            msmpg= localvalue.getString("msmpg", "");






        }



        sharedpreferences = getActivity().getSharedPreferences("userdetail", Context.MODE_PRIVATE);






        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_v, R.id.vers );

        listView = (ListView) rootView.findViewById(R.id.list);

// dispaly menu value in listview
        for(String splitvalue:msmpg.split(",",0)) {
            HashMap<String, String> map1 = new HashMap<String, String>();
            Log.i("splitvalue",splitvalue);

            map1.put("msmpg", splitvalue);


            oslist1.add(map1);


        }







        for(String splitvalue:id.split(",",0)) {


            if(splitvalue!=null) {
                adapter.add(splitvalue);


                HashMap<String, String> map = new HashMap<String, String>();

                map.put("submenutitle", splitvalue);



                oslist.add(map);

            }



            listView.setAdapter(adapter);

        }




        listView.setFastScrollEnabled(true);



        listView. setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                positionvalue = position ;

                Test.check();

                t.obj(getActivity());

               // Toast.makeText(getActivity(), "user_usersubid"+"="+user_usersubid+"user_subid"+"="+user_subid+"user_usertype"+"="+user_usertype,
                        //Toast.LENGTH_LONG).show();


                String save= oslist.get(+position).get("submenutitle");
                String menuprgromname= oslist1.get(+position).get("msmpg");

                Log.i("menuprgromname",menuprgromname);

                if(menuprgromname.equals("menu")){

                    menuRedirect();

                }

                else{


                    submenuvalue =getActivity(). getSharedPreferences("menuprgromname", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = submenuvalue.edit();

                    editor.putString("menuprgromname", menuprgromname);

                    editor.commit();



                    Intent i=new Intent(getActivity(),MenuActivity.class);
                    startActivity(i);


                }




                submenuvalue =getActivity(). getSharedPreferences("submenutitile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = submenuvalue.edit();

                editor.putString("submenutitile", save);

                editor.commit();






            }
        });






        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (mLastFirstVisibleItem < firstVisibleItem) {

//
                    Test.check();

                    t.obj(getActivity());

                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
//
//
                    Test.check();

                    t.obj(getActivity());
                    Log.i("SCROLLING UP", "TRUE");
                }
                mLastFirstVisibleItem = firstVisibleItem;

            }
        });


















        return rootView;
    }


    // method condition checking the submenu available in local or not
    // based on the usertype+id+msm_menu_id;
    public void menuRedirect(){


        localvalue = getActivity().getSharedPreferences("localvaluecode", Context.MODE_PRIVATE);

        if (localvalue.contains("code")) {

            menucode = localvalue.getString("code", "");




        }



        String[] splitvaluecode=menucode.split(",",0) ;


        msm_menu_id =splitvaluecode[positionvalue];










        sharedpreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("usertype")) {

            usertype = sharedpreferences.getString("usertype", "");

            id = sharedpreferences.getString("usersubid", "");


        }
        sharedpreferancevalue=usertype+id+msm_menu_id;

        submenuvalue =getActivity(). getSharedPreferences(sharedpreferancevalue, Context.MODE_PRIVATE);
        if (submenuvalue.contains("menu")) {

            usertype = submenuvalue.getString("menu", "");




            submenuvalue =getActivity(). getSharedPreferences("fragmentvalueif", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = submenuvalue.edit();

            editor.putString("fragmentvalue", sharedpreferancevalue);

            editor.commit();

            Intent i=new Intent(getActivity(),OfflineActivity.class);
            startActivity(i);


        }
        else{



            submenuvalue =getActivity(). getSharedPreferences("fragmentvalueelse", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = submenuvalue.edit();

            editor.putString("fragmentvalue", msm_menu_id);

            editor.commit();

            Intent i=new Intent(getActivity(),OnlineActivity.class);
            startActivity(i);
        }


    }













}