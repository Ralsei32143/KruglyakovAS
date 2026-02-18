package com.example.rzhdbrigada;

import java.io.Serializable;

public class TaskItem implements Serializable {
    public String title;
    public String dateTime;
    public String brigade;
    public String priority;
    public String status;

    public TaskItem(String title
            , String dateTime
            , String brigade
            , String priority
            , String status) {
        this.title = title;
        this.dateTime = dateTime;
        this.brigade = brigade;
        this.priority = priority;
        this.status = status;
    }

    public TaskItem(String title, String dateTime, String brigade, String priority) {
        this(title, dateTime, brigade, priority, "active");
    }
}