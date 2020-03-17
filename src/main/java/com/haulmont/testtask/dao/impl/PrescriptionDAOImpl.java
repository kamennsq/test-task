package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.Priority;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrescriptionDAOImpl implements PrescriptionDAO {

    @Override
    public Prescription getPrescriptionById(Long id) {
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        PatientDAO patientDAO = new PatientDAOImpl();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Prescription where Id = ?");
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            Prescription prescription = new Prescription();
            while (rs.next()){
                prescription.setId(rs.getLong("id"));
                prescription.setDescription(rs.getString("Description"));
                prescription.setPatient(patientDAO.getPatientById(rs.getLong("Patient")));
                prescription.setDoctor(doctorDAO.getDoctorById(rs.getLong("Doctor")));
                prescription.setDate(rs.getDate("CreationDate"));
                prescription.setPeriod(rs.getInt("ExpirationPeriod"));
                prescription.setPriority(Priority.valueOf(rs.getString("Priority")));
                prescription.setDoctorFullName();
                prescription.setPatientFullName();
            }
            return prescription;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public void insertPrescription(Long id, String description, Long patientId, Long doctorId, Integer expirationPeriod, Priority priority) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("insert into Prescription values (?, ?, ?, ?, sysdate, ?, ?)");
            ps.setLong(1, id);
            ps.setString(2, description);
            ps.setLong(3, patientId);
            ps.setLong(4, doctorId);
            ps.setInt(5, expirationPeriod);
            ps.setString(6, priority.toString());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
