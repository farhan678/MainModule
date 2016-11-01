package com.androidbelieve.drawerwithswipetabs.RecyclerViewAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class Custom_Spinner_Adapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private ArrayList<String> cus_spinner_array;


    public Custom_Spinner_Adapter(Context context, ArrayList<String> cus_spinner_array) {
        this.cus_spinner_array=cus_spinner_array;
        activity = context;
    }

    public int getCount()
    {
        return cus_spinner_array.size();
    }

    public Object getItem(int i)
    {
        return cus_spinner_array.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        txt.setWidth(metrics.widthPixels);
        txt.setGravity(Gravity.LEFT);
        txt.setText(cus_spinner_array.get(position));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.LEFT);
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        txt.setWidth(metrics.widthPixels);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
     //   txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        txt.setText(cus_spinner_array.get(i));
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

}
