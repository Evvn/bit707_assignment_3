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
    private JLabel labelTitle;
    private JScrollPane scrollTasks;
    private JPanel panelTaskList;

    public App(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(375, 667));
        this.setLocationRelativeTo(null);
        this.pack();

        // run select all to fetch all tasks from db
        getTasks();

        // print all tasks to page
        printTasks();

        buttonAddTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on add button click
            }
        });


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

                // iterate over each record and print ln with data
//                System.out.println(rs.getInt("taskNumber") + "\t" + rs.getString("taskName"));
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printTasks() {
        // add tasks to scrollable panel
        for (Task t : tasks)
        {
            // add a task
            panelTaskList.add(t.drawTask());
        }
        panelTaskList.validate();
    }

    public static void main(String[] args) {
//        App app = new App();

        JFrame frame = new App("To Do List");
        frame.setVisible(true);
    }
}
