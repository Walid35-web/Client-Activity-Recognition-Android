package org.project.walid_fajri_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import org.project.walid_fajri_projet.databinding.ActivityLoginBinding;
import org.project.walid_fajri_projet.db.DatabaseHelper;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener((view) -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();
            if (email.equals("") || password.equals(""))
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else if(!isValidEmail(email)){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
            else {
                Boolean checkEmailPassword = db.checkEmailPassword(email, password);
                if (checkEmailPassword) {
                    if(db.checkfirtslog(email))
                    {   Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "please update your data", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, EditProfilActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                       // Intent intent = new Intent(this, MainActivity.class);
                        Intent intent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            intent = new Intent(this, HomeActivity.class);
                        }
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }

                } else
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();

            }

        });

        binding.signupRedirectText.setOnClickListener((view) -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });
    }

    public  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}