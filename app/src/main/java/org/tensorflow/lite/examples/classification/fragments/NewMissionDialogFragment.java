package org.tensorflow.lite.examples.classification.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class NewMissionDialogFragment extends DialogFragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private NewMissionViewModel mViewModel;

    private EditText durationTV;
    private int duration;

    private EditText etWidth;
    private EditText etLength;

    private int mWidth;
    private int mLength;


    public NewMissionDialogFragment() {
        // Required empty public constructor
    }

    public static NewMissionDialogFragment newInstance() {
        return new NewMissionDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_mission_dialog, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.start).setOnClickListener(this);

        durationTV = view.findViewById(R.id.in_duration);

        etWidth = view.findViewById(R.id.in_width);
        etLength = view.findViewById(R.id.in_length);

        mViewModel = ViewModelProviders.of(this).get(NewMissionViewModel.class);


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
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onClick(View view) {
        duration = Integer.parseInt(durationTV.getText().toString());

        mLength = Integer.parseInt(etLength.getText().toString());

        mWidth = Integer.parseInt(etWidth.getText().toString());

        mViewModel.setmDuration(duration);
        if (mListener != null) {
            mListener.onStartMission(mViewModel.getmDuration(), mLength, mWidth);
        }


        dismiss();
    }

    public int getmLength() {
        return mLength;
    }

    public int getmWidth() {
        return mWidth;
    }

    public interface OnFragmentInteractionListener {
        void onStartMission(int minutes, int mLength, int mWidth);
    }
}
