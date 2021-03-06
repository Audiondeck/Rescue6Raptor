package org.tensorflow.lite.examples.classification.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.SensorDataObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




public class SensorReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SensorReader.db";


    public SensorReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SensorReaderContract.SQL_CREATE_ENTRIES);


    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SensorReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Insert sensor data object in to SQLite Table
     * @param data
     * @return
     */
    public long insertSensorData(SensorDataObject data){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_ACC_X, data.getAccx());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_ACC_Y, data.getAccy());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_ACC_Z, data.getAccz());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_LIGHT, data.getLight());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_RELATIVE_HUMIDITY, data.getRelativeHumidity());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_PRESSURE, data.getPressure());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_TEMPERATURE, data.getAmbient_temp());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_LATITUDE, data.getLatitude());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_LONGITUDE, data.getLongitude());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_ALTITUDE, data.getAltitude());
        values.put(SensorReaderContract.SensorEntry.COLUMN_NAME_BATTERY, data.getU_batttery_level());

        SQLiteDatabase db = getWritableDatabase();
        try {
            // Insert the new row, returning the primary key value of the new row
            return db.insert(SensorReaderContract.SensorEntry.TABLE_NAME, null, values);
        } finally {
            db.close();
        }
    }

    /**
     * Read data from SQLite table. Helps to show the list on the mobile screen
     * @return list of sensor objects
     */
    public List<SensorDataObject> getSensorData(){
        List<SensorDataObject> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try{
            Cursor cursor = db.query(SensorReaderContract.SensorEntry.TABLE_NAME, null,null,null, null, null, null);
            while (cursor.moveToNext()) {
                SensorDataObject sdo = new SensorDataObject();
                sdo.setAccx(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_ACC_X)));
                sdo.setAccy(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_ACC_Y)));
                sdo.setAccz(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_ACC_Z)));
                sdo.setLight(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_LIGHT)));
                sdo.setPressure(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_PRESSURE)));
                sdo.setRelativeHumidity(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_RELATIVE_HUMIDITY)));
                sdo.setAmbient_temp(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_TEMPERATURE)));
                sdo.setLatitude(cursor.getDouble(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_LATITUDE)));
                sdo.setLongitude(cursor.getDouble(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_LONGITUDE)));
                sdo.setAltitude(cursor.getDouble(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_ALTITUDE)));
                sdo.setU_batttery_level(cursor.getFloat(cursor.getColumnIndex(SensorReaderContract.SensorEntry.COLUMN_NAME_BATTERY)));
                result.add(sdo);
            }
            cursor.close();
        } finally {
            db.close();
        }
        // reverse the data to show the latest inserted object first
        Collections.reverse(result);
        return result;
    }
}