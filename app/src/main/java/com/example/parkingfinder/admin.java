package com.example.parkingfinder;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class admin extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        Button changeRateButton;
        EditText editTextNewRate;
        editTextNewRate = findViewById(R.id.editTextNewRate);

        changeRateButton = findViewById(R.id.buttonChangeRate);
        changeRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editTextNewRate.getText().toString();
                double rate = Double.parseDouble(value);
                sharedPreferencesManager.setParkingRate(rate);
            }
        });


    }
}