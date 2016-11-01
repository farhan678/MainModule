package com.androidbelieve.drawerwithswipetabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.library.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import urllink.Url;

// geeting submenu values from server
public class SocialFragment extends Fragment {
    ListView list;
    TextView ver;
    String usertype;
    String id, msm_menu_id, msmmenuidforsubmenuclick, sharedpreferancevalue;
    SharedPreferences sharedpreferences;

    SharedPreferences fragmentvalueelse;
    SharedPreferences submenuvalue;
    StringBuilder savethevalueofmsmprogramname = new StringBuilder();

    StringBuilder menunamelocalsave = new StringBuilder();

    StringBuilder msm_menu_idsubmenu = new StringBuilder();
    private static final String TAG_VER = "msm_menu_id";
    private static final String TAG_NAME = "msm_menu_name";

    String user_subid, user_usertype, user_usersubid;

    JSONArray android = null;

    UserDatabaseHandler db;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Test t;


    Date valuecfirst;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    Date valuesecond;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Test.check();
        t = new Test();
        t.obj(getActivity());

        View rootView = inflater.inflate(R.layout.social_layout, container, false);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();


        ver = (TextView) rootView.findViewById(R.id.vers);
        list = (ListView) rootView.findViewById(R.id.list);


        db = new UserDatabaseHandler(getActivity());


        sharedpreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("usertype")) {

            user_usertype = sharedpreferences.getString("usertype", "");

            user_usersubid = sharedpreferences.getString("usersubid", "");
            user_subid = sharedpreferences.getString("subid", "");

        }


        fragmentvalueelse = getActivity().getSharedPreferences("fragmentvalueelse", Context.MODE_PRIVATE);

        if (fragmentvalueelse.contains("fragmentvalue")) {

            msm_menu_id = fragmentvalueelse.getString("fragmentvalue", "");


        } else {

            Log.i("msm_menu_id", msm_menu_id);

        }


        sharedpreferences = getActivity().getSharedPreferences("cms", Context.MODE_PRIVATE);
        if (sharedpreferences.contains("usertype")) {

            usertype = sharedpreferences.getString("usertype", "");

            id = sharedpreferences.getString("usersubid", "");


            Log.i("value of sub id", id);
            Log.i("value of sub id", usertype);


        }

        // create sharedprefences in based (usertype+id+msm_menu_id) and store the submenu values

        submenuvalue = getActivity().getSharedPreferences(usertype + id + msm_menu_id, Context.MODE_PRIVATE);

        // store the sharedprefences name  in database for logout action delete the sharedprefences names
        db.addContact(new UserInformation(usertype + id + msm_menu_id, "tablenames", "msm_menu_id"));


        new JSONParse().execute();


        list.setOnScrollListener(new AbsListView.OnScrollListener() {
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


    // getting value from server

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            JSONObject json = null;

            try {
                json = jParser.getJSONFromUrl(Url.url + "usr_user_type=" + usertype + "&sub_id=" + id + "&msm_menu_id=" + msm_menu_id);
            } catch (Exception e) {


            }
            return json;


        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();


            try {


                sharedpreferences = getActivity().getSharedPreferences("userdetail", Context.MODE_PRIVATE);

                String first = sharedpreferences.getString("loginidofuser", "");

                String second = sharedpreferences.getString("logintime", "");


                Log.i("values", first + second);
                valuecfirst = sdf.parse(first);
                valuesecond = sdf.parse(second);

                if (valuecfirst.compareTo(valuesecond) < 0) {


                } else if (json != null) {


                    JSONArray posts = json.optJSONArray("storeList");
                    // Getting JSON Array from URL

                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject post = posts.optJSONObject(i);


                        String ver = post.optString("msm_menu_name");

                        String msm_prg_name = post.optString("msm_prg_name");

                        String name = post.optString("msm_menu_id");
                        Log.i("msm_prg_name", msm_prg_name);

                        String a = "";
                        if (a.equals(msm_prg_name)) {


                            msm_prg_name = "menu";
                            Log.i("msm_prg_name value", msm_prg_name);


                        }

                        Log.i("string value", ver);

                        menunamelocalsave.append(ver + ",");
                        msm_menu_idsubmenu.append(name + ",");
                        savethevalueofmsmprogramname.append(msm_prg_name + ",");

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_VER, ver);

                        map.put(TAG_NAME, name);
                        map.put("msm_prg_name", msm_prg_name);

                        oslist.add(map);


                        ListAdapter adapter = new SimpleAdapter(getActivity(), oslist,
                                R.layout.list_v,
                                new String[]{TAG_VER}, new int[]{
                                R.id.vers});

                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                //Toast.makeText(SubmenuActivity.this, "You Clicked at " + oslist.get(+position).get("msm_menu_name"), Toast.LENGTH_SHORT).show();
                                String saveprogramname = oslist.get(+position).get("msm_prg_name");
                                msmmenuidforsubmenuclick = oslist.get
                                        (+position).get(TAG_NAME);
                                Test.check();
                                t.obj(getActivity());
                                String save = oslist.get
                                        (+position).get(TAG_VER);

                                Log.i("saveprogramname", saveprogramname);


                               // Toast.makeText(getActivity(), "user_usersubid" + "=" + user_usersubid + "user_subid" + "=" + user_subid + "user_usertype" + "=" + user_usertype,
                               //         Toast.LENGTH_LONG).show();
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
                               /*
                                if (saveprogramname.equals("menu")) {


                                    submenuvalue = getActivity().getSharedPreferences("submenutitile", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = submenuvalue.edit();

                                    editor.putString("submenutitile", save);

                                    editor.commit();


                                    menuredirecttosumenus();
                                } else {
                                    submenuvalue = getActivity
                                            ().getSharedPreferences("msm_prg_name", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor =
                                            submenuvalue.edit();

                                    editor.putString("msm_prg_name",
                                            saveprogramname);

                                    editor.commit();


                                    Intent i = new Intent(getActivity(),
                                            Main2Activity.class);
                                    startActivity(i);
                                }
                                */
                            }
                        });

                    }

                    savevaluemenu(menunamelocalsave, savethevalueofmsmprogramname, msm_menu_idsubmenu);

                } else {

                    Toast.makeText(getActivity(), "Datas not found or Internet Issue", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
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

    // store the sunmenu value in local sharedpreferences
    public void savevaluemenu(StringBuilder menu, StringBuilder msmprogramname, StringBuilder msm_menu_idsubmenus) {


        SharedPreferences.Editor editor = submenuvalue.edit();

        editor.putString("menu", menu.toString());

        editor.putString("msmprogramname", msmprogramname.toString());
        editor.putString("msm_menu_idsubmenus", msm_menu_idsubmenus.toString());

        editor.commit();


    }


    public void menuredirecttosumenus() {


        sharedpreferences = getActivity().getSharedPreferences("cms",
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("usertype")) {

            usertype = sharedpreferences.getString("usertype", "");

            id = sharedpreferences.getString("usersubid", "");
            Log.i("value of sub id", id);
            Log.i("value of sub id", usertype);
            Log.i("value menu sub id", msm_menu_id);

        }
        sharedpreferancevalue = usertype + id + msmmenuidforsubmenuclick;

        submenuvalue = getActivity().getSharedPreferences
                (sharedpreferancevalue, Context.MODE_PRIVATE);

        if (submenuvalue.contains("menu")) {

            usertype = submenuvalue.getString("menu", "");


            submenuvalue = getActivity().getSharedPreferences
                    ("fragmentvalueif", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = submenuvalue.edit();

            editor.putString("fragmentvalue", sharedpreferancevalue);

            editor.commit();

            Intent i = new Intent(getActivity(), Offlinesubmenuclick.class);
            startActivity(i);


        } else {


            submenuvalue = getActivity().getSharedPreferences
                    ("fragmentvalueelse", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = submenuvalue.edit();

            editor.putString("fragmentvalue", msmmenuidforsubmenuclick);

            editor.commit();

            Intent i = new Intent(getActivity(), Offlinesubmenuclick.class);
            startActivity(i);
        }
    }


}
