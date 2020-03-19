package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.exception.ImpossibleToInsertPatient;
import com.haulmont.testtask.exception.PatientNotFound;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public List<Patient> getPatients() {
        List<Patient> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Patient");
            ResultSet rs  = ps.executeQuery();
            while (rs.next()){
                Patient patient = new Patient();
                patient.setId(rs.getLong("Id"));
                patient.setName(rs.getString("Name"));
                patient.setSurname(rs.getString("Surname"));
                patient.setPatronymic(rs.getString("Patronymic"));
                patient.setPhoneNumber(rs.getString("PhoneNumber"));
                list.add(patient);
            }
            return list;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public void insertPatient(Patient patient) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("insert into PATIENT values (next value for idSequence, ?, ?, ?, ?)");
            ps.setString(1, patient.getName());
            ps.setString(2, patient.getSurname());
            ps.setString(3, patient.getPatronymic());
            ps.setString(4, patient.getPhoneNumber());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ImpossibleToInsertPatient();
        }
    }

    @Override
    public Patient getPatientById(Long id) {
        try {
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Patient a where id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            Patient patient = new Patient();
            while (rs.next()) {
                patient.setId(rs.getLong("ID"));
                patient.setName(rs.getString("Name"));
                patient.setSurname(rs.getString("Surname"));
                patient.setPatronymic(rs.getString("Patronymic"));
                patient.setPhoneNumber(rs.getString("PhoneNumber"));
            }
            return patient;
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new PatientNotFound();
            //return null;
        }
    }

    @Override
    public void updatePatient(Patient patient) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("merge into Patient p " +
                    "using (select Id from Patient where Id = ?) a " +
                    "on p.Id = a.Id " +
                    "when matched then update set " +
                    "Name = ?, " +
                    "Surname = ?, " +
                    "Patronymic = ?, " +
                    "PhoneNumber = ?");
            ps.setLong(1, patient.getId());
            ps.setString(2, patient.getName());
            ps.setString(3, patient.getSurname());
            ps.setString(4, patient.getPatronymic());
            ps.setString(5, patient.getPhoneNumber());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deletePatient(Patient patient) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("delete from Patient where Id = ?");
            ps.setLong(1, patient.getId());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
