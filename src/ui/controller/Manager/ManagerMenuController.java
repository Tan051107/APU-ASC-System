package ui.controller.Manager;
import services.UserService;
import ui.pages.Login;
import ui.pages.ManagerMenu;
import ui.pages.Manager.ViewFeedbackPanel;
import utils.DialogUtil;
import services.ServicesService;
import services.AppointmentService;
import services.FeedbackService;
import services.PaymentRecordService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import models.User;
import models.Services;
import models.Feedback;
import models.Appointment;
import models.PaymentRecord;

public class ManagerMenuController {
    private final UserService userService = new UserService();
    private final ServicesService servicesService = new ServicesService();
    private final FeedbackService feedbackService = new FeedbackService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final PaymentRecordService paymentRecordService = new PaymentRecordService();
    private final ManagerMenu managerMenu;
    

    public ManagerMenuController(ManagerMenu managerMenu) {
        this.managerMenu = managerMenu;
    }

    public void initListeners(){
        managerMenu.viewFeedback.addActionListener(e -> {
            int selectedRow = managerMenu.feedbackTable.getSelectedRow();
            
            if (selectedRow == -1) {
                DialogUtil.showWarningMessage("No Selection", "Please select a feedback record to view.");
                return;
            }

            String feedbackId = managerMenu.feedbackTable.getValueAt(selectedRow, 0).toString();
            ViewFeedbackPanel viewPanel = new ViewFeedbackPanel(managerMenu);
            new ViewFeedbackController(viewPanel, feedbackId);
            viewPanel.setVisible(true);
        });

        managerMenu.btnLogOut.addActionListener(e -> {
            managerMenu.dispose();
            new Login().createUI();
        });
    }

    public DefaultTableModel loadUserToTable() {
        String[] columns = {"User ID", "Name", "Role"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        String currentUserRole = managerMenu.getUser().getUserType().getDisplayUserType();
        
        try {
            List<User> userData = userService.getUsers();
            
            for (User user : userData) {
                String rowUserRole = user.getUserType().getDisplayUserType();
                
                if (currentUserRole.equals("Super Manager")) {
                    if (!rowUserRole.equals("Customer") && !rowUserRole.equals("Super Manager")) {
                        Object[] rowData = { user.getId(), user.getName(), rowUserRole };
                        tableModel.addRow(rowData);
                    }
                    
                } else {
                    if (rowUserRole.equals("Counter Staff") || rowUserRole.equals("Technician")) {
                        Object[] rowData = { user.getId(), user.getName(), rowUserRole };
                        tableModel.addRow(rowData);
                    }
                }
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load user data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return tableModel;
    }
    
    public DefaultTableModel loadServiceToTable() {
        String[] columns = {"Service ID", "Service Type", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        try {
            List<Services> servicesData = servicesService.getServices();
            
            for (Services services : servicesData) {
                String formattedPrice = String.format("RM %.2f", services.getPrice());
                Object[] rowData = {
                    services.getId(),
                    services.getName(),
                    formattedPrice
                };
                tableModel.addRow(rowData);
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load service data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return tableModel;
    }

    public Object[] loadServiceDetails(String serviceId) {
        try {
            Services service = servicesService.getServicesById(serviceId);
            
            if (service != null) {
                String formattedPrice = String.format("%.2f", service.getPrice());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String lastEdited = (service.getUpdatedAt() != null) 
                                    ? service.getUpdatedAt().format(formatter) 
                                    : "Never";
                return new Object[] {
                    service.getId(),
                    service.getName(),
                    formattedPrice,
                    service.getDetails(),
                    lastEdited
                };
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load service data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return null; 
    }

    public boolean updateServicePrice(String serviceId, double newPrice) {
        try {
            Services serviceToUpdate = servicesService.getServicesById(serviceId);

            if (serviceToUpdate == null) {
                JOptionPane.showMessageDialog(null, "Could not find the selected service in the database.", "Not Found", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            serviceToUpdate.setPrice(newPrice);
            serviceToUpdate.setUpdatedAt(LocalDateTime.now());
            servicesService.updateService(serviceToUpdate);

            return true;

        } catch (GetEntityListException | FileCorruptedException | NotFoundException | UpdateException | IOException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to update service price: " + e.getMessage(), 
                "Update Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return false; 
        }
    }

    public DefaultTableModel loadFeedbackToTable() {
        String[] columns = {"Feedback ID", "Feedback Details", "Appointment ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        
        try {
            List<Feedback> feedbacksData = feedbackService.getFeedbacks();
            
            for (Feedback feedbacks : feedbacksData) {
                Object[] rowData = {
                    feedbacks.getId(),
                    feedbacks.getComment(),
                    feedbacks.getAppointmentId()
                };
                tableModel.addRow(rowData);
            }
            
        } catch (GetEntityListException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to load feedback data: " + e.getMessage(), 
                "Data Load Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return tableModel;
    }

    public String getAppointmentTotal(){
        try {
            List<Appointment> allAppointments = appointmentService.getAllAppointments();

            int total = 0;
            LocalDate today = LocalDate.now();
            int currentMonth = today.getMonthValue();
            int currentYear = today.getYear();

            for (Appointment appointment : allAppointments){
                if (appointment.getDate().getMonthValue() == currentMonth && appointment.getDate().getYear() == currentYear){
                    total++;
                }
            }
            return String.valueOf(total);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Appointment Count");
            return "0";
        }
    }

    public String getRevenueTotal(){
        try {
            List<PaymentRecord> allPaymentRecords = paymentRecordService.getPaymentRecords();

            double total = 0;
            LocalDate today = LocalDate.now();
            int currentMonth = today.getMonthValue();
            int currentYear = today.getYear();

            for (PaymentRecord paymentRecord : allPaymentRecords){
                if (paymentRecord.isHasPaid() && 
                    paymentRecord.getPaymentDateTime() != null && 
                    paymentRecord.getPaymentDateTime().getMonthValue() == currentMonth && 
                    paymentRecord.getPaymentDateTime().getYear() == currentYear) {
                    total = total + paymentRecord.getAmount();
                }
            }
            
            return "RM" + String.format("%.2f", total); 
            
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Revenue: " + e.getMessage());
            return "0.00";
        }
    }
}
