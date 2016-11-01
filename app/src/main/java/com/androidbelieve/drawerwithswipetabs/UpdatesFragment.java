package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

// geeting submenu values from local shared prefences
public class UpdatesFragment extends Fragment {


    String id, msmprogramname, usertype, msm_menu_idsubmenus;

    String sharedpreferancevalue, msmmenuidforsubmenuclick;

    Runnable r;


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    JSONArray android = null;
    SharedPreferences submenuvalue;

    SharedPreferences localvalue, sharedpreferences;
    ListView listView;
    ArrayList<HashMap<String, String>> oslist1 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> oslist2 = new ArrayList<HashMap<String, String>>();
    Test t;
    String user_subid, user_usertype, user_usersubid;
    int positionvalue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.updates_layout, container, false);

        Test.check();
        t = new Test();
        t.obj(getActivity());
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


        submenuvalue = getActivity().getSharedPreferences("fragmentvalueif", Context.MODE_PRIVATE);

        if (submenuvalue.contains("fragmentvalue")) {

            sharedpreferancevalue = submenuvalue.getString("fragmentvalue", "");


        }


        localvalue = getActivity().getSharedPreferences(sharedpreferancevalue, Context.MODE_PRIVATE);

        if (localvalue.contains("menu")) {

            id = localvalue.getString("menu", "");
            msmprogramname = localvalue.getString("msmprogramname", "");
            msm_menu_idsubmenus = localvalue.getString("msm_menu_idsubmenus", "");

            Log.i("value of sub id", id);

        }


        for (String msm_menu_idsubm : msm_menu_idsubmenus.split(",", 0)) {

            HashMap<String, String> map1 = new HashMap<String, String>();


            map1.put("msm_menu_idsub", msm_menu_idsubm);

            oslist1.add(map1);

        }


        listView = (ListView) rootView.findViewById(R.id.list);


        sharedpreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("usertype")) {

            user_usertype = sharedpreferences.getString("usertype", "");

            user_usersubid = sharedpreferences.getString("usersubid", "");
            user_subid = sharedpreferences.getString("subid", "");

        }


        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_v, R.id.vers);

        for (String msm_prg_name : msmprogramname.split(",", 0)) {

            HashMap<String, String> map = new HashMap<String, String>();


            map.put("msm_prg_name", msm_prg_name);

            oslist.add(map);

        }


        for (String splitvalue : id.split(",", 0)) {


            if (splitvalue != null) {
                adapter.add(splitvalue);

                HashMap<String, String> map = new HashMap<String, String>();


                map.put("sub", splitvalue);

                oslist2.add(map);


            }
            listView.setAdapter(adapter);

        }


        listView.setFastScrollEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                positionvalue = position;
                String saveprogramname = oslist.get(+position).get("msm_prg_name");
                Test.check();
                t.obj(getActivity());
                msmmenuidforsubmenuclick = oslist1.get
                        (+position).get("msm_menu_idsub");

                String save = oslist2.get
                        (+position).get("sub");


                //Toast.makeText(getActivity(), "user_usersubid"+"="+user_usersubid+"user_subid"+"="+user_subid+"user_usertype"+"="+user_usertype,
                //  Toast.LENGTH_LONG).show();
                Log.i("value of the check", user_usersubid + user_subid + user_usertype);
                if (saveprogramname.equals("menu")) {

                    submenuvalue = getActivity().getSharedPreferences("submenutitile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = submenuvalue.edit();

                    editor.putString("submenutitile", save);

                    editor.commit();


                    menuRedirect();

                } else if (saveprogramname.equals("LabourActivity.onCreate")) {
                    Intent i = new Intent(getActivity(), LabourActivity.class);
                    startActivity(i);
                } else if (saveprogramname.equals("SubcontractActivity.onCreate")) {
                    Intent i = new Intent(getActivity(), SubcontractActivity.class);
                    startActivity(i);
                } else {
                    Log.i("saveprogramname", saveprogramname);
                    submenuvalue = getActivity().getSharedPreferences("msm_prg_name", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = submenuvalue.edit();

                    editor.putString("msm_prg_name", saveprogramname);

                    editor.commit();

                    Intent i = new Intent(getActivity(), Main2Activity.class);
                    startActivity(i);
                }

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
                    Log.i("SCROLLING DOWN", "TRUE");
                    Test.check();
                    t.obj(getActivity());

                }
                if (mLastFirstVisibleItem > firstVisibleItem) {

                    Test.check();
                    t.obj(getActivity());
                    Log.i("SCROLLING UP", "TRUE");
                }
                mLastFirstVisibleItem = firstVisibleItem;

            }
        });

        return rootView;

    }

    public void menuRedirect() {


        sharedpreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("usertype")) {

            usertype = sharedpreferences.getString("usertype", "");

            id = sharedpreferences.getString("usersubid", "");
            Log.i("value of sub id", id);
            Log.i("value of sub id", usertype);
            Log.i("value menu sub id", msmmenuidforsubmenuclick);

        }
        sharedpreferancevalue = usertype + id + msmmenuidforsubmenuclick;

        submenuvalue = getActivity().getSharedPreferences(sharedpreferancevalue, Context.MODE_PRIVATE);
        if (submenuvalue.contains("menu")) {

            usertype = submenuvalue.getString("menu", "");


            submenuvalue = getActivity().getSharedPreferences("fragmentvalueif", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = submenuvalue.edit();

            editor.putString("fragmentvalue", sharedpreferancevalue);

            editor.commit();

            Intent i = new Intent(getActivity(), Offlinesubmenuclick.class);
            startActivity(i);


        } else {


            submenuvalue = getActivity().getSharedPreferences("fragmentvalueelse", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = submenuvalue.edit();

            editor.putString("fragmentvalue", msmmenuidforsubmenuclick);

            editor.commit();

            Intent i = new Intent(getActivity(), onlinesubmenuclick.class);
            startActivity(i);
        }


    }

}
