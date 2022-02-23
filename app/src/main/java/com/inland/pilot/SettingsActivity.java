package com.inland.pilot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.inland.pilot.Login.LoginActivity;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    RadioButton genderradioButton;
    RadioGroup radioGroup;
    Context context;
    Resources resources;
    SharedPreferences prefSettings;
    String KEY_ACTIVITY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        KEY_ACTIVITY = getIntent().getStringExtra("activity");
        prefSettings = getSharedPreferences("AppSettings",MODE_PRIVATE);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
    }

    public void submitLanguage(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        if(selectedId==-1){
            Toast.makeText(SettingsActivity.this,"Nothing selected", Toast.LENGTH_SHORT).show();
        }
        else if(genderradioButton.getText().toString().contains("English")){
            prefSettings.edit().putString("Language", "en").commit();
        }
        else if(genderradioButton.getText().toString().contains("हिंदी")){
            prefSettings.edit().putString("Language", "hi").commit();
        }
        else if(genderradioButton.getText().toString().contains("मराठी")){
            prefSettings.edit().putString("Language", "mr").commit();
        }
        if(KEY_ACTIVITY.equalsIgnoreCase("Nav"))
        startActivity(new Intent(this,NavigationActivity.class));
        else if(KEY_ACTIVITY.equalsIgnoreCase("Login"))
            startActivity(new Intent(this, LoginActivity.class));

        finish();
    }

    @Override
    public void onBackPressed() {
        if(KEY_ACTIVITY.equalsIgnoreCase("Nav"))
            startActivity(new Intent(this,NavigationActivity.class));
        else if(KEY_ACTIVITY.equalsIgnoreCase("Login"))
            startActivity(new Intent(this, LoginActivity.class));

        finish();
    }
}