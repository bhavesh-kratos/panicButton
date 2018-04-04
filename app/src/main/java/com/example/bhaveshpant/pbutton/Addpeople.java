package com.example.bhaveshpant.pbutton;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.database.sqlite.SQLiteDatabase.openDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Addpeople extends Fragment  {
    private static String DB_PATH = "/data/data/com.example.bhaveshpant.pbutton/databases/";

    private static final String DATABASE_NAME = "panicb";
    static final String TABLE_Name = "tableName";
    static SQLiteDatabase sqliteDataBase;


    public Addpeople() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.addpeople, container, false);
        ScrollView backgrnd = (ScrollView) view.findViewById(R.id.mybackgrnd);

        final com.getbase.floatingactionbutton.FloatingActionsMenu floatingActionsMenu=(com.getbase.floatingactionbutton.FloatingActionsMenu) view.findViewById(R.id.multiple_actions);

        backgrnd.setOnTouchListener(new View.OnTouchListener() {
              @Override
                  public boolean onTouch(View v, MotionEvent event) {
                  if(floatingActionsMenu.isExpanded()){
                      floatingActionsMenu.collapse();
                         return true;}
                  return false;
             }

            });



        if(tablechecker()){

            View container1=view.findViewById(R.id.container);

            String myPath = DB_PATH + DATABASE_NAME;
            sqliteDataBase = openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor c=sqliteDataBase.rawQuery("SELECT * FROM Pabutton",null);
            c.moveToLast();
            do{
                final View newView = inflater.inflate(R.layout.row,null);



                final TextView nameadd = (TextView) newView.findViewById(R.id.name4);
                TextView phoneadd = (TextView) newView.findViewById(R.id.phone4);
                TextView emailadd = (TextView) newView.findViewById(R.id.email4);
                    nameadd.setText(c.getString(c.getColumnIndex("Name")));
                phoneadd.setText(c.getString(c.getColumnIndex("Phone")));
                emailadd.setText(c.getString(c.getColumnIndex("Email")));

                    Button buttonRemove = (Button) newView.findViewById(R.id.remove);
                    buttonRemove.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String na=nameadd.getText().toString();
                            ((LinearLayout) newView.getParent()).removeView(newView);
                            removed(na);

                        }
                    });

                ((LinearLayout) container1).addView(newView,0);
                }while(c.moveToPrevious());
            sqliteDataBase.close();
            }

        else{}





/*


//action b add from cont
        final FloatingActionButton actionB = (FloatingActionButton) view.findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionB.setTitle("Action B clicked");
            }
        });


//action c a


      /* ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));
        ((FloatingActionButton) view.findViewById(R.id.setter_drawable)).setIconDrawable(drawable);


//action add by yourself
        final FloatingActionButton actionA = (FloatingActionButton) view.findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).apyourself();
            }
        });
*/
        // Test that FAMs containing FABs with visibility GONE do not cause crashes
        //view.findViewById(R.id.button_gone).setVisibility(View.GONE);



        return view;
    }
    public boolean tablechecker(){


        String myPath = DB_PATH + DATABASE_NAME;
        sqliteDataBase = openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        String count="SELECT count(*) FROM Pabutton";
        Cursor mcursor=sqliteDataBase.rawQuery(count,null);
        mcursor.moveToFirst();
        int icount=mcursor.getInt(0);
        sqliteDataBase.close();
        if(icount>0)
            return true;
        else
            return false;
    }

    public void removed(String n){
        SQLiteDatabase db;
        String myPath = DB_PATH + DATABASE_NAME;
        db = openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        db.execSQL("delete from Pabutton where Name='"+n+"'");
        db.close();
    }

}
