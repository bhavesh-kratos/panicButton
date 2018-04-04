package com.example.bhaveshpant.pbutton;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.facebook.AccessToken;


/**
 * A simple {@link Fragment} subclass.
 */
/*
public class Settings extends android.support.v4.app.Fragment {


    public Settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction2=getFragmentManager().beginTransaction();
        android.app.Fragment thirdfrag=new settin();
        fragmentTransaction2.add(R.id.setting1
                ,thirdfrag,"settin" ).commit();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings1, container, false);
    }
    */

    public class Settings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        public Settings(){}
        CheckBoxPreference pref;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settingpref);
            PreferenceManager.setDefaultValues(getActivity().getApplicationContext(),R.xml.settingpref,false);
             pref = (CheckBoxPreference) findPreference("checkbox_preference");
            SharedPreferences profile=getActivity().getSharedPreferences("settingpref",0);
            String emailcheckid=profile.getString("mailid","");
           //for enabling and disabling email
            boolean t;
            if(emailcheckid=="") {
                t=false;
            }else
            t=true;
                getPreferenceScreen().findPreference("email").setEnabled(t);
            //for enabling fb

            boolean fbc;
            if(AccessToken.getCurrentAccessToken()==null) {
                fbc=false;
            }else
                fbc=true;
            getPreferenceScreen().findPreference("faceb").setEnabled(fbc);

        }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("volpress")) {
            boolean test = sharedPreferences.getBoolean("volpress", false);
            //Do whatever you want here. This is an example.
            if (test) {

                Intent intent = new Intent(getActivity(), ButtonService.class);
                getActivity().startService(intent);
            } else {
                Intent intent = new Intent(getActivity(), ButtonService.class);
                getActivity().stopService(intent);
            }

        }
        if (key.equals("powerpress")) {
            boolean test = sharedPreferences.getBoolean("powerpress", false);
            //Do whatever you want here. This is an example.
            if (test) {

                Intent intent = new Intent(getActivity(), PowerService.class);
                getActivity().startService(intent);
            } else {
                Intent intent = new Intent(getActivity(), PowerService.class);
                getActivity().stopService(intent);
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }



}
