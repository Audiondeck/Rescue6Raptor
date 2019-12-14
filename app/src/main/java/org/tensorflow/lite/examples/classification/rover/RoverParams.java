package org.tensorflow.lite.examples.classification.rover;

public class RoverParams {
    static int roverQuantity;
    static int victimQuantity;
    static int roverId;

    public static int getRoverQuantity() {
        return roverQuantity;
    }

    public static void setRoverQuantity(int roverQuantity) {
        RoverParams.roverQuantity = roverQuantity;
    }

    public static int getVictimQuantity() {
        return victimQuantity;
    }

    public static void setVictimQuantity(int victimQuantity) {
        RoverParams.victimQuantity = victimQuantity;
    }

    public static int getRoverId() {
        return roverId;
    }

    public void setRoverId(String roverId) {
        this.roverId = Integer.parseInt(roverId.replaceAll("\\D+",""));
    }

    //If even, returns 0, else returns 1.
    public static int isEven() {return (roverId % 2 == 0)? 0:1;}
}
