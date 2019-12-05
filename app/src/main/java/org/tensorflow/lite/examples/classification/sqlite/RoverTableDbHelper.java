package org.tensorflow.lite.examples.classification.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.tensorflow.lite.examples.classification.model.MissionDataObject;
import org.tensorflow.lite.examples.classification.model.RoverDataObject;

import java.util.ArrayList;
import java.util.List;

public class RoverTableDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME= "RoverTabe.db";

    public RoverTableDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase rdb) {
        rdb.execSQL(RoverTable.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase rdb, int oldVersion, int newVersion) {

        rdb.execSQL(RoverTable.SQL_DELETE_ENTRIES);
        onCreate(rdb);
    }

    public long insertRoverData(RoverDataObject data){
        ContentValues values = new ContentValues();
        values.put(RoverTable.RoverEntry.COLUMN_ROVER_ID, data.getRover_id());
        values.put(RoverTable.RoverEntry.COLUMN_ROVER_NAME, data.getRover_Name());

        SQLiteDatabase rdb = getWritableDatabase();
        try{
            return rdb.insert(RoverTable.RoverEntry.TABLE_NAME, null, values);
        }
        finally {
            rdb.close();
        }
    }


    public List<RoverDataObject> getRoverData(){
        List<RoverDataObject> rResult = new ArrayList<>();
        SQLiteDatabase rdb = getReadableDatabase();
        try{
            Cursor cursor = rdb.query(RoverTable.RoverEntry.TABLE_NAME,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                RoverDataObject rdo = new RoverDataObject();
                rdo.setRover_id(cursor.getInt(cursor.getColumnIndex(RoverTable.RoverEntry.COLUMN_ROVER_ID)));
                rResult.add(rdo);
            }
            cursor.close();
        } finally {
            rdb.close();
        }
        return rResult;
    }
}
