package com.haulmont.testtask.exception;

public class ImpossibleToInsertPatient extends RuntimeException {

    public ImpossibleToInsertPatient(){
        super("It is impossible to insert the patient");
    }

}
