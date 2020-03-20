package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.Priority;

import java.util.List;

public interface PrescriptionDAO {

    Prescription getPrescriptionById(Long id);

    void insertPrescription(Prescription prescription);

    List<Prescription> getPrescriptions();

    List<Prescription> getPrescriptionsByDoctor(Doctor doctor);

    List<Prescription> getPrescriptionByPatientName(String name);

    List<Prescription> getPrescriptionByPriority(String priority);

    List<Prescription> getPrescriptionByDescription(String description);

    void updatePrescription(Prescription prescription);

    void deletePrescription(Prescription prescription);

    List<Patient> getPatientList();

    List<Doctor> getDoctorList();

}
