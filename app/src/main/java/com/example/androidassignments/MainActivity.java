package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button back, button,startbtn,toolbarbtn,weatherbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        weatherbtn = findViewById(R.id.weatherforecast);
        weatherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startweatherforecast();
            }
        });

        back = findViewById(R.id.button2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent,10);

                }
            }
        });
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        startbtn =findViewById(R.id.startchat);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startchat();
            }
        });
        toolbarbtn = findViewById(R.id.testoolbar);
        toolbarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testtoolbar();
            }
        });
    }

public void startweatherforecast(){
        Intent intent = new Intent(this, WeatherForecast.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            Log.i("MAIN_ACTIVITY", "Returned to MainActivity.onActivityResult");
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, data.getStringExtra("Response"), Toast.LENGTH_LONG).show();
            }
        }
    }
    public void testtoolbar(){
        Intent intent = new Intent(this, TestToolbar.class);
        startActivity(intent);
    }
    public void startchat(){
        Intent intent = new Intent(this, ChatWindowActivity.class);
        startActivityForResult(intent,10);
        Log.i(TAG,"User clicked Start Chat");
    }

    public void openActivity() {
        Intent intent = new Intent(this,ListItemsActivity.class);
        startActivityForResult(intent,10);
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, "Resume function called");
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "Start function called");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.i(TAG, "Pause function called");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG, "Stop function called");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "Destroy function called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        super.onSaveInstanceState(savedInstance);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstance){
        super.onRestoreInstanceState(savedInstance);
    }

    private void print(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
