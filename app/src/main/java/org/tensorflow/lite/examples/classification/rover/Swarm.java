package org.tensorflow.lite.examples.classification.rover;

import android.view.View;

import org.tensorflow.lite.examples.classification.Bluetooth.UartService;
import org.tensorflow.lite.examples.classification.fragments.CameraFragment;
import org.tensorflow.lite.examples.classification.model.SensorDataObject;

import java.io.Console;
import java.nio.ByteBuffer;

public class Swarm {

    UartService uart = new UartService();
    SensorDataObject sdoCompass = new SensorDataObject();
    FieldActivity fieldActivity = new FieldActivity();
    RoverParams rover = new RoverParams();

    int stop = 0;
    int goForward = 1;
    int goAroundVictim =2;
    int rightUTurn = 3;
    int leftUTurn = 4;

    float oppositeDepartureDirection;
    float departureDirection;
    int blocksPerLane = fieldActivity.getBlocksPerLane();
    int lanes = fieldActivity.getLanes();
    int roverId = rover.getRoverId();
    int startingLane = roverId;
    String generalRoverDirection = (rover.isEven() == 0)? "down": "up";
    int startingBlock = (generalRoverDirection == "up")? 0: blocksPerLane;
    CameraFragment cameraFragment = new CameraFragment();

    //Rovers 1 and 3 are going to start at the bottom of the first and third lanes
    //Rovers 2 and 4 are going to start at the top of the second and fourth lanes
    public void startSwarm() {
        departureDirection = sdoCompass.getU_compass();
        setOtherDirections();

        for (int i = startingLane; i < lanes; i += 4) {
            switch(generalRoverDirection){
                case "down":
                    for (int j = startingBlock - 1; j >= 0; j--) {
                        runSwarmLogic(i, j);
                    }
                    break;
                case "up":
                    for (int j = startingBlock; j < blocksPerLane; j++) {
                        runSwarmLogic(i, j);
                    }
                    break;
            }
        }
        /*  If rovers get to the end of their searches, but found != 4, the rover with
            unsearched blocks where they should've searched, will go around victim and continue
            Requirements:
            1. How many objects are found
            2. We need to know the status of the rest of the grid
            3. We need to know if a rover still has blocks to search
        */

    }

    //Called for each block in one lane
    void runSwarmLogic(int currentLane, int currentBlock){
        //Goes forward and marks each block in the grid if it's not the last block
        if (currentBlock != blocksPerLane - 1 || currentBlock != 1) {
            goForward();
            if (cameraFragment.foundView.getVisibility() != View.VISIBLE)
                fieldActivity.searchedBlock(currentLane, currentBlock);
            else
                fieldActivity.foundInBlock(currentLane, currentBlock);
                stop();
        }
        //Logic for making the proper U-turn to get into their next designated lane
        else {
            if (cameraFragment.foundView.getVisibility() != View.VISIBLE) {
                fieldActivity.searchedBlock(currentLane, currentBlock);
                switch (generalRoverDirection) {
                    case "down":
                        leftUturn();
                        generalRoverDirection = "up";
                        break;
                    case "up":
                        rightUturn();
                        generalRoverDirection = "down";
                        break;
                }
            }
            else
                fieldActivity.foundInBlock(currentLane, currentBlock);
                stop();
        }
    }

    void rightUturn(){
        byte [] bytes = ByteBuffer.allocate(4).putInt(rightUTurn).array();
        uart.writeRXCharacteristic(bytes);
    }

    void leftUturn(){
        byte [] bytes = ByteBuffer.allocate(4).putInt(leftUTurn).array();
        uart.writeRXCharacteristic(bytes);
    }

    //Adjusts wheel direction to stay straight and moves the rover forward one block
    void goForward(){
        float degreeAdjustment = adjustWheelAlignment();

        byte [] orderByte = ByteBuffer.allocate(4).putInt(goForward).array();
        byte [] degreeBytes = ByteBuffer.allocate(4).putFloat(degreeAdjustment).array();
        byte[] combinedBytes = new byte[orderByte.length + degreeBytes.length];

        System.arraycopy(orderByte, 0, combinedBytes, 0, orderByte.length);
        System.arraycopy(degreeBytes, 0, combinedBytes, orderByte.length, degreeBytes.length);

        uart.writeRXCharacteristic(combinedBytes);
        System.out.println("Sending info for forward: " +combinedBytes);
    }

    //Called at the end of finding everything, if there are still lanes unsearched, the rover that
    //is assigned those lanes will go around the ball and continue searching
    void goAroundVictim(){
        byte [] bytes = ByteBuffer.allocate(4).putInt(goAroundVictim).array();
        uart.writeRXCharacteristic(bytes);
    }

    //Called when something is found and just stops the rover
    void stop(){
        byte [] bytes = ByteBuffer.allocate(4).putInt(stop).array();
        uart.writeRXCharacteristic(bytes);

    }

    //Turns wheels in proper direction and angle to realign arduino
    //back into original orientation while continuing forward
    float adjustWheelAlignment(){
        float currentDirection = sdoCompass.getU_compass();

        //Calculate degree necessary
        return getAdjustedDegreeForSending(currentDirection);
    }

    //Initialize rover orientation using android compass
    //determine compass degree rover is facing for purpose of lane departure direction and lane return direction
    public float getAdjustedDegreeForSending(float currentDirection) {
        float degreeForAdjusting = (departureDirection - currentDirection) % 360;

        if (degreeForAdjusting < -180)
            degreeForAdjusting += 360;
        if (degreeForAdjusting >= 180)
            degreeForAdjusting -= 360;

        return degreeForAdjusting;
    }

    //Sets the opposite direction compass value for adjusting the wheels when facing down in the grid
    public void setOtherDirections(){
        float sum = departureDirection + 180;
        oppositeDepartureDirection = (sum > 360)? sum - 360 : sum;
    }

}
