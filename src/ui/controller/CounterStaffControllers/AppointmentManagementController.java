package ui.controller.CounterStaffControllers;

import enums.AppointmentStatus;
import exceptions.FileCorruptedException;
import exceptions.NotFoundException;
import models.Appointment;
import services.AppointmentService;
import ui.controller.CounterStaffControllers.FormController.AddAppointmentFormController;
import ui.pages.CounterStaffPanels.ManageAppointmentPanel;
import ui.pages.CounterStaffPanels.forms.AddAppointmentForm;
import utils.DialogUtil;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
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

    private void openAddAppointmentForm(boolean isEdit , Appointment appointmentToAdd){
        AddAppointmentForm addAppointmentForm = new AddAppointmentForm(isEdit,appointmentToAdd,manageAppointmentPanel.getLoginStaff());
        addAppointmentForm.setVisible(true);
        new AddAppointmentFormController(addAppointmentForm);
        addAppointmentForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                resetAllAppointments();
            }
        });
    }

    private void handleEdit(Appointment appointment) {
        openAddAppointmentForm(true, appointment);
    }

    private void handleCancel(Appointment appointment) {
        boolean confirmToCancel = DialogUtil.showConfirmationMessage("Confirm to Cancel?" , String.format("Are you sure you want to cancel %s?" , appointment.getId()));
        if(confirmToCancel){
            try {
                appointmentService.cancelAppointment(appointment);
                resetAllAppointments();
            } catch (FileCorruptedException e) {
                logger.log(Level.SEVERE , e.getMessage());
                DialogUtil.showErrorMessage("Encountered Error" , "Failed to cancel appointment");
            } catch (NotFoundException e) {
                DialogUtil.showErrorMessage("Encountered Error" , e.getMessage());
            }
        }
    }

    private void loadAppointments(){
        List<Appointment> appointments = manageAppointmentPanel.getAppointments();
        for(Appointment appointment : appointments){
            LocalDateTime appointmentDateTime = LocalDateTime.of(appointment.getDate(),appointment.getTime());
            LocalDateTime now = LocalDateTime.now();
            boolean showActionButtons = appointment.getStatusService().equals(AppointmentStatus.ASSIGNED) && now.isBefore(appointmentDateTime);
            manageAppointmentPanel.addAppointmentRow(appointment, this::handleEdit, this::handleCancel,showActionButtons);
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
        manageAppointmentPanel.newAppointmentBtn.addActionListener(e->openAddAppointmentForm(false , null));
    }
}
