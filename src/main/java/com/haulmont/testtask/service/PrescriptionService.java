package com.haulmont.testtask.service;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.impl.PrescriptionDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.Priority;
import com.haulmont.testtask.exception.prescription.EmptyPrescriptionCollection;
import com.haulmont.testtask.validation.SimpleStringValidator;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionService {
    private VerticalLayout layout = new VerticalLayout();
    private Window window = new Window("Пожалуйста, заполните поля для создания/редактирования Рецепта");
    private VerticalLayout windowLayout = new VerticalLayout();

    private PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();

    private SimpleStringValidator stringValidator = new SimpleStringValidator();

    private TextField description = new TextField("Описание");
    private TextField patientName = new TextField("Имя пациента");
    private TextField doctorsName = new TextField("Имя доктора");
    private TextField priority = new TextField("Приоритет");
    private TextField creationDate = new TextField("Дата создания");
    private TextField expirationPeriod = new TextField("Срок действия (мес.)");

    private Grid<Doctor> doctors;
    private Grid<Patient> patients;

    private Button editButton = new Button("Редактировать");
    private Button deleteButton;

    private Prescription prescription;
    private Doctor doctor;
    private Patient patient;
    private boolean isValidDescription = false;
    private boolean isValidExpirationPeriod = false;
    private boolean isValidPriority = false;
    boolean isValidFilterDescription = true;
    boolean isValidFilterPatientName = true;
    boolean isValidFilterPriority = true;

    private String patientNameInFilter = "";
    private String priorityInFilter = "";
    private String descriptionInFilter = "";

    private List<Prescription> filteredList = new ArrayList<>();

    public Layout getPrescriptionsLayout() {
        constructLayoutComponents();
        return layout;
    }

    private Grid<Prescription> getGrid(){
        List<Prescription> prescriptions;

        if(filteredList.isEmpty()){
            prescriptions = prescriptionDAO.getPrescriptions();
            patientNameInFilter = "";
            descriptionInFilter = "";
            priorityInFilter = "";
        }
        else{
            prescriptions = filteredList;
        }

        Grid<Prescription> grid = new Grid<>(Prescription.class);
        grid.getColumn("id").setHidden(true);
        grid.getColumn("doctor").setHidden(true);
        grid.getColumn("patient").setHidden(true);
        grid.setColumnOrder("doctorFullName", "patientFullName", "priority", "description", "creationDate", "expirationPeriod");
        grid.getColumn("doctorFullName").setCaption("Полное имя доктора");
        grid.getColumn("patientFullName").setCaption("Полное имя пациента");
        grid.getColumn("priority").setCaption("Приоритет");
        grid.getColumn("description").setCaption("Описание");
        grid.getColumn("creationDate").setCaption("Дата создания");
        grid.getColumn("expirationPeriod").setCaption("Срок действия (мес.)");
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
            if(areValuesValid()){
                interactWithTable("insert");
            }
        });
        return confirmCreation;
    }

    private Button getCancelButton(){
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e -> constructLayoutComponents());
        return cancelButton;
    }

    private Button getEditButton(){
        editButton.setEnabled(false);
        editButton.addClickListener(e ->{
            description.setValue(prescription.getDescription());
            patientName.setValue(prescription.getPatientFullName());
            doctorsName.setValue(prescription.getDoctorFullName());
            priority.setValue(prescription.getPriority().toString());
            creationDate.setValue(prescription.getCreationDate().toString());
            expirationPeriod.setValue(String.valueOf(prescription.getExpirationPeriod()));
            toBuildModalWindow();
            windowLayout.addComponent(getConfirmEditButton());
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
        Prescription prescription = new Prescription();
        switch (action){
            case "delete" : prescription.setId(this.prescription.getId());
                            prescriptionDAO.deletePrescription(prescription);
                            break;
            case "update" : prescription.setId(this.prescription.getId());
                            prescription.setExpirationPeriod(Integer.parseInt(expirationPeriod.getValue()));
                            prescription.setDescription(description.getValue());
                            prescription.setDoctor(doctor);
                            prescription.setPatient(patient);
                            prescription.setPriority(Priority.valueOf(priority.getValue().toUpperCase()));
                            prescriptionDAO.updatePrescription(prescription);
                            break;
            case "insert" : prescription.setDescription(description.getValue());
                            prescription.setExpirationPeriod(Integer.parseInt(expirationPeriod.getValue()));
                            prescription.setDoctor(doctor);
                            prescription.setPatient(patient);
                            prescription.setPriority(Priority.valueOf(priority.getValue().toUpperCase()));
                            prescriptionDAO.insertPrescription(prescription);
                            break;
            case "showByPatient" : filteredList = prescriptionDAO.getPrescriptionByPatientName(patientNameInFilter);
                                   break;
            case "showByDescription" : filteredList = prescriptionDAO.getPrescriptionByDescription(descriptionInFilter);
                                       break;
            case "showByPriority" : filteredList = prescriptionDAO.getPrescriptionByPriority(priorityInFilter);
                                    break;
        }
        constructLayoutComponents();
    }

    private Button getDeleteButton(){
        deleteButton = new Button("Удалить");
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> interactWithTable("delete"));
        return deleteButton;
    }

    private void constructLayoutComponents(){
        description.clear();
        doctorsName.clear();
        patientName.clear();
        priority.clear();
        creationDate.clear();
        expirationPeriod.clear();
        prescription = null;
        doctors = null;
        doctor = null;
        patients = null;
        patient = null;
        layout.removeAllComponents();
        layout.addComponent(getGrid());
        layout.addComponent(getCreateButton());
        layout.addComponent(getEditButton());
        layout.addComponent(getDeleteButton());
        layout.addComponentAsFirst(getFilterPanel());
        layout.addComponent(getBackButton());
        UI.getCurrent().removeWindow(window);
    }

    private Grid<Doctor> getDoctorsList(){
        doctors = new Grid<>(Doctor.class);
        List<Doctor> doctorsList = prescriptionDAO.getDoctorList();

        for (int i = 0; i < doctorsList.size(); i++){
            if(prescription.getDoctor().getId().equals(doctorsList.get(i).getId())){
                prescription.setDoctor(doctorsList.get(i));
            }
        }

        doctors.getColumn("id").setHidden(true);
        doctors.setColumnOrder("name", "surname", "patronymic", "specialization");
        doctors.getColumn("name").setCaption("Имя");
        doctors.getColumn("surname").setCaption("Фамилия");
        doctors.getColumn("patronymic").setCaption("Отчество");
        doctors.getColumn("specialization").setCaption("Специальность");
        doctors.setItems(doctorsList);
        doctors.setCaption("Список докторов");

        if(prescription != null) {
            doctors.select(prescription.getDoctor());
            doctor = prescription.getDoctor();
        }

        doctors.asSingleSelect().addSelectionListener(e ->{
            for (int i = 0; i < doctorsList.size(); i++){
                if (doctorsList.get(i).equals(e.getFirstSelectedItem().get())){
                    doctor = doctorsList.get(i);
                }
            }
        });

        return doctors;
    }

    private Grid<Patient> getPatientsList(){
        patients = new Grid<>(Patient.class);
        List<Patient> patientsList = prescriptionDAO.getPatientList();

        for (int i = 0; i < patientsList.size(); i++){
            if(prescription.getPatient().getId().equals(patientsList.get(i).getId())){
                prescription.setPatient(patientsList.get(i));
            }
        }

        patients.getColumn("id").setHidden(true);
        patients.setColumnOrder("name", "surname", "patronymic", "phoneNumber");
        patients.getColumn("name").setCaption("Имя");
        patients.getColumn("surname").setCaption("Фамилия");
        patients.getColumn("patronymic").setCaption("Отчество");
        patients.getColumn("phoneNumber").setCaption("Номер телефона");
        patients.setItems(patientsList);
        patients.setCaption("Список пациентов");

        if(prescription != null) {
            patients.select(prescription.getPatient());
            patient = prescription.getPatient();
        }

        patients.asSingleSelect().addSelectionListener(e ->{
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            for (int i = 0; i < patientsList.size(); i++){
                if (patientsList.get(i).equals(e.getFirstSelectedItem().get())){
                    patient = patientsList.get(i);
                }
            }
        });

        return patients;
    }

    private Panel getFilterPanel(){
        Panel panel = new Panel("Фильтр");

        Label patientLabel = new Label("Имя должно содержать от 1-го до 15-ти букв");
        Label descriptionLabel = new Label("Описание должно содержать от 1-го до 30-ти любых символов");
        Label priorityLabel = new Label("Приоритет должен быть один из следующих: CITO, NORMAL, STATIM");
        patientLabel.setVisible(false);
        descriptionLabel.setVisible(false);
        priorityLabel.setVisible(false);

        FormLayout content = new FormLayout();

        Button applyButton = new Button("Применить");
        Button clearFilterButton = new Button("Сбросить фильтр");

        TextField patientNameField = new TextField("Имя пациента");
        TextField descriptionField = new TextField("Описание");
        TextField priorityField = new TextField("Приоритет");

        patientNameField.addValueChangeListener(e ->{
            if (patientNameField.getValue().length() > 0) {
                descriptionField.setEnabled(false);
                priorityField.setEnabled(false);
                isValidFilterPatientName = stringValidator.isValidString(patientNameField.getValue());
                if(!isValidFilterPatientName){
                    patientLabel.setVisible(true);
                }
                else{
                    patientLabel.setVisible(false);
                }
            }
            else{
                descriptionField.setEnabled(true);
                priorityField.setEnabled(true);
                isValidFilterPatientName = true;
                patientLabel.setVisible(false);
            }
        });
        descriptionField.addValueChangeListener(e ->{
            if (descriptionField.getValue().length() > 0) {
                patientNameField.setEnabled(false);
                priorityField.setEnabled(false);
                isValidFilterDescription = stringValidator.isValidDescription(descriptionField.getValue());
                if(!isValidFilterDescription){
                    descriptionLabel.setVisible(true);
                }
                else{
                    descriptionLabel.setVisible(false);
                }
            }
            else{
                patientNameField.setEnabled(true);
                priorityField.setEnabled(true);
                isValidFilterDescription = true;
                descriptionLabel.setVisible(false);
            }
        });
        priorityField.addValueChangeListener(e ->{
            if (priorityField.getValue().length() > 0) {
                descriptionField.setEnabled(false);
                patientNameField.setEnabled(false);
                isValidFilterPriority = stringValidator.isValidPriority(priorityField.getValue().toUpperCase());
                if(!isValidFilterPriority){
                    priorityLabel.setVisible(true);
                }
                else{
                    priorityLabel.setVisible(false);
                }
            }
            else{
                descriptionField.setEnabled(true);
                patientNameField.setEnabled(true);
                isValidFilterPriority = true;
                priorityLabel.setVisible(false);
            }
        });

        patientNameField.setValue(patientNameInFilter);
        descriptionField.setValue(descriptionInFilter);
        priorityField.setValue(priorityInFilter);

        applyButton.addClickListener(e ->{
            if(areFilterValuesValid()) {
                if (patientNameField.getValue().length() > 0) {
                    patientNameInFilter = patientNameField.getValue();
                    try {
                        interactWithTable("showByPatient");
                    }
                    catch (EmptyPrescriptionCollection exception){
                        Window alertWindow = new Window("Error");
                        alertWindow.setModal(true);
                        alertWindow.setResizable(false);
                        alertWindow.setContent(new Label(exception.getMessage()));
                        UI.getCurrent().addWindow(alertWindow);
                    }
                }
                if (descriptionField.getValue().length() > 0) {
                    descriptionInFilter = descriptionField.getValue();
                    try {
                        interactWithTable("showByDescription");
                    }
                    catch (EmptyPrescriptionCollection exception){
                        Window alertWindow = new Window("Error");
                        alertWindow.setModal(true);
                        alertWindow.setResizable(false);
                        alertWindow.setContent(new Label(exception.getMessage()));
                        UI.getCurrent().addWindow(alertWindow);
                    }
                }
                if (priorityField.getValue().length() > 0) {
                    priorityInFilter = priorityField.getValue().toUpperCase();
                    try {
                        interactWithTable("showByPriority");
                    }
                    catch (EmptyPrescriptionCollection exception){
                        Window alertWindow = new Window("Error");
                        alertWindow.setModal(true);
                        alertWindow.setResizable(false);
                        alertWindow.setContent(new Label(exception.getMessage()));
                        UI.getCurrent().addWindow(alertWindow);
                    }
                }
                patientNameInFilter = "";
                descriptionInFilter = "";
                priorityInFilter = "";
            }
        });
        clearFilterButton.addClickListener(e ->{
            patientNameInFilter = "";
            descriptionInFilter = "";
            priorityInFilter = "";
            filteredList.clear();
            constructLayoutComponents();
        });

        content.addComponent(patientNameField);
        content.addComponent(patientLabel);
        content.addComponent(priorityField);
        content.addComponent(priorityLabel);
        content.addComponent(descriptionField);
        content.addComponent(descriptionLabel);
        content.addComponent(applyButton);
        content.addComponent(clearFilterButton);
        panel.setContent(content);
        return panel;
    }

    private boolean areValuesValid(){
        return isValidPriority && isValidExpirationPeriod && isValidDescription;
    }

    private boolean areFilterValuesValid(){
        return isValidFilterPriority && isValidFilterDescription && isValidFilterPatientName;
    }

    private void toBuildModalWindow(){
        UI.getCurrent().removeWindow(window);
        windowLayout.removeAllComponents();
        window.setWidthFull();
        window.setHeight("600");
        window.setClosable(false);
        Label descriptionLabel = new Label("Описание должно содержать от 1-го до 30-ти любых символов");
        Label expirationLabel = new Label("Это поле должно содержать число от 1 до 99");
        Label priorityLabel = new Label("Приоритет должен быть один из следующих: CITO, NORMAL, STATIM");
        descriptionLabel.setVisible(false);
        expirationLabel.setVisible(false);
        priorityLabel.setVisible(false);

        windowLayout.addComponent(description);
        description.addValueChangeListener(e ->{
            isValidDescription = stringValidator.isValidDescription(description.getValue());
            if(!isValidDescription){
                descriptionLabel.setVisible(true);
            }
            else{
                descriptionLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(descriptionLabel);

        windowLayout.addComponent(expirationPeriod);
        expirationPeriod.addValueChangeListener(e ->{
            isValidExpirationPeriod = stringValidator.isValidDatePeriod(expirationPeriod.getValue());
            if(!isValidExpirationPeriod){
                expirationLabel.setVisible(true);
            }
            else{
                expirationLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(expirationLabel);

        windowLayout.addComponent(getDoctorsList());
        windowLayout.addComponent(getPatientsList());

        windowLayout.addComponent(priority);
        priority.addValueChangeListener(e ->{
            isValidPriority = stringValidator.isValidPriority(priority.getValue().toUpperCase());
            if(!isValidPriority){
                priorityLabel.setVisible(true);
            }
            else{
                priorityLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(priorityLabel);

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
