package com.company;

import java.sql.*;

public class DBConnection {
    private final Connection connection;

    public DBConnection() {
        connection = Connect();
    }

    public ResultSet RunSql(String sqlStatement) throws SQLException {
        String sql = sqlStatement;

        // try connection, return error if fails
        try  {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public Connection Connect() {
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

    public static void main(String[] args) {
        DBConnection connection = new DBConnection();
    }
}
