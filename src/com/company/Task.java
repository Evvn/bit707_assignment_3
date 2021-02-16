package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Date;

/**
 * Task object used to store Task attributes and details
 */
public class Task {

    // task attribute fields
    private int taskNumber;
    private String taskName;
    private String taskDescription;
    private boolean isComplete;
    private Date creationDate;
    private Date updatedDate;
    private Date dueDate;

    /**
     * task creation
     * @param num task number
     * @param name task name
     * @param description task description
     * @param complete is task complete?
     * @param createdAt date task is created
     * @param updatedAt date task was last updated
     * @param dueAt date task is due
     */
    public Task(int num, String name, String description, boolean complete, Date createdAt, Date updatedAt, Date dueAt) {
        taskNumber = num;
        taskName = name;
        taskDescription = description;
        isComplete = complete;
        creationDate = createdAt;
        updatedDate = updatedAt;
        dueDate = dueAt;
    }

    /**
     * sort method used when sorting list of tasks by due date
     */
    static class SortByDue implements Comparator<Task> {
        @Override
        public int compare(Task a, Task b) {
            return a.getDueDate().compareTo(b.getDueDate());
        }
    }

    /**
     * sort method used when sorting list of tasks by creation date
     */
    static class SortByCreation implements Comparator<Task> {
        @Override
        public int compare(Task a, Task b) {
            return a.getCreationDate().compareTo(b.getCreationDate());
        }
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
