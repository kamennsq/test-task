package com.haulmont.testtask.exception.doctor;

public class ImpossibleToDeleteDoctor extends RuntimeException {

    public ImpossibleToDeleteDoctor(){super("It is impossible to delete Doctor with Prescription");}
}
