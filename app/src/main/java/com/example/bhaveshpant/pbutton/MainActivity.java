package com.example.bhaveshpant.pbutton;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * For using this class please add activation.jar, additionnal.jar and mail.jar
 * file to your build path
 */


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;
    String lat, lon;
    String link,link1;
    String phoneNo;
    String msgg;
    Activity context;
    Vibrator vibe;
    String msg;
    Bundle sav;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final int finalloc = 101;
    private static final int finalloc2 = 102;
    private static final int smsperm = 103;
    private static final int teleperm = 104;
    private static final int contactperm = 105;


    private static String TAG = "Permissions Check";




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sav=savedInstanceState;
        setContentView(R.layout.activity_main);

        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);

        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager=getSupportFragmentManager();
        if(fragmentManager.findFragmentById(R.id.content_frame)==null)
        {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            navigationView.setCheckedItem(R.id.panicbuttn);

        }
        gpschecker();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=",getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }




        final SQLiteDatabase db=openOrCreateDatabase("panicb",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Pabutton(Name VARCHAR,Email VARCHAR,Phone VARCHAR)");
        db.close();

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("Location", "Permission to location denied");
            makeRequest();
        }
//coarse
        int permission2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            Log.i("Location", "Permission to location denied");
            makeRequest2();
        }

        //sms
        int permission3 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        if (permission3 != PackageManager.PERMISSION_GRANTED) {
            Log.i("Location", "Permission to sms denied");
            makeRequest3();
        }
//TELEPH
        int permission4 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        if (permission4 != PackageManager.PERMISSION_GRANTED) {
            Log.i("Location", "Permission to telephone denied");
            makeRequest4();
        }
        //conatcts
        int permission5 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        if (permission5 != PackageManager.PERMISSION_GRANTED) {
            Log.i("Location", "Permission to telephone denied");
            makeRequest5();
        }




    }

    public void makeRequest5(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_CONTACTS},
                contactperm);
    }


    public void makeRequest4(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},
                teleperm);
    }

    public void makeRequest3(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                smsperm);
    }

    public void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                finalloc);
    }
    public void makeRequest2() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                finalloc2);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, msgg, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case finalloc:{

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
            case finalloc2:{

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
            case smsperm:{

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
            case teleperm:{

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
            case contactperm:{

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.app.Fragment currentfrag=getFragmentManager().findFragmentById(R.id.content_frame);

        if(currentfrag instanceof Settings) {
            getFragmentManager().beginTransaction().remove(currentfrag).commit();
        }
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        if (id == R.id.panicbuttn) {
            setTitle("Panic Button");
                    panicbuttn firstfrag=new panicbuttn();
                    fragmentTransaction.replace(R.id.content_frame
                            ,firstfrag,"panicbutton" ).commit();
        }
        else if (id == R.id.nav_people) {
            setTitle("Add People");
            Addpeople secondfrag=new Addpeople();
            fragmentTransaction.replace(R.id.content_frame
                    ,secondfrag,"addppl").commit();


        }
        else if (id == R.id.nav_settings) {
            android.app.FragmentTransaction fragmentTransaction2=getFragmentManager().beginTransaction();

            setTitle("Settings");

            Settingback fourthfrag=new Settingback();
            fragmentTransaction.replace(R.id.content_frame
                    ,fourthfrag,"Settings").commit();

            android.app.Fragment thirdfrag=new Settings();
            fragmentTransaction2.add(R.id.content_frame
                    ,thirdfrag,"settin" ).commit();

        }
        else if (id == R.id.nav_account) {
            Intent intent;
            intent=new Intent(getApplicationContext(),Accountactivity.class);
            startActivityForResult(intent,150);
        }
        else if (id == R.id.nav_about) {
            setTitle("About");
            aboutfrag sixthfrag=new aboutfrag();
            fragmentTransaction.replace(R.id.content_frame
                    ,sixthfrag,"about").commit();

        }

            nDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        nToggle.syncState();
    }



    public void callMe(View v){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean textch = preferences.getBoolean("text", false);
            boolean emailch = preferences.getBoolean("email", false);
            boolean callch = preferences.getBoolean("call", false);
            boolean fbch = preferences.getBoolean("faceb", false);

        gpsturner();

        vibe.vibrate(100);
        final SharedPreferences profile=getSharedPreferences("settingpref",0);
        String emailcheckid=profile.getString("mailid","");
        String emailpassword=profile.getString("mailpass","");
        if(fbch && AccessToken.getCurrentAccessToken()!=null)
            fb();
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

    public void fb(){
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentTitle("testing ")
                .setContentDescription("testonly")
                .setContentUrl(Uri.parse(link1))
                .build();
        ShareApi.share(shareLinkContent,null);
        Toast.makeText(getApplicationContext(),"Posted on Facebook",Toast.LENGTH_SHORT).show();

    }


    public  void callPlace() {

        final SQLiteDatabase db1=openOrCreateDatabase("panicb",MODE_PRIVATE,null);

        Cursor cu=db1.rawQuery("SELECT * FROM Pabutton",null);
        cu.moveToFirst();
        do {
            phoneNo = cu.getString(cu.getColumnIndex("Phone"));
            if(!phoneNo.equals("NO PHONE NUMBER")){
                break;
            }
        } while(cu.moveToNext());
        if(phoneNo.equals("NO PHONE NUMBER"))
        {
            Toast.makeText(getApplicationContext(), "Can't place call", Toast.LENGTH_SHORT).show();
        }
        else {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNo));

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

        Log.e("value of k:::",Integer.toString(k));
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


    public void apyourself(View v){
        final com.getbase.floatingactionbutton.FloatingActionsMenu floatingActionsMenu=(com.getbase.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.multiple_actions);
        floatingActionsMenu.collapse();
        Intent intent;
        intent=new Intent(getApplicationContext(),addpplyourself.class);
        startActivityForResult(intent,50);


    }

    public void apbycontact(View v){

        final com.getbase.floatingactionbutton.FloatingActionsMenu floatingActionsMenu=(com.getbase.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.multiple_actions);
        floatingActionsMenu.collapse();
        Intent intentContact=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intentContact,100);
    }

    @Override
    public void onActivityResult(int reqCode,int resultCode,Intent data){
        super.onActivityResult(reqCode,resultCode,data);

        if(reqCode==50){

                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();

                setTitle("Add People");
                Addpeople secondfrag=new Addpeople();
                fragmentTransaction.replace(R.id.content_frame
                        ,secondfrag,"addppl").commit();
            }
        if(reqCode==100){
            if (resultCode== Activity.RESULT_OK){
                Uri contactData=data.getData();
                Cursor cursor=getContentResolver().query(contactData,null,null,null,null);

                if(cursor.moveToFirst()){
                    //id for future use
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    //name
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    //phone num
                    String phonenumber;
                    phones.moveToFirst();
                    if(phones.moveToFirst())
                    phonenumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    else
                    phonenumber="NO PHONE NUMBER";

                    String emailAddress;
                    Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                     emails.moveToFirst();
                    if(emails.moveToFirst())
                        emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    else
                        emailAddress="NO EMAIL";
                    emails.close();
                    sqldb1(name,emailAddress,phonenumber);
                }
//refreshing frag
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();

                setTitle("Add People");
                Addpeople secondfrag=new Addpeople();
                fragmentTransaction.replace(R.id.content_frame
                        ,secondfrag,"addppl").commit();

            }
        }

        else if(reqCode==150){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            Fragment frag=getSupportFragmentManager().findFragmentByTag("panicbutton");

            boolean check=frag instanceof panicbuttn;
            if(!check){
                android.app.Fragment currentfrag=getFragmentManager().findFragmentById(R.id.content_frame);

                if(currentfrag instanceof Settings) {
                    getFragmentManager().beginTransaction().remove(currentfrag).commit();
                }
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                onOptionsItemSelected(navigationView.getMenu().getItem(0));


            }
        }
    }

    public void sqldb1(String name2,String email2,String phone2){
        final SQLiteDatabase db=openOrCreateDatabase("panicb",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Pabutton(Name VARCHAR,Email VARCHAR,Phone VARCHAR)");
        String quer="INSERT INTO Pabutton VALUES('" +name2+ "','"+email2+ "','"+phone2+"');";
        db.execSQL(quer);
        db.close();
    }


       /*Mandrill
    public void sendEmail() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void result) {
                Toast.makeText(MainActivity.this,
                        "Your message was sent successfully.",
                        Toast.LENGTH_SHORT).show();

                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... params) {


                String respond = POST(
                        URL,
                        makeMandrillRequest("notoriouskratos@gmail.com", "bp3955@gmail.com","bhavesh", "fdsfsdfsdf", "dfsdfd"));
                Log.d("respond is ", respond);


                return null;
            }
        }.execute();
    }

    //*********method to post json to uri
    public String POST(String url, JSONObject jsonObject) {
        InputStream inputStream = null;
        String result = "";
        try {


            Log.d("internet json ", "In post Method");
            // 1. create HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = "";

            // 3. convert JSONObject to JSON to String
            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);

            // 4. set httpPost Entity
            httpPost.setEntity(se);

            // 5. Set some headers to inform server about the type of the
            // content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 6. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 7. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 8. convert inputstream to string
            if (inputStream != null) {
                result = convertStreamToString(inputStream);
            } else {
                result = "Did not work!";
                Log.d("json", "Did not work!");
            }

    //*****************TO create email json
    private JSONObject makeMandrillRequest(String from, String to, String name,
                                           String text, String htmlText) {

        JSONObject jsonObject = new JSONObject();
        JSONObject messageObj = new JSONObject();
        JSONArray toObjArray = new JSONArray();
        JSONArray imageObjArray = new JSONArray();
        JSONObject imageObjects = new JSONObject();
        JSONObject toObjects = new JSONObject();

        try {
            jsonObject.put("key", "********************");

            messageObj.put("html", htmlText);
            messageObj.put("text", text);
            messageObj.put("subject", "testSubject");
            messageObj.put("from_email", from);
            messageObj.put("from_name", name);

            messageObj.put("track_opens", true);
            messageObj.put("tarck_clicks", true);
            messageObj.put("auto_text", true);
            messageObj.put("url_strip_qs", true);
            messageObj.put("preserve_recipients", true);

            toObjects.put("email", to);
            toObjects.put("name", name);
            toObjects.put("type", "to");

            toObjArray.put(toObjects);

            messageObj.put("to", toObjArray);
            if (encodedImage != null) {
                imageObjects.put("type", "image/png");
                imageObjects.put("name", "IMAGE");
                imageObjects.put("content", encodedImage);

                imageObjArray.put(imageObjects);
                messageObj.put("images", imageObjArray);
            }

            jsonObject.put("message", messageObj);

            jsonObject.put("async", false);


            Log.d("Json object is ", " " + jsonObject);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }

*/



   /* private void sendMail() {
        String host = "smtp.gmail.com";
        String from = "notoriouskratos@gmail.com";//To set Users ID
        String userId = "notoriouskratos@gmail.com";
        String rec = "notoriouskratos@gmail.com";
        String password = "kinggoogle1";

        try {
            Toast.makeText(this, "Altitude-"  + "\n" + "Longitude-" + lon + "\n" + "Latitude-" + lat + "\n"
                    + "Accuracy-"  + "\n", Toast.LENGTH_SHORT).show();

            Properties p = new Properties();
            p.put("mail.smtp.host", host);
            p.put("mail.smtp.socketFactory.port", "465");
            p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.port", "465");
            p.put("mail.transport.protocol", "smtp");
            p.put("mail.smtp.user", userId);
            p.put("mail.smtp.password", password);


            Session session = Session.getDefaultInstance(p);
            MimeMessage message = new MimeMessage(session);

            Console con = System.console();
            String to = con.readLine("welham_622@yahoo.com:\n");
            String subject = con.readLine("LOCATION:\n");
            String msg = con.readLine("Altitude-"  + "\n" + "Longitude-" + lon + "\n" + "Latitude-" + lat + "\n" + "Accuracy-" );

            InternetAddress fromAdr = new InternetAddress(from);
            InternetAddress toAdr = new InternetAddress(to);

            message.setFrom(fromAdr);
            message.setRecipient(Message.RecipientType.TO, toAdr);
            message.setSubject(subject);

            MimeMultipart body = new MimeMultipart();
            MimeBodyPart part1 = new MimeBodyPart();
            part1.setText(msg);
            body.addBodyPart(part1);

            message.setContent(body);

            Transport t = session.getTransport("smtps");//name of protocol
            t.connect(host, userId, password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();

            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            System.out. println(e);
        }
    }
    public void checker(){
        try{
            sendMail();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

        public void lets(View v){
            // retrieve the configuration values from preferences

                         final GmailSender sender = new GmailSender("notoriouskratos@gmail.com", "kinggoogle1");

            String toArr = {"bp3955@gmail.com"};
            sender.setTo(toArr);
            sender.setFrom("wooo@wooo.com");
            sender.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
            sender.setBody("Email body.");

            new AsyncTask<Void, Void, Void>() {
                @Override public Void doInBackground(Void... arg) {
                    try {

                        if(sender.send()) {
                            Toast.makeText(this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                        } else {
                             Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG).show();
                        }
                    } catch(Exception e) {
                        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                        Log.e("MailApp", "Could not send email", e);
                    }
                }
                return null;}
            }.execute();


                    }
                    */
public void gpschecker(){
    String lat1,lon2;
    GPSTracker gps2 = new GPSTracker(this);

    lat1 = String.valueOf(gps2.getLatitude());
    lon2 = String.valueOf(gps2.getLongitude());
    if (gps2.isGPSEnabled) {
        Toast.makeText(this, "Latitude=" + lat1 + " Longitude=" + lon2, Toast.LENGTH_SHORT).show();

    } else if(gps2.isNetworkEnabled) {
        Toast.makeText(this, "Latitude=" + lat1 + " Longitude=" + lon2, Toast.LENGTH_SHORT).show();

    }
    else{
            gps2.showSettingsAlert();
    }


}

public void gpsturner(){
    GPSTracker gps = new GPSTracker(this);
    lat = String.valueOf(gps.getLatitude());
    lon = String.valueOf(gps.getLongitude());
    link = "http://maps.google.com/maps?q=loc:" +lat+","+lon;
    link1=link;
    if (gps.isGPSEnabled) {

    } else if(gps.isNetworkEnabled) {

    }
    else{

        link = "GPS is OFF";

    }


}
    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    public void onBackPressed(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment frag=getSupportFragmentManager().findFragmentByTag("panicbutton");
        boolean check=frag instanceof panicbuttn;
        if(!check){
            android.app.Fragment currentfrag=getFragmentManager().findFragmentById(R.id.content_frame);

            if(currentfrag instanceof Settings) {
                getFragmentManager().beginTransaction().remove(currentfrag).commit();
            }

            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            navigationView.setCheckedItem(R.id.panicbuttn);



        }
        else
            finish();

    }

}


