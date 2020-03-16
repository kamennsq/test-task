package com.haulmont.testtask.entity;

import com.haulmont.testtask.entity.parent.ParentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Date;
import java.time.Instant;

@Entity
public class Prescription extends ParentEntity {

    @Column(name = "Description")
    private String description;

    @JoinColumn(name = "Patient")
    @ManyToOne
    private Long patient;

    @JoinColumn(name = "Doctor")
    @ManyToOne
    private Long doctor;

    @Column(name = "CreationDate")
    private Date date;

    @Column(name = "ExpirationPeriod")
    private Integer period;

    @Column(name = "Priority")
    private Priority priority;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public void setDoctor(Long doctor) {
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

    public Long getPatient() {
        return patient;
    }

    public Long getDoctor() {
        return doctor;
    }

    public Date getDate() {
        return date;
    }

    public Priority getPriority() {
        return priority;
    }
}
