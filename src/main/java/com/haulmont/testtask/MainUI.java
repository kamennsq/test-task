package com.haulmont.testtask;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.exception.PatientNotFound;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        MyConnection connection = new MyConnection();


        Patient patient = new Patient();
        PatientDAO patientDAO = new PatientDAOImpl();

        patient.setId(1L);
        patient.setName("Peter");
        patient.setSurname("Smith");
        patient.setPatronymic("None");
        patient.setPhoneNumber("444444");

        patientDAO.insertPatient(patient);
        patient = null;

        patient = patientDAO.getPatientByName("Peter");
        if (patient == null) {throw new PatientNotFound();}
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);

        layout.addComponent(new Label(patient.getName()));

        setContent(layout);
        connection.closeConnection();
    }
}