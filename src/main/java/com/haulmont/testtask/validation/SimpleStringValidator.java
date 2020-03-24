package com.haulmont.testtask.validation;

import com.haulmont.testtask.entity.Priority;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleStringValidator {

    private Pattern stringPattern = Pattern.compile("[a-zA-Zа-яА-Я]{1,15}");
    private Pattern numberPattern = Pattern.compile("[0-9]{6}");
    private Pattern periodPattern = Pattern.compile("[0-9]{1,2}");
    private Pattern descriptionPattern = Pattern.compile(".{1,30}");
    private Matcher matcher;

    public boolean isValidString(String value){
        matcher = stringPattern.matcher(value);
        return matcher.matches();
    }

    public boolean isValidNumber(String value){
        matcher = numberPattern.matcher(value);
        return matcher.matches();
    }

    public boolean isValidDescription(String value){
        matcher = descriptionPattern.matcher(value);
        return matcher.matches();
    }

    public boolean isValidDatePeriod(String value){
        matcher = periodPattern.matcher(value);
        return matcher.matches();
    }

    public boolean isValidPriority(String value){
        try{
            Priority.valueOf(value);
            return true;
        }
        catch (IllegalArgumentException e){
            return false;
        }
    }
}
