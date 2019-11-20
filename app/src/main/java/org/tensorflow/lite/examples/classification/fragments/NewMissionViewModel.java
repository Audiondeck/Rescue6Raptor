package org.tensorflow.lite.examples.classification.fragments;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.tensorflow.lite.examples.classification.R;

public class NewMissionViewModel extends AndroidViewModel {

    private int mDuration;

    public NewMissionViewModel(Application application) {
        super(application);

    }


    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration){
        this.mDuration = mDuration;
    }
}