package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.DoctorDAO;
import com.haulmont.testtask.dao.impl.DoctorDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.exception.doctor.ImpossibleToDeleteDoctor;
import com.haulmont.testtask.validation.SimpleStringValidator;
import com.vaadin.ui.*;

import java.util.List;


public class DoctorService {
    private VerticalLayout layout = new VerticalLayout();
    private Window window = new Window("Please, fill a new data for Doctor");
    private VerticalLayout windowLayout = new VerticalLayout();

    private DoctorDAO doctorDAO = new DoctorDAOImpl();

    private SimpleStringValidator stringValidator = new SimpleStringValidator();

    private TextField name = new TextField("Name");
    private TextField surname = new TextField("Surname");
    private TextField patronymic = new TextField("Patronymic");
    private TextField specialization = new TextField("Specialization");

    private Button statisticButton = new Button("Show Statistics");
    private Button editButton = new Button("Edit");
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
        grid.setColumnOrder("name", "surname", "patronymic", "specialization");
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
        Button createButton = new Button("Create");
        createButton.addClickListener(e ->{
            toBuildModalWindow();
            windowLayout.addComponent(getConfirmCreationButton());
        });
        return createButton;
    }

    private Button getConfirmCreationButton(){
        Button confirmCreation = new Button("OK");
        confirmCreation.addClickListener(e ->{
            if(areValuesValid()){
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
        Button button = new Button("Confirm");
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
        deleteButton = new Button("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> {
            try {
                interactWithTable("delete");
            }
            catch (ImpossibleToDeleteDoctor exception){
                Window alertWindow = new Window("Error");
                alertWindow.setModal(true);
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
        List<String> list = doctorDAO.buildStatistics();
        Grid grid = new Grid();
        grid.setData(list);
        //grid.setItems(list);
        layout.removeAllComponents();
        layout.addComponent(grid);
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
        Label nameLabel = new Label("Name should contain from 3 to 15 letters");
        Label surnameLabel = new Label("Surname should contain from 3 to 15 letters");
        Label patronymicLabel = new Label("Patronymic should contain from 3 to 15 letters");
        Label specializationLabel = new Label("Specialization should contain from 3 to 15 letters");
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
}
