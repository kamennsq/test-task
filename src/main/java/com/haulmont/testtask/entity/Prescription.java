package com.haulmont.testtask.entity;

import com.haulmont.testtask.entity.parent.ParentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class Prescription extends ParentEntity {

    @Column(name = "Description")
    private String description;

    @JoinColumn(name = "Patient")
    @ManyToOne
    private Patient patient;

    @JoinColumn(name = "Doctor")
    @ManyToOne
    private Doctor doctor;

    @Column(name = "CreationDate")
    private Instant date;

    @Column(name = "ExpirationPeriod")
    private Integer period;

    @Column(name = "Priority")
    private Priority priority;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setDate(Instant date) {
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

    public Instant getDate() {
        return date;
    }

    public Priority getPriority() {
        return priority;
    }
}
