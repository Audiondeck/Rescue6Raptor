package org.tensorflow.lite.examples.classification.model;

import androidx.annotation.NonNull;


public class RoverDataObject {
    //Sets rover id
    int rover_id = 1;

    String rover_Name = "Team 4";

    public int getRover_id(){return rover_id;}

    public void setRover_id(int rover_id){this.rover_id = rover_id;}

    public String getRover_Name(){return rover_Name;}


    //Name matches with ServiceNow
    @NonNull
    @Override
    public String toString() {
        return "Rover ID:"+rover_id
                + ", Name:" + rover_Name;

    }

}
