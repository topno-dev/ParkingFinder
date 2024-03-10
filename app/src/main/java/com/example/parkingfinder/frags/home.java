package com.example.parkingfinder.frags;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingfinder.DatabaseHelper;
import com.example.parkingfinder.MainActivity;
import com.example.parkingfinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class home extends Fragment {

    TextView textView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext(),null,null,1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        textView = view.findViewById(R.id.searchSpots);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    getLocation();
                }
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        List<parking_list> items = new ArrayList<parking_list>();
        items.add(new parking_list("Cat1","address",50,"+911234567890",R.drawable.logo));
        items.add(new parking_list("Cat2","address",50,"+911234567890",R.drawable.logo));

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new parking_adapterclass(getContext(),items));

    }



    public void getLocation() {
        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null /* callback for location updates */)
                .addOnSuccessListener(requireActivity(), location -> {
                    // Got location!
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        dbHelper.loadHandler();
                        String res = dbHelper.getNearbyParking(latitude, longitude);
                        Log.w("After ", res);

                    }
                });
    }

//    private void getLocation(){
//
//
//
//
////        fusedLocationClient.getCurrentLocation().addOnSuccessListener(requireActivity(), location -> {
////            if (location!=null){
////                double lati = location.getLatitude();
////                double longi = location.getLongitude();
////
////                String message = "Latitude: " + lati + "\nLongitude: " + longi;
////                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
////            }
////        });
//
//    }



}