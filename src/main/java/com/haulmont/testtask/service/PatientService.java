package com.haulmont.testtask.service;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.dao.PatientDAO;
import com.haulmont.testtask.dao.impl.PatientDAOImpl;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.exception.patient.ImpossibleToDeletePatient;
import com.haulmont.testtask.validation.SimpleStringValidator;
import com.vaadin.ui.*;

import java.util.List;

public class PatientService {
    private VerticalLayout layout = new VerticalLayout();
    private Window window = new Window("Пожалуйста, заполните поля для создания/редактирования Пациента");
    private VerticalLayout windowLayout = new VerticalLayout();

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

    private Grid<Patient> getGrid(){
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

    private Button getCreateButton(){
        Button createButton = new Button("Создать");
        createButton.addClickListener(e ->{
            toBuildModalWindow();
            windowLayout.addComponent(getConfirmCreationButton());
        });
        return createButton;
    }

    private Button getConfirmCreationButton(){
        Button confirmCreation = new Button("ОК");
        confirmCreation.addClickListener(e ->{
            if (areValuesValid()){
                interactWithTable("insert");
            }
        });
        return confirmCreation;
    }

    private Button getCancelButton(){
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e ->{
            constructLayoutComponents();
        });
        return cancelButton;
    }

    private Button getEditButton(){
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

    private Button getConfirmEditButton(){
        Button button = new Button("ОК");
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
        deleteButton = new Button("Удалить");
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> {
            try {
                interactWithTable("delete");
            }
            catch (ImpossibleToDeletePatient exception){
                Window alertWindow = new Window("Error");
                alertWindow.setModal(true);
                alertWindow.setResizable(false);
                alertWindow.setContent(new Label(exception.getMessage()));
                UI.getCurrent().addWindow(alertWindow);
            }
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
        layout.addComponent(getBackButton());
        UI.getCurrent().removeWindow(window);
    }

    private boolean areValuesValid(){
        return isNameValid && isSurnameValid && isPatronymicValid && isPhoneNumberValid;
    }

    private void toBuildModalWindow(){
        UI.getCurrent().removeWindow(window);
        windowLayout.removeAllComponents();
        window.setWidthFull();
        window.setClosable(false);
        Label nameLabel = new Label("Имя должно содержать от 3-ех до 15-ти букв");
        Label surnameLabel = new Label("Фамилия должно содержать от 3-ех до 15-ти букв");
        Label patronymicLabel = new Label("Отчество должно содержать от 3-ех до 15-ти букв");
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

    private Button getBackButton(){
        Button backButton = new Button("Назад");
        backButton.addClickListener(e ->{
            MainUI.ui.constructInitialLayout();
        });
        return backButton;
    }
}
