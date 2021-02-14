package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Task {

    // task attribute fields
    private int taskNumber;
    private String taskName;
    private String taskDescription;
    private boolean isComplete;
    private Date creationDate;
    private Date updatedDate;
    private Date dueDate;

    // constructor
    public Task(int num, String name, String description, boolean complete, Date createdAt, Date updatedAt, Date dueAt) {
        taskNumber = num;
        taskName = name;
        taskDescription = description;
        isComplete = complete;
        creationDate = createdAt;
        updatedDate = updatedAt;
        dueDate = dueAt;
    }

    // generate task for gui
    public JPanel drawTask() {
        // task container
        JPanel panelTask = new JPanel();
        panelTask.setLayout(new FlowLayout());
        // complete checkbox
        JCheckBox checkIsComplete = new JCheckBox();
        // task title
        JLabel labelTitle = new JLabel(taskName);
        // task options
        JButton buttonView = new JButton("View");
        JButton buttonEdit = new JButton("Edit");
        JButton buttonDelete = new JButton("Delete");

        // add to panel
        panelTask.add(checkIsComplete);
        panelTask.add(labelTitle);
        panelTask.add(buttonView);
        panelTask.add(buttonEdit);
        panelTask.add(buttonDelete);

        return panelTask;
    }

    // task methods
    public void updateTask(String newTitle, String newDescription, Date newDueDate) {
        // update task here
    }

    public void completeTask(boolean isComplete) {
        // set task to complete or not here
    }

    // generate getters and setters
    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
