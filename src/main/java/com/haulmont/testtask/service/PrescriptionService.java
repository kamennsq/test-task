package com.haulmont.testtask.service;

import com.haulmont.testtask.dao.PrescriptionDAO;
import com.haulmont.testtask.dao.impl.PrescriptionDAOImpl;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Prescription;
import com.haulmont.testtask.entity.Priority;
import com.vaadin.ui.*;

import java.util.List;

public class PrescriptionService {
    private VerticalLayout layout = new VerticalLayout();

    private PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();

    private TextField description = new TextField("Description");
    private TextField patientName = new TextField("Patient's name");
    private TextField doctorsName = new TextField("Doctor's name");
    private TextField priority = new TextField("Priority");
    private TextField creationDate = new TextField("Creation Date");
    private TextField expirationPeriod = new TextField("Expiration Period");

    private Grid<Doctor> doctors;
    private Grid<Patient> patients;

    private Button editButton = new Button("Edit");;
    private Button deleteButton;

    private Prescription prescription;
    private Doctor doctor;
    private Patient patient;

    public Layout getPrescriptionsLayout() {
        constructLayoutComponents();
        return layout;
    }

    private Grid<Prescription> getGrid(){
        List<Prescription> prescriptions = prescriptionDAO.getPrescriptions();

        Grid<Prescription> grid = new Grid<>(Prescription.class);
        grid.getColumn("id").setHidden(true);
        grid.getColumn("doctor").setHidden(true);
        grid.getColumn("patient").setHidden(true);
        grid.setColumnOrder("doctorFullName", "patientFullName", "priority", "description", "creationDate", "expirationPeriod");
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
            creationDate.setValue(prescription.getCreationDate().toString());
            expirationPeriod.setValue(String.valueOf(prescription.getExpirationPeriod()));
        });
        return editButton;
    }

    private void toBuildExtraLayout(){
        layout.removeAllComponents();

        layout.addComponent(new Label("Please, fill a new data for Prescription"));
        layout.addComponent(description);
        layout.addComponent(expirationPeriod);
        layout.addComponent(getDoctorsList());
        layout.addComponent(getPatientsList());
        layout.addComponent(priority);

        layout.addComponent(getCancelButton());
    }

    private Button getConfirmEditButton(){
        Button button = new Button("Confirm");
        button.addClickListener(e -> interactWithTable("update"));
        return button;
    }

//    private void interactWithTable(String action){
//        Prescription prescription = new Prescription();
//        if (this.prescription != null){
//            prescription.setId(this.prescription.getId());
//            prescription.setDoctor(this.prescription.getDoctor());
//            prescription.setPatient(this.prescription.getPatient());
//            prescription.setPriority(this.prescription.getPriority());
//            prescription.setExpirationPeriod(this.prescription.getExpirationPeriod());
//        }
//        else{
//            prescription.setExpirationPeriod(Integer.parseInt(expirationPeriod.getValue()));
//            prescription.setDescription(description.getValue());
//        }
//        switch (action){
//            case "insert": prescriptionDAO.insertPrescription(prescription); break;
//            case "update": prescriptionDAO.updatePrescription(prescription); break;
//            case "delete": prescriptionDAO.deletePrescription(prescription); break;
//        }
//        constructLayoutComponents();
//    }

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
    }

    private Grid<Doctor> getDoctorsList(){
        doctors = new Grid<>(Doctor.class);
        List<Doctor> doctorsList = prescriptionDAO.getDoctorList();

        doctors.getColumn("id").setHidden(true);
        doctors.setColumnOrder("name", "surname", "patronymic", "specialization");
        doctors.setItems(doctorsList);
        doctors.setCaption("Doctors");

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

        patients.getColumn("id").setHidden(true);
        patients.setColumnOrder("name", "surname", "patronymic", "phoneNumber");
        patients.setItems(patientsList);
        patients.setCaption("Patients");

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
}
