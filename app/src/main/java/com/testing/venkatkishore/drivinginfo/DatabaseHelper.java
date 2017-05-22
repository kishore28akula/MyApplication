package com.testing.venkatkishore.drivinginfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Akula on 05-05-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DrivingInformation";

    private static final String TABLE_DRIVINGINFO = "drivinginfo";

    //Registration table one

    public static final String FIRST_NOW = "now";
    public static final String FIRST_FILENAME = "imagefileinfo";


    private final ArrayList<Drivingimagelistdetails> driving_oneinfo = new ArrayList<Drivingimagelistdetails>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FIRST_TABLE = "CREATE TABLE if not exists "
                + TABLE_DRIVINGINFO + " (" + FIRST_NOW + ","
                + FIRST_FILENAME +   ")";

        db.execSQL(CREATE_FIRST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVINGINFO);


        onCreate(db);
    }


    public void Add_preslocInboxDetails(Drivingimagelistdetails drivingimagelistdetails) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cvmsg = new ContentValues();
        cvmsg.put(FIRST_NOW,
                drivingimagelistdetails.getNowinfo());
        cvmsg.put(FIRST_FILENAME,
                drivingimagelistdetails.getImagefileinfo());

        // Inserting Row
        db.insert(TABLE_DRIVINGINFO, null, cvmsg);


    }

    public ArrayList<Drivingimagelistdetails> Get_firstOutbox() {
        try {
            driving_oneinfo.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_DRIVINGINFO;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Drivingimagelistdetails firstbox = new Drivingimagelistdetails();
                    firstbox.setNowinfo(""+cursor.getString(0));
                    firstbox.setImagefileinfo(""+cursor.getString(1));

                    // Adding contact to list
                    driving_oneinfo.add(firstbox);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return driving_oneinfo;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("all_contact", "" + e);
        }

        return driving_oneinfo;
    }
}
