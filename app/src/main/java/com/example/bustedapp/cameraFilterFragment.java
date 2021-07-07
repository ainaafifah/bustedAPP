package com.example.bustedapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class cameraFilterFragment extends Fragment {

    private Button mCamera;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_filter, container, false);

        mCamera = view.findViewById(R.id.buttonC);
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(.this, Register.class));
//                askCameraPermission();
                Intent myIntent = new Intent(cameraFilterFragment.this.getActivity(), CameraActivity.class);
                startActivity(myIntent);
            }
        });

         return view;
    }

//    private void askCameraPermission() {
//        if(ContextCompat);
//    }

}