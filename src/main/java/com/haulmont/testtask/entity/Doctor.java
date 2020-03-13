package com.haulmont.testtask.entity;

import com.haulmont.testtask.entity.parent.ParentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Doctor extends ParentEntity {

    @Column(name = "Name")
    private String name;

    @Column(name = "Surname")
    private String surname;

    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "Patronymic")
    private String patronymic;

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPatronymic() {
        return patronymic;
    }
}
