package com.company;

import java.sql.*;

/**
 * Database connection class
 */
public class DBConnection {
    private final Connection connection;

    /**
     * sets sqlite db connection to be used by class
     */
    public DBConnection() {
        connection = Connect();
    }

    /**
     * run sql query on sqlite db connected
     * @param sqlStatement sql query to run
     * @return returns ResultSet to iterate over if needed
     * @throws SQLException exception for catching sql errors from sqlite
     */
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

    /**
     * Creates new connection with sqlite db to be used for queries
     * @return returns Connection object to store in Class (only want to init one per session)
     */
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

    /**
     * psvm boilerplate
     * @param args psvm boilerplate
     */
    public static void main(String[] args) {
        DBConnection connection = new DBConnection();
    }
}
