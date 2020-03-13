package com.haulmont.testtask.dao.connection;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


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
                Class.forName("org.hsqldb.jdbc.JDBCDriver");
                connection = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "");
                connection.createStatement().executeUpdate("create table SYSTEM_LOBS.PATIENT (ID BIGINT NOT NULL, " +
                        "Name VARCHAR(10) NOT NULL, " +
                        "Surname VARCHAR(10) NOT NULL, " +
                        "Patronymic VARCHAR(20) NOT NULL, " +
                        "PhoneNumber VARCHAR(10) NOT NULL, " +
                        "PRIMARY KEY(ID))");
                connection.createStatement().execute("CHECKPOINT");
            }
            catch (SQLException e) {
                System.out.println("Unable to get connection");
            }
            catch (ClassNotFoundException e){
                System.out.println("Driver was not found");
            }
        }
        return connection;
    }

    public void closeConnection(){
        if (connection != null) {
            try {

                connection.close();
            } catch (SQLException e) {
                System.out.println("Unable to close connection");
            }
        }
    }
}
