package org.tensorflow.lite.examples.classification.sqlite;

import android.provider.BaseColumns;

public final class MissionTable {

    private MissionTable() {}

    public static class MissionEntry implements BaseColumns{
        public static final String TABLE_NAME = "mission_data";
        //public static final String COLUMN_NAME_MISSION_RUNTIME = "mission_runtime";
        public static final String COLUMN_NAME_MISSION_WIDTH = "mission_width";
        public static final String COLUMN_NAME_MISSION_Length = "mission_length";
        public static final String COLUMN_NAME_MISSION_DURATION = "mission_duration";

    }

    static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MissionEntry.TABLE_NAME +" ("+
            MissionEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            //MissionEntry.COLUMN_NAME_MISSION_RUNTIME + " REAL,"+
            MissionEntry.COLUMN_NAME_MISSION_Length + " REAL,"+
            MissionEntry.COLUMN_NAME_MISSION_WIDTH + " REAL," +
            MissionEntry.COLUMN_NAME_MISSION_DURATION + " REAL)"

    ;



    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MissionEntry.TABLE_NAME;
}
