package com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;


public class cAddLabourDetailHolder extends RecyclerView.ViewHolder {

    TextView cDescription,cNoOfLabour,cRemark,cSrNo;

    public cAddLabourDetailHolder(View itemView) {
        super(itemView);

        cDescription = (TextView) itemView.findViewById(R.id.txtdescription);
        cNoOfLabour = (TextView) itemView.findViewById(R.id.txtNoOfLabour);
       // cRemark = (TextView) itemView.findViewById(R.id.txtRemark);
        cSrNo = (TextView) itemView.findViewById(R.id.txtsr_no);
    }
}

