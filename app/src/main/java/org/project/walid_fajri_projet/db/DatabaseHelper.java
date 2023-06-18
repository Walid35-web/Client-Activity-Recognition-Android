package org.project.walid_fajri_projet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.project.walid_fajri_projet.entities.Activity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ayoub_akanoun_projet.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table allusers(email TEXT primary key, password TEXT,firstlog boolean, name Text,genre Text ,phone Text, major Text, image BLOB)");
        db.execSQL("create Table activity(id INTEGER PRIMARY KEY AUTOINCREMENT,user TEXT, type TEXT, startdate TEXT, enddate TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists allusers");
        db.execSQL("drop Table if exists activity");
    }

    public Boolean insertData(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("firstlog", true);
        contentValues.put("name", "");
        contentValues.put("phone", "");
        contentValues.put("major", "");
        contentValues.put("genre", "");
        long result = db.insert("allusers", null, contentValues);
        return result != -1;
    }
    
    public Long addActivity(Activity activity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user", activity.getUser());
        contentValues.put("type", activity.getTypeActivity());
        contentValues.put("startdate", activity.getStartDate());
        contentValues.put("enddate", "");
        Long result = db.insert("activity",null, contentValues);
        return result;
    }

    public int updateEndDate(String endDate, Long id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("enddate", endDate);
        return db.update("activity", contentValues, "id = ?", new String[]{String.valueOf(id)});
    }

    public String[] getLastActivity(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select id,user,type,startdate,enddate from activity order by id desc limit 1", null);
        String []data = new String[5];
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
           data[0]= cursor.getString(0);
           data[1]= cursor.getString(1);
           data[2]= cursor.getString(2);
           data[3]= cursor.getString(3);
           data[4]= cursor.getString(4);
        }
        return data;
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from allusers where email = ?", new String[] {email});
        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from allusers where email = ? and password = ?", new String[] {email, password});
        return cursor.getCount() > 0;
    }

    public Boolean checkfirtslog(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from allusers where email = ? and firstlog = ?", new String[] {email, "1"});
        return cursor.getCount() > 0;
    }
    //        db.execSQL("create Table allusers(email TEXT primary key, password TEXT,firstlog boolean, name Text,genre Text ,phone Text, major Text)");
    public String[] getData(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from allusers where email = ?", new String[] {email});
        String[] data = new String[7];
        if (cursor.moveToFirst()) {
            data[0] = cursor.getString(0);
            data[1] = cursor.getString(1);
            data[2] = cursor.getString(3);
            data[3] = cursor.getString(4);
            data[4] = cursor.getString(5);
            data[5] = cursor.getString(6);
        }
        return data;
    }
    
    public List<Activity> getActivities(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM activity", null);
        List<Activity> activities = new ArrayList<>();
        int culumnIndex ;
        while (cur.moveToNext()){
            culumnIndex = cur.getColumnIndexOrThrow("user");
            String user = cur.getString(culumnIndex);
            culumnIndex = cur.getColumnIndexOrThrow("type");
            String typeActivity = cur.getString(culumnIndex);
            culumnIndex = cur.getColumnIndexOrThrow("startdate");
            String startdate = cur.getString(culumnIndex);
            culumnIndex = cur.getColumnIndexOrThrow("enddate");
            String enddate = cur.getString(culumnIndex);
            activities.add(new Activity(user, typeActivity, startdate, enddate));
        }
        return activities;
    }

    public int saveEditedData(String email,String password, String name, String genre, String phone, String major){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstlog", false);
        contentValues.put("password", password);
        contentValues.put("name", name);
        contentValues.put("genre", genre);
        contentValues.put("phone", phone);
        contentValues.put("major", major);
        return db.update("allusers", contentValues, "email = ?", new String[] {email});
    }

    public void updateImage(String email, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", image);
        db.update("allusers", contentValues, "email = ?", new String[] {email});
    }

    public byte[] getImage(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select image from allusers where email = ?",new String[]{email});
        byte[] imageByte= null;
        if (cursor.moveToLast())
            imageByte= cursor.getBlob(0);
        return imageByte;
    }
}
