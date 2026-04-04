package ui.controller.CounterStaffControllers;

import exceptions.FileCorruptedException;
import models.Appointment;
import services.AppointmentService;
import ui.controller.CounterStaffControllers.FormController.AddAppointmentFormController;
import ui.pages.CounterStaffPanels.ManageAppointmentPanel;
import ui.pages.CounterStaffPanels.forms.AddAppointmentForm;
import utils.DialogUtil;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentManagementController {

    private final ManageAppointmentPanel manageAppointmentPanel;
    private final AppointmentService appointmentService = new AppointmentService();
    Logger logger = Logger.getLogger(AppointmentManagementController.class.getName());

    public AppointmentManagementController(ManageAppointmentPanel manageAppointmentPanel){
        this.manageAppointmentPanel = manageAppointmentPanel;
        initPanel();
    }

    private void openAddAppointmentForm(){
        AddAppointmentForm addAppointmentForm = new AddAppointmentForm();
        addAppointmentForm.setVisible(true);
        new AddAppointmentFormController(addAppointmentForm);
        addAppointmentForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                resetAllAppointments();
            }
        });
    }

    private void loadAppointments(){
        List<Appointment> appointments = manageAppointmentPanel.getAppointments();
        for(Appointment appointment : appointments){
            manageAppointmentPanel.addAppointmentRow(appointment);
        }
    }

    private void resetAllAppointments(){
        try {
            List<Appointment> appointments = appointmentService.getAppointments();
            manageAppointmentPanel.setAppointments(appointments);
            loadAppointments();
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Encountered Error" , "Failed to get appointments");
            logger.log(Level.SEVERE, e.getMessage());

        }
    }

    private void initPanel(){
        resetAllAppointments();
        manageAppointmentPanel.newAppointmentBtn.addActionListener(e->openAddAppointmentForm());
    }
}
