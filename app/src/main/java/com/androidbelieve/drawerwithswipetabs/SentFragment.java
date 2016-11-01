package com.androidbelieve.drawerwithswipetabs;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.library.Changepassword_layout;

/**
 * Created by Ratan on 7/29/2015.
 */
public class SentFragment extends Fragment {
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    Test t;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();





        View rootView = inflater.inflate(R.layout.sent_layout, container, false);
        TextView password=(TextView)rootView.findViewById(R.id.password);
        TextView email=(TextView)rootView.findViewById(R.id.email);
        t=new Test();
        Test.check();

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.check();
                t.obj(getActivity());
                Intent i=new Intent(getActivity(),PasswordActivity.class);
                startActivity(i);


            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.check();
                t.obj(getActivity());
                Intent i=new Intent(getActivity(),EmailActivity.class);
                startActivity(i);

            }
        });

        return rootView;
    }








}
