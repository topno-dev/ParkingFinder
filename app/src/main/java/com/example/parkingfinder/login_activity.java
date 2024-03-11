package com.example.parkingfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parkingfinder.databinding.ActivityLoginBinding;

public class login_activity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(getApplicationContext(),null,null,1);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signUpButton.setOnClickListener(openSignup ->{
            Intent intent = new Intent(this, signup_page.class);
            startActivity(intent);
        });

        binding.loginButton.setOnClickListener(processLogin ->{


            EditText usernameEditText = findViewById(R.id.editTextUsername);
            String username = usernameEditText.getText().toString();

            EditText passwordEditText = findViewById(R.id.editTextPassword);
            String password = passwordEditText.getText().toString();

            dbHelper.loadHandler();
            boolean login_check = dbHelper.verifyUser(username, password);

            if (username.equals("admin") && password.equals("admin")) {
                Intent intent = new Intent(this, admin.class);
                startActivity(intent);
            } else {

                if (login_check){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", username);
                    editor.apply();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Username or Password is incorrect",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}