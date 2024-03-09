package com.example.parkingfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parkingfinder.databinding.ActivityLoginBinding;
import com.example.parkingfinder.databinding.ActivityStartBinding;

public class Start_Activity extends AppCompatActivity {
    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityStartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.nextButton.setOnClickListener(openLogin ->{
            Intent intent = new Intent(Start_Activity.this, login_activity.class);
            startActivity(intent);
        });
    }
}