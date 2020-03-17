package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.impl.DoctorDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

import java.util.List;


public class DoctorService {
    private VerticalLayout layout = new VerticalLayout();
    private DoctorDAO doctorDAO = new DoctorDAOImpl();
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField patronymic = new TextField("Patronymic");
    private TextField specialization = new TextField("Specialization");
    private Button editButton = new Button("Edit");;
    private Doctor doctor;

    public Layout getDoctorsLayout() {
        layout.addComponent(getGrid());
        layout.addComponent(getCreateButton());
        layout.addComponent(getEditButton());
        return layout;
    }

    private Grid<Doctor> getGrid(){
        List<Doctor> doctors = doctorDAO.getDoctors();

        Grid<Doctor> grid = new Grid(Doctor.class);
        grid.getColumn("id").setHidden(true);
        grid.setColumnOrder("name", "surname", "patronymic", "specialization");
        grid.setItems(doctors);
        grid.setSizeFull();

        grid.asSingleSelect().addSelectionListener(e ->{
            editButton.setEnabled(true);
            doctor = e.getFirstSelectedItem().get();
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
            interactWithTable("insert");
        });
        return confirmCreation;
    }

    private Button getCancelButton(){
        Button confirmCreation = new Button("Cancel");
        confirmCreation.addClickListener(e ->{
            layout.removeAllComponents();
            clearContent();
            layout.addComponent(getGrid());
            layout.addComponent(getCreateButton());
        });
        return confirmCreation;
    }

    private void clearContent(){
        name.clear();
        surname.clear();
        patronymic.clear();
        specialization.clear();
    }

    private Button getEditButton(){
        editButton.setEnabled(false);
        editButton.addClickListener(e ->{
            toBuildExtraLayout();
            layout.addComponent(getConfirmEditButton());
            name.setValue(doctor.getName());
            surname.setValue(doctor.getSurname());
            patronymic.setValue(doctor.getPatronymic());
            specialization.setValue(doctor.getSpecialization());
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
        button.addClickListener(e ->{
            interactWithTable("update");
        });
        return button;
    }

    private void interactWithTable(String action){
        Doctor doctor = new Doctor();
        doctor.setName(name.getValue());
        doctor.setSurname(surname.getValue());
        doctor.setPatronymic(patronymic.getValue());
        doctor.setSpecialization(specialization.getValue());
        doctor.setId(this.doctor.getId());
        clearContent();
//        switch (action){
//            case "insert": doctorDAO.insertDoctor(doctor); break;
//            case "update": doctorDAO.updateDoctor(doctor); break;
//        }
        doctorDAO.mergeDoctor(doctor);
        layout.removeAllComponents();
        layout.addComponent(getGrid());
        layout.addComponent(getCreateButton());
        layout.addComponent(getEditButton());
    }
}
