package com.example.kyle.bjjlogapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by Kyle on 5/15/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "student.db";
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Date";
    public static final String COL_3 = "subsHit";
    public static final String COL_4 = "subsHitBy";
    public static final String COL_5 = "Hours";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER, Date TEXT PRIMARY KEY, subsHit TEXT, subsHitBy TEXT, Hours TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertData(String date, int subPosition, String subSelection) {
        String[] columns = new String[]{COL_3, COL_4, COL_5};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, date);
        //grab the current value of the column if not null, and add it into the content values
        contentValues.put(columns[subPosition], subSelection);

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Date = '" +date + "'", null);
        cursor.moveToFirst();
        //Log.d(itemName, "here it is");
        //boolean exists = (cursor.getCount() > 0);
        if (exists(date) == true){
            String itemName = cursor.getString(cursor.getColumnIndex(columns[subPosition]));
            ContentValues cv = new ContentValues();
            cv.put(COL_2, date);
            if(itemName == null) {
                cv.put(columns[subPosition], subSelection);
            }else{
                try {
                    int numTest = Integer.parseInt(subSelection);
                    cv.put(columns[subPosition], subSelection);
                } catch (NumberFormatException e) {
                    cv.put(columns[subPosition], subSelection + ", " + itemName);
                }
            }
            db.update(TABLE_NAME, cv, "Date ='" + date + "'", null);
        }else {
            db.replace(TABLE_NAME, null, contentValues);
        }
        cursor.close();

    }

    public boolean doesDatabaseExist(Context context) {

        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    public boolean exists (String date) {
        boolean exists;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Date = '" +date + "'", null);
        cursor.moveToFirst();

        exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public String getColumnValue(String date, int columnPosition) {
        String[] columns = new String[]{COL_3, COL_4, COL_5};
        String itemName2 = "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Date = '" +date + "'", null);
        cursor.moveToFirst();
        if(columnPosition == 0) {
            itemName2 = cursor.getString(cursor.getColumnIndex("subsHit"));
        } else if (columnPosition == 1) {
            itemName2 = cursor.getString(cursor.getColumnIndex("subsHitBy"));
        } else if (columnPosition == 2) {
            itemName2 = cursor.getString(cursor.getColumnIndex("Hours"));
        }
        cursor.close();
        return itemName2;
    }

    public float getColumnAverage(String sub, String column) {
        SQLiteDatabase db = this.getWritableDatabase();
        float average;
        long numRows = getNumOfRows();
        Log.d(Long.toString(numRows), "ROWNUM");


        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + column + " LIKE " + "'%" + sub + "%'", null);
        cursor.moveToFirst();
        average = cursor.getInt(0);
        //need this to make sure there are no errors if there is no database
        if (numRows != 0) {
            average = average/numRows;
        } else {
            average = 0;
        }



        float subNumAvg = 0;

        for (int i = 0; i < getNumOfRows(); i++) {
            int columnInt = 0;
            if(column.equals("SubsHit")) {
                columnInt = 0;
            } else if (column.equals("SubsHitBy")) {
                columnInt = 1;
            }
            int subNum = getSubmissionPerDay(sub, columnInt, getRowAndDate(i+1));
            Log.d(getRowAndDate(i+1), "ROWNUM");
            if (subNum > 0) {
                int newSubNum = subNum - 1;
               // int newSubNum = subNum;
                subNumAvg += newSubNum;
            }


        }
        subNumAvg = subNumAvg/numRows;


        //return subNumAvg;
        return average + subNumAvg;
       // return average;
    }

    public long getNumOfRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public int getSubmissionPerDay(String sub, int columnPosition, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        int numOfSubs = 0;
        String subList = "";
        String cellString ="";

        Cursor cursor = db.rawQuery("select * FROM " + TABLE_NAME + " WHERE Date = '" +date + "'", null);
        cursor.moveToFirst();
        if(columnPosition == 0) {
            cellString = cursor.getString(cursor.getColumnIndex("subsHit"));
        } else if (columnPosition == 1) {
            cellString = cursor.getString(cursor.getColumnIndex("subsHitBy"));
        } else if (columnPosition == 2) {
            cellString = cursor.getString(cursor.getColumnIndex("Hours"));
        }

        subList = cellString;

        if (subList != null) {
            numOfSubs = getRepetitions(subList, sub);
        }
        return numOfSubs;
    }

    public String getLastestDate() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where rowid = (Select max(rowid) from " + TABLE_NAME + ")", null);
        cursor.moveToFirst();
        String latestDate = cursor.getString(cursor.getColumnIndex("Date"));
        Log.d(latestDate,"DATE");

        return latestDate;
    }

    public int getRepetitions(String s, String rep) {
        int count = 0;
        int index = s.indexOf(rep);
        while (index != -1) {
            count++;
            s = s.substring(index + 1);
            index = s.indexOf(rep);
        }
        //numOfSubs = 2f;
        return count;
    }

    public String getRowAndDate(int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where rowid = '" + row + "'", null);
        cursor.moveToFirst();
        Log.d(cursor.toString(), "DATE");
        String date = cursor.getString(cursor.getColumnIndex("Date"));
        return date;
    }

}




