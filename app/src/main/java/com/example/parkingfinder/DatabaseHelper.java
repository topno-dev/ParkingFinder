package com.example.parkingfinder;

import static android.content.ContentValues.TAG;

import com.example.parkingfinder.frags.parking_list;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Layout;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION  = 1;
    private static final String DB_NAME = "parking.db";
    private static final String DB_PATH = "/data/user/0/com.example.parkingfinder/databases/";
    SQLiteDatabase myDataBase;
    private final Context mContext;


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME  , factory, DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean checkDatabase(){
        try {
            final String mPath = DB_PATH + DB_NAME;
            final File file = new File(mPath);
            if (file.exists()){
                return true;
            } else {
                return false;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void copyDatabase() throws IOException{
        try {
            InputStream mInputStream = mContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0){
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createDatabase() throws IOException{
        boolean mDatabaseExist = checkDatabase();
        if (!mDatabaseExist){
            this.getReadableDatabase();
            this.close();
            try {
                copyDatabase();
            } catch (IOException mIOException){
                mIOException.printStackTrace();
                throw new Error("Error copying Database");
            } finally {
                this.close();
            }
        }
    }

    @Override
    public synchronized void close(){
        if(myDataBase != null){
            myDataBase.close();
        }
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    public void loadHandler(){
        try{
            createDatabase();
        } catch (IOException e){
            e.printStackTrace();
        }
//        String result = "";
//        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor c = db.rawQuery("SELECT * from parking",null);

//        while (c.moveToNext()){
//            int result_id = c.getInt(0);
//            String result_name = c.getString(1);
//            result += String.valueOf(result_id) + " " + result_name + System.getProperty("line.separator");
//        }
//        c.close();
//        db.close();
//
//        return result;
    }

    public List<parking_list> getNearbyParking(double latitude, double longitude){
        List<parking_list> items = new ArrayList<parking_list>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.w("Query","SELECT * from parking WHERE lat BETWEEN "+latitude+" -0.01 AND "+latitude+"+0.01 AND long BETWEEN "+longitude+" -0.01 AND "+longitude+" +0.01;");
//        Cursor c = db.rawQuery("SELECT * from parking WHERE lat BETWEEN "+latitude+" -0.01 AND "+latitude+"+0.01 AND long BETWEEN "+longitude+" -0.01 AND "+longitude+" +0.01;" ,null);
//        Example query because of the default location being USA.
        Cursor c = db.rawQuery("SELECT * FROM parking WHERE lat BETWEEN 18.5131587397036 - 0.01 AND 18.5131587397036 + 0.01 AND long BETWEEN 73.92643376963856 - 0.01 AND 73.92643376963856 + 0.01;",null);
        while (c.moveToNext()){
//            int result_id = c.getInt(0);
            String category = c.getString(1);
            String address = c.getString(5);
            int space_avail = c.getInt(4);
            String phone = c.getString(7);
            int auto_id = c.getInt(0);
            double lat = c.getDouble(2);
            double longi = c.getDouble(3);
            String url = c.getString(6);
            items.add(new parking_list(category,address,space_avail,phone,R.drawable.logo,auto_id, lat, longi, url));
//            result += String.valueOf(result_id) + " " + result_name + System.getProperty("line.separator");
        }
        c.close();
        db.close();

        return items;
    }



}

