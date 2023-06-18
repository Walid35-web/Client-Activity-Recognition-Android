package org.project.walid_fajri_projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;


import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.project.walid_fajri_projet.databinding.ActivityMainBinding;
import org.project.walid_fajri_projet.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity  {
    ActivityMainBinding binding;
    String email;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    Sensor accelerometre;
    SensorManager sensorManager;
    double longitude, latitude,speed;
    long lastUpdate = 0;
    float last_x,last_y,last_z;
    DatabaseHelper mydb=new DatabaseHelper(this);
    double magnitudeP = 0, mPrevious=0;
    double magnitudeD, mDelta;
    float x, y, z;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        email= getIntent().getStringExtra("email");


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            binding.actionact.setText("Latitude : "+latitude+"\nLongitude : "
                                    +longitude);
                            LatLng latLng = new LatLng(latitude, longitude);
                            MarkerOptions options = new MarkerOptions().position(latLng).title("I am there");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }
}