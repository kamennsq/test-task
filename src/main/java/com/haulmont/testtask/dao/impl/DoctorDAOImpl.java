package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Prescription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public List<Doctor> getDoctors() {
        List<Doctor> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Doctor");
            ResultSet rs  = ps.executeQuery();
            while (rs.next()){
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("Id"));
                doctor.setName(rs.getString("Name"));
                doctor.setSurname(rs.getString("Surname"));
                doctor.setPatronymic(rs.getString("Patronymic"));
                doctor.setSpecialization(rs.getString("Specialization"));
                list.add(doctor);
            }
            return list;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public void insertDoctor(Doctor doctor) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("insert into Doctor values ((select max(Id)+1 from Doctor), ?, ?, ?, ?)");
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSurname());
            ps.setString(3, doctor.getPatronymic());
            ps.setString(4, doctor.getSpecialization());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Doctor getDoctorById(Long id) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Doctor where id = ?");
            ps.setLong(1, id);

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
    public void updateDoctor(Doctor doctor) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("merge into Doctor doc " +
                    "using (select Id from Doctor where Id = ?) a " +
                    "on doc.Id = a.Id " +
                    "when matched then update set " +
                    "Name = ?, " +
                    "Surname = ?, " +
                    "Patronymic = ?, " +
                    "Specialization = ?");
            ps.setLong(1, doctor.getId());
            ps.setString(2, doctor.getName());
            ps.setString(3, doctor.getSurname());
            ps.setString(4, doctor.getPatronymic());
            ps.setString(5, doctor.getSpecialization());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDoctor(Doctor doctor) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("delete from Doctor where Id = ?");
            ps.setLong(1, doctor.getId());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
