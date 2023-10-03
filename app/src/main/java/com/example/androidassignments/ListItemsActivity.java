package com.example.androidassignments;
import static android.content.Intent.ACTION_CAMERA_BUTTON;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;


public class ListItemsActivity extends AppCompatActivity {
    private static final String TAG = "ListItemsActivity";
    private static final int CAMERA_REQUEST_CODE = 180;
    private Button backbutton;
    private ImageButton imagebutton;
    private Switch S;
    private CheckBox CB;

    private String s1, s2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap capturedBitmap = (Bitmap) extras.get("data");
                if (capturedBitmap != null) {
                    ImageButton IB = findViewById(R.id.imageButton);
                    IB.setImageBitmap(capturedBitmap);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        s1=getString(R.string.ON);
        s2=getString(R.string.OFF);
       imagebutton = findViewById(R.id.imageButton);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CI = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(CI, CAMERA_REQUEST_CODE);
            }
        });

        backbutton = findViewById(R.id.button12);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                {
                    Intent intent = new Intent(ListItemsActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        S = findViewById(R.id.switch1);
        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnCheckedChanged();
            }
        });
       CB = findViewById(R.id.checkBox);
        CB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog();
            }
        });
    }
    private void AlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        builder.setMessage(R.string.dialog_message)

                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent(  );
                        resultIntent.putExtra("Response", "ListItemsActivity passed: my information to share");
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
}
    private void print(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void setOnCheckedChanged(){

        if(S.isChecked())
        {
            Toast.makeText(this, s1,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, s2, Toast.LENGTH_LONG).show();
        }

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
}




