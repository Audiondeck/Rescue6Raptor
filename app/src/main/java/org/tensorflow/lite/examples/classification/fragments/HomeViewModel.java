package org.tensorflow.lite.examples.classification.fragments;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.tensorflow.lite.examples.classification.Bluetooth.Bluetooth;
import org.tensorflow.lite.examples.classification.model.MissionDataObject;
import org.tensorflow.lite.examples.classification.model.RoverDataObject;
import org.tensorflow.lite.examples.classification.model.SensorDataObject;
import org.tensorflow.lite.examples.classification.rover.AsyncSwarm;
import org.tensorflow.lite.examples.classification.rover.FieldActivity;
import org.tensorflow.lite.examples.classification.rover.Swarm;
import org.tensorflow.lite.examples.classification.servicenow.AsyncMaster;
import org.tensorflow.lite.examples.classification.servicenow.AsyncMission;
import org.tensorflow.lite.examples.classification.servicenow.AsyncRover;
import org.tensorflow.lite.examples.classification.servicenow.AsyncTaskTableTes4;
import org.tensorflow.lite.examples.classification.sqlite.MissionTableDbHelper;
import org.tensorflow.lite.examples.classification.sqlite.RoverTableDbHelper;
import org.tensorflow.lite.examples.classification.sqlite.SensorReaderDbHelper;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

import static android.content.Context.BATTERY_SERVICE;

public class HomeViewModel extends AndroidViewModel implements SensorEventListener {

    private MutableLiveData<String> mBattery;
    private MutableLiveData<String> mBattery_percentage;
    private MutableLiveData<String> mWifi;
    private MutableLiveData<String> mBluetooth;

    private MutableLiveData<SensorDataObject> dataObjectMutableLiveData;
    private MutableLiveData<Boolean> isMission;
    private MutableLiveData<MissionDataObject> missionDataObjectMutableLiveData;

    private CountDownTimer missionTimer;
    SensorDataObject sensorDO;
    private MissionDataObject missionDO;
    private SensorReaderDbHelper dbHelper;
    private MissionTableDbHelper mdbHelper;

    private RoverDataObject roverDO;
    private RoverTableDbHelper rdbHelper;


    // capture sensor details when there is an active mission
    private SensorManager manager;
    private Sensor accSensor;
    private Sensor lightSensor;
    private Sensor pressureSensor;
    private Sensor ambientTempSensor;
    private Sensor relativeHumiditySensor;
    private Sensor compassSensor;

    public HomeViewModel(Application context) {
        super(context);
        mBattery = new MutableLiveData<>();
        mBattery_percentage = new MutableLiveData<>();
        mWifi = new MutableLiveData<>();
        dataObjectMutableLiveData = new MutableLiveData<>();
        mBluetooth = new MutableLiveData<>();

        missionDataObjectMutableLiveData = new MutableLiveData<>();
        isMission = new MutableLiveData<>();

        mdbHelper = new MissionTableDbHelper(context);
        dbHelper = new SensorReaderDbHelper(context);

        rdbHelper = new RoverTableDbHelper(context);


        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            mBattery.setValue("Battery Percentage: " + percentage + "%");
            mBattery_percentage.setValue(percentage+"");
        }

        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiCheck.isConnected()) {
            mWifi.setValue("WiFi: Connected");
        } else {
            mWifi.setValue("Wifi: Not Connected");
        }
    }


    public LiveData<String> getBattery() {
        return mBattery;
    }

    public LiveData<String> getWifi() {
        return mWifi;
    }

    public LiveData<String> getBluetooth() {
        return mBluetooth;
    }

    public LiveData<SensorDataObject> getSensorDO() {
        return dataObjectMutableLiveData;
    }

/*    public LiveData<MissionDataObject> getMissionDO(){
        return missionDataObjectMutableLiveData;
    }*/

    public MutableLiveData<Boolean> getIsMission() {
        return isMission;
    }

    public void activatePhoneSensors() {
        manager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);

        accSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        pressureSensor = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        relativeHumiditySensor = manager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        ambientTempSensor = manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        compassSensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        missionDO = new MissionDataObject();
        sensorDO = new SensorDataObject();
        roverDO = new RoverDataObject();


        boolean isavailable = manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(HomeViewModel.class.getSimpleName(), "Accelerometer " + isavailable);
        isavailable = manager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(HomeViewModel.class.getSimpleName(), "Light Sensor " + isavailable);
        isavailable = manager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(HomeViewModel.class.getSimpleName(), "Pressure " + isavailable);
        isavailable = manager.registerListener(this, relativeHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(HomeViewModel.class.getSimpleName(), "Relative Humidity " + isavailable);
        isavailable = manager.registerListener(this, ambientTempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(HomeViewModel.class.getSimpleName(), "Ambient Temperature " + isavailable);
        isavailable = manager.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_GAME);
        Log.i(HomeViewModel.class.getSimpleName(), "Compass "+isavailable);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // update the sensor object. the object carries latest sensor value by the time it saves to the database.
        float[] values = sensorEvent.values;
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorDO.setAccx(values[0]);
                sensorDO.setAccy(values[1]);
                sensorDO.setAccz(values[2]);
                sensorDO.setU_batttery_level(Float.parseFloat(mBattery_percentage.getValue()));
                break;
            case Sensor.TYPE_LIGHT:
                sensorDO.setLight(values[0]);
                break;
            case Sensor.TYPE_PRESSURE:
                sensorDO.setPressure(values[0]);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorDO.setAmbient_temp(values[0]);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                sensorDO.setRelativeHumidity(values[0]);
                break;
            case Sensor.TYPE_ORIENTATION:
                sensorDO.setU_compass(values[0]);
                sensorDO.setU_compass_direction(values[0]);

        }
        dataObjectMutableLiveData.postValue(sensorDO);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void stopPhoneSensors() {
        if (manager != null) {
            manager.unregisterListener(this, accSensor);
            manager.unregisterListener(this, lightSensor);
            manager.unregisterListener(this, pressureSensor);
            manager.unregisterListener(this, ambientTempSensor);
            manager.unregisterListener(this, relativeHumiditySensor);
            manager.unregisterListener(this, compassSensor);
        }

        // TODO stop location updates
    }
    //Holds the return Result from AsyncTasks
    String aResult = "";
    String mResult = "";



    public void onStartMission(int minutes, float mLength, float mWidth, int duration, String roverTeam) {
        missionTimer = new CountDownTimer(minutes * 60 * 1000, 10 * 1000) {
            //Run Counter to ensure Mission and Rover table only gets hit once
            int run = 0;

            @Override
            public void onTick(long l) {
                // will be called every interval
                // save to SQLite

                missionDO.setU_grid_length(mLength);
                missionDO.setU_grid_width(mWidth);
                missionDO.setU_mission_duration(duration);
                roverDO.setRover_Name(roverTeam);


                SensorDataObject sdo = sensorDO;
                sensorDO = new SensorDataObject();
                MissionDataObject mdo = missionDO;
                missionDO = new MissionDataObject();
                RoverDataObject rdo = roverDO;
                roverDO = new RoverDataObject();


                missionDO.setU_grid_length(mLength);
                missionDO.setU_grid_width(mWidth);
                missionDO.setU_mission_duration(duration);
                roverDO.setRover_Name(roverTeam);


                //Insert Sensor data to SQL
                dbHelper.insertSensorData(sdo);


                if(run == 0){

                    //Starts Async Rover and Async Mission
                    //And Returns the Results
                    try {
                        AsyncRover ASRT = new AsyncRover(rdo);
                        ASRT.execute();
                        aResult = ASRT.get();

                        AsyncMission AsyM = new AsyncMission(mdo);
                        AsyM.execute();
                        mResult = AsyM.get();


                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Sets Rover ID to Rover Data Object
                    rdo.setU_roverID(aResult);
                    //Sets Mission Data
                    mdo.setU_mission_id(mResult);


                    //Inserts data into SQLite Tables
                    rdbHelper.insertRoverData(rdo);
                    mdbHelper.insertMissionData(mdo);

                    Log.i(HomeViewModel.class.getSimpleName(), "Save sensor data " + sdo.toString());

                    Log.i(HomeViewModel.class.getSimpleName(), "Mission Data " + mdo.toString());
                    Log.i(HomeViewModel.class.getSimpleName(), "Rover Data " + rdo.toString());

                    new AsyncMaster(sdo, rdo, mdo).execute();

                    run =1;
                }
                else {
                    Log.i(HomeViewModel.class.getSimpleName(), "Mission Data " + mdo.toString());
                    Log.i(HomeViewModel.class.getSimpleName(), "Rover Data " + rdo.toString());
                    Log.i(HomeViewModel.class.getSimpleName(), "Save sensor data " + sdo.toString());


                    //Sets Rover ID to Rover Data Object
                    rdo.setU_roverID(aResult);
                    //Sets Mission ID to Mission Data Object
                    mdo.setU_mission_id(mResult);



                    //Inserts data into SQLite Tables
                    rdbHelper.insertRoverData(rdo);
                    mdbHelper.insertMissionData(mdo);


                    // send to service now
                    new AsyncMaster(sdo, rdo, mdo).execute();
                }
            }

            @Override
            public void onFinish() {
                // send the soccer ball found/not status
                missionTimer = null;
                isMission.postValue(false);
            }
        };
        missionTimer.start();
        // send the Mission data to service now and save to SQlite
        isMission.postValue(true);
    }

    public void onDetached() {
        if (missionTimer != null) {
            missionTimer.cancel();
            missionTimer = null;
        }
    }

    public void objectDetected() {


        SensorDataObject sdo = sensorDO;
        MissionDataObject mdo = missionDO;
        RoverDataObject rdo = roverDO;

        rdo.setRover_Name(aResult);
        mdo.setU_mission_id(mResult);
        float mLength = missionDO.getU_grid_length();
        float mWidth = missionDO.getU_grid_width();
        int duration = missionDO.getU_mission_duration();
        String roverID = roverDO.getU_roverID();


        missionDO.setU_grid_length(mLength);
        missionDO.setU_grid_width(mWidth);
        missionDO.setU_mission_duration(duration);
        roverDO.setU_roverID(roverID);


        //Inserts data into SQLite Tables
        rdbHelper.insertRoverData(rdo);
        mdbHelper.insertMissionData(mdo);
        dbHelper.insertSensorData(sdo);



        sdo.setU_object_found(true);

        new AsyncMaster(sdo,rdo,mdo).execute();


    }
}