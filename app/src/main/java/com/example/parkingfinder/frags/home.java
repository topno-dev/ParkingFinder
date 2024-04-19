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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parkingfinder.DatabaseHelper;
import com.example.parkingfinder.MainActivity;
import com.example.parkingfinder.R;
import com.example.parkingfinder.SharedPreferencesManager;
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
    Dialog bookSlot, payWindow;
    AppCompatButton dateButton;
    AppCompatButton startTimeButton;
    AppCompatButton endTimeButton, bookButton, paymentButton;
    TextView date, startTime, endTime, paymentTextView;
    String username, vehicle_name, vehicle_number;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    double rate;
    int start_hour, end_hour;

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
        vehicle_name = "None";
        vehicle_number = "None";
        bookSlot = new Dialog(requireContext());
        payWindow = new Dialog(requireContext());

        bookSlot.setContentView(R.layout.buyticket);
        payWindow.setContentView(R.layout.paymentscreen);



        date = bookSlot.findViewById(R.id.dateTextView);
        startTime = bookSlot.findViewById(R.id.startTimeTextView);
        endTime = bookSlot.findViewById(R.id.endTimeTextView);

        dateButton = bookSlot.findViewById(R.id.dateSelect);
        startTimeButton = bookSlot.findViewById(R.id.startTime);
        endTimeButton = bookSlot.findViewById(R.id.endTime);
        bookButton = bookSlot.findViewById(R.id.bookButton);
        autoCompleteTextView = bookSlot.findViewById(R.id.autoCompleteTextView);

        paymentButton = payWindow.findViewById(R.id.payButton);
        paymentTextView = payWindow.findViewById(R.id.paymentValueTextView);


        List<vehicle> vehicleList = dbHelper.selectVehiclesByUsername(username);
        String[] names = new String[vehicleList.size()];
        for (int i = 0; i < vehicleList.size(); i++) {
            names[i] = vehicleList.get(i).getName();
        }
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, names);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                vehicle_name = item;
                vehicle_number = vehicleList.get(position).getVeh_num();
            }
        });




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
                openTimeDialog(getContext(),startTime,0);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog(getContext(),endTime,1);
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



                    if (!vehicle_number.equals("None")) {
                        if (etime.compareTo(stime) > 0) {

                            if (datetime.length() < 2 | etime.length() < 2 | stime.length() < 2) {
                                Toast.makeText(getContext(), "Please select date, start time and end time properly", Toast.LENGTH_SHORT).show();
                            } else {
                                rate = dbHelper.getRate();
                                paymentTextView.setText(String.valueOf((end_hour-start_hour)*rate));
                                paymentButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean success = dbHelper.createPaymentHistory(username, vehicle_number, res.get(position).getAuto_id(), stime, etime, datetime);
                                        if (success) {
                                            Log.w("Added to DB", "DB");
                                            double lati = res.get(position).getLatitude();
                                            double longi = res.get(position).getLongitude();
                                            String location = String.valueOf(lati) + "," + String.valueOf(longi);
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("google.navigation:q=" + location + "&mode=d"));
                                            intent.setPackage("com.google.android.apps.maps");
                                            startActivity(intent);
                                            bookSlot.dismiss();
                                        }
                                        payWindow.dismiss();
                                    }
                                });
                                payWindow.setCancelable(false);
                                payWindow.show();


                            }
                        } else {
                            Toast.makeText(getContext(), "End time is before start time", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getContext(),"Please add vehicle / Select Vehicle", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(),"Please select date, start time and end time properly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bookSlot.show();

    }

    private void openDateDialog(Context context){
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(String.valueOf(year)+"."+String.valueOf(month)+"."+String.valueOf(dayOfMonth));
            }
        },2024,3,15);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void openTimeDialog(Context context, TextView textView, int typeOfDate){
        TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay>9) {
                    textView.setText(String.valueOf(hourOfDay) + "Hrs " + String.valueOf(minute) + "Mins");
                } else {
                    textView.setText("0"+String.valueOf(hourOfDay) + "Hrs " + String.valueOf(minute) + "Mins");
                }
                if (typeOfDate == 0) {
                    start_hour = hourOfDay;
                } else {
                    end_hour = hourOfDay;
                }
            }
        },15,00,true);
        dialog.show();
    }


    @Override
    public void onVehicleClick(String vehicleNumber) {

    }
}