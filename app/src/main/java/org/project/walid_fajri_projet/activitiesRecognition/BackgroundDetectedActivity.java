package org.project.walid_fajri_projet.activitiesRecognition;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class BackgroundDetectedActivity extends Service {

    private PendingIntent pendingIntent;
    private ActivityRecognitionClient activityRecognitionClient;

    IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {

        public BackgroundDetectedActivity getServerInstance() {
            return BackgroundDetectedActivity.this;
        }
    }

    public BackgroundDetectedActivity() {

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void onCreate() {
        super.onCreate();
        activityRecognitionClient = ActivityRecognition.getClient(this);
        Intent intentService = new Intent(this, ActivityRecognitionService.class);
       // pendingIntent = PendingIntent.getService(this, 1, intentService, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent = PendingIntent.getService(this, 1, intentService, PendingIntent.FLAG_UPDATE_CURRENT);
        requestActivityUpdatesButtonHandler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void requestActivityUpdatesButtonHandler() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
       // Task<Void> task = activityRecognitionClient.requestActivityUpdates(1000, pendingIntent);
        Task<Void> task = activityRecognitionClient.requestActivityUpdates(500, pendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void result) {
                Toast.makeText(getApplicationContext(), "Mises à jour d'activité demandées avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "La demande de mises à jour d'activité n'a pas pu démarrer", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeActivityUpdate();
    }

    public void removeActivityUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Void> task = activityRecognitionClient.removeActivityUpdates(pendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(getApplicationContext(), "Mises à jour d'activité supprimées avec succès !", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Échec de la suppression des mises à jour d'activité!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
