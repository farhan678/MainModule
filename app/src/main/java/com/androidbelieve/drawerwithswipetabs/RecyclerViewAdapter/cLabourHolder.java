package com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;


public class cLabourHolder extends RecyclerView.ViewHolder {

    TextView cBatchID,cProject_NO,cVendor_Name,cReported_Date,cRemark;
    ImageView cDelete,cEdit,cView;

    public cLabourHolder(View itemView) {
        super(itemView);

        cBatchID = (TextView) itemView.findViewById(R.id.cBatchID);
        cProject_NO = (TextView) itemView.findViewById(R.id.cProject_NO);
        cVendor_Name = (TextView) itemView.findViewById(R.id.cVendor_Nam);
        cReported_Date = (TextView) itemView.findViewById(R.id.cReported_Date);
        cRemark = (TextView) itemView.findViewById(R.id.cRemark);
        cDelete = (ImageView) itemView.findViewById(R.id.btndelete);
        cEdit = (ImageView) itemView.findViewById(R.id.btnEdit);
        cView = (ImageView) itemView.findViewById(R.id.btnView);
    }
}
