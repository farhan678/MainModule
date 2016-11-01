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

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    SharedPreferences sharedpreferences;
    String hposition,yposition,zposition,symbol,logintime;
     Menu menu;
ImageButton logout;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Bean bean=new Bean();
//
//
//        String t=bean.getSub_id();
//      Log.i("bean value ",t)  ;

        sharedpreferences = getSharedPreferences("cms", Context.MODE_PRIVATE);



        NavigationView navigationView = (NavigationView) findViewById(R.id.shitstuff);
        Menu menu = navigationView.getMenu(); zposition="07";
         MenuItem nav_camara = menu.findItem(R.id.usrname);




        if (sharedpreferences.contains("username")) {

          String  usertype = sharedpreferences.getString("username", "");
            yposition="9016";
            hposition=sharedpreferences.getString("loginidofuser", "");
            String code= sharedpreferences.getString("code", "");
            symbol=sharedpreferences.getString("s", "");
            logintime=sharedpreferences.getString("logintime", "");
            nav_camara.setTitle(code+"-"+usertype);

        }


        sharedpreferences = getSharedPreferences("userdetail", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("loginidofuser",yposition+symbol+zposition+symbol+hposition);
        editor.putString("logintime",logintime);
        editor.commit();






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
        mFragmentTransaction.replace(R.id.containerView,new PrimaryFragment()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                if (menuItem.getItemId() == R.id.nav_item_sent) {
                   Intent i=new Intent(MainActivity.this,SettingActivity.class);
                    startActivity(i);

                }

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    Intent i=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(i);
                }
                if (menuItem.getItemId() == R.id.nav_item_help) {
                    Intent i=new Intent(MainActivity.this,HelpActivity.class);
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


    public void onBackPressed() {


        Toast.makeText(getApplicationContext(), "You Are On Home Page Already", Toast.LENGTH_LONG).show();

    }

    }







