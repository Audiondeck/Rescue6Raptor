package org.tensorflow.lite.examples.classification.rover;

import android.view.View;

import org.tensorflow.lite.examples.classification.Bluetooth.UartService;
import org.tensorflow.lite.examples.classification.model.SensorDataObject;

import java.io.UnsupportedEncodingException;

public class Swarm{
    View cameraFoundView = null;
    RoverParams rover;
    FieldActivity fieldActivity;
    SensorDataObject sensorDataObject;
    UartService uart;

    String stop = "0";
    String goForward = "1";
    String rightUTurn = "2";
    String leftUTurn = "3";

    float oppositeDepartureDirection;
    float departureDirection;
    int blocksPerLane;
    int lanes;
    int roverId;
    int startingLane = roverId;
    String generalRoverDirection;
    int startingBlock;

//UartService extUart, SensorDataObject extSensorDataObject
    public Swarm(){
        sensorDataObject = new SensorDataObject();
        fieldActivity = new FieldActivity();
        rover = new RoverParams();
    }

    public void setUartService(UartService extUart){
        uart = extUart;
    }

    public void checkForCameraView(View extCamFoundView){
        cameraFoundView = extCamFoundView;
    }

    public void setSensorDataObject(SensorDataObject exSensor){
        sensorDataObject = exSensor;
    }

    //Rovers 1 and 3 are going to start at the bottom of the first and third lanes
    //Rovers 2 and 4 are going to start at the top of the second and fourth lanes
    public void startSwarm(){
        blocksPerLane = fieldActivity.getBlocksPerLane();
        lanes = fieldActivity.getLanes();
        roverId = rover.getRoverId();
        generalRoverDirection = (rover.isEven() == 0)?  "down" : "up";
        startingBlock = (generalRoverDirection == "up")? 0: blocksPerLane;
        departureDirection = sensorDataObject.getU_compass();
        setOtherDirections();

        for (int i = startingLane; i < lanes; i += 4) {
            switch(generalRoverDirection){
                case "down":
                    for (int j = blocksPerLane; j >= 1; j--) {
                        runSwarmLogicDown(i, j);
                    }
                    break;
                case "up":
                    for (int j = startingBlock; j < blocksPerLane; j++) {
                        runSwarmLogicUp(i, j);
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
    void runSwarmLogicDown(int currentLane, int currentBlock){
        //Goes forward and marks each block in the grid if it's not the last block
            if (currentBlock != 1) {
                goForward();
                if (cameraFoundView != null)
                    stop();
            }
            else if(currentLane != lanes){
                if (cameraFoundView == null) {
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
                    stop();
            }
            else
                stop();
        //Logic for making the proper U-turn to get into their next designated lane
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void runSwarmLogicUp(int currentLane, int currentBlock) {
        //Goes forward and marks each block in the grid if it's not the last block
            if (currentBlock != (blocksPerLane - 1)) {
                goForward();
                if (cameraFoundView != null)
                    stop();
            } else if(currentLane != lanes){
                if (cameraFoundView == null) {
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
                } else
                    stop();
            }
            else
                stop();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    void rightUturn(){
        byte[] value;
        try {
            value = rightUTurn.getBytes("UTF-8");
            uart.writeRXCharacteristic(value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    void leftUturn(){
        byte[] value;
        try {
            value = leftUTurn.getBytes("UTF-8");
            uart.writeRXCharacteristic(value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //Adjusts wheel direction to stay straight and moves the rover forward one block
    void goForward(){
        float degreeAdjustment = adjustWheelAlignment();

        byte[] value;
        try {
            value = goForward.getBytes("UTF-8");
            uart.writeRXCharacteristic(value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        byte [] orderByte = ByteBuffer.allocate(4).putInt(goForward).array();
//        byte [] degreeBytes = ByteBuffer.allocate(4).putFloat(degreeAdjustment).array();
//        byte[] combinedBytes = new byte[orderByte.length + degreeBytes.length];
//
//        System.arraycopy(orderByte, 0, combinedBytes, 0, orderByte.length);
//        System.arraycopy(degreeBytes, 0, combinedBytes, orderByte.length, degreeBytes.length);

        System.out.println("Sending info for forward: ");
    }

    //Called at the end of finding everything, if there are still lanes unsearched, the rover that
    //is assigned those lanes will go around the ball and continue searching
//    void goAroundVictim(){
//        byte [] bytes = ByteBuffer.allocate(4).putInt(goAroundVictim).array();
//        uart.writeRXCharacteristic(bytes);
//    }

    //Called when something is found and just stops the rover
    void stop(){
        byte[] value;
        try {
            value = stop.getBytes("UTF-8");
            uart.writeRXCharacteristic(value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //Turns wheels in proper direction and angle to realign arduino
    //back into original orientation while continuing forward
    float adjustWheelAlignment(){
        float currentDirection = sensorDataObject.getU_compass();

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
