package org.tensorflow.lite.examples.classification.sqlite;

import android.provider.BaseColumns;

public final class MissionTable {

    private MissionTable() {}

    public static class MissionEntry implements BaseColumns{
        public static final String TABLE_NAME = "mission_data";
        public static final String COLUMN_NAME_MISSION_WIDTH = "mission_width";
        public static final String COLUMN_NAME_MISSION_Length = "mission_length";
        public static final String COLUMN_NAME_MISSION_DURATION = "mission_duration";
        //TODO Create fields for Columns Below
        public static final String COLUMN_NAME_MISSION_OUTCOME = "mission_outcome";
        public static final String COLUMN_NAME_START_TIME = "mission_start_time";
        public static final String COLUMN_NAME_END_TIME = "mission_end_time";
        public static final String COLUMN_NAME_MISSION_ID = "mission_id";

    }

    static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MissionEntry.TABLE_NAME +" ("+
            MissionEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            MissionEntry.COLUMN_NAME_MISSION_Length + " REAL,"+
            MissionEntry.COLUMN_NAME_MISSION_WIDTH + " REAL," +
            MissionEntry.COLUMN_NAME_MISSION_DURATION + " REAL," +
            MissionEntry.COLUMN_NAME_MISSION_OUTCOME + " REAL," +
            MissionEntry.COLUMN_NAME_START_TIME + " REAL," +
            MissionEntry.COLUMN_NAME_END_TIME + " REAL," +
            MissionEntry.COLUMN_NAME_MISSION_ID +" REAL)"

            ;



    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MissionEntry.TABLE_NAME;
}
