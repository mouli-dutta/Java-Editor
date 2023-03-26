package com.javaeditor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

public class CodeDatabase extends SQLiteOpenHelper {
    private final Context context;
    private final static String DATABASE_NAME = "CodeDatabase.db";
    private final static int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "MyCodes";
    private static final String COLUMN_ID = "_id";

    private static final String COLUMN_CODE_TITLE = "code_title";
    private static final String COLUMN_CODE_DESCRIPTION = "code_des";

    public CodeDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CODE_TITLE + " TEXT, " +
                    COLUMN_CODE_DESCRIPTION + " TEXT);";

            db.execSQL(query);

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void addData(String title, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CODE_TITLE, title);
        contentValues.put(COLUMN_CODE_DESCRIPTION, code);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Added code", Toast.LENGTH_SHORT).show();
    }

    public void updateData(String row_id, String title, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CODE_TITLE, title);
        contentValues.put(COLUMN_CODE_DESCRIPTION, code);

        int update = db.update(TABLE_NAME, contentValues, "_id=?", new String[]{row_id});
        if (update == -1) Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "Updated Code", Toast.LENGTH_SHORT).show();
    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
