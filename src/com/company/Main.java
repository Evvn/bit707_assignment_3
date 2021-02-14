package com.company;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Main {

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
    public void selectAll() {
        // sql statement to select all records
        String sql = "SELECT * FROM ToDo";

        // try connection, return error if fails
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // iterate over each record and print ln with data
                System.out.println(rs.getInt("taskNumber") + "\t" + rs.getString("taskName"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        Main app = new Main();

        // app container
        JFrame appContainer = new JFrame();
        appContainer.setTitle("To Do List");
        appContainer.setSize(new Dimension(500, 850));
        appContainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appContainer.setLocationRelativeTo(null);
        appContainer.setResizable(false);
        appContainer.setVisible(true);

        // run select all tasks
        app.selectAll();
    }
}
