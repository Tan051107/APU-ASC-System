package ui.controller;

import enums.AppointmentStatus;
import exceptions.FileCorruptedException;
import mapper.*;
import models.*;
import repositories.CrudRepository;
import utils.DialogUtil;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.Month;
import java.util.*;

public class CustomerController {

    private final String customerId;

    // ================= REPOSITORIES =================
    // Handle file operations for each module
    private final CrudRepository<Appointment> appointmentRepo =
            new CrudRepository<>("txt_files/Appointment.txt", new AppointmentMapper());

    private final CrudRepository<Feedback> feedbackRepo =
            new CrudRepository<>("txt_files/Feedback.txt", new FeedbackMapper());

    private final CrudRepository<User> userRepo =
            new CrudRepository<>("txt_files/User.txt", new UserMapper());

    private final CrudRepository<Services> servicesRepo =
            new CrudRepository<>("txt_files/Services.txt", new ServicesMapper());

    private final CrudRepository<PaymentRecord> paymentRepo =
            new CrudRepository<>("txt_files/PaymentRecord.txt", new PaymentRecordMapper());

    public CustomerController(String customerId) {
        this.customerId = customerId;
    }

    // ================= VIEW APPOINTMENT HISTORY =================
    // Purpose: Display customer's appointments with search + filter
    public DefaultTableModel getServiceHistoryTableModel(String search, String statusFilter) {

        String[] columns = {"Appointment ID", "Date", "Time", "Service Type", "Description", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            List<Appointment> appointments = appointmentRepo.getAll();
            String keyword = search == null ? "" : search.toLowerCase();

            for (Appointment a : appointments) {

                // Only show current customer's appointments
                if (!a.getCustomerId().equalsIgnoreCase(customerId)) continue;

                // Get service info
                Services service = getServiceById(a.getServiceId());
                String type = service == null ? "Unknown" : service.getName();
                String desc = service == null ? "-" : service.getDetails();
                String status = a.getStatusService().getDisplayAppointmentStatus();

                // Status filter logic
                boolean matchStatus =
                        statusFilter.equalsIgnoreCase("All") ||
                        (statusFilter.equalsIgnoreCase("Assigned") && a.getStatusService() == AppointmentStatus.ASSIGNED) ||
                        (statusFilter.equalsIgnoreCase("Completed") && a.getStatusService() == AppointmentStatus.COMPLETED);

                // Search logic
                boolean matchSearch =
                        keyword.isEmpty() ||
                        a.getId().toLowerCase().contains(keyword) ||
                        type.toLowerCase().contains(keyword) ||
                        desc.toLowerCase().contains(keyword);

                // Add row if matches filter
                if (matchStatus && matchSearch) {
                    model.addRow(new Object[]{
                            a.getId(), a.getDate(), a.getTime(), type, desc, status
                    });
                }
            }

        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Failed to load appointments");
        }

        return model;
    }
    
    // ================= DROPDOWN =================
    // Purpose: Show only completed appointments WITHOUT feedback
    public DefaultComboBoxModel<String> getCompletedAppointmentComboModel() {

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try {
            List<Appointment> appointments = appointmentRepo.getAll();
            List<Feedback> feedbacks = feedbackRepo.getAll();

            for (Appointment a : appointments) {

                // Only own appointment
                if (!a.getCustomerId().equalsIgnoreCase(customerId)) continue;

                // Must be completed
                if (a.getStatusService() != AppointmentStatus.COMPLETED) continue;

                // Check if already has feedback
                boolean alreadyRated = feedbacks.stream()
                        .anyMatch(f ->
                                f.getAppointmentId().equalsIgnoreCase(a.getId()) &&
                                f.getStaffRating() != null &&
                                f.getStaffRating() > 0
                        );

                if (!alreadyRated) {
                    model.addElement(a.getId());
                }
            }

        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Failed to load appointments");
        }

        return model;
    }

    // ================= GET STAFF & TECHNICIAN =================
    // Purpose: Display staff + technician name in UI
    public String[] getAppointmentPeopleDetails(String appointmentId) {
        try {
            Appointment appointment = appointmentRepo.getOne(appointmentId);
            if (appointment == null) return null;

            User staff = userRepo.getOne(appointment.getStaffId());
            User technician = userRepo.getOne(appointment.getTechnicianId());

            String staffText = (staff == null)
                    ? appointment.getStaffId()
                    : staff.getId() + " | " + staff.getName();

            String technicianText = (technician == null)
                    ? appointment.getTechnicianId()
                    : technician.getId() + " | " + technician.getName();

            return new String[]{staffText, technicianText};

        } catch (Exception e) {
            return null;
        }
    }

    // ================= PAYMENT HISTORY =================
    // Purpose: Show payment using PaymentRecord.txt (NOT appointment anymore)
    public DefaultTableModel getPaymentHistoryTableModel(String search, String year, String month) {

        String[] columns = {"Appointment ID", "Service", "Description", "Amount (RM)", "Transaction Type", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            List<PaymentRecord> payments = paymentRepo.getAll();
            String keyword = search == null ? "" : search.toLowerCase();

            for (PaymentRecord p : payments) {

                Appointment a = appointmentRepo.getOne(p.getAppointmentId());
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
                String y = (p.getPaymentDateTime() == null) ? "" :
                        String.valueOf(p.getPaymentDateTime().getYear());

                String m = (p.getPaymentDateTime() == null) ? "" :
                        Month.of(p.getPaymentDateTime().getMonthValue()).name();

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
            DialogUtil.showErrorMessage("Error", "Failed to load payment history");
        }

        return model;
    }

    // ================= FILTER (YEAR) =================
    public DefaultComboBoxModel<String> getPaymentYearComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            Set<String> years = new LinkedHashSet<>();
            for (PaymentRecord p : paymentRepo.getAll()) {
                if (p.getPaymentDateTime() != null) {
                    years.add(String.valueOf(p.getPaymentDateTime().getYear()));
                }
            }
            years.forEach(model::addElement);
        } catch (Exception ignored) {}

        return model;
    }

    // ================= FILTER (MONTH) =================
    public DefaultComboBoxModel<String> getPaymentMonthComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            Set<String> months = new LinkedHashSet<>();
            for (PaymentRecord p : paymentRepo.getAll()) {
                if (p.getPaymentDateTime() != null) {
                    months.add(Month.of(p.getPaymentDateTime().getMonthValue()).name());
                }
            }
            months.forEach(model::addElement);
        } catch (Exception ignored) {}

        return model;
    }

    // ================= FEEDBACK TABLE =================
    // Purpose: Display all feedback for current customer
    public DefaultTableModel getFeedbackTableModel() {

        String[] columns = {"Feedback ID", "Appointment ID", "Staff Rating", "Technician Rating", "Comment"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {
            List<Feedback> feedbacks = feedbackRepo.getAll();

            for (Feedback f : feedbacks) {

                // Only show feedback for THIS customer's appointments
                Appointment a = appointmentRepo.getOne(f.getAppointmentId());
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
            DialogUtil.showErrorMessage("Error", "Failed to load feedback");
        }

        return model;
    }    
    
    // ================= SUBMIT FEEDBACK =================
    // IMPORTANT: Update existing record (NOT create new)
    public void submitFeedback(String appointmentId, String staffText, String techText, String comment) {

        try {
            // ===== VALIDATION =====
            ValidationResult vr = new ValidationResult();

            Validator.required(vr, "Appointment ID", appointmentId);
            Validator.validateInteger(vr, "Staff Rating", staffText);
            Validator.validateInteger(vr, "Technician Rating", techText);

            if (vr.hasError()) {
                DialogUtil.showWarningMessage("Validation Error", vr.getErrors());
                return;
            }

            int staff = Integer.parseInt(staffText);
            int tech = Integer.parseInt(techText);

            if (staff < 1 || staff > 5 || tech < 1 || tech > 5) {
                DialogUtil.showWarningMessage("Validation Error", "Rating must be 1–5");
                return;
            }

            // ===== BUSINESS RULE =====
            Appointment a = appointmentRepo.getOne(appointmentId);

            if (a == null || !a.getCustomerId().equalsIgnoreCase(customerId)
                    || a.getStatusService() != AppointmentStatus.COMPLETED) {

                DialogUtil.showWarningMessage("Error", "Invalid appointment");
                return;
            }

            // ===== FIND EXISTING FEEDBACK =====
            Feedback existing = null;
            for (Feedback f : feedbackRepo.getAll()) {
                if (f.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                    existing = f;
                    break;
                }
            }

            if (existing == null) {
                DialogUtil.showErrorMessage("Error", "Feedback record not found");
                return;
            }

            // Prevent duplicate submission
            if (existing.getStaffRating() != null && existing.getStaffRating() > 0) {
                DialogUtil.showWarningMessage("Error", "Already submitted");
                return;
            }

            // ===== UPDATE =====
            existing.setStaffRating(staff);
            existing.setTechnicianRating(tech);
            existing.setComment(comment == null ? "" : comment.trim());

            feedbackRepo.update(existing);

            DialogUtil.showInfoMessage("Success", "Feedback submitted");

        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Failed to submit feedback");
        }
    }

    // ================= PROFILE =================
    public User getCustomerUser() {
        try {
            return userRepo.getOne(customerId);
        } catch (Exception e) {
            return null;
        }
    }

    // ================= HELPER =================
    private Services getServiceById(String id) {
        try {
            return servicesRepo.getOne(id);
        } catch (Exception e) {
            return null;
        }
    }
}