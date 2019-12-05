package org.tensorflow.lite.examples.classification.model;

import androidx.annotation.NonNull;


public class MissionDataObject {


    float u_width;
    float u_length;

    int u_duration;



    public int getU_duration() {
        return u_duration;
    }

    public void setU_duration(int u_duration) {
        this.u_duration = u_duration;
    }

    public float getU_width() {
        return u_width;
    }

    public void setU_width(float u_width) {
        this.u_width = u_width;
    }

    public float getU_length() {
        return u_length;
    }

    public void setU_length(float u_length) {
        this.u_length = u_length;
    }

    //Name matches with ServiceNow
    @NonNull
    @Override
    public String toString() {
        return "Duration:"+u_duration
                + ", Width:"+u_width
                + ", Length:"+u_length;

    }
}