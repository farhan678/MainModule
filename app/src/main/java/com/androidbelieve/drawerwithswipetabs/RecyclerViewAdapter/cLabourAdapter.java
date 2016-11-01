package com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.androidbelieve.drawerwithswipetabs.ApiCall;
import com.androidbelieve.drawerwithswipetabs.EditLabourActivity;
import com.androidbelieve.drawerwithswipetabs.LabourActivity;
import com.androidbelieve.drawerwithswipetabs.Model.Labour;
import com.androidbelieve.drawerwithswipetabs.R;
import com.androidbelieve.drawerwithswipetabs.ViewLabourActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cLabourAdapter extends RecyclerView.Adapter<cLabourHolder> {

    LayoutInflater layoutInflater;
    cLabourHolder cLabourHolderClass;
    ArrayList<Labour> cLabourArrayList;
    private ProgressDialog pDialog;
    ApiCall mApiCall;
    Context context;
    int positionIndex;
    TextView messageText = null;

    public cLabourAdapter(ArrayList<Labour> cLabourArrayListt, Context context) {
        this.cLabourArrayList = cLabourArrayListt;
        this.context = context;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        mApiCall = new ApiCall(context);
    }

    @Override
    public cLabourHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // set layout for recyclerview cell
        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.clabourdata,null);
        cLabourHolderClass = new cLabourHolder(view);

        return cLabourHolderClass;
    }

    @Override
    public void onBindViewHolder(final cLabourHolder holder, final int position) {

        Labour labour = cLabourArrayList.get(position);
        holder.cBatchID.setText(labour.getCustom_batch_id());
        holder.cProject_NO.setText(labour.getProject_NO());
        holder.cVendor_Name.setText(labour.getVendor_Name());
        holder.cReported_Date.setText(labour.getReported_Date());
        holder.cRemark.setText(labour.getRemark());

        holder.cDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        LabourActivity.activity);

              /*  // set title
                alertDialogBuilder.setTitle(context.getResources().getString(R.string.app_name));*/

                // set dialog message
                alertDialogBuilder
                        .setMessage("Labour Record\n" + "Are you  sure to delete the record\n"+ "Batch Id : " + cLabourArrayList.get(position).getCustom_batch_id())
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                positionIndex = position;
                                new mGetLabourDeleteTask().execute(cLabourArrayList.get(position).getBatchID());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.show();
                messageText = (TextView)alertDialog.findViewById(android.R.id.message);
                messageText.setGravity(Gravity.CENTER);
                // show it
                alertDialog.show();
            }

        });

        holder.cEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditLabourActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Batch_Id",cLabourArrayList.get(position).getBatchID());
                context.startActivity(intent);

            }
        });

        holder.cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewLabourActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Batch_Id",cLabourArrayList.get(position).getBatchID());
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return cLabourArrayList.size();
    }

    private class mGetLabourDeleteTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
           /* LabourActivity.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pDialog.show();
            }
        });*/

        }

        @Override
        protected String doInBackground(String... params) {
            String result = mApiCall.doGetDeleteLabour(params[0]);
            return result;
        }

        protected void onPostExecute(String result) {
          //  pDialog.hide();
            if (result != null) {

                Log.v("Result","Result : "+ result);

                try {
                    JSONObject jsonLabourData = new JSONObject(result);
                    if (jsonLabourData.getString("status").equals("1")){
                        cLabourArrayList.remove(positionIndex);
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
