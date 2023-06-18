package org.project.walid_fajri_projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.project.walid_fajri_projet.entities.Activity;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.HolderActivity> {

    private Context context;
    private List<Activity> activities;

    public ActionAdapter(Context context, List<Activity> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActionAdapter.HolderActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.actions_row,parent,false);

        return new HolderActivity(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ActionAdapter.HolderActivity holder, int position) {
        Activity activity = activities.get(position);

        String type = activity.getTypeActivity();
        String startdate = activity.getStartDate();
        String endDate = activity.getEndDate();


        holder.typeActivity.setText(type);
        holder.startdate.setText(startdate);
        holder.enddate.setText(endDate);

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class HolderActivity extends RecyclerView.ViewHolder{
        TextView typeActivity,startdate,enddate;

        public HolderActivity(@NonNull View itemView) {
            super(itemView);
            typeActivity = itemView.findViewById(R.id.type);
            startdate = itemView.findViewById(R.id.startdate);
            enddate = itemView.findViewById(R.id.enddate);

        }
    }
}
