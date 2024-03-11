package com.example.parkingfinder;

import android.content.Intent;
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

import com.example.parkingfinder.databinding.ActivitySignupPageBinding;

public class signup_page extends AppCompatActivity {

    private ActivitySignupPageBinding binding;
    private DatabaseHelper dbHelper;
    Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivitySignupPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.goToLoginText.setOnClickListener(gotolog ->{
            Intent intent = new Intent(this, login_activity.class);
            startActivity(intent);
        });

        sign_up = findViewById(R.id.sign_up_button);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextUsername = findViewById(R.id.editTextUsername);
                String username = editTextUsername.getText().toString();

                EditText editTextEmail = findViewById(R.id.editTextEmail);
                String email = editTextEmail.getText().toString();

                EditText editTextPassword = findViewById(R.id.editTextPassword);
                String password = editTextPassword.getText().toString();

                if (username.length() < 6) {
                    editTextUsername.setError("Username must be at least 6 characters long");
                }
                else if (password.length() < 6) {
                    editTextPassword.setError("Password must be at least 6 characters long");
                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Invalid email address");
                }
                else {
                    dbHelper = new DatabaseHelper(getApplicationContext(), null,null,1);
                    dbHelper.loadHandler();
                    boolean Success = dbHelper.createUser(username, email, password);
                    if (Success){
                        Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Failed, Username or Email already exists",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}