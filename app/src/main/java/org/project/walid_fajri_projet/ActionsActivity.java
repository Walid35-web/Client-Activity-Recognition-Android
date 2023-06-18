package org.project.walid_fajri_projet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;

import org.project.walid_fajri_projet.databinding.ActivityActionsBinding;
import org.project.walid_fajri_projet.db.DatabaseHelper;
import org.project.walid_fajri_projet.entities.Activity;

import java.util.List;

public class ActionsActivity extends AppCompatActivity {

    private DatabaseHelper mydb;
    private RecyclerView recyclerView;
    ActivityActionsBinding binding;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        email= getIntent().getStringExtra("email");
        recyclerView = binding.idRecHistory;
        mydb = new DatabaseHelper(ActionsActivity.this);
        List<Activity> activities = mydb.getActivities();
        ActionAdapter adapter = new ActionAdapter(this,activities);
        recyclerView.setAdapter(adapter);
    }

}