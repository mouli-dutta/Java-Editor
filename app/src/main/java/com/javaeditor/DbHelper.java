package com.javaeditor;

import android.database.Cursor;

import java.util.ArrayList;

public class DbHelper {
    private final ArrayList<UserCode> userCodes;
    private final Cursor cursor;

    public DbHelper(CodeDatabase db) {
        this.userCodes = new ArrayList<>();
        cursor = db.readAllData();
    }

    public boolean isEmptyDb() {
        return cursor.getCount() == 0;
    }

    private void dbDataToList() {
        try {
            // sort the data by newly added item first
            if (cursor.moveToLast()) {
                do {
                    userCodes.add(new UserCode(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                } while (cursor.moveToPrevious());
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            cursor.close();
        }

    }

    public ArrayList<UserCode> getUserCodes() {
        dbDataToList();
        return userCodes;
    }
}
