package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Patient;

import java.util.List;

public interface PatientDAO {

    List<Patient> getPatients();

    void insertPatient(Patient patient);

    Patient getPatientById(Long id);

    void updatePatient(Patient patient);

    void deletePatient(Patient patient);

}
