package com.haulmont.testtask.exception.patient;

public class ImpossibleToDeletePatient extends RuntimeException {

    public ImpossibleToDeletePatient (){super("It is impossible to delete the patient with Prescriptions");}
}
