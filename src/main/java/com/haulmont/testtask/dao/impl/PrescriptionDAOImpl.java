package com.haulmont.testtask.dao.impl;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.Priority;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAOImpl implements PrescriptionDAO {
    private List<Doctor> doctorList = new ArrayList<>();
    private List<Patient> patientList = new ArrayList<>();

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
                prescription.setCreationDate(rs.getDate("CreationDate"));
                prescription.setExpirationPeriod(rs.getInt("ExpirationPeriod"));
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
    public List<Prescription> getPrescriptions() {
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        PatientDAO patientDAO = new PatientDAOImpl();
        List<Prescription> list = new ArrayList<>();
        setDoctorList(doctorDAO);
        setPatientList(patientDAO);
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Prescription");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Prescription prescription = new Prescription();
                prescription.setId(rs.getLong("id"));
                prescription.setDescription(rs.getString("Description"));
                prescription.setPatient(patientDAO.getPatientById(rs.getLong("Patient")));
                prescription.setDoctor(doctorDAO.getDoctorById(rs.getLong("Doctor")));
                prescription.setCreationDate(rs.getDate("CreationDate"));
                prescription.setExpirationPeriod(rs.getInt("ExpirationPeriod"));
                prescription.setPriority(Priority.valueOf(rs.getString("Priority")));
                prescription.setDoctorFullName();
                prescription.setPatientFullName();
                list.add(prescription);
            }
            return list;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public List<Prescription> getPrescriptionsByDoctor(Doctor doctor) {
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        PatientDAO patientDAO = new PatientDAOImpl();
        List<Prescription> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Prescription where Doctor = ?");
            ps.setLong(1, doctor.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Prescription prescription = new Prescription();
                prescription.setId(rs.getLong("id"));
                prescription.setDescription(rs.getString("Description"));
                prescription.setPatient(patientDAO.getPatientById(rs.getLong("Patient")));
                prescription.setDoctor(doctorDAO.getDoctorById(rs.getLong("Doctor")));
                prescription.setCreationDate(rs.getDate("CreationDate"));
                prescription.setExpirationPeriod(rs.getInt("ExpirationPeriod"));
                prescription.setPriority(Priority.valueOf(rs.getString("Priority")));
                prescription.setDoctorFullName();
                prescription.setPatientFullName();
                list.add(prescription);
            }
            return list;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public List<Prescription> getPrescriptionByPatient(Patient patient) {
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        PatientDAO patientDAO = new PatientDAOImpl();
        List<Prescription> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Prescription where Patient = ?");
            ps.setLong(1, patient.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Prescription prescription = new Prescription();
                prescription.setId(rs.getLong("id"));
                prescription.setDescription(rs.getString("Description"));
                prescription.setPatient(patientDAO.getPatientById(rs.getLong("Patient")));
                prescription.setDoctor(doctorDAO.getDoctorById(rs.getLong("Doctor")));
                prescription.setCreationDate(rs.getDate("CreationDate"));
                prescription.setExpirationPeriod(rs.getInt("ExpirationPeriod"));
                prescription.setPriority(Priority.valueOf(rs.getString("Priority")));
                prescription.setDoctorFullName();
                prescription.setPatientFullName();
                list.add(prescription);
            }
            return list;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public List<Prescription> getPrescriptionByPriority(Priority priority) {
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        PatientDAO patientDAO = new PatientDAOImpl();
        List<Prescription> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Prescription where Priority = ?");
            ps.setString(1, priority.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Prescription prescription = new Prescription();
                prescription.setId(rs.getLong("id"));
                prescription.setDescription(rs.getString("Description"));
                prescription.setPatient(patientDAO.getPatientById(rs.getLong("Patient")));
                prescription.setDoctor(doctorDAO.getDoctorById(rs.getLong("Doctor")));
                prescription.setCreationDate(rs.getDate("CreationDate"));
                prescription.setExpirationPeriod(rs.getInt("ExpirationPeriod"));
                prescription.setPriority(Priority.valueOf(rs.getString("Priority")));
                prescription.setDoctorFullName();
                prescription.setPatientFullName();
                list.add(prescription);
            }
            return list;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public List<Prescription> getPrescriptionByDescription(String description) {
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        PatientDAO patientDAO = new PatientDAOImpl();
        List<Prescription> list = new ArrayList<>();
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("select * from Prescription where Description like (%?%)");
            ps.setString(1, description);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Prescription prescription = new Prescription();
                prescription.setId(rs.getLong("id"));
                prescription.setDescription(rs.getString("Description"));
                prescription.setPatient(patientDAO.getPatientById(rs.getLong("Patient")));
                prescription.setDoctor(doctorDAO.getDoctorById(rs.getLong("Doctor")));
                prescription.setCreationDate(rs.getDate("CreationDate"));
                prescription.setExpirationPeriod(rs.getInt("ExpirationPeriod"));
                prescription.setPriority(Priority.valueOf(rs.getString("Priority")));
                prescription.setDoctorFullName();
                prescription.setPatientFullName();
                list.add(prescription);
            }
            return list;
        }
        catch (SQLException e){
            return null;
        }
    }

    @Override
    public void updatePrescription(Prescription prescription) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("merge into Prescription ps " +
                    "using (select Id from Prescription where Id = ?) a " +
                    "on ps.Id = a.Id " +
                    "when matched then update set " +
                    "Description = ?, " +
                    "ExpirationPeriod = ?, " +
                    "Patient = ?, " +
                    "Doctor = ?, " +
                    "Priority = ?");
            ps.setLong(1, prescription.getId());
            ps.setString(2, prescription.getDescription());
            ps.setInt(3, prescription.getExpirationPeriod());
            ps.setLong(4, prescription.getPatient().getId());
            ps.setLong(5, prescription.getDoctor().getId());
            ps.setString(6, prescription.getPriority().toString());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deletePrescription(Prescription prescription) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("delete from Prescription where Id = ?");
            ps.setLong(1, prescription.getId());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertPrescription(Prescription prescription) {
        try{
            PreparedStatement ps = MyConnection.connection.prepareStatement("insert into Prescription values (next value for idSequence, ?, ?, ?, ?, sysdate, ?)");
            ps.setString(1, prescription.getDescription());
            ps.setLong(2, prescription.getPatient().getId());
            ps.setLong(3, prescription.getDoctor().getId());
            ps.setString(4, prescription.getPriority().toString());
            ps.setInt(5, prescription.getExpirationPeriod());
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void setDoctorList(DoctorDAO doctorDAO){
        doctorList = doctorDAO.getDoctors();
    }

    private void setPatientList(PatientDAO patientDAO){
        patientList = patientDAO.getPatients();
    }

    public List<Patient> getPatientList(){
        return patientList;
    }

    public List<Doctor> getDoctorList(){
        return doctorList;
    }
}
