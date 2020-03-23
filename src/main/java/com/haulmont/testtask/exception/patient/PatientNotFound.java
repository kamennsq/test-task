package com.haulmont.testtask.exception.patient;

public class PatientNotFound extends RuntimeException {

    public PatientNotFound(){
        super("Patient with this id does not exist");
    }

}
