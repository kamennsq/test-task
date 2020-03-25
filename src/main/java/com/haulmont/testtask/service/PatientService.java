package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.service.basic.AbstractService;
import com.haulmont.testtask.validation.SimpleStringValidator;
import com.vaadin.ui.*;

import java.util.List;

public class PatientService extends AbstractService {
    private Window window = new Window("Пожалуйста, заполните поля для создания/редактирования Пациента");

    private PatientDAO patientDAO = new PatientDAOImpl();

    private SimpleStringValidator stringValidator = new SimpleStringValidator();

    private TextField name = new TextField("Имя");
    private TextField surname = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField phoneNumber = new TextField("Номер телефона");

    private Button editButton = new Button("Редактировать");
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

    protected Grid<Patient> getGrid(){
        List<Patient> patients = patientDAO.getPatients();

        Grid<Patient> grid = new Grid(Patient.class);
        grid.getColumn("id").setHidden(true);
        grid.setColumnOrder("surname", "name", "patronymic", "phoneNumber");
        grid.getColumn("name").setCaption("Имя");
        grid.getColumn("surname").setCaption("Фамилия");
        grid.getColumn("patronymic").setCaption("Отчество");
        grid.getColumn("phoneNumber").setCaption("Номер телефона");
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

    protected Button getEditButton(){
        editButton.setEnabled(false);
        editButton.addClickListener(e ->{
            toBuildModalWindow();
            windowLayout.addComponent(getConfirmEditButton());
            name.setValue(patient.getName());
            surname.setValue(patient.getSurname());
            patronymic.setValue(patient.getPatronymic());
            phoneNumber.setValue(patient.getPhoneNumber());
        });
        return editButton;
    }

    protected void interactWithTable(String action){
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

    protected void constructLayoutComponents(){
        name.clear();
        surname.clear();
        patronymic.clear();
        phoneNumber.clear();
        patient = null;
        layout.removeAllComponents();
        layout.addComponent(getGrid());
        layout.addComponent(getCreateButton());
        layout.addComponent(getEditButton());
        deleteButton = getDeleteButton();
        layout.addComponent(deleteButton);
        layout.addComponent(getBackButton());
        UI.getCurrent().removeWindow(window);
    }

    protected boolean areValuesValid(){
        return isNameValid && isSurnameValid && isPatronymicValid && isPhoneNumberValid;
    }

    protected void toBuildModalWindow(){
        UI.getCurrent().removeWindow(window);
        windowLayout.removeAllComponents();
        window.setWidthFull();
        window.setClosable(false);
        Label nameLabel = new Label("Имя должно содержать от 1-го до 15-ти букв");
        Label surnameLabel = new Label("Фамилия должно содержать от 1-го до 15-ти букв");
        Label patronymicLabel = new Label("Отчество должно содержать от 1-го до 15-ти букв");
        Label phoneNumberLabel = new Label("Номер телефона должен содержать ровно 6 цифр");
        nameLabel.setVisible(false);
        surnameLabel.setVisible(false);
        patronymicLabel.setVisible(false);
        phoneNumberLabel.setVisible(false);

        windowLayout.addComponent(surname);
        surname.addValueChangeListener(e ->{
            isSurnameValid = stringValidator.isValidString(surname.getValue());
            if(!isSurnameValid){
                surnameLabel.setVisible(true);
            }
            else{
                surnameLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(surnameLabel);

        windowLayout.addComponent(name);
        name.addValueChangeListener(e ->{
            isNameValid = stringValidator.isValidString(name.getValue());
            if(!isNameValid){
                nameLabel.setVisible(true);
            }
            else{
                nameLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(nameLabel);

        windowLayout.addComponent(patronymic);
        patronymic.addValueChangeListener(e ->{
            isPatronymicValid = stringValidator.isValidString(patronymic.getValue());
            if(!isPatronymicValid){
                patronymicLabel.setVisible(true);
            }
            else{
                patronymicLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(patronymicLabel);

        windowLayout.addComponent(phoneNumber);
        phoneNumber.addValueChangeListener(e ->{
            isPhoneNumberValid = stringValidator.isValidNumber(phoneNumber.getValue());
            if(!isPhoneNumberValid){
                phoneNumberLabel.setVisible(true);
            }
            else{
                phoneNumberLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(phoneNumberLabel);

        windowLayout.addComponent(getCancelButton());

        window.setContent(windowLayout);
        window.setModal(true);
        UI.getCurrent().addWindow(window);
    }

}
