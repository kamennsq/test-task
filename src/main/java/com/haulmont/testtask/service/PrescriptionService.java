package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.impl.PrescriptionDAOImpl;
import com.haulmont.testtask.entity.Prescription;
import com.vaadin.ui.Grid;

public class PrescriptionService {

    public Grid<Prescription> getPrescriptionsGrid(){
        PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();

        Prescription prescription = prescriptionDAO.getPrescriptionById(1L);

        Grid<Prescription> grid = new Grid<>(Prescription.class);
        grid.removeColumn("id");
        grid.removeColumn("doctor");
        grid.removeColumn("patient");
        grid.setColumnOrder("doctorFullName", "patientFullName","description", "priority", "date", "period");
        grid.setItems(prescription);
        grid.setSizeFull();

        return grid;
    }
}
