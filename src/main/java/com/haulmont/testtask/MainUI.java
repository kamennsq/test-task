package com.haulmont.testtask;

import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.service.DoctorService;
import com.haulmont.testtask.service.PatientService;
import com.haulmont.testtask.service.PrescriptionService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
    public static MainUI ui;
    VerticalLayout layout = new VerticalLayout();
    Button doctorsButton = new Button("Доктора");
    Button patientButton = new Button("Пациенты");
    Button prescriptionButton = new Button("Рецепты");

    @Override
    protected void init(VaadinRequest request) {
        MyConnection connection = new MyConnection();
        ui = this;

        DoctorService doctorService = new DoctorService();
        PatientService patientService = new PatientService();
        PrescriptionService prescriptionService = new PrescriptionService();

        layout.setMargin(true);

        doctorsButton.addClickListener(e -> {
            layout.removeAllComponents();
            rebuild(doctorService.getDoctorsLayout());
        });

        patientButton.addClickListener(e -> {
            layout.removeAllComponents();
            rebuild(patientService.getPatientsLayout());
        });

        prescriptionButton.addClickListener(e -> {
            layout.removeAllComponents();
            rebuild(prescriptionService.getPrescriptionsLayout());
        });

        constructInitialLayout();
    }

    protected void rebuild(Layout layout){
        setContent(layout);
    }

    public void constructInitialLayout(){
        layout.addComponent(doctorsButton);
        layout.addComponent(patientButton);
        layout.addComponent(prescriptionButton);

        rebuild(layout);
    }
}