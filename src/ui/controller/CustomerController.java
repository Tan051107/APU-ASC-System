package ui.controller;

import enums.AppointmentStatus;
import models.*;
import services.*;
import utils.DialogUtil;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.Month;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerController {

    private final String customerId;
    private final Logger logger = Logger.getLogger(CustomerController.class.getName());

    // SERVICES
    private final AppointmentService appointmentService = new AppointmentService();
    private final FeedbackService feedbackService = new FeedbackService();
    private final UserService userService = new UserService();
    private final ServicesService servicesService = new ServicesService();
    private final PaymentRecordService paymentRecordService = new PaymentRecordService();

    public CustomerController(String customerId) {
        this.customerId = customerId;
    }

    // VIEW APPOINTMENT HISTORY
    // Display appointments with search + filter
    public DefaultTableModel getServiceHistoryTableModel(String search, String statusFilter) {

        String[] columns = {"Appointment ID", "Date", "Time", "Service Type", "Description", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            String keyword = search == null ? "" : search.toLowerCase();

            for (Appointment a : appointments) {

                // Only show current customer's appointments
                if (!a.getCustomerId().equalsIgnoreCase(customerId)) continue;

                // Get service info
                Services service = getServiceById(a.getServiceId());
                String type = service == null ? "Unknown" : service.getName();
                String desc = service == null ? "-" : service.getDetails();
                String status = a.getStatusService().getDisplayAppointmentStatus();

                // Status filter
                boolean matchStatus =
                        statusFilter.equalsIgnoreCase("All") ||
                        (statusFilter.equalsIgnoreCase("Assigned") && a.getStatusService() == AppointmentStatus.ASSIGNED) ||
                        (statusFilter.equalsIgnoreCase("Completed") && a.getStatusService() == AppointmentStatus.COMPLETED) ||
                        (statusFilter.equalsIgnoreCase("Cancelled") && a.getStatusService() == AppointmentStatus.CANCELLED);

                // Search
                boolean matchSearch =
                        keyword.isEmpty() ||
                        a.getId().toLowerCase().contains(keyword) ||
                        type.toLowerCase().contains(keyword) ||
                        desc.toLowerCase().contains(keyword);

                if (matchStatus && matchSearch) {
                    model.addRow(new Object[]{
                            a.getId(), a.getDate(), a.getTime(), type, desc, status
                    });
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            DialogUtil.showErrorMessage("Error", "Failed to load appointments");
        }

        return model;
    }

    // DROPDOWN
    // Show only completed appointments WITHOUT feedback submitted by customer yet
    public DefaultComboBoxModel<String> getCompletedAppointmentComboModel() {

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();

            for (Appointment a : appointments) {

                // Only own appointment
                if (!a.getCustomerId().equalsIgnoreCase(customerId)) continue;

                // Must be completed
                if (a.getStatusService() != AppointmentStatus.COMPLETED) continue;

                // Check if customer already submitted rating via FeedbackService
                Feedback existing = feedbackService.getFeedbackByAppointmentId(a.getId());
                boolean alreadyRated = existing != null
                        && existing.getStaffRating() != null
                        && existing.getStaffRating() > 0;

                if (!alreadyRated) {
                    model.addElement(a.getId());
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            DialogUtil.showErrorMessage("Error", "Failed to load appointments");
        }

        return model;
    }

    // GET STAFF & TECHNICIAN
    // Display staff + technician name for a given appointment
    public String[] getAppointmentPeopleDetails(String appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            if (appointment == null) return null;

            User staff = userService.getUserById(appointment.getStaffId());
            User technician = userService.getUserById(appointment.getTechnicianId());

            String staffText = (staff == null)
                    ? appointment.getStaffId()
                    : staff.getId() + " | " + staff.getName();

            String technicianText = (technician == null)
                    ? appointment.getTechnicianId()
                    : technician.getId() + " | " + technician.getName();

            return new String[]{staffText, technicianText};

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    // PAYMENT HISTORY
    // Show payment records for this customer
    public DefaultTableModel getPaymentHistoryTableModel(String search, String year, String month) {

        String[] columns = {"Appointment ID", "Service", "Description", "Amount (RM)", "Transaction Type", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            List<PaymentRecord> payments = paymentRecordService.getPaymentRecords();
            String keyword = search == null ? "" : search.toLowerCase();

            for (PaymentRecord p : payments) {

                Appointment a = appointmentService.getAppointmentById(p.getAppointmentId());
                if (a == null) continue;

                // Only show own payment
                if (!a.getCustomerId().equalsIgnoreCase(customerId)) continue;

                Services s = getServiceById(a.getServiceId());

                String type = s == null ? "Unknown" : s.getName();
                String desc = s == null ? "-" : s.getDetails();
                String amount = String.format("%.2f", p.getAmount());

                // Convert boolean to readable text
                String transactionType = p.isHasPaid() ? "Paid" : "Pending";

                // Format date
                String date = (p.getPaymentDateTime() == null)
                        ? "-"
                        : p.getPaymentDateTime().toLocalDate().toString();

                // Extract year/month for filter
                String y = (p.getPaymentDateTime() == null) ? ""
                        : String.valueOf(p.getPaymentDateTime().getYear());

                String m = (p.getPaymentDateTime() == null) ? ""
                        : Month.of(p.getPaymentDateTime().getMonthValue()).name();

                boolean matchYear = year.equalsIgnoreCase("All") || y.equals(year);
                boolean matchMonth = month.equalsIgnoreCase("All") || m.equalsIgnoreCase(month);

                boolean matchSearch =
                        keyword.isEmpty() ||
                        a.getId().toLowerCase().contains(keyword) ||
                        type.toLowerCase().contains(keyword);

                if (matchYear && matchMonth && matchSearch) {
                    model.addRow(new Object[]{
                            a.getId(), type, desc, amount, transactionType, date
                    });
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            DialogUtil.showErrorMessage("Error", "Failed to load payment history");
        }

        return model;
    }

    // FILTER (YEAR)
    public DefaultComboBoxModel<String> getPaymentYearComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            Set<String> years = new LinkedHashSet<>();
            for (PaymentRecord p : paymentRecordService.getPaymentRecords()) {
                if (p.getPaymentDateTime() != null) {
                    years.add(String.valueOf(p.getPaymentDateTime().getYear()));
                }
            }
            years.forEach(model::addElement);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return model;
    }

    // FILTER (MONTH)
    public DefaultComboBoxModel<String> getPaymentMonthComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            Set<String> months = new LinkedHashSet<>();
            for (PaymentRecord p : paymentRecordService.getPaymentRecords()) {
                if (p.getPaymentDateTime() != null) {
                    months.add(Month.of(p.getPaymentDateTime().getMonthValue()).name());
                }
            }
            months.forEach(model::addElement);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return model;
    }

    // FEEDBACK TABLE
    // Display all feedback
    public DefaultTableModel getFeedbackTableModel() {

        String[] columns = {"Feedback ID", "Appointment ID", "Staff Rating", "Technician Rating", "Comment"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacks();

            for (Feedback f : feedbacks) {

                // Only show feedback for this customer
                Appointment a = appointmentService.getAppointmentById(f.getAppointmentId());
                if (a == null) continue;

                if (!a.getCustomerId().equalsIgnoreCase(customerId)) continue;

                model.addRow(new Object[]{
                        f.getId(),
                        f.getAppointmentId(),
                        f.getStaffRating(),
                        f.getTechnicianRating(),
                        f.getComment()
                });
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            DialogUtil.showErrorMessage("Error", "Failed to load feedback");
        }

        return model;
    }

    // SUBMIT FEEDBACK
    // Customer rates staff and technician for completed appointment
    public void submitFeedback(String appointmentId, String staffText, String techText, String comment) {

        try {
            // VALIDATION
            ValidationResult vr = new ValidationResult();
            Validator.required(vr, "Appointment ID", appointmentId);
            Validator.validateInteger(vr, "Staff Rating", staffText);
            Validator.validateInteger(vr, "Technician Rating", techText);

            if (vr.hasError()) {
                DialogUtil.showWarningMessage("Validation Error", vr.getErrors());
                return;
            }

            int staffRating = Integer.parseInt(staffText);
            int techRating = Integer.parseInt(techText);

            // Rating range check
            if (staffRating < 1 || staffRating > 5 || techRating < 1 || techRating > 5) {
                DialogUtil.showWarningMessage("Validation Error", "Rating must be between 1 and 5");
                return;
            }

            // BUSINESS RULE
            Appointment a = appointmentService.getAppointmentById(appointmentId);

            if (a == null || !a.getCustomerId().equalsIgnoreCase(customerId)) {
                DialogUtil.showWarningMessage("Error", "Appointment not found");
                return;
            }

            if (a.getStatusService() != AppointmentStatus.COMPLETED) {
                DialogUtil.showWarningMessage("Error", "Feedback can only be submitted for completed appointments");
                return;
            }

            // FIND EXISTING FEEDBACK via FeedbackService
            Feedback existing = feedbackService.getFeedbackByAppointmentId(appointmentId);

            if (existing == null) {
                DialogUtil.showErrorMessage("Error", "Feedback record not found. Please contact staff.");
                return;
            }

            // PREVENT DUPLICATE
            if (existing.getStaffRating() != null && existing.getStaffRating() > 0) {
                DialogUtil.showWarningMessage("Already Submitted", "You have already submitted feedback for this appointment");
                return;
            }

            // UPDATE feedback w customer's ratings and comment
            existing.setStaffRating(staffRating);
            existing.setTechnicianRating(techRating);
            existing.setComment(comment == null ? "" : comment.trim());

            feedbackService.updateFeedback(existing);

            DialogUtil.showInfoMessage("Success", "Feedback submitted successfully!");

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            DialogUtil.showErrorMessage("Error", "Failed to submit feedback");
        }
    }

    // PROFILE
    public User getCustomerUser() {
        try {
            return userService.getUserById(customerId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    // HELPER
    private Services getServiceById(String id) {
        try {
            return servicesService.getServicesById(id);
        } catch (Exception e) {
            return null;
        }
    }
}