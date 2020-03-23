package com.haulmont.testtask.exception.prescription;

public class EmptyPrescriptionCollection extends RuntimeException {

    public EmptyPrescriptionCollection(){super("The search ended up with no result found by filter parameters");}
}
