package ui.controller;

import models.Feedback;
import models.Appointment;
import models.User;
import services.FeedbackService;
import services.AppointmentService;
import services.UserService;
import ui.pages.Manager.ViewFeedbackPanel;
import utils.DialogUtil;

public class ViewFeedbackController {
    private final ViewFeedbackPanel panel;
    private final String targetFeedbackId;
    
    // Initialize your services
    private final FeedbackService feedbackService = new FeedbackService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final UserService userService = new UserService();

    public ViewFeedbackController(ViewFeedbackPanel panel, String targetFeedbackId) {
        this.panel = panel;
        this.targetFeedbackId = targetFeedbackId;
        
        initListeners();
        loadData();
    }

    private void initListeners() {
        panel.closeButton.addActionListener(e -> panel.dispose());
    }

    private void loadData() {
        try {
            // 1. Fetch the Feedback
            Feedback feedback = feedbackService.getFeedbackById(targetFeedbackId);
            if (feedback == null) throw new Exception("Feedback not found.");

            // 2. Fetch the linked Appointment
            Appointment appointment = appointmentService.getAppointmentById(feedback.getAppointmentId());
            if (appointment == null) throw new Exception("Linked appointment not found.");

            // 3. Fetch the Users based on IDs stored in the appointment
            User customer = userService.getUserById(appointment.getCustomerId());
            User technician = userService.getUserById(appointment.getTechnicianId());
            User staff = userService.getUserById(appointment.getStaffId());

            // 4. Populate the UI Panel
            panel.feedbackId.setText(feedback.getId());
            panel.appointmentId.setText(appointment.getId());
            
            panel.technicianId.setText(technician != null ? technician.getId() : "N/A");
            panel.technicianName.setText(technician != null ? technician.getName() : "N/A");
            panel.technicianFeedback.setText(feedback.getTechnicianFeedback());
            if (feedback.getTechnicianRating() == null){
                panel.technicianRating.setText("No rating given");
            } else {
                panel.technicianRating.setText(String.valueOf(feedback.getTechnicianRating()) + " / 5");
            }
            

            panel.staffId.setText(staff != null ? staff.getId() : "N/A");
            panel.staffName.setText(staff != null ? staff.getName() : "N/A");
            if (feedback.getStaffRating() == null){
                panel.staffRating.setText("No rating given");
            } else {
                panel.staffRating.setText(String.valueOf(feedback.getStaffRating()) + " / 5");
            }
            
            panel.customerId.setText(customer != null ? customer.getId() : "N/A");
            panel.customerName.setText(customer != null ? customer.getName() : "N/A");
            panel.comment.setText(feedback.getComment());

        } catch (Exception ex) {
            DialogUtil.showErrorMessage("Data Error", "Could not load full details: " + ex.getMessage());
            panel.dispose();
        }
    }
}