package com.haulmont.testtask;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.dao.impl.DoctorDAOImpl;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.dao.impl.PrescriptionDAOImpl;
import com.haulmont.testtask.entity.Doctor;
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

        PatientDAO patientDAO = new PatientDAOImpl();
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);

        Doctor doctor = doctorDAO.getDoctorByName("Ivan");

        Grid<Doctor> grid = new Grid(Doctor.class);
        grid.removeColumn("id");
        grid.setColumnOrder("name", "surname", "patronymic", "specialization");
        grid.setItems(doctor);
        grid.setSizeFull();

        layout.addComponent(grid);

        setContent(layout);
    }
}