package org.project.walid_fajri_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import org.project.walid_fajri_projet.databinding.ActivitySignupBinding;
import org.project.walid_fajri_projet.db.DatabaseHelper;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DatabaseHelper(this);
        binding.signupButton.setOnClickListener((view) -> {
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();
            String confirmPassword = binding.signupConfirm.getText().toString();
            System.out.println(email + " " + password + " " + confirmPassword);
            if (email.equals("") || password.equals("") || confirmPassword.equals(""))
                Toast.makeText(SignupActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else if(!isValidEmail(email)){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
            else {
                if (password.equals(confirmPassword)) {
                    Boolean checkEmail = db.checkEmail(email);
                    if (!checkEmail) {
                        Boolean insert = db.insertData(email, password);
                        if (insert) {
                            Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(SignupActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.loginRedirectText.setOnClickListener((view) -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
    public  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}