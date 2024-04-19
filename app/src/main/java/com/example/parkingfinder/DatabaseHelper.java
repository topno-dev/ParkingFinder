package com.example.parkingfinder;

import static android.content.ContentValues.TAG;

import com.example.parkingfinder.frags.parking_list;
import com.example.parkingfinder.frags.vehicle;

import android.content.ContentValues;
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
        Cursor c = db.rawQuery("SELECT * from parking WHERE lat BETWEEN "+latitude+" -0.01 AND "+latitude+"+0.01 AND long BETWEEN "+longitude+" -0.01 AND "+longitude+" +0.01;" ,null);
//        Example query because of the default location being USA.
//        Cursor c = db.rawQuery("SELECT * FROM parking WHERE lat BETWEEN 18.5131587397036 - 0.01 AND 18.5131587397036 + 0.01 AND long BETWEEN 73.92643376963856 - 0.01 AND 73.92643376963856 + 0.01;",null);
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

    public boolean createUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        values.put("user_type", "user");

        long result = db.insert("User", null, values);
        db.close();

        return result != -1; // Returns true if insertion was successful, false otherwise
    }


    public double getRate(){
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(mContext);
        return sharedPreferencesManager.getParkingRate();
    }
    public boolean verifyUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("User",
                new String[]{"username"},
                "username = ? AND password = ?",
                new String[]{username, password},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public boolean createVehicle(String username, String vehicle_name, String vehicle_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("vehicle_name", vehicle_name);
        values.put("vehicle_number", vehicle_number);

        long result = db.insert("Vehicle", null, values);
        db.close();

        return result != -1; // Returns true if insertion was successful, false otherwise
    }

    public boolean createPaymentHistory(String username, String vehicle_number, int auto_id, String startTime, String endTime, String date) {
        // Get the rate from SharedPreferences
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(mContext);
        double rate = sharedPreferencesManager.getParkingRate();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("vehicle_number", vehicle_number);
        values.put("auto_id", auto_id);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        values.put("date", date);
        values.put("rate", rate);

        long result = db.insert("PaymentHistory", null, values);
        if (result != -1) {
            ContentValues parkingValues = new ContentValues();
            parkingValues.put("space_avail", "space_avail + 1"); // Increment space_avail by 1
            String[] whereArgs = {String.valueOf(auto_id)};
            Cursor c = db.rawQuery("UPDATE parking SET space_avail = space_avail + 1 WHERE auto_id = ?", whereArgs);
            c.close();
            db.close();
            return true;
//            if (updateResult == 1) {
//                // Successfully updated parking table
//                return true;
//            } else {
//                // Error updating parking table
//                return false;
//            }
        } else {
            // Error inserting into PaymentHistory table
            db.close();
            return false;
        }
        // Returns true if insertion was successful, false otherwise
    }


    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause
        String selection = "username = ?";
        // Define the selection arguments
        String[] selectionArgs = { username };

        // Perform the deletion
        int deletedRows = db.delete("your_table_name", selection, selectionArgs);

        db.close();
        if (deletedRows > 0) {
            return true;

        } else {
            return false;
        }

        // Close the database connection
    }

    public List<vehicle> selectVehiclesByUsername(String username) {
        List<vehicle> vehicles = new ArrayList<vehicle>();

        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] projection = {
                "vehicle_name",
                "vehicle_number"
        };

        String selection = "username = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query("Vehicle", projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String vehicleName = cursor.getString(cursor.getColumnIndexOrThrow("vehicle_name"));
            String vehicleNumber = cursor.getString(cursor.getColumnIndexOrThrow("vehicle_number"));

            vehicle vehicle = new vehicle(vehicleName, vehicleNumber, R.drawable.baseline_directions_car_24);
            vehicles.add(vehicle);
        }

        cursor.close();
        db.close();

        return vehicles;
    }

    public List<parking_list> getParkingHistory(String username) {
        List<parking_list> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT auto_id FROM PaymentHistory WHERE username = ?", new String[]{username});
        while (c.moveToNext()) {
            int auto_id = c.getInt(c.getColumnIndex("auto_id"));
            Cursor parkingCursor = db.rawQuery("SELECT * FROM parking WHERE auto_id = ?", new String[]{String.valueOf(auto_id)});
            if (parkingCursor.moveToFirst()) {
                String category = parkingCursor.getString(parkingCursor.getColumnIndex("category"));
                String address = parkingCursor.getString(parkingCursor.getColumnIndex("address"));
                int space_avail = parkingCursor.getInt(parkingCursor.getColumnIndex("space_avail"));
                String phone = parkingCursor.getString(parkingCursor.getColumnIndex("phone"));
                double lat = parkingCursor.getDouble(parkingCursor.getColumnIndex("lat"));
                double longi = parkingCursor.getDouble(parkingCursor.getColumnIndex("long"));
                String url = parkingCursor.getString(parkingCursor.getColumnIndex("url"));
                items.add(new parking_list(category, address, space_avail, phone, R.drawable.logo, auto_id, lat, longi, url));
            }
            parkingCursor.close();
        }
        c.close();
        db.close();
        return items;
    }









}

