package org.tensorflow.lite.examples.classification.model;

import androidx.annotation.NonNull;

/**
 * POJO class for DataSensor
 */
public class SensorDataObject {

    // match with service now column names
    float u_ambient_light;
    float u_x_cord;
    float u_y_cord;
    float u_acc_z;
    float u_temperature;
    float u_pressure;
    float u_humidity;
    double u_latitude;
    double u_longitude;
    double u_altitude;
    boolean u_object_found;

    float u_batttery_level;

    public void setU_batttery_level(float u_batttery_level) {
        this.u_batttery_level = u_batttery_level;
    }

    public float getU_batttery_level() {
        return u_batttery_level;
    }

    public float getLight() {
        return u_ambient_light;
    }

    public void setLight(float light) {
        this.u_ambient_light = light;
    }

    public float getAccx() {
        return u_x_cord;
    }

    public void setAccx(float accx) {
        this.u_x_cord = accx;
    }

    public float getAccy() {
        return u_y_cord;
    }

    public void setAccy(float accy) {
        this.u_y_cord = accy;
    }

    public float getAccz() {
        return u_acc_z;
    }

    public void setAccz(float accz) {
        this.u_acc_z = accz;
    }

    public float getAmbient_temp() {
        return u_temperature;
    }

    public void setAmbient_temp(float ambient_temp) {
        this.u_temperature = ambient_temp;
    }

    public void setPressure(float pressure) {
        this.u_pressure = pressure;
    }

    public float getPressure() {
        return u_pressure;
    }

    public void setRelativeHumidity(float relative_humidity) {
        this.u_humidity = relative_humidity;
    }

    public float getRelativeHumidity() {
        return u_humidity;
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
                +", accX:"+ u_x_cord
                +", accY:"+ u_y_cord
                +", accZ:"+u_acc_z
                +", Ambient Temperature:"+ u_temperature
                +", Pressure:"+u_pressure
                +", Relative Humidity:"+ u_humidity
                +", Latitude:"+u_latitude
                +", Longitude:"+u_longitude
                +", Altitude:"+u_altitude
                +", Objectfound:"+u_object_found
                +", Battery Level:"+ u_batttery_level;
    }
}
