package com.example.parkingfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class popup_window_addCar extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_popup_window_add_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this, null, null, 1);

        SharedPreferences sharedPreferences = this.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

//        Button button;
//
//        button = findViewById(R.id.addCarButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.w("Buton Clucked", "Click");
//                EditText vehicle_name;
//                EditText vehicle_number;
//                vehicle_name = findViewById(R.id.addVehicleNameEditText);
//                vehicle_number = findViewById(R.id.addVehicleNumEditText);
//
//                String vehicleName = vehicle_name.getText().toString();
//                String vehicleNumber = vehicle_number.getText().toString();
//                boolean result = dbHelper.createVehicle(username,vehicleName,vehicleNumber);
//                if (result){
//                    Toast.makeText(getApplicationContext(), "Added Vehicle",Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(),"Vehicle number already added",Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });

    }
}