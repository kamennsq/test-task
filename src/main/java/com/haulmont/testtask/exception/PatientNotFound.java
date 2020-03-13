package com.haulmont.testtask.exception;

public class PatientNotFound extends RuntimeException {

    public PatientNotFound(){
        super("Patient with this name does not exist");
    }

}
