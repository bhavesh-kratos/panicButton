package com.example.bhaveshpant.pbutton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver2 extends BroadcastReceiver {
    public MyReceiver2() {
    }
 Context cntx;
    Vibrator vibe;
    long seconds_screenoff;
    long seconds_screenon;
    boolean sent_msg;
    long a,OLD_TIME;
    boolean OFF_SCREEN,ON_SCREEN;
    int countPowerOff=0;


    @Override
    public void onReceive(final Context context, final Intent intent) {
        cntx = context;
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.e("onReceive", "Power button is pressed.");


            new CountDownTimer(500, 1) {

                public void onTick(long millisUntilFinished) {

                    if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {


                        countPowerOff++;

                    } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                        if (countPowerOff == 4) {
                            vibe.vibrate(100);
                            Toast.makeText(cntx, "power button clicked 3 times", Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent("done");


                        }

                    } else {
                        countPowerOff = 0;
                    }
                }


                public void onFinish() {

                    countPowerOff = 0;
                }
            }.start();
        }







        }






