package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.impl.DoctorDAOImpl;
import com.haulmont.testtask.dao.impl.PrescriptionDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.Priority;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

import java.util.List;

public class PrescriptionService {
    private VerticalLayout layout = new VerticalLayout();
    private PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();
    private Binder<Prescription> binder = new Binder<>(Prescription.class);
    private TextField description = new TextField("Description");
    private TextField patientName = new TextField("Patient's name");
    private TextField doctorsName = new TextField("Doctor's name");
    private TextField priority = new TextField("Priority");
    private TextField creationDate = new TextField("Creation Date");
    private TextField expirationPeriod = new TextField("Expiration Period");
    private Button editButton = new Button("Edit");
    private Button deleteButton;
    private Prescription prescription;

    public Layout getPrescriptionsLayout() {
        constructLayoutComponents();
        return layout;
    }

    private Grid<Prescription> getGrid(){
        List<Prescription> prescriptions = prescriptionDAO.getPrescriptions();

        Grid<Prescription> grid = new Grid(Prescription.class);
        grid.getColumn("id").setHidden(true);
        grid.getColumn("doctor").setHidden(true);
        grid.getColumn("patient").setHidden(true);
        grid.setColumnOrder("doctorFullName", "patientFullName", "priority", "description", "date", "period");
        grid.setItems(prescriptions);
        grid.setSizeFull();

        grid.asSingleSelect().addSelectionListener(e ->{
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            for (int i = 0; i < prescriptions.size(); i++){
                if (prescriptions.get(i).equals(e.getFirstSelectedItem().get())){
                    prescription = prescriptions.get(i);
                }
            }
        });

        return grid;
    }

    private Button getCreateButton(){
        Button createButton = new Button("Create");
        createButton.addClickListener(e ->{
            toBuildExtraLayout();
            layout.addComponent(getConfirmCreationButton());
        });
        return createButton;
    }

    private Button getConfirmCreationButton(){
        Button confirmCreation = new Button("OK");
        confirmCreation.addClickListener(e -> interactWithTable("insert"));
        return confirmCreation;
    }

    private Button getCancelButton(){
        Button confirmCreation = new Button("Cancel");
        confirmCreation.addClickListener(e ->{
            constructLayoutComponents();
        });
        return confirmCreation;
    }

    private Button getEditButton(){
        editButton.setEnabled(false);
        editButton.addClickListener(e ->{
            toBuildExtraLayout();
            layout.addComponent(getConfirmEditButton());
            description.setValue(prescription.getDescription());
            patientName.setValue(prescription.getPatientFullName());
            doctorsName.setValue(prescription.getDoctorFullName());
            priority.setValue(prescription.getPriority().toString());
            creationDate.setValue(prescription.getDate().toString());
            expirationPeriod.setValue(prescription.getPeriod().toString());
        });
        return editButton;
    }

    private void toBuildExtraLayout(){
        layout.removeAllComponents();

        layout.addComponent(new Label("Please, fill a new data for Doctor"));

        layout.addComponent(name);
        binder.forField(name)
                .withValidator(new StringLengthValidator("Name should be from 2 to 12 symbols", 2, 12))
                .bind("name");

        layout.addComponent(surname);
        binder.forField(surname)
                .withValidator(new StringLengthValidator("Surname should be from 2 to 15 symbols", 2, 15))
                .bind("surname");

        layout.addComponent(patronymic);
        binder.forField(patronymic)
                .withValidator(new StringLengthValidator("Patronymic should be from 2 to 15 symbols", 2, 15))
                .bind("patronymic");

        layout.addComponent(specialization);
        binder.forField(specialization)
                .withValidator(new StringLengthValidator("Specialization should be from 2 to 15 symbols", 2, 15))
                .bind("specialization");

        layout.addComponent(getCancelButton());
    }

    private Button getConfirmEditButton(){
        Button button = new Button("Confirm");
        button.addClickListener(e -> interactWithTable("update"));
        return button;
    }

    private void interactWithTable(String action){
        Prescription prescription = new Prescription();
        prescription.setDescription(description.getValue());
        prescription.setPatient(p.getValue());
        prescription.setDoctor(patronymic.getValue());
        prescription.setPriority();
        prescription.setPeriod(Integer.valueOf(expirationPeriod.getValue()));
        if (this.prescription != null){
            prescription.setId(this.prescription.getId());
        }
        switch (action){
            case "insert": prescriptionDAO.insertPrescription(prescription); break;
            case "update": prescriptionDAO.updatePrescription(prescription); break;
            case "delete": prescriptionDAO.deletePrescription(prescription); break;
        }
        constructLayoutComponents();
    }

    private Button getDeleteButton(){
        deleteButton = new Button("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> {
            interactWithTable("delete");
        });
        return deleteButton;
    }

    private void constructLayoutComponents(){
        name.clear();
        surname.clear();
        patronymic.clear();
        specialization.clear();
        doctor = null;
        layout.removeAllComponents();
        layout.addComponent(getGrid());
        layout.addComponent(getCreateButton());
        layout.addComponent(getEditButton());
        layout.addComponent(getDeleteButton());
    }
}
