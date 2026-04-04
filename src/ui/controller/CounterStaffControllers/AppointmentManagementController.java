package ui.controller.CounterStaffControllers;

import models.Appointment;
import repositories.AppointmentRepository;
import ui.pages.CounterStaffPanels.ManageAppointmentPanel;

import java.util.List;

public class AppointmentManagementController {

    private final ManageAppointmentPanel manageAppointmentPanel;

    public AppointmentManagementController(ManageAppointmentPanel manageAppointmentPanel){
        this.manageAppointmentPanel = manageAppointmentPanel;
        initPanel();
    }

    private void reloadAllAppointments(){
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        List<Appointment> appointments = appointmentRepository.findAll();
        for(Appointment appointment :appointments){
            manageAppointmentPanel.addAppointmentRow(appointment);
        }
        manageAppointmentPanel.revalidate();
        manageAppointmentPanel.repaint();
    }

    private void initPanel(){
        reloadAllAppointments();
    }
}
