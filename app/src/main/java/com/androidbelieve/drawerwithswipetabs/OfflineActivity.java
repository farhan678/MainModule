package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageButton;
import android.widget.TextView;

public class OfflineActivity extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    SharedPreferences sharedpreferences;


    ImageButton logout;
    TextView username,toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        sharedpreferences = getSharedPreferences("cms", Context.MODE_PRIVATE);


        toolbar_title=(TextView)findViewById(R.id.toolbar_title);



        NavigationView navigationView = (NavigationView) findViewById(R.id.shitstuff);
        Menu menu = navigationView.getMenu();
        MenuItem nav_camara = menu.findItem(R.id.usrname);




        if (sharedpreferences.contains("username")) {

            String  usertype = sharedpreferences.getString("username", "");


            String code= sharedpreferences.getString("code", "");

            nav_camara.setTitle(code+"-"+usertype);

        }



        sharedpreferences = getSharedPreferences("submenutitile", Context.MODE_PRIVATE);


        if (sharedpreferences.contains("submenutitile")) {

            String  usertype = sharedpreferences.getString("submenutitile", "");


            toolbar_title.setText(usertype);

        }






        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new UpdatesFragment()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                if (menuItem.getItemId() == R.id.nav_item_sent) {
                    Intent i=new Intent(OfflineActivity.this,SettingActivity.class);
                    startActivity(i);

                }

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    Intent i=new Intent(OfflineActivity.this,HomeActivity.class);
                    startActivity(i);
                }
                if (menuItem.getItemId() == R.id.nav_item_help) {
                    Intent i=new Intent(OfflineActivity.this,HelpActivity.class);
                    startActivity(i);
                }
                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new LogoutFragment()).commit();
                }
                return false;
            }

        });

        /**
         *
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }




}







