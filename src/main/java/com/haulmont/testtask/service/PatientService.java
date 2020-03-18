package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.entity.Patient;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

import java.util.List;

public class PatientService {
    private VerticalLayout layout = new VerticalLayout();
    private PatientDAO patientDAO = new PatientDAOImpl();
    private Binder<Patient> binder = new Binder<>(Patient.class);
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField patronymic = new TextField("Patronymic");
    private TextField phoneNumber = new TextField("Phone Number");
    private Button editButton = new Button("Edit");
    private Button deleteButton;
    private Patient patient;

    public Layout getPatientsLayout() {
        constructLayoutComponents();
        return layout;
    }

    private Grid<Patient> getGrid(){
        List<Patient> patients = patientDAO.getPatients();

        Grid<Patient> grid = new Grid(Patient.class);
        grid.getColumn("id").setHidden(true);
        grid.setColumnOrder("name", "surname", "patronymic", "phoneNumber");
        grid.setItems(patients);
        grid.setSizeFull();

        grid.asSingleSelect().addSelectionListener(e ->{
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            for (int i = 0; i < patients.size(); i++){
                if (patients.get(i).equals(e.getFirstSelectedItem().get())){
                    patient = patients.get(i);
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
            name.setValue(patient.getName());
            surname.setValue(patient.getSurname());
            patronymic.setValue(patient.getPatronymic());
            phoneNumber.setValue(patient.getPhoneNumber());
        });
        return editButton;
    }

    private void toBuildExtraLayout(){
        layout.removeAllComponents();

        layout.addComponent(new Label("Please, fill a new data for Patient"));

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

        layout.addComponent(phoneNumber);
        binder.forField(phoneNumber)
                .withValidator(new StringLengthValidator("Phone number should 6 symbols", 6, 6))
                .bind("phoneNumber");

        layout.addComponent(getCancelButton());
    }

    private Button getConfirmEditButton(){
        Button button = new Button("Confirm");
        button.addClickListener(e -> interactWithTable("update"));
        return button;
    }

    private void interactWithTable(String action){
        Patient patient = new Patient();
        patient.setName(name.getValue());
        patient.setSurname(surname.getValue());
        patient.setPatronymic(patronymic.getValue());
        patient.setPhoneNumber(phoneNumber.getValue());
        if (this.patient != null){
            patient.setId(this.patient.getId());
        }
        switch (action){
            case "insert": patientDAO.insertPatient(patient); break;
            case "update": patientDAO.updatePatient(patient); break;
            case "delete": patientDAO.deletePatient(patient); break;
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
        phoneNumber.clear();
        patient = null;
        layout.removeAllComponents();
        layout.addComponent(getGrid());
        layout.addComponent(getCreateButton());
        layout.addComponent(getEditButton());
        layout.addComponent(getDeleteButton());
    }
}
