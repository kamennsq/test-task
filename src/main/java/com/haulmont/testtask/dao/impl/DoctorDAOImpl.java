package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.entity.Doctor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public Doctor getDoctorByName(String name) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Doctor where name = ?");
            ps.setString(1, name);

            ResultSet rs  = ps.executeQuery();
            Doctor doctor = new Doctor();
            while (rs.next()){
                doctor.setId(rs.getLong("Id"));
                doctor.setName(rs.getString("Name"));
                doctor.setSurname(rs.getString("Surname"));
                doctor.setPatronymic(rs.getString("Patronymic"));
                doctor.setSpecialization(rs.getString("Specialization"));
            }
            return doctor;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public void insertDoctor(Long id, String name, String surname, String patronymic, String specialization) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("insert into Doctor values (?, ?, ?, ?, ?)");
            ps.setLong(1, id);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, patronymic);
            ps.setString(5, specialization);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
