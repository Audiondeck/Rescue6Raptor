package org.tensorflow.lite.examples.classification.model;

import androidx.annotation.NonNull;


public class MissionDataObject {


    float u_grid_width;
    float u_grid_length;

    int u_mission_duration;

    String u_mission_id;

    public String getU_mission_id(){
        return u_mission_id;
    }

    public void setU_mission_id(String Mission_ID){this.u_mission_id = Mission_ID;}

    public int getU_mission_duration() {
        return u_mission_duration;
    }

    public void setU_mission_duration(int u_mission_duration) {
        this.u_mission_duration = u_mission_duration;
    }

    public float getU_grid_width() {
        return u_grid_width;
    }

    public void setU_grid_width(float u_grid_width) {
        this.u_grid_width = u_grid_width;
    }

    public float getU_grid_length() {
        return u_grid_length;
    }

    public void setU_grid_length(float u_grid_length) {
        this.u_grid_length = u_grid_length;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mission Duration : "+ u_mission_duration
                + ", Grid Width : "+ u_grid_width
                + ", Grid Length : "+ u_grid_length
                + ", Mission ID : " + u_mission_id;

    }
}