package org.project.walid_fajri_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;



import android.Manifest;
        import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import org.project.walid_fajri_projet.db.DatabaseHelper;
import org.project.walid_fajri_projet.entities.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecogniweWSensorActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvStill, tvStillConfidence, tvWalking, tvWalkingConfidence, tvRunning, tvRunningConfidence, tvJumping, tvJumpingConfidence;
    private static final float MIN_MOVEMENT_THRESHOLD = 0.3f; // Adjust this value as needed
    private SensorManager sensorManager;
    private Sensor accelerometerSensor, gyroscopeSensor, stepCounterSensor, stepDetectorSensor;
    private float[] accelerometerData = new float[3];
    private float[] gyroscopeData = new float[3];
    private float[] orientation = new float[3];

    private static final float GRAVITY_ALPHA = 0.8f;
    private static final float GYROSCOPE_ALPHA = 0.98f;
    private static final float ACCELEROMETER_THRESHOLD = 1.0f;
    private static final float GYROSCOPE_THRESHOLD = 0.1f;
    private List<Float> accelerometerValues;
    private List<Float> gyroscopeValues;
    String email;
    private int stepCount;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recogniwe_wsensor);

        // Initialize TextViews
        tvStill = findViewById(R.id.tv_still);
        email = getIntent().getStringExtra("email");

        // Request permission to use activity recognition and sensors
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 44);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 45);
        }

        // Initialize sensor manager and sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        accelerometerValues = new ArrayList<>();
        gyroscopeValues = new ArrayList<>();
        stepCount = 0;

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register sensor listeners
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listeners to save battery
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometerSensor) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

            accelerometerValues.add(acceleration);
        } else if (event.sensor == gyroscopeSensor) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float rotation = (float) Math.sqrt(x * x + y * y + z * z);

            gyroscopeValues.add(rotation);
        } else if (event.sensor == stepCounterSensor) {
            // Increment step count
            stepCount = (int) event.values[0];
        }

        // Update activity based on the latest sensor values
        updateActivity(getHighestAcceleration(), getHighestRotation(), stepCount); }


    private long lastCleaningTime= 0;
    private void updateActivity(float acceleration, float rotation, int stepCount) {
        String activityText;

        // Clean sensor values every 20 seconds
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCleaningTime >= 5000) {
            Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_SHORT).show();

            accelerometerValues.clear();
            gyroscopeValues.clear();
            lastCleaningTime = currentTime;
        }

        if (acceleration > 15 && rotation > 5) {
            activityText = "Running";
        } else if (acceleration > 10 && rotation > 2) {
            activityText = "Walking";
        } else if (acceleration > 5 && rotation > 1) {
            activityText = "Standing";
        } else {
            activityText = "Sitting";
        }

        tvStill.setText(activityText);
        db.addActivity(new Activity(email,activityText, new Date().toString(), new Date().toString()));
    }


    private float getHighestAcceleration() {
        float highestAcceleration = 0;

        for (float acceleration : accelerometerValues) {
            if (acceleration > highestAcceleration) {
                highestAcceleration = acceleration;
            }
        }

        return highestAcceleration;
    }

    private float getHighestRotation() {
        float highestRotation = 0;

        for (float rotation : gyroscopeValues) {
            if (rotation > highestRotation) {
                highestRotation = rotation;
            }
        }

        return highestRotation;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }
}