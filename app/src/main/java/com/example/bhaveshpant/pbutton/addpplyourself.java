package com.example.bhaveshpant.pbutton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class addpplyourself extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saver(View v) {
        email = (EditText) findViewById(R.id.email2);
        phone = (EditText) findViewById(R.id.phone2);
        name = (EditText) findViewById(R.id.name2);
        String remail;
        String rname;
        String rphone;
        remail = email.getText().toString();
        rname = name.getText().toString();
        rphone = phone.getText().toString();
        if (checker(rname, remail, rphone)) {
            sqldb1(rname, remail, rphone);
            addpplyourself.this.finish();

        } else {
            if(name.equals(""))
            Toast.makeText(getApplicationContext(), "Name can't be left blank", Toast.LENGTH_SHORT).show();
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(remail).matches()){
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();

            }

        }


    }


    public void sqldb1(String name2, String email2, String phone2) {
        if (email2.equals("")) {
            email2 = "NO EMAIL";
        }
        if (phone2.equals("")) {
            phone2 = "NO PHONE NUMBER";
        }
        final SQLiteDatabase db = openOrCreateDatabase("panicb", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Pabutton(Name VARCHAR,Email VARCHAR,Phone VARCHAR)");
        String quer = "INSERT INTO Pabutton VALUES('" + name2 + "','" + email2 + "','" + phone2 + "');";
        db.execSQL(quer);
        db.close();
    }


    public boolean checker(String name2, String email2, String phone2) {
        if (!TextUtils.isEmpty(name2) && isValidMobile(phone2) && vaildemail(email2)) {
            Log.e("Checker", "Valu is" + String.valueOf(vaildemail(email2)));
            return true;
        } else {
            Log.e("Checker", "Valu is" + String.valueOf(vaildemail(email2)));

            return false;

        }
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
        alertDialog.setMessage("Name cannot be left blank");
        alertDialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private boolean isValidMobile(String phone) {
        if (phone.equals("")) {
            return true;
        } else
            return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public boolean vaildemail(String c) {
        if (c.equals("")) {
            return true;
        }
        else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(c).matches();


    }



}