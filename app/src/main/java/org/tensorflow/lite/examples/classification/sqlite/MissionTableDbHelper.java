package org.tensorflow.lite.examples.classification.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.tensorflow.lite.examples.classification.model.MissionDataObject;

import java.util.ArrayList;
import java.util.List;

public class MissionTableDbHelper  extends SQLiteOpenHelper {
    //Database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MissionTable.db";

    public MissionTableDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase mdb) {
        mdb.execSQL(MissionTable.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase mdb, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        mdb.execSQL(MissionTable.SQL_DELETE_ENTRIES);
        onCreate(mdb);
    }

    public void onDowngrade(SQLiteDatabase mdb, int oldVersion, int newVersion) {
        onUpgrade(mdb, oldVersion, newVersion);
    }

    public long insertMissionData(MissionDataObject data) {

        ContentValues values = new ContentValues();
        values.put(MissionTable.MissionEntry.COLUMN_NAME_MISSION_DURATION, data.getU_mission_duration());
        values.put(MissionTable.MissionEntry.COLUMN_NAME_MISSION_Length, data.getU_grid_length());
        values.put(MissionTable.MissionEntry.COLUMN_NAME_MISSION_WIDTH, data.getU_grid_width());
        values.put(MissionTable.MissionEntry.COLUMN_NAME_MISSION_ID, data.getU_mission_id());


        SQLiteDatabase mdb = getWritableDatabase();
        try{
            // Insert the new row, returning the primary key value of the new row
            return mdb.insert(MissionTable.MissionEntry.TABLE_NAME, null, values);
        }
        finally{
            mdb.close();
        }
    }
    public List<MissionDataObject> getMissionData(){
        List<MissionDataObject> mResult = new ArrayList<>();
        SQLiteDatabase mdb = getReadableDatabase();
        try {
            Cursor cursor = mdb.query(MissionTable.MissionEntry.TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                MissionDataObject mdo = new MissionDataObject();
                mdo.setU_mission_duration(cursor.getInt(cursor.getColumnIndex(MissionTable.MissionEntry.COLUMN_NAME_MISSION_DURATION)));
                mdo.setU_grid_length(cursor.getInt(cursor.getColumnIndex(MissionTable.MissionEntry.COLUMN_NAME_MISSION_Length)));
                mdo.setU_grid_width(cursor.getInt(cursor.getColumnIndex(MissionTable.MissionEntry.COLUMN_NAME_MISSION_WIDTH)));
                mdo.setU_mission_id(cursor.getString(cursor.getColumnIndex(MissionTable.MissionEntry.COLUMN_NAME_MISSION_ID)));
                mResult.add(mdo);

            }
            cursor.close();
        } finally {
            mdb.close();
        }
        return mResult;
    }

}

