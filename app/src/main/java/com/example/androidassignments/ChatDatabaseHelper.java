package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Messages.db";
    public static final int VERSION_NUM = 3;
    public static final String TABLE_NAME = "table_name";
    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "message_text";

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ChatDatabaseHelper", "OnCreate is called");

        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_MESSAGE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("ChatDatabaseHelper", "onUpgrade calls, oldVersion=" + oldVersion + " newVersion=" + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public static boolean VerifyDatabaseName(String string){
        return string.equals(DATABASE_NAME);
    }

}
