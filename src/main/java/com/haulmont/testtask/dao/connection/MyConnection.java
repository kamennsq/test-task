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
                connection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
                connection.createStatement().executeUpdate("create table if not exists Patient (ID BIGINT NOT NULL, " +
                        "Name VARCHAR(10) NOT NULL, " +
                        "Surname VARCHAR(10) NOT NULL, " +
                        "Patronymic VARCHAR(20) NOT NULL, " +
                        "PhoneNumber VARCHAR(10) NOT NULL, " +
                        "PRIMARY KEY(ID))");
                connection.createStatement().executeUpdate("create table if not exists Doctor (ID BIGINT NOT NULL, " +
                        "Name VARCHAR(10) NOT NULL, " +
                        "Surname VARCHAR(10) NOT NULL, " +
                        "Patronymic VARCHAR(20) NOT NULL, " +
                        "Specialization VARCHAR(20) NOT NULL, " +
                        "PRIMARY KEY(ID))");
                connection.createStatement().executeUpdate("create table if not exists Prescription (ID BIGINT NOT NULL, " +
                        "Description VARCHAR(50) NOT NULL, " +
                        "Patient BIGINT NOT NULL, " +
                        "Doctor BIGINT NOT NULL, " +
                        "Priority VARCHAR(10) NOT NULL, " +
                        "CreationDate DATE NOT NULL, " +
                        "ExpirationPeriod INTEGER NOT NULL, " +
                        "PRIMARY KEY(ID), " +
                        "FOREIGN KEY (Doctor) REFERENCES Doctor(Id), " +
                        "FOREIGN KEY (Patient) REFERENCES Patient(Id))");
                connection.createStatement().executeUpdate("create sequence idSequence start with 1 increment by 1");
                connection.createStatement().executeUpdate("insert into Patient values (next value for idSequence, 'Peter', 'Smith', 'None', '4545454')");
                connection.createStatement().executeUpdate("insert into Doctor values (next value for idSequence, 'Ivan', 'Post', 'None', 'Surgeon')");
                connection.createStatement().executeUpdate("insert into Prescription values (next value for idSequence, 'To kill the pain', 1, 2, 'NORMAL', to_date('20.03.20', 'DD.MM.YY'), 12)");
                connection.createStatement().executeUpdate("insert into Patient values (next value for idSequence, 'Andrew', 'Smith', 'None', '4545454')");
                connection.createStatement().executeUpdate("insert into Prescription values (next value for idSequence, 'New', 4, 2, 'CITO', sysdate, 1)");
                connection.createStatement().executeUpdate("insert into Doctor values (next value for idSequence, 'John', 'Post', 'None', 'Therapist')");
                connection.createStatement().executeUpdate("insert into Prescription values (next value for idSequence, 'Brand New', 4, 6, 'STATIM', sysdate, 1)");
                connection.createStatement().execute("CHECKPOINT");
            }
            catch (SQLException e) {
                System.out.println("Unable to get connection");
                e.printStackTrace();
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
                connection.createStatement().execute("SHUTDOWN");
                connection.close();
            } catch (SQLException e) {
                System.out.println("Unable to close connection");
            }
        }
    }
}
