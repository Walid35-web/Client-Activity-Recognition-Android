package org.project.walid_fajri_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import org.project.walid_fajri_projet.databinding.ActivityHomeBinding;
import org.project.walid_fajri_projet.db.DatabaseHelper;
import org.project.walid_fajri_projet.utils.UtilFunctions;

public class HomeActivity extends AppCompatActivity {
       ActivityHomeBinding binding;
       DatabaseHelper myDb;
       String email ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myDb = new DatabaseHelper(this);
        email = getIntent().getStringExtra("email");

        byte[] image = myDb.getImage(email);
        if(image!=null){
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(image, 0, image.length);
            binding.profileImage.setImageBitmap(UtilFunctions.getRoundedBitmap(bitmapImage));
        }

        binding.profileImage.setOnClickListener((view) -> {
            Intent intent = new Intent(this, EditProfilActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        });
        binding.myActivity.setOnClickListener((view) -> {
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
          //      intent = new Intent(this, ActivityConfidenceActivity.class);
                intent = new Intent(this, RecogniweWSensorActivity.class);
            }
            startActivity(intent);
        });


        binding.myLocation.setOnClickListener((view) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.logout.setOnClickListener((view) -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.activitiesHistory.setOnClickListener((view) -> {
            Intent intent = new Intent(this, ActionsActivity.class);
            startActivity(intent);
        });




    }
}