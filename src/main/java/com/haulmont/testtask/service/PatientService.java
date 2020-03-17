package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.impl.DoctorDAOImpl;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.vaadin.ui.Grid;

public class PatientService {

    public Grid<Patient> getPatientsGrid(){
        PatientDAO patientDAO = new PatientDAOImpl();

        Patient patient = patientDAO.getPatientByName("Peter");

        Grid<Patient> grid = new Grid(Patient.class);
        grid.removeColumn("id");
        grid.setColumnOrder("name", "surname", "patronymic", "phoneNumber");
        grid.setItems(patient);
        grid.setSizeFull();

        return grid;
    }
}
