package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidbelieve.drawerwithswipetabs.Model.Labour;
import com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter.cLabourAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SubContractFragment extends Fragment {
    RecyclerView recyclerView;
    cLabourAdapter cLabourAdapterr;
    ArrayList<Labour> labourArrayList = new ArrayList<>();
    private ProgressDialog pDialog;
    public static Activity activity;

    FloatingActionButton mBtnAddLabour;
    ApiCall mApiCall;

    public SubContractFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_labour, container, false);
        mApiCall = new ApiCall(getActivity());
        mApiCall.varAssign();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cLabourAdapterr = new cLabourAdapter(labourArrayList, getActivity().getApplicationContext());
        recyclerView.setAdapter(cLabourAdapterr);

        mBtnAddLabour = (FloatingActionButton) rootView.findViewById(R.id.mBtnAddLabour);
        mBtnAddLabour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntAddLabour = new Intent(getActivity(), AddLabourActivity.class);
                startActivity(mIntAddLabour);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (mBtnAddLabour.isShown()) {
                        mBtnAddLabour.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!mBtnAddLabour.isShown()) {
                        mBtnAddLabour.show();
                    }
                }
            }
        });

        new mGetLabourListingTask().execute();
        new mGetAllTask().execute();

        return rootView;
    }

    private class mGetLabourListingTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = mApiCall.doGetLabourListing();
            return result;
        }

        protected void onPostExecute(String result) {
            hideProgressDialog();
            if (result != null) {

                Log.v("Result","Result : "+ result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    if (jsonLabourData.getString("status").equals("1")){
                        JSONArray jsonLabourArray = jsonLabourData.getJSONArray("result");
                        for (int i=0; i<jsonLabourArray.length(); i++){
                            JSONObject jsonLabourObject = (JSONObject) jsonLabourArray.get(i);
                            labourArrayList.add(new Labour(
                                    jsonLabourObject.getString("batch_id"),
                                    jsonLabourObject.getString("project_number"),
                                    jsonLabourObject.getString("vendor_name"),
                                    jsonLabourObject.getString("reported_date"),
                                    jsonLabourObject.getString("remarks"),
                                    jsonLabourObject.getString("custom_batch_id")
                            ));
                        }
                        cLabourAdapterr.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private class mGetAllTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = mApiCall.doGetAllProjectData();
            return result;
        }

        protected void onPostExecute(String result) {
            hideProgressDialog();
            if (result != null) {

                Log.v("Result","Result get all data : "+ result);

                FetchLabourInfoService service = new FetchLabourInfoService();
                service.transferData(result);

            }
        }
    }




    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
}
