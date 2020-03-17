package com.haulmont.testtask.entity;

import com.haulmont.testtask.entity.parent.ParentEntity;

import java.sql.Date;

public class Prescription extends ParentEntity {
    private String description;

    private Patient patient;

    private Doctor doctor;

    private Date date;

    private Integer period;

    private Priority priority;

    private String doctorFullName;

    private String patientFullName;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPeriod() {
        return period;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getDate() {
        return date;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setDoctorFullName() {
        this.doctorFullName = doctor.getSurname() + " " + doctor.getName() + " " + doctor.getPatronymic();
    }

    public String getDoctorFullName(){
        return doctorFullName;
    }

    public void setPatientFullName() {
        this.patientFullName = patient.getSurname() + " " + patient.getName() + " " + patient.getPatronymic();
    }

    public String getPatientFullName() {
        return patientFullName;
    }
}
