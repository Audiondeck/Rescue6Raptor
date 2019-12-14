package org.tensorflow.lite.examples.classification.model;

import androidx.annotation.NonNull;

/**
 * POJO class for DataSensor
 */
public class SensorDataObject {

    // match with service now column names
    float u_ambient_light;
    float u_acc_x;
    float u_acc_y;
    float u_acc_z;
    float u_ambient_temp;
    float u_pressure;
    float u_relative_humidity;
    double u_latitude;
    double u_longitude;
    double u_altitude;
    boolean u_object_found;


    public float getLight() {
        return u_ambient_light;
    }

    public void setLight(float light) {
        this.u_ambient_light = light;
    }

    public float getAccx() {
        return u_acc_x;
    }

    public void setAccx(float accx) {
        this.u_acc_x = accx;
    }

    public float getAccy() {
        return u_acc_y;
    }

    public void setAccy(float accy) {
        this.u_acc_y = accy;
    }

    public float getAccz() {
        return u_acc_z;
    }

    public void setAccz(float accz) {
        this.u_acc_z = accz;
    }

    public float getAmbient_temp() {
        return u_ambient_temp;
    }

    public void setAmbient_temp(float ambient_temp) {
        this.u_ambient_temp = ambient_temp;
    }

    public void setPressure(float pressure) {
        this.u_pressure = pressure;
    }

    public float getPressure() {
        return u_pressure;
    }

    public void setRelativeHumidity(float relative_humidity) {
        this.u_relative_humidity = relative_humidity;
    }

    public float getRelativeHumidity() {
        return u_relative_humidity;
    }

    public double getLatitude() {
        return u_latitude;
    }

    public void setLatitude(double latitude) {
        this.u_latitude = latitude;
    }

    public double getLongitude() {
        return u_longitude;
    }

    public void setLongitude(double longitude) {
        this.u_longitude = longitude;
    }

    public double getAltitude() {
        return u_altitude;
    }

    public void setAltitude(double altitude) {
        this.u_altitude = altitude;
    }

    public boolean isU_object_found() {
        return u_object_found;
    }

    public void setU_object_found(boolean u_object_found) {
        this.u_object_found = u_object_found;
    }

    @NonNull
    @Override
    public String toString() {
        return "Light:"+ u_ambient_light
                +", accX:"+u_acc_x
                +", accY:"+u_acc_y
                +", accZ:"+u_acc_z
                +", Ambient Temperature:"+u_ambient_temp
                +", Pressure:"+u_pressure
                +", Relative Humidity:"+u_relative_humidity
                +", Latitude:"+u_latitude
                +", Longitude:"+u_longitude
                +", Altitude:"+u_altitude
                +", Objectfound:"+u_object_found;
    }
}
