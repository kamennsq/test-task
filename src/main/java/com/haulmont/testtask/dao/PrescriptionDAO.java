package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.Priority;

public interface PrescriptionDAO {

    Prescription getPrescriptionById(Long id);

    void insertPrescription(Long id, String description, Long patientId, Long doctorId, Integer expirationPeriod, Priority priority);

}
