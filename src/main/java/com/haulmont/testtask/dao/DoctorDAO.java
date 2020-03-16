package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;

public interface DoctorDAO {

    Doctor getDoctorByName(String name);

    void insertDoctor(Long id, String name, String surname, String patronymic, String specialization);

}
