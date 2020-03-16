package com.haulmont.testtask.servlet;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.impl.DoctorDAOImpl;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.dao.impl.PrescriptionDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class DoctorService extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

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
