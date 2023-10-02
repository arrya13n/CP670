package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Button btn;
    private EditText emailtext,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpref = getSharedPreferences("SP", LoginActivity.MODE_PRIVATE);
        setContentView(R.layout.activity_login);
        emailtext = findViewById(R.id.emailtext);
        String Text = sharedpref.getString("DefaultEmail", "email@domain.com");
        emailtext.setText(Text);
        String pass = sharedpref.getString("defaultpassword", "");
        password =findViewById(R.id.passwordtext);

        btn = findViewById(R.id.button3);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
    }

    public void openActivity() {
        String Text = emailtext.getText().toString();
        String pass = password.getText().toString();
        if(!Text.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(Text).matches() && !pass.isEmpty())
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
            {
                Toast.makeText(this, "Not valid Email and password", Toast.LENGTH_SHORT).show();
            }
        }

    public void onResume(){
        super.onResume();
        Log.i(TAG, "Resume function called");
    }
    public void onStart(){
        super.onStart();
        Log.i(TAG, "Start function called");
    }
    public void onPause(){
        super.onPause();
        Log.i(TAG, "Pause function called");
    }
    public void onStop(){
        super.onStop();
        Log.i(TAG, "Stop function called");
    }
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "Destroy function called");
    }
    public void onSaveInstanceState(Bundle savedInstance){
        super.onSaveInstanceState(savedInstance);
    }
    public void onRestoreInstanceState(Bundle savedInstance){
        super.onRestoreInstanceState(savedInstance);
    }

}