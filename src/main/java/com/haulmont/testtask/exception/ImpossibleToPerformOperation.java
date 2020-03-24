package com.haulmont.testtask.exception;

public class ImpossibleToPerformOperation extends RuntimeException {

    public ImpossibleToPerformOperation(){super("It is impossible to perform operation. Please check DB connection");}
}
