package org.tensorflow.lite.examples.classification.sqlite;

import android.provider.BaseColumns;

public final class RoverTable {
    private RoverTable(){}

    public static class RoverEntry implements BaseColumns{
        public static final String TABLE_NAME = "rover_data";
        public static final String COLUMN_ROVER_ID = "rover_id";
        public static final String COLUMN_ROVER_NAME = "rover_name";

    }

    static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + RoverEntry.TABLE_NAME +" ("+
            RoverEntry.COLUMN_ROVER_ID +" REAL," +
            RoverEntry.COLUMN_ROVER_NAME +" REAL)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RoverEntry.TABLE_NAME;
}
