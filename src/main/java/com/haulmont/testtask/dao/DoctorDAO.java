package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Doctor;

import java.util.List;

public interface DoctorDAO {

    List<Doctor> getDoctors();

    void insertDoctor(Doctor doctor);

    Doctor getDoctorById(Long id);

    void updateDoctor(Doctor doctor);

    void deleteDoctor(Doctor doctor);
}
