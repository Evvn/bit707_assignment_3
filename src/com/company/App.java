package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
    private JButton sortButton;

    public App(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.setMinimumSize(new Dimension(375, 667));
        this.setMaximumSize(new Dimension(375, 667));
        this.setLocationRelativeTo(null);
        this.pack();

        // make header
        labelTitle.setText("<html><h2>Tasks</h2></html>");

        panelTaskList.setLayout(new BoxLayout(panelTaskList, BoxLayout.Y_AXIS));
        scrollTasks.setPreferredSize(new Dimension(350, 520));

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

    public void closeScreen(JPanel container) {
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

    public void createTask() {
        JPanel container = new JPanel();
        JLabel nameLabel = new JLabel("Task name");
        JTextField taskName = new JTextField();
        JLabel descriptionLabel = new JLabel("Task description");
        JTextArea taskDescription = new JTextArea();
        taskDescription.setRows(5);
        JLabel dueLabel = new JLabel("Due at... (yyyy-mm-dd hh:mm)");
        JTextField dueDate = new JTextField();

        // controls
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        JButton cancelButton = new JButton("Cancel");
        JButton addButton = new JButton("Create");
        panelButtons.add(cancelButton);
        panelButtons.add(addButton);

        // set container layout
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        // add to container
        container.add(nameLabel);
        container.add(taskName);
        container.add(descriptionLabel);
        container.add(taskDescription);
        container.add(dueLabel);
        container.add(dueDate);
        container.add(panelButtons);

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
                closeScreen(container);
            }
        });
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
        closeScreen(container);
    }

    public void viewTaskScreen(Task t) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // launch edit task panel (like create, pre-populated)
        JPanel container = new JPanel();
        JLabel nameLabel = new JLabel("Task name");
        JTextField taskName = new JTextField(t.getTaskName());
        JLabel descriptionLabel = new JLabel("Task description");
        JTextArea taskDescription = new JTextArea(t.getTaskDescription());
        taskDescription.setRows(5);
        JLabel dueLabel = new JLabel("Due at... (yyyy-mm-dd hh:mm)");
        JTextField dueDate = new JTextField(df.format(t.getDueDate()));

        // controls
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        JButton cancelButton = new JButton("Cancel");
        JButton updateButton = new JButton("Update");
        panelButtons.add(cancelButton);
        panelButtons.add(updateButton);

        // set container layout
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        // add to container
        container.add(nameLabel);
        container.add(taskName);
        container.add(descriptionLabel);
        container.add(taskDescription);
        container.add(dueLabel);
        container.add(dueDate);
        container.add(panelButtons);

        // hide tasks page
        tasksPanel.setVisible(false);
        // show controls to add task
        this.add(container);
        this.validate();

        // create listeners for create and cancel
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on update button click

                // get task values
                editTask(t, container, taskName.getText(), taskDescription.getText(), dueDate.getText());
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on cancel button click, return tasks panel
                // return to main page and refresh tasks
                closeScreen(container);
            }
        });
    }

    public void editTaskScreen(Task t) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // launch edit task panel (like create, pre-populated)
        JPanel container = new JPanel();
        JLabel nameLabel = new JLabel("Task name");
        JTextField taskName = new JTextField(t.getTaskName());
        JLabel descriptionLabel = new JLabel("Task description");
        JTextArea taskDescription = new JTextArea(t.getTaskDescription());
        taskDescription.setRows(5);
        JLabel dueLabel = new JLabel("Due at... (yyyy-mm-dd hh:mm)");
        JTextField dueDate = new JTextField(df.format(t.getDueDate()));

        // controls
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        JButton cancelButton = new JButton("Cancel");
        JButton updateButton = new JButton("Update");
        panelButtons.add(cancelButton);
        panelButtons.add(updateButton);

        // set container layout
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        // add to container
        container.add(nameLabel);
        container.add(taskName);
        container.add(descriptionLabel);
        container.add(taskDescription);
        container.add(dueLabel);
        container.add(dueDate);
        container.add(panelButtons);

        // hide tasks page
        tasksPanel.setVisible(false);
        // show controls to add task
        this.add(container);
        this.validate();

        // create listeners for create and cancel
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on update button click

                // get task values
                editTask(t, container, taskName.getText(), taskDescription.getText(), dueDate.getText());
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // on cancel button click, return tasks panel
                // return to main page and refresh tasks
                closeScreen(container);
            }
        });
    }

    public void editTask(Task t, JPanel container, String taskName, String taskDescription, String dueDate) {
        // sql statement to edit task
        String sql = "UPDATE ToDo SET taskName = '" + taskName + "', taskDescription = '" + taskDescription
                + "', dueDate = '" + dueDate + "', updatedDate = datetime('now','localtime') WHERE taskNumber = "
                + t.getTaskNumber() + ";";

        // try connection, return error if fails
        try  {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // return to main page and refresh tasks
        closeScreen(container);
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
            panelTask.setLayout(new BorderLayout());
            Border border = new LineBorder(Color.darkGray, 2, true);
            panelTask.setBorder(border);

            // spacer container
            JPanel panelSpacer = new JPanel();
            panelSpacer.setLayout(new BorderLayout());
            panelSpacer.setBorder(new EmptyBorder(5,0,5,0));

            // panel for checkbox and title
            JPanel panelTitle = new JPanel();
            panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.Y_AXIS));

            // complete checkbox
            JCheckBox checkIsComplete = new JCheckBox();
            checkIsComplete.setSelected(t.isComplete());

            // formatted task title
            JLabel labelTitle = new JLabel("<html><h3>" + t.getTaskName() + "</h3></html>");
            labelTitle.setBorder(new EmptyBorder(0,10,0,0));

            panelTitle.add(checkIsComplete);
            panelTitle.add(labelTitle);

            // task options
            JPanel buttonsContainer = new JPanel();
            buttonsContainer.setLayout(new BoxLayout(buttonsContainer, BoxLayout.Y_AXIS));
            JButton buttonView = new JButton("View");
            JButton buttonEdit = new JButton("Edit");
            JButton buttonDelete = new JButton("Delete");
            buttonsContainer.add(buttonView);
            buttonsContainer.add(buttonEdit);
            buttonsContainer.add(buttonDelete);

            // add to panel
            panelTask.add(panelTitle, BorderLayout.WEST);
            panelTask.add(buttonsContainer, BorderLayout.EAST);

            // view button event listener
            buttonView.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // on vie click, launch detailed view panel
                    System.out.println("view task: " + t.getTaskNumber());
                    viewTaskScreen(t);
                }
            });

            // edit button event listener
            buttonEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // on edit click, launch edit task panel
                    System.out.println("edit task: " + t.getTaskNumber());
                    editTaskScreen(t);
                }
            });

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

            // add to spacer panel
            panelSpacer.add(panelTask);

            // add the task
            panelTaskList.add(panelSpacer);
        }
        panelTaskList.validate();
        panelTaskList.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new App("To Do List");
        frame.setVisible(true);
    }
}
