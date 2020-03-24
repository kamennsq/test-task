package com.haulmont.testtask.exception.prescription;

public class EmptyPrescriptionCollection extends RuntimeException {

    public EmptyPrescriptionCollection(){super("Поиск по заданным параметрам фильтра не дал результата");}
}
