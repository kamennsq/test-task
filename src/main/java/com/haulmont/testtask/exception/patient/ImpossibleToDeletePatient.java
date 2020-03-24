package com.haulmont.testtask.exception.patient;

public class ImpossibleToDeletePatient extends RuntimeException {

    public ImpossibleToDeletePatient (){super("Невозможно удалить пациента пока существуют рецепты на его имя");}
}
