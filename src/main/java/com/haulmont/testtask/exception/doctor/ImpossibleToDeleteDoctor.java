package com.haulmont.testtask.exception.doctor;

public class ImpossibleToDeleteDoctor extends RuntimeException {

    public ImpossibleToDeleteDoctor(){super("Невозможно удалить доктора пока есть выписанные им рецепты");}
}
