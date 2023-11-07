package com.example.androidassignments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.appcompat.widget.Toolbar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

public class ChatWindowActivity extends AppCompatActivity {
    private ChatDatabaseHelper dbh;
    private SQLiteDatabase database;
    private static final String ACTIVITY_NAME = "ChatWindow";
    private ListView listview;
    private Button sendbutton;
    private EditText edittext;
    private ArrayList<String> chatmessages;
    private ChatAdapter adapter;
    private class ChatAdapter extends ArrayAdapter<String>{
        private final ArrayList<String> string2;
        private Context ct;
        public ChatAdapter(@NonNull Context context, ArrayList<String> string1) {
            super(context,0,string1);
            this.string2 = string1;
            this.ct = context;
        }

        @Override
        public int getCount() {
            return string2.size();
        }
        public String getItem(int position){
            return string2.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();
            View result = null;
            if(position%2==0){
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            }
            else {
                result = inflater.inflate(R.layout.chat_row_outgoing,null);
            }
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }
    private Toolbar TB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        sendbutton = findViewById(R.id.sendbutton);
        edittext = findViewById(R.id.edittext);
        listview = findViewById(R.id.chatlistview);
        chatmessages = new ArrayList<String>();
        adapter = new ChatAdapter(this, chatmessages);
        listview.setAdapter(adapter);
        dbh = new ChatDatabaseHelper(this);
        database = dbh.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(1));
            chatmessages.add(
                    cursor.getString(1)

            );

            adapter.notifyDataSetChanged();
        }

        Log.i(ACTIVITY_NAME, "Column count of cursor is =" + cursor.getColumnCount());

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column name: " + cursor.getColumnName(i));
        }

        cursor.close();



        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"send button callled");
                String message = edittext.getText().toString();
                if (!message.isEmpty()) {
                    chatmessages.add(message);
                    ContentValues values = new ContentValues();
                    values.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                    database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
                    adapter.notifyDataSetChanged();
                    edittext.setText("");

                }
            }
        });

        TB= findViewById(R.id.toolbar);
        setSupportActionBar(TB);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onDestroy() {
        super.onDestroy();

        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Handle the back button click (e.g., navigate back)
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}





