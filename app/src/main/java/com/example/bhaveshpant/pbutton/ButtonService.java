package com.example.bhaveshpant.pbutton;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class ButtonService extends Service {


    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */

    AudioManager mAudioManager;
    Handler mHandler;
    Context c;
    int prevvol;
    String link;
    String lat, lon;
    String phoneNo;
    String msg,msgg;


    private ContentObserver mVolumeObserver = new ContentObserver(mHandler) {
        Vibrator vibe;

        @Override
        public void onChange(final boolean selfChange) {
            super.onChange(selfChange);

            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            final int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
                Log.e("TEST2","VOLUME STREAM IS : " + volume);
                Log.e("TESTtt","prev VOLUME STREAM IS : " + prevvol);

                final int delta=prevvol-volume;
                if((delta>=0 || delta<0)&& delta!=2 &&delta!=-2)
                {
                    prevvol=volume;
                }

                    if (delta==2) {
                    Log.e("Testing1 ::::", "Succesful!!!!!!!!");
                        vibe.vibrate(100);
                        prevvol=volume;
                        callMe1();

                    } else if (delta==-2) {
                        vibe.vibrate(100);
                    Log.e("Testing2 ::::", "Succesful!!!!!!!!");


                        prevvol=volume;
                        callMe1();
                }

                Log.e("TESTt333"," new  prev VOLUME STREAM IS : " + prevvol);


        }
    };
 // SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver(c,mHandler);

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean msgset = preferences.getBoolean("choose_message", false);

        String message12 = preferences.getString("message_set", "");
        if(msgset){
            msg=message12;
        }
        else
            msg="";


       // this.getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
       prevvol= mAudioManager.getStreamVolume(AudioManager.STREAM_RING);

        Log.e("TEST1","VOLUME STREAM IS : " + prevvol);
        Uri uri = android.provider.Settings.System.CONTENT_URI;
        getContentResolver().registerContentObserver(uri, true, mVolumeObserver);

    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Toast.makeText(this, "3 tap service is ON", Toast.LENGTH_SHORT).show();
        gpsturner();
        return START_STICKY;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        Toast.makeText(this, "3 tap service is OFF", Toast.LENGTH_SHORT).show();
        getContentResolver().unregisterContentObserver(mVolumeObserver);
    }

    public void callMe1(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean textch = preferences.getBoolean("text", false);
        boolean emailch = preferences.getBoolean("email", false);
        boolean callch = preferences.getBoolean("call", false);


        final SharedPreferences profile=getSharedPreferences("settingpref",0);
        String emailcheckid=profile.getString("mailid","");
        String emailpassword=profile.getString("mailpass","");
        if(tablechecker()){
            if(emailch && emailcheckid != ""){
                sendEmail1(emailcheckid,emailpassword);
            }

            if(callch) {
                callPlace();
            }
            if(textch) {
                sending();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Add People First",Toast.LENGTH_SHORT).show();

        }

    }


    public void gpsturner(){
        GPSTracker gps = new GPSTracker(this);
        if (gps.isGPSEnabled) {

            lat = String.valueOf(gps.getLatitude());
            lon = String.valueOf(gps.getLongitude());
            link = "http://maps.google.com/maps?q=loc:" +lat+","+lon;

        } else if(gps.isNetworkEnabled) {

            lat = String.valueOf(gps.getLatitude());
            lon = String.valueOf(gps.getLongitude());
            link = "http://maps.google.com/maps?q=loc:" +lat+","+lon;

        }
        else{
            link="My GPS is OFF";
            Toast.makeText(this,"GPS is Off, location won't be sent", Toast.LENGTH_SHORT).show();

        }

    }

    public  void callPlace() {

        final SQLiteDatabase db1 = openOrCreateDatabase("panicb", MODE_PRIVATE, null);

        Cursor cu = db1.rawQuery("SELECT * FROM Pabutton", null);
        cu.moveToFirst();
        do {
            phoneNo = cu.getString(cu.getColumnIndex("Phone"));
            if (!phoneNo.equals("NO PHONE NUMBER")) {
                break;
            }
        } while (cu.moveToNext());
        if (phoneNo.equals("NO PHONE NUMBER")) {
            Toast.makeText(getApplicationContext(), "Can't place call", Toast.LENGTH_SHORT).show();
        }
        else {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNo));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);

        }
    }

    public void sending() {
        //for adding multiple contacts
        final SQLiteDatabase db1=openOrCreateDatabase("panicb",MODE_PRIVATE,null);
        long cnt= DatabaseUtils.queryNumEntries(db1,"Pabutton");
        int n=(int)cnt;
        final String[] mailer=new String[n];



        Cursor cu=db1.rawQuery("SELECT * FROM Pabutton",null);
        cu.moveToFirst();
        int i;
        i=0;
        String mailp;
        do{
            mailp=cu.getString(cu.getColumnIndex("Phone"));
            if(mailp.equals("NO PHONE NUMBER")){
                continue;
            }
            else{
                mailer[i]=mailp;
                i++;
            }
        }while (cu.moveToNext());
        int size=0;

        final int r=size;
        Log.e("Checking values","value of size is"+r);
        Log.e("Checking values","value of mailer " +
                "is"+mailer[0]);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean msgset = preferences.getBoolean("choose_message", false);

        String message12 = preferences.getString("message_set", "");
        if(msgset){
            msg=message12;
        }
        else
            msg="";

        //for message
        msgg = msg+"\n"+"This is my location \n"+link;
//checking permissions
        if(mailer[0]==null){
            Toast.makeText(getApplicationContext(), "Can't send message", Toast.LENGTH_SHORT).show();

        }

        else {
            try {


                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPostExecute(Void result) {
                        Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();

                        super.onPostExecute(result);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {

                            SmsManager smsManager = SmsManager.getDefault();
                            int op = 0;
                            while (mailer[op] != null) {
                                smsManager.sendTextMessage(mailer[op], null, msgg, null, null);
                                op++;
                            }

                        } catch (Exception e) {
                            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                            Log.e("textsms:::", "Could not send text message", e);

                        }


                        return null;
                    }
                }.execute();
            } catch (Exception e) {
                Log.e("exception name ", "Cant be processed", e);
            }
        }

       /*
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        else {
            SmsManager smsManager = SmsManager.getDefault();

            for(int o=0;o<r-1;o++) {
                smsManager.sendTextMessage(mailer[i], null, msgg, null, null);
            }

            Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();

        }
        */
    }

    public boolean tablechecker(){
        final SQLiteDatabase db1=openOrCreateDatabase("panicb",MODE_PRIVATE,null);
        String count="SELECT count(*) FROM Pabutton";
        Cursor mcursor=db1.rawQuery(count,null);
        mcursor.moveToFirst();
        int icount=mcursor.getInt(0);
        if(icount>0)
            return true;
        else
            return false;
    }

    public void sendEmail1(String mailid,String mailpassword) {
        final Mail m = new Mail(mailid,mailpassword);
        final SQLiteDatabase db1=openOrCreateDatabase("panicb",MODE_PRIVATE,null);
        long cnt= DatabaseUtils.queryNumEntries(db1,"Pabutton");
        int n=(int)cnt;
        String[] mailer=new String[n+1];

        if(tablechecker()){

            Cursor cu=db1.rawQuery("SELECT * FROM Pabutton",null);
            cu.moveToFirst();
            int i;
            i=0;
            String mailp;
            do{
                mailp=cu.getString(cu.getColumnIndex("Email"));
                if(mailp.equals("NO EMAIL")){

                    Log.e("checking emaillll","statement executed");
                    continue;
                }
                else{
                    mailer[i]=mailp;
                    i++;
                }
            }while (cu.moveToNext());
        }
        int k=0;
        while(mailer[k]!=null){
            k++;
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean msgset = preferences.getBoolean("choose_message", false);

        String message12 = preferences.getString("message_set", "");
        if(msgset){
            msg=message12;
        }
        else
            msg="";

        m.setTo(mailer,k-1);
        m.setFrom(mailid);
        m.setSubject("Panic Button");
        m.setBody(msg+"\n"+"This is my location \n"+link);



        try {



            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPostExecute(Void result) {
                    Toast.makeText(getApplicationContext(),
                            "Your Email was sent successfully.",
                            Toast.LENGTH_SHORT).show();

                    super.onPostExecute(result);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {

                        if(m.send()) {
                            Toast.makeText(getApplicationContext(), "Email was sent successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Email was not sent.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                        Log.e("MailApp", "Could not send email", e);

                    }


                    return null;
                }
            }.execute();
        }
        catch (Exception e){
            Log.e("exception name ","Cant be processed",e);
        }






    }



    /*  @Override
    public void onCreate() {
       SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver(this,new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver(this,new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );


        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver(this,new Handler());

        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);

    }*/
}
