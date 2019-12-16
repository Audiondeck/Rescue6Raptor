package org.tensorflow.lite.examples.classification.model;

import androidx.annotation.NonNull;


public class RoverDataObject {
    //Sets rover id

    String u_rover_Name ="";
    String u_rover_id ="";


    public void setU_roverID(String u_roverID){ this.u_rover_Name = u_roverID;}

    public String getU_roverID(){return u_rover_id;}

    public String getRover_Name(){return u_rover_Name;}

    public void setRover_Name(String u_roverName){
        this.u_rover_id = u_roverName;
    }



    //Name matches with ServiceNow
    @NonNull
    @Override
    public String toString() {
        return "Rover ID:"+u_rover_id
                + ",u_name:" + u_rover_Name;

    }

}
