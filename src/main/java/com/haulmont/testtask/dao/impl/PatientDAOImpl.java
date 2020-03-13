package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.exception.ImpossibleToInsertPatient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public Patient getPatientByName(String name) {
        try {
            PreparedStatement ps = MyConnection.connection.prepareStatement("select a from Patient a where name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.getObject(1, Patient.class);
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public void insertPatient(Patient patient) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("insert into SYSTEM_LOBS.PATIENT(ID, Name, Surname, Patronymic, PhoneNumber) " +
                    "values (?, ?, ?, ?, ?)");
            ps.setLong(1, patient.getId());
            ps.setString(2, patient.getName());
            ps.setString(3, patient.getSurname());
            ps.setString(4, patient.getPatronymic());
            ps.setString(5, patient.getPhoneNumber());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
            //throw new ImpossibleToInsertPatient();
        }
    }
}
