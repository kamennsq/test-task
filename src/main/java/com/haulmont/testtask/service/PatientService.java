package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.validation.SimpleStringValidator;
import com.vaadin.ui.*;

import java.util.List;

public class PatientService {
    private VerticalLayout layout = new VerticalLayout();
    private PatientDAO patientDAO = new PatientDAOImpl();
    private SimpleStringValidator stringValidator = new SimpleStringValidator();
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField patronymic = new TextField("Patronymic");
    private TextField phoneNumber = new TextField("Phone Number");
    private Button editButton = new Button("Edit");
    private Button deleteButton;
    private Patient patient;
    private boolean isNameValid = false;
    private boolean isSurnameValid = false;
    private boolean isPatronymicValid = false;
    private boolean isPhoneNumberValid = false;

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
        confirmCreation.addClickListener(e ->{
            if (areValuesValid()){
                interactWithTable("insert");
            }
        });
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

        Label nameLabel = new Label("Name should contain from 3 to 15 symbols");
        Label surnameLabel = new Label("Surname should contain from 3 to 15 symbols");
        Label patronymicLabel = new Label("Patronymic should contain from 3 to 15 symbols");
        Label phoneLabel = new Label("Phone number should contain 6 integer numbers");
        nameLabel.setVisible(false);
        surnameLabel.setVisible(false);
        patronymicLabel.setVisible(false);
        phoneLabel.setVisible(false);

        layout.addComponent(new Label("Please, fill a new data for Patient"));

        layout.addComponent(name);
        name.addValueChangeListener(e ->{
            isNameValid = stringValidator.isValidString(name.getValue());
            if(!isNameValid){
                nameLabel.setVisible(true);
            }
            else{
                nameLabel.setVisible(false);
            }
        });
        layout.addComponentAsFirst(nameLabel);

        layout.addComponent(surname);
        surname.addValueChangeListener(e ->{
            isSurnameValid = stringValidator.isValidString(surname.getValue());
            if(!isSurnameValid){
                surnameLabel.setVisible(true);
            }
            else{
                surnameLabel.setVisible(false);
            }
        });
        layout.addComponent(surnameLabel);

        layout.addComponent(patronymic);
        patronymic.addValueChangeListener(e ->{
            isPatronymicValid = stringValidator.isValidString(patronymic.getValue());
            if(!isPatronymicValid){
                patronymicLabel.setVisible(true);
            }
            else{
                patronymicLabel.setVisible(false);
            }
        });
        layout.addComponent(patronymicLabel);

        layout.addComponent(phoneNumber);
        phoneNumber.addValueChangeListener(e ->{
            isPhoneNumberValid = stringValidator.isValidNumber(phoneNumber.getValue());
            if(!isPhoneNumberValid){
                phoneLabel.setVisible(true);
            }
            else{
                phoneLabel.setVisible(false);
            }
        });
        layout.addComponent(phoneLabel);

        layout.addComponent(getCancelButton());
    }

    private Button getConfirmEditButton(){
        Button button = new Button("Confirm");
        button.addClickListener(e ->{
            if(areValuesValid()){
                interactWithTable("update");
            }
        });
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

    private boolean areValuesValid(){
        return isNameValid && isSurnameValid && isPatronymicValid && isPhoneNumberValid;
    }
}
