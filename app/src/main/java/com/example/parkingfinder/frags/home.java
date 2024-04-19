package com.example.parkingfinder.frags;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parkingfinder.DatabaseHelper;
import com.example.parkingfinder.MainActivity;
import com.example.parkingfinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class home extends Fragment implements RecyclerViewInterface, vehicleInterface{

    AppCompatButton textView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseHelper dbHelper;
    List<parking_list> res;
    Dialog bookSlot;
    AppCompatButton dateButton;
    AppCompatButton startTimeButton;
    AppCompatButton endTimeButton, bookButton;
    TextView date, startTime, endTime;
    String username;

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

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        bookSlot = new Dialog(requireContext());

        bookSlot.setContentView(R.layout.buyticket);


        date = bookSlot.findViewById(R.id.dateTextView);
        startTime = bookSlot.findViewById(R.id.startTimeTextView);
        endTime = bookSlot.findViewById(R.id.endTimeTextView);

        dateButton = bookSlot.findViewById(R.id.dateSelect);
        startTimeButton = bookSlot.findViewById(R.id.startTime);
        endTimeButton = bookSlot.findViewById(R.id.endTime);
        bookButton = bookSlot.findViewById(R.id.bookButton);


        textView = view.findViewById(R.id.searchSpots);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    getLocation(view);
                }
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.getContext());

    }

    private void updateRecyclerView(View view, List<parking_list>items){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new parking_adapterclass(getContext(),items,this));
    }



    public void getLocation(View view) {
        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null /* callback for location updates */)
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        dbHelper.loadHandler();
                        res = dbHelper.getNearbyParking(latitude, longitude);
                        updateRecyclerView(view, res);


                    }
                });
    }

    @Override
    public void onItemClick(int position) {

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog(getContext());
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(getContext(),startTime);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(getContext(),endTime);
            }
        });

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String datetime, stime, etime;
                    datetime = date.getText().toString();
                    etime = endTime.getText().toString();
                    stime = startTime.getText().toString();

                    if (datetime.length()<2 | etime.length()<2 | stime.length()<2){
                        Toast.makeText(getContext(),"Please select date, start time and end time properly",Toast.LENGTH_SHORT).show();
                    } else {


                        boolean success = dbHelper.createPaymentHistory(username, "2", res.get(position).getAuto_id(), stime, etime, datetime);
                        if (success){
                            Log.w("Added to DB","DB");
                            double lati = res.get(position).getLatitude();
                            double longi = res.get(position).getLongitude();
                            String location = String.valueOf(lati)+","+String.valueOf(longi);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("google.navigation:q="+location+"&mode=d"));
                            intent.setPackage("com.google.android.apps.maps");
                            startActivity(intent);
                            bookSlot.dismiss();
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(),"Please select date, start time and end time properly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bookSlot.show();





//        double lati = res.get(position).getLatitude();
//        double longi = res.get(position).getLongitude();
//        String location = String.valueOf(lati)+","+String.valueOf(longi);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("google.navigation:q="+location+"&mode=d"));
//        intent.setPackage("com.google.android.apps.maps");
//        startActivity(intent);
    }

    private void openDateDialog(Context context){
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(String.valueOf(year)+"."+String.valueOf(month)+"."+String.valueOf(dayOfMonth));
            }
        },2024,3,15);
        dialog.show();
    }

    private void openTimeDialog(Context context, TextView textView){
        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(String.valueOf(hourOfDay)+"."+String.valueOf(minute));
            }
        },15,00,true);
        dialog.show();
    }


    @Override
    public void onVehicleClick(String vehicleNumber) {

    }
}