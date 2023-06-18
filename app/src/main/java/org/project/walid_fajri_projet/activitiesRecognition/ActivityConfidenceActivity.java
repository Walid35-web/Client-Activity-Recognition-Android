package org.project.walid_fajri_projet.activitiesRecognition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;

import org.project.walid_fajri_projet.R;
import org.project.walid_fajri_projet.db.DatabaseHelper;
import org.project.walid_fajri_projet.entities.Activity;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class ActivityConfidenceActivity extends AppCompatActivity {
    private Button btnStart, btnStop;
    private TextView txt_activity, txt_confidence;
    private ImageView img_activity;
    DatabaseHelper db;
    String email;


    private BroadcastReceiver activityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confidence);
        txt_activity = findViewById(R.id.txt_activity);
        txt_confidence = findViewById(R.id.txt_confidence);
        img_activity = findViewById(R.id.img_activity);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        db = new DatabaseHelper(this);
        if (ActivityCompat.checkSelfPermission(ActivityConfidenceActivity.this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            // When permission grated
            // call Method
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txt_activity.setText("starting the service may take a while  ...");
                    txt_confidence.setText("");
                    img_activity.setImageResource(0);
                    requestActivityUpdates();

                }

            });

        } else {
            // When permission denied
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION
            }, 44);
        }


        activityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("ActivityUpdate", "Received update");
                int type = intent.getIntExtra("type", -1);
                int confidence = intent.getIntExtra("confidence", 0);
                // Update the TextViews according to the activity type and confidence
              //  handleUserActivity(type, confidence);


            }
        };

        btnStop.setOnClickListener((v) -> {
                    Intent intent1 = new Intent(ActivityConfidenceActivity.this, BackgroundDetectedActivity.class);
                    stopService(intent1);
                    txt_activity.setText("Service stopped");
                    txt_confidence.setText("To start the service click on start button");
                    img_activity.setImageResource(0);
                    if (!TextUtils.isEmpty(db.getLastActivity()[4])){
                        db.updateEndDate(new Date().toString(),Long.parseLong(db.getLastActivity()[0]));
                    }

                }
        );
    }



    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(activityReceiver, new IntentFilter("activity_intent"));
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(activityReceiver);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              return;
            }
        }
    }

    private void requestActivityUpdates() {
        Intent intent1 = new Intent(ActivityConfidenceActivity.this, BackgroundDetectedActivity.class);
        startService(intent1);
    }


}