package ui.controller.CounterStaffControllers;

import enums.AppointmentStatus;
import exceptions.*;
import models.Appointment;
import models.Services;
import services.AppointmentService;
import services.ServicesService;
import ui.controller.CounterStaffControllers.FormController.AddAppointmentFormController;
import ui.pages.CounterStaffPanels.ManageAppointmentPanel;
import ui.pages.CounterStaffPanels.ManagePaymentPanel;
import ui.pages.CounterStaffPanels.components.ComboBoxItems.CustomComboBoxItem;
import ui.pages.CounterStaffPanels.components.ComboBoxItems.ServiceComboBoxItem;
import ui.pages.CounterStaffPanels.forms.AddAppointmentForm;
import utils.CSVExporter;
import utils.DialogUtil;
import utils.exporters.CsvExporters.AppointmentCsvExporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentManagementController {

    private final ManageAppointmentPanel manageAppointmentPanel;
    private final PaymentRecordManagementController paymentRecordManagementController;
    private final AppointmentService appointmentService = new AppointmentService();
    Logger logger = Logger.getLogger(AppointmentManagementController.class.getName());

    public AppointmentManagementController(ManageAppointmentPanel manageAppointmentPanel , ManagePaymentPanel managePaymentPanel){
        this.manageAppointmentPanel = manageAppointmentPanel;
        this.paymentRecordManagementController = new PaymentRecordManagementController(managePaymentPanel);
        initPanel();
    }

    private void openAddAppointmentForm(boolean isEdit , Appointment appointmentToAdd){
        Window parent = SwingUtilities.getWindowAncestor(manageAppointmentPanel);
        AddAppointmentForm addAppointmentForm = new AddAppointmentForm((Frame)parent, isEdit, appointmentToAdd, manageAppointmentPanel.getLoginStaff());
        new AddAppointmentFormController(addAppointmentForm);
        addAppointmentForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                resetAllAppointments();
                paymentRecordManagementController.resetAllPaymentRecords();
            }
        });
        addAppointmentForm.setVisible(true);
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
                paymentRecordManagementController.resetAllPaymentRecords();
            } catch (FileCorruptedException | GetEntityListException e) {
                logger.log(Level.SEVERE , e.getMessage());
                DialogUtil.showErrorMessage("Encountered Error" , "Failed to cancel appointment");
            } catch (NotFoundException | DeleteException | BusinessRuleException e) {
                DialogUtil.showErrorMessage("Encountered Error" , e.getMessage());
            }
        }
    }

    private void searchAppointment(){
        String keyword = manageAppointmentPanel.searchField.getText();
        String statusFilterSelection = Objects.requireNonNull(manageAppointmentPanel.statusFilterCombo.getSelectedItem()).toString();
        AppointmentStatus appointmentStatusFilterSelected = statusFilterSelection.equalsIgnoreCase("All") ? null :AppointmentStatus.fromString(statusFilterSelection);
        ServiceComboBoxItem serviceTypeSelection = (ServiceComboBoxItem) manageAppointmentPanel.serviceTypeFilterCombo.getSelectedItem();
        String serviceTypeId;
        if(serviceTypeSelection == null){
            serviceTypeId = "";
        }
        else{
            serviceTypeId = serviceTypeSelection.getId();
        }
        if(keyword.isEmpty() && serviceTypeId.isEmpty() && statusFilterSelection.equalsIgnoreCase("All")){
            resetAllAppointments();
        }
        else{
            try {
                List<Appointment> appointmentsFound = appointmentService.searchAppointment(keyword, appointmentStatusFilterSelected,serviceTypeId);
                manageAppointmentPanel.setAppointments(appointmentsFound);
                loadAppointments();
            } catch (FileCorruptedException e) {
                logger.log(Level.SEVERE,e.getMessage());
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

    private void exportAppointment(){
        List<Appointment> appointmentsToExport = manageAppointmentPanel.getAppointments();
        CSVExporter<Appointment> CSVExporter = new CSVExporter<>();
        AppointmentCsvExporter appointmentCsvExporter = new AppointmentCsvExporter();
        CSVExporter.exportData(appointmentsToExport, "Appointments" , appointmentCsvExporter);
    }

    private void initServiceTypeComboBox() {
        ServicesService servicesService = new ServicesService();
        try {
            List<Services> services = servicesService.getServices();
            for(Services service : services){
                manageAppointmentPanel.serviceTypeFilterCombo.addItem(new ServiceComboBoxItem(service.getId() , service.getServiceName()));
            }
        } catch (GetEntityListException e) {
            DialogUtil.showErrorMessage("Encountered Error" , "Failed to get service types");
            logger.log(Level.SEVERE, e.getMessage());
        }
    }


    private void initPanel(){
        resetAllAppointments();
        initServiceTypeComboBox();
        manageAppointmentPanel.newAppointmentBtn.addActionListener(e->openAddAppointmentForm(false , null));
        manageAppointmentPanel.searchField.addActionListener(e->searchAppointment());
        manageAppointmentPanel.serviceTypeFilterCombo.addActionListener(e->searchAppointment());
        manageAppointmentPanel.statusFilterCombo.addActionListener(e->searchAppointment());
        manageAppointmentPanel.exportBtn.addActionListener(e->exportAppointment());
    }
}
