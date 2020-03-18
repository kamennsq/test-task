package com.haulmont.testtask;

import com.haulmont.testtask.dao.connection.MyConnection;
import com.haulmont.testtask.service.DoctorService;
import com.haulmont.testtask.service.PatientService;
import com.haulmont.testtask.service.PrescriptionService;
import com.vaadin.annotations.Theme;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        MyConnection connection = new MyConnection();

        DoctorService doctorService = new DoctorService();
        PatientService patientService = new PatientService();
        PrescriptionService prescriptionService = new PrescriptionService();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Button doctorsButton = new Button("Doctors");
        doctorsButton.addClickListener(e -> {
            layout.removeAllComponents();
            rebuild(doctorService.getDoctorsLayout());
        });

        Button patientButton = new Button("Patients");
        patientButton.addClickListener(e -> {
            layout.removeAllComponents();
            rebuild(patientService.getPatientsLayout());
        });

        Button prescriptionButton = new Button("Prescriptions");
        prescriptionButton.addClickListener(e -> {
            layout.removeAllComponents();
            rebuild(prescriptionService.getPrescriptionsLayout());
        });

        layout.addComponent(doctorsButton);
        layout.addComponent(patientButton);
        layout.addComponent(prescriptionButton);

        rebuild(layout);
    }

    protected void rebuild(Layout layout){
        setContent(layout);
    }

}