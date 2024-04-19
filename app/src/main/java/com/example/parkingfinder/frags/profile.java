package com.example.parkingfinder.frags;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingfinder.DatabaseHelper;
import com.example.parkingfinder.R;

import java.util.ArrayList;
import java.util.List;

public class profile extends Fragment implements RecyclerViewInterface {

    DatabaseHelper dbHelper;

    Dialog addCarDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        addCarDialog = new Dialog(requireContext());

        if (username != null) {
            dbHelper = new DatabaseHelper(requireContext(), null, null, 1);
            List<vehicle> items = dbHelper.selectVehiclesByUsername(username);
            if (!items.isEmpty()){
                updateRecyclerView(view, items);
            } else {
                items.add(new vehicle("No Vehicle"," ",R.drawable.baseline_directions_car_24));
                updateRecyclerView(view, items);
            }

        }

        // For parking history
        if (username != null) {
            dbHelper = new DatabaseHelper(requireContext(), null, null, 1);
            List<parking_list> items = dbHelper.getParkingHistory(username);
            if (!items.isEmpty()){
                updateRecyclerViewHistory(view, items);
            }

        }

        TextView addCarTextView;
        addCarTextView = view.findViewById(R.id.textView8);
        addCarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCarPopup(username, view);
            }
        });


    }

    private void updateRecyclerView(View view, List<vehicle>items){

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new vehicle_adapterclass(getContext(),items));
    }

    private void updateRecyclerViewHistory(View view, List<parking_list>items){

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new parking_adapterclass(getContext(),items, this));
    }

    private void showAddCarPopup(String username,View view) {
        addCarDialog.setContentView(R.layout.activity_popup_window_add_car);

        Button button;
        button = addCarDialog.findViewById(R.id.addCarButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText vehicle_name;
                EditText vehicle_rto_code, vehicle_state_code, vehicle_unique_code;
                vehicle_name = addCarDialog.findViewById(R.id.addVehicleNameEditText);
                vehicle_state_code = addCarDialog.findViewById(R.id.addVehicleStateCode);
                vehicle_rto_code = addCarDialog.findViewById(R.id.addVehicleRTOCODE);
                vehicle_unique_code = addCarDialog.findViewById(R.id.addVehicleUniqueCode);

                String vehicleName = vehicle_name.getText().toString();

                //add all three together
                if (vehicle_name.getText().toString().isEmpty() | vehicle_state_code.getText().toString().isEmpty() | vehicle_rto_code.getText().toString().isEmpty() | vehicle_unique_code.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in complete info!", Toast.LENGTH_SHORT).show();
                } else {
                    String vehicleNumber = vehicle_state_code.getText().toString() + vehicle_rto_code.getText().toString() + vehicle_unique_code.getText().toString();
                boolean result = dbHelper.createVehicle(username, vehicleName, vehicleNumber);
                if (result) {
                    Toast.makeText(requireContext(), "Added Vehicle", Toast.LENGTH_SHORT).show();
                    List<vehicle> items = dbHelper.selectVehiclesByUsername(username);
                    updateRecyclerView(view, items);
                    addCarDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Vehicle number already added", Toast.LENGTH_SHORT).show();
                }
            }
            }
        });

        addCarDialog.show();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(requireContext(),"Parking Info",Toast.LENGTH_SHORT).show();
    }
}