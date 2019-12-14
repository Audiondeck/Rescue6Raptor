package org.tensorflow.lite.examples.classification.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.SensorDataObject;
import org.tensorflow.lite.examples.classification.tflite.Classifier;


public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private HomeViewModel homeViewModel;

    private TextView bluetooth;
    private TextView accTV;
    private TextView lightTV;
    private TextView pressureTV;
    private TextView ambientTempTV;
    private TextView relativeHumidityTV;
    private TextView latLngTV;
    private Button startMissionButton;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView battery = root.findViewById(R.id.battery);
        final TextView wifi = root.findViewById(R.id.wifi);
        bluetooth = root.findViewById(R.id.bluetooth);

        accTV = root.findViewById(R.id.acc_values);
        lightTV = root.findViewById(R.id.light);
        pressureTV = root.findViewById(R.id.pressure);
        ambientTempTV = root.findViewById(R.id.ambientTemp);
        relativeHumidityTV = root.findViewById(R.id.relativeHumidity);
        latLngTV = root.findViewById(R.id.lat_lng);

        homeViewModel.getBattery().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                battery.setText(s);
            }
        });

        homeViewModel.getWifi().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                wifi.setText(s);
            }
        });

        homeViewModel.getBluetooth().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                bluetooth.setText(s);
            }
        });

        homeViewModel.getSensorDO().observe(this, new Observer<SensorDataObject>(){

            @Override
            public void onChanged(SensorDataObject dataObject) {
                accTV.setText(getString(R.string.acc_format,dataObject.getAccx(),dataObject.getAccy(),dataObject.getAccz()));
                lightTV.setText(getString(R.string.light_format, dataObject.getLight()));
                pressureTV.setText(getString(R.string.pressure_format, dataObject.getPressure()));
                ambientTempTV.setText(getString(R.string.ambient_temp_format, dataObject.getAmbient_temp()));
                relativeHumidityTV.setText(getString(R.string.relative_humidity_format, dataObject.getRelativeHumidity()));

                if(mLocation!=null) {
                    homeViewModel.sensorDO.setLatitude(mLocation.getLatitude());
                    homeViewModel.sensorDO.setLongitude(mLocation.getLongitude());
                    homeViewModel.sensorDO.setAltitude(mLocation.getAltitude());
                }
            }
        });

        homeViewModel.getIsMission().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    startMissionButton.setText("Stop Mission");
                }else{
                    startMissionButton.setText("Start Mission");
                }
            }
        });

        startMissionButton = root.findViewById(R.id.start_mission_button);
        startMissionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mListener.onStartMissionButtonPressed();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.activatePhoneSensors();
        createLocationRequest();
    }

    @Override
    public void onPause() {
        super.onPause();
        homeViewModel.stopPhoneSensors();
        stopLocationRequest();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        homeViewModel.onDetached();
    }

    public void onStartMission(int minutes, float mLength, float mWidth, int duration, String roverTeam) {
        homeViewModel.onStartMission(minutes, mLength, mWidth, duration, roverTeam);
    }

    public interface OnFragmentInteractionListener {
        void onStartMissionButtonPressed();
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 sec  = 10 * 1000 milliseconds
        locationRequest.setFastestInterval(5000); // 5 sec  =  5 * 1000 milliseconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i(HomeViewModel.class.getSimpleName(), "Location service is ready to read");
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(HomeViewModel.class.getSimpleName(), "Failed to request location service");
            }
        });
    }

    // Stop the location update when user quits the app
    private void stopLocationRequest(){
        if(fusedLocationProviderClient!=null){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private Location mLocation;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                mLocation = location;
                latLngTV.setText("Latitude:" + location.getLatitude() +
                        "\nLongitude:" + location.getLongitude() +
                        "\nAltitude:" + location.getAltitude());
                break;
            }
        }
    };

    boolean isLastUpdated = false;
    public void onItemDetected(Classifier.Recognition recognition) {
        if (recognition != null) {
            if (recognition.getTitle() == null) {
                return;
            }
            if (recognition.getConfidence() == null) {
                return;
            }

            if(recognition.getTitle().equals("soccer ball") && recognition.getConfidence() >= 0.55){
                //  insert immediately data in to service now that object has detected.
                if(!isLastUpdated){
                    isLastUpdated = true;
                    homeViewModel.objectDetected();
                }
            } else {
                isLastUpdated = false;
            }
        }
    }


}