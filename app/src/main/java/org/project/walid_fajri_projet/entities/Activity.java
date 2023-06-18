package org.project.walid_fajri_projet.entities;

public class Activity {
    private int id;
    private String user;
    private String typeActivity;
    private String startDate;
    private String endDate;

    public Activity(String user, String typeActivity, String startDate,String endDate) {
        this.user = user;
        this.typeActivity = typeActivity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTypeActivity() {
        return typeActivity;
    }

    public void setTypeActivity(String typeActivity) {
        this.typeActivity = typeActivity;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
