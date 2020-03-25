package com.haulmont.testtask.service.basic;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.exception.doctor.ImpossibleToDeleteDoctor;
import com.haulmont.testtask.exception.patient.ImpossibleToDeletePatient;
import com.vaadin.ui.*;

public abstract class AbstractService {
    protected VerticalLayout layout = new VerticalLayout();
    protected VerticalLayout windowLayout = new VerticalLayout();

    protected Button getConfirmCreationButton(){
        Button confirmCreation = new Button("ОК");
        confirmCreation.addClickListener(e ->{
            if(areValuesValid()){
                interactWithTable("insert");
            }
        });
        return confirmCreation;
    }

    protected Button getCancelButton(){
        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e ->{
            constructLayoutComponents();
        });
        return cancelButton;
    }

    protected Button getConfirmEditButton(){
        Button button = new Button("ОК");
        button.addClickListener(e ->{
            if(areValuesValid()){
                interactWithTable("update");
            }
        });
        return button;
    }

    protected Button getDeleteButton(){
        Button deleteButton = new Button("Удалить");
        deleteButton.setEnabled(false);
        deleteButton.addClickListener(e -> {
            try {
                interactWithTable("delete");
            }
            catch (ImpossibleToDeleteDoctor | ImpossibleToDeletePatient exception){
                Window alertWindow = new Window("Error");
                alertWindow.setModal(true);
                alertWindow.setResizable(false);
                alertWindow.setContent(new Label(exception.getMessage()));
                UI.getCurrent().addWindow(alertWindow);
            }
        });
        return deleteButton;
    }

    protected Button getBackButton(){
        Button backButton = new Button("Назад");
        backButton.addClickListener(e ->{
            MainUI.ui.constructInitialLayout();
        });
        return backButton;
    }

    protected abstract void interactWithTable(String action);

    protected abstract boolean areValuesValid();

    protected abstract void constructLayoutComponents();

    protected abstract void toBuildModalWindow();

    protected abstract Grid<?> getGrid();

    protected abstract Button getEditButton();

    protected Button getCreateButton(){
        Button createButton = new Button("Создать");
        createButton.addClickListener(e ->{
            toBuildModalWindow();
            windowLayout.addComponent(getConfirmCreationButton());
        });
        return createButton;
    }
}
