package com.haulmont.testtask.service;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.impl.DoctorDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.exception.doctor.ImpossibleToDeleteDoctor;
import com.haulmont.testtask.validation.SimpleStringValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.sql.Date;
import java.util.List;


public class DoctorService {
    private VerticalLayout layout = new VerticalLayout();
    private Window window = new Window("Пожалуйста, заполните поля для создания/редактирования Доктора");
    private VerticalLayout windowLayout = new VerticalLayout();

    private DoctorDAO doctorDAO = new DoctorDAOImpl();

    private SimpleStringValidator stringValidator = new SimpleStringValidator();

    private TextField name = new TextField("Имя");
    private TextField surname = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField specialization = new TextField("Специальность");

    private Button statisticButton = new Button("Показать статистику");
    private Button editButton = new Button("Редактировать");
    private Button deleteButton;

    private Doctor doctor;
    private boolean isNameValid = false;
    private boolean isSurnameValid = false;
    private boolean isPatronymicValid = false;
    private boolean isSpecializationValid = false;

    public Layout getDoctorsLayout() {
        constructLayoutComponents();
        return layout;
    }

    private Grid<Doctor> getGrid(){
        List<Doctor> doctors = doctorDAO.getDoctors();

        Grid<Doctor> grid = new Grid<>(Doctor.class);
        grid.getColumn("id").setHidden(true);
        grid.setColumnOrder("surname", "name", "patronymic", "specialization");
        grid.getColumn("name").setCaption("Имя");
        grid.getColumn("surname").setCaption("Фамилия");
        grid.getColumn("patronymic").setCaption("Отчество");
        grid.getColumn("specialization").setCaption("Специальность");
        grid.setItems(doctors);
        grid.setSizeFull();

        grid.asSingleSelect().addSelectionListener(e ->{
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            for (int i = 0; i < doctors.size(); i++){
                if (doctors.get(i).equals(e.getFirstSelectedItem().get())){
                    doctor = doctors.get(i);
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
            name.setValue(doctor.getName());
            surname.setValue(doctor.getSurname());
            patronymic.setValue(doctor.getPatronymic());
            specialization.setValue(doctor.getSpecialization());
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
        Doctor doctor = new Doctor();
        doctor.setName(name.getValue());
        doctor.setSurname(surname.getValue());
        doctor.setPatronymic(patronymic.getValue());
        doctor.setSpecialization(specialization.getValue());
        if (this.doctor != null){
            doctor.setId(this.doctor.getId());
        }
        switch (action){
            case "insert": doctorDAO.insertDoctor(doctor); break;
            case "update": doctorDAO.updateDoctor(doctor); break;
            case "delete": doctorDAO.deleteDoctor(doctor); break;
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
            catch (ImpossibleToDeleteDoctor exception){
                Window alertWindow = new Window("Error");
                alertWindow.setModal(true);
                alertWindow.setResizable(false);
                alertWindow.setContent(new Label(exception.getMessage()));
                UI.getCurrent().addWindow(alertWindow);
            }
        });
        return deleteButton;
    }

    private Button getStatisticButton(){
        statisticButton.addClickListener(e ->{
            toBuildStatistics();
        });
        return statisticButton;
    }

    private void toBuildStatistics(){
        Button backToLayout = new Button("Назад");
        List<Date> dateList = doctorDAO.datesList();
        List<Doctor> doctorList = doctorDAO.getDoctors();
        GridLayout statistics = new GridLayout(dateList.size()+1, doctorList.size()+1);
        backToLayout.addClickListener(e ->{
            constructLayoutComponents();
        });

        statistics.addComponent(new Label("Имя доктора/Дата"),0,0);
        for (int i = 0; i < doctorList.size(); i++){
            statistics.addComponent(new Label(doctorList.get(i).getName()), 0, i + 1);
            for (int y = 0; y < dateList.size(); y++){
                if(statistics.getComponent(y + 1, 0) == null) {
                    statistics.addComponent(new Label(dateList.get(y).toString()), y + 1, 0);
                }
                statistics.addComponent(new Label(doctorDAO.getPrescriptionCountByDoctorAndDate(doctorList.get(i).getId(), dateList.get(y)).toString()), y + 1, i + 1);
            }
        }

        statistics.setSizeFull();

        layout.removeAllComponents();
        layout.addComponent(statistics);
        layout.addComponent(backToLayout);
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
        layout.addComponent(getStatisticButton());
        layout.addComponent(getBackButton());
        UI.getCurrent().removeWindow(window);
    }

    private boolean areValuesValid(){
        return isNameValid && isSurnameValid && isPatronymicValid && isSpecializationValid;
    }

    private void toBuildModalWindow(){
        UI.getCurrent().removeWindow(window);
        windowLayout.removeAllComponents();
        window.setWidthFull();
        window.setClosable(false);
        Label nameLabel = new Label("Имя должно содержать от 3-ех до 15-ти букв");
        Label surnameLabel = new Label("Фамилия должно содержать от 3-ех до 15-ти букв");
        Label patronymicLabel = new Label("Отчество должно содержать от 3-ех до 15-ти букв");
        Label specializationLabel = new Label("Специальность должно содержать от 3-ех до 15-ти букв");
        nameLabel.setVisible(false);
        surnameLabel.setVisible(false);
        patronymicLabel.setVisible(false);
        specializationLabel.setVisible(false);

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

        windowLayout.addComponent(specialization);
        specialization.addValueChangeListener(e ->{
            isSpecializationValid = stringValidator.isValidString(specialization.getValue());
            if(!isSpecializationValid){
                specializationLabel.setVisible(true);
            }
            else{
                specializationLabel.setVisible(false);
            }
        });
        windowLayout.addComponent(specializationLabel);

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
