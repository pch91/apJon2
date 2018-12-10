package com.example.scorp.apjon2.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnApi {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tasks")
    @Expose
    private List<Task> tasks = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
