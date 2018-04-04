package com.example.bhaveshpant.pbutton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }
    Context cntx;
    Vibrator vibe;
    long seconds_screenoff;
    long seconds_screenon;
    boolean sent_msg;
    long a,OLD_TIME;
    boolean OFF_SCREEN,ON_SCREEN;
    int n=0;
    @Override
    public void onReceive(final Context context, final Intent intent) {
         cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.v("onReceive", "Power button is pressed.");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            a = System.currentTimeMillis();
            seconds_screenoff = a;
            OLD_TIME = seconds_screenoff;
            OFF_SCREEN = true;


            new CountDownTimer(900, 1) {

                public void onTick(long millisUntilFinished) {


                    if (ON_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {

                            long actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 400  ) {
                                sent_msg = true;
                                n++;
                                if (sent_msg && n==2) {
                                    Log.e("Checker","Value of n"+String.valueOf(n));
                                    Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_SHORT).show();
                                    vibe.vibrate(100);
                                    seconds_screenon = 0 ;
                                    seconds_screenoff = 0 ;
                                    sent_msg = false;
                                    n=0;

                                }
                                else {
                                    }
                            }
                            else{
                                seconds_screenon = 0 ;
                                seconds_screenoff = 0 ;


                            }
                        }
                    }
                }

                public void onFinish() {

                    seconds_screenoff = 0 ;
                    n=0;
                }
            }.start();



        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            a = System.currentTimeMillis();
            seconds_screenon = a;
            OLD_TIME = seconds_screenoff;



            new CountDownTimer(900, 250) {

                public void onTick(long millisUntilFinished) {
                    if (OFF_SCREEN) {
                        if (seconds_screenon != 0 && seconds_screenoff != 0) {
                            long actual_diff = cal_diff(seconds_screenon, seconds_screenoff);
                            if (actual_diff <= 400) {
                                sent_msg = true;
                                n++;

                                if (sent_msg &&n==2) {
                                    Log.e("Checker","Value of n"+String.valueOf(n));
                                    Toast.makeText(cntx, "POWER BUTTON CLICKED 2 TIMES", Toast.LENGTH_SHORT).show();
                                    vibe.vibrate(100);
                                    seconds_screenon = 0 ;
                                    seconds_screenoff = 0 ;
                                    sent_msg = false;
                                    n=0;


                                }
                                else{

                                }
                            }
                            else {
                                seconds_screenon = 0 ;
                                seconds_screenoff = 0 ;

                            }
                        }
                    }

                }

                public void onFinish() {

                    seconds_screenon = 0 ;
                    n=0;
                }
            }.start();



        }
    }

    private long cal_diff(long seconds_screenon2, long seconds_screenoff2) {
        long diffrence;
        if (seconds_screenon2 >= seconds_screenoff2) {
            diffrence = (seconds_screenon2) - (seconds_screenoff2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        } else {
            diffrence = (seconds_screenoff2) - (seconds_screenon2);
            seconds_screenon2 = 0;
            seconds_screenoff2 = 0;
        }

        return diffrence;
    }



}


