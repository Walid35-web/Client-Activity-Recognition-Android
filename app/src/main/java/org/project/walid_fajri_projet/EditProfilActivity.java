package org.project.walid_fajri_projet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import org.project.walid_fajri_projet.databinding.ActivityEditProfilBinding;
import org.project.walid_fajri_projet.db.DatabaseHelper;
import org.project.walid_fajri_projet.utils.UtilFunctions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditProfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ActivityEditProfilBinding binding;
    DatabaseHelper db;
    Boolean passwordVisible = false;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DatabaseHelper(this);
        email = getIntent().getStringExtra("email");
        fillrows(email);
        binding.editEmail.setEnabled(false);

        binding.editPassword.setOnTouchListener((view, motionEvent) -> {
                    final int DRAWABLE_RIGHT = 2;
                    if(motionEvent.getAction() == motionEvent.ACTION_UP) {
                        if(motionEvent.getRawX() >= (binding.editPassword.getRight() - binding.editPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(!passwordVisible) {
                                binding.editPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);
                                binding.editPassword.setInputType(1);
                                passwordVisible = true;
                            } else {
                                binding.editPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                                binding.editPassword.setInputType(129);
                                passwordVisible = false;
                            }
                            return true;
                        }
                    }
                    return false;
                }
        );

        binding.profileImageView.setOnClickListener((view) -> {
            openFileChooser();
        });


        binding.genderRadioGroup.setOnCheckedChangeListener((group,checkid)->{
            switch (checkid){
                case R.id.maleRadioButton:
                    Toast.makeText(this, "male chosed ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.femaleRadioButton:

                    Toast.makeText(this, "female chosed ", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        binding.saveButton.setOnClickListener(view->{
            if( binding.editPassword.getText().toString().equals("") || binding.editName.getText().toString().equals("") || binding.editPhone.getText().toString().equals("") || binding.editMajor.getText().toString().equals("")|| binding.genderRadioGroup.getCheckedRadioButtonId() == -1){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }else{
                String genre = binding.maleRadioButton.isChecked() ? "M" : "F";
                if(db.saveEditedData(binding.editEmail.getText().toString(),binding.editPassword.getText().toString(),binding.editName.getText().toString(),genre,binding.editPhone.getText().toString(),binding.editMajor.getText().toString())!=0){
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,HomeActivity.class);
                    intent.putExtra("email",binding.editEmail.getText().toString());
                    startActivity(intent);
                    finish();
                }}
        });

        binding.cancelButton.setOnClickListener(view->{
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra("email",binding.editEmail.getText().toString());
            startActivity(intent);
            finish();
        });

    }
    // fill textview with data from database
    private void fillrows(String email){
        String[] data = db.getData(email);

        binding.editEmail.setText(data[0]);
        binding.editPassword.setText(data[1]);
        binding.editName.setText(data[2]);
        String genre = data[3];
        if(!genre.equals("")){
            if(genre.equals("F"))
                binding.femaleRadioButton.setChecked(true);
            else
                binding.maleRadioButton.setChecked(true);
        }
        binding.editPhone.setText(data[4]);
        binding.editMajor.setText(data[5]);
        byte[] image = db.getImage(email);

        if (image != null) {
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(image, 0, image.length);
            binding.profileImageView.setImageBitmap(UtilFunctions.getRoundedBitmap(bitmapImage));
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {

                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Convert the Bitmap to a byte[] array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                db.updateImage(email,byteArray);


                binding.profileImageView.setImageBitmap(UtilFunctions.getRoundedBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}