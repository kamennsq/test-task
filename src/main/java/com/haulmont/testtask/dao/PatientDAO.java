package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Patient;

public interface PatientDAO {

    Patient getPatientByName(String name);

    void insertPatient(Patient patient);

    Patient getPatientById(Long id);

}
