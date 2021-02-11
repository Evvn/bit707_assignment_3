package com.company;

import java.util.Date;

public class Task {

    // task attribute fields
    private int taskId;
    private String taskTitle;
    private String taskDescription;
    private boolean isComplete;
    private Date creationDate;
    private Date updatedDate;
    private Date dueDate;

    // task methods
    public void updateTask(String newTitle, String newDescription, Date newDueDate) {
        // update task here
    }

    public void completeTask(boolean isComplete) {
        // set task to complete or not here
    }
}
