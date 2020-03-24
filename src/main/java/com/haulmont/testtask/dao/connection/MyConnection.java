package com.haulmont.testtask.dao.connection;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class MyConnection {
    public static Connection connection;

    private static final String URL = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public MyConnection(){
        getConnection();
    }

    private Connection getConnection(){
        if (connection == null) {
            try {
                InputStream inputStream = new BufferedInputStream(new FileInputStream("src/main/resources/script.sql"));
                Class.forName("org.hsqldb.jdbc.JDBCDriver");
                connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "SA", "");
                Scanner s = new Scanner(inputStream).useDelimiter(";");
                String result;
                while (s.hasNext()){
                    result = s.next();
                    PreparedStatement ps = connection.prepareStatement(result);
                    ps.executeUpdate();
                }
                inputStream.close();
            }
            catch (SQLException e) {
                System.out.println("Unable to get connection");
                e.printStackTrace();
            }
            catch (ClassNotFoundException e){
                System.out.println("Driver was not found");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void closeConnection(){
        if (connection != null) {
            try {
                connection.createStatement().execute("SHUTDOWN");
                connection.close();
            } catch (SQLException e) {
                System.out.println("Unable to close connection");
            }
        }
    }
}
