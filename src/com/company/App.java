package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class App extends JFrame {
    // all tasks
    ArrayList<Task> tasks = new ArrayList<>();

    private JButton buttonAddTask;
    private JPanel panelMain;
    private JPanel panelTaskList;
    private JPanel tasksPanel;
    private JLabel labelTitle;
    private JScrollPane scrollTasks;

    public App(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(375, 667));
        this.setLocationRelativeTo(null);
        this.pack();

        panelTaskList.setLayout(new BoxLayout(panelTaskList, BoxLayout.Y_AXIS));

        // run select all to fetch all tasks from db
        getTasks();
        // print all tasks to page
        printTasks();

        buttonAddTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on add button click
                createTask();
            }
        });
    }

    public void createTask() {
        JPanel container = new JPanel();
        JLabel nameLabel = new JLabel("Task name");
        JTextField taskName = new JTextField();
        JLabel descriptionLabel = new JLabel("Task description");
        JTextField taskDescription = new JTextField();
        JLabel dueLabel = new JLabel("Due at... (yyyy-mm-dd hh:mm)");
        JTextField dueDate = new JTextField();
        JButton cancelButton = new JButton("Cancel");
        JButton addButton = new JButton("Create");

        // set container layout
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        // add to container
        container.add(nameLabel);
        container.add(taskName);
        container.add(descriptionLabel);
        container.add(taskDescription);
        container.add(dueLabel);
        container.add(dueDate);
        container.add(cancelButton);
        container.add(addButton);

        // hide tasks page
        tasksPanel.setVisible(false);
        // show controls to add task
        this.add(container);
        this.validate();

        // create listeners for create and cancel
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on add button click

                // get task values
                addTask(taskName.getText(), taskDescription.getText(), dueDate.getText(), container);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on cancel button click, return tasks panel
                closeCreateTask(container);
            }
        });
    }

    public void closeCreateTask(JPanel container) {
        // remove create task and bring back tasks pane
        this.remove(container);
        tasksPanel.setVisible(true);
        this.validate();
        this.repaint();
        // run select all to fetch all tasks from db
        getTasks();
        // print all tasks to page
        printTasks();
    }

    public void addTask(String taskName, String taskDescription, String dueDate, JPanel container) {
        // create new task in sqlite db

        // sql statement to add task
        String sql = "INSERT INTO ToDo (taskName, taskDescription, isComplete, creationDate, updatedDate, dueDate)" +
                " VALUES('"+ taskName + "', '" + taskDescription + "', 0, " +
                "datetime('now','localtime'), datetime('now','localtime'), '" + dueDate + "');";

        // try connection, return error if fails
        try  {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // return to main page and get new tasks
        closeCreateTask(container);
    }

    public void deleteTask(int taskNumber) {
        // delete task from sqlite db

        // sql statement to add task
        String sql = "DELETE FROM ToDo WHERE taskNumber = " + taskNumber + ";";

        // try connection, return error if fails
        try  {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // run select all to fetch all tasks from db
        getTasks();
        // print all tasks to page
        printTasks();
    }

    public void setIsComplete(int taskNumber, boolean isComplete) {
        // update if task is complete in db

        // turn bool to int for update in sqlite
        int sqlBool = 0;
        if (isComplete) { sqlBool = 1; }

        // sql statement to edit task
        String sql = "UPDATE ToDo SET isComplete = " + sqlBool + " WHERE taskNumber = " + taskNumber + ";";

        // try connection, return error if fails
        try  {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // run select all to fetch all tasks from db
        getTasks();
        // print all tasks to page
        printTasks();
    }

    // return connection to sqlite db "ToDo.db" in project root
    private Connection connect() {
        String url = "jdbc:sqlite:ToDo.db";
        Connection conn = null;

        // try to connect to db, if fail print error
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // select all records from ToDo db
    public void getTasks() {
        // sql statement to select all records
        String sql = "SELECT * FROM ToDo";

        // clear tasks list first
        tasks.clear();

        // try connection, return error if fails
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                // create Task objects and put in list
                int taskNumber = rs.getInt("taskNumber");
                String taskName = rs.getString("taskName");
                String taskDescription = rs.getString("taskDescription");
                boolean isComplete = (rs.getInt("isComplete") != 0);
                java.util.Date creationDate = df.parse(rs.getString("creationDate"));
                java.util.Date updatedDate = df.parse(rs.getString("updatedDate"));
                java.util.Date dueDate = df.parse(rs.getString("dueDate"));

                Task task = new Task(taskNumber, taskName, taskDescription, isComplete, creationDate, updatedDate, dueDate);
                tasks.add(task);
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printTasks() {
        // clear panel first
        panelTaskList.removeAll();
        // add tasks to scrollable panel
        for (Task t : tasks)
        {
            // task panel
            // task container
            JPanel panelTask = new JPanel();
            panelTask.setLayout(new FlowLayout());
            // complete checkbox
            JCheckBox checkIsComplete = new JCheckBox();
            checkIsComplete.setSelected(t.isComplete());
            // task title
            JLabel labelTitle = new JLabel(t.getTaskName());
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

            // delete button event listener
            buttonDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // on delete click, remove task
                    System.out.println("delete task: " + t.getTaskNumber());
                    deleteTask(t.getTaskNumber());
                }
            });

            // checked (completed) event listener
            checkIsComplete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // on checkbox
                    System.out.println(checkIsComplete.isSelected() + " task: " + t.getTaskNumber());
                    setIsComplete(t.getTaskNumber(), checkIsComplete.isSelected());
                }
            });

            // add the task
            panelTaskList.add(panelTask);
        }
        panelTaskList.validate();
        panelTaskList.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new App("To Do List");
        frame.setVisible(true);
    }
}
