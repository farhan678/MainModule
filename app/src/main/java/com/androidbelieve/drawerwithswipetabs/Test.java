package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import session.Sessiontimeout;
/**
 * Created by Maslin-Android on 5/5/2016.
 */
public class Test {

    private static Context con;

    Date dNow = new Date( );


    SimpleDateFormat ft =
            new SimpleDateFormat("yyyy-MM-dd ");



    String strDate = ft.format(dNow);



    public void obj(Context c){
        this.con=c;


    }

    public  static  void  check(){

       timer.cancel();
timer.start();
        Log.i("is calling ", "calling");

    }


  static   CountDownTimer timer = new CountDownTimer(Sessiontimeout.times*120*1000, 1000) {

        public void onTick(long millisUntilFinished) {
            //Some code
        }

        public void onFinish() {

            click();

          Log.i("is working","working");

        }

    };


    public  static void click(){
        timer.cancel();
        Test tt=new Test();
       tt. logout();
    }


    public void logout(){
        Log.i("seseeion","session");
        Intent i=new Intent(con,PopActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        con.startActivity(i);


    }



    public static void logfinal(){

        timer.cancel();
        Test tt=new Test();
        tt. logout();
    }


}
