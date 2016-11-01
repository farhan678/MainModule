package com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidbelieve.drawerwithswipetabs.R;

import java.util.ArrayList;

import com.androidbelieve.drawerwithswipetabs.Model.AddLabourDetailModelClass;


public class cAddLabourDetailAdapter extends RecyclerView.Adapter<cAddLabourDetailHolder> {

    LayoutInflater layoutInflater;
    cAddLabourDetailHolder cAddLabourDetailHolder;
    ArrayList<AddLabourDetailModelClass> cAddDetailModelArray;

    public cAddLabourDetailAdapter(ArrayList<AddLabourDetailModelClass> aAddDetailModelClasses) {
        this.cAddDetailModelArray = aAddDetailModelClasses;
    }

    @Override
    public cAddLabourDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.caddlabourdetail,null);
        cAddLabourDetailHolder = new cAddLabourDetailHolder(view);
        return cAddLabourDetailHolder;
    }

    @Override
    public void onBindViewHolder(cAddLabourDetailHolder holder, int position) {

        AddLabourDetailModelClass modelClass = cAddDetailModelArray.get(position);
        holder.cDescription.setText(modelClass.getcDescription());
        holder.cNoOfLabour.setText(modelClass.getcNoOfLabour());

        holder.cSrNo.setText(""+(position+1));


    }

    @Override
    public int getItemCount() {
        return cAddDetailModelArray.size();
    }
}
