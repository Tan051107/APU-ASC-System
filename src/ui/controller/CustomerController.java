package ui.controller;

import enums.AppointmentStatus;
import exceptions.FileCorruptedException;
import mapper.AppointmentMapper;
import mapper.FeedbackMapper;
import mapper.ServicesMapper;
import mapper.UserMapper;
import models.Appointment;
import models.Feedback;
import models.Services;
import models.User;
import repositories.CrudRepository;
import utils.DialogUtil;
import utils.RandomIdGenerator;
import utils.validators.ValidationResult;
import utils.validators.Validator;
import mapper.PaymentRecordMapper;
import models.PaymentRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CustomerController {

    // Store the currently logged-in customer ID
    private final String customerId;

    // Repository for reading customer appointment data from Appointment.txt
    private final CrudRepository<Appointment> appointmentRepo =
            new CrudRepository<>("txt_files/Appointment.txt", new AppointmentMapper());

    // Repository for reading and creating feedback records from Feedback.txt
    private final CrudRepository<Feedback> feedbackRepo =
            new CrudRepository<>("txt_files/Feedback.txt", new FeedbackMapper());

    // Repository for reading and updating user profile data from User.txt
    private final CrudRepository<User> userRepo =
            new CrudRepository<>("txt_files/User.txt", new UserMapper());

    // Repository for reading service information such as service type and description
    private final CrudRepository<Services> servicesRepo =
            new CrudRepository<>("txt_files/Services.txt", new ServicesMapper());

    // Repository for reading payment transactions from PaymentRecord.txt
    private final CrudRepository<PaymentRecord> paymentRecordRepo =
            new CrudRepository<>("txt_files/PaymentRecord.txt", new PaymentRecordMapper());

    public CustomerController(String customerId) {
        // Save the logged-in customer ID so that only this customer's data is shown
        this.customerId = customerId;
    }
    
    public User getCustomerUser() {
        // Return the current logged-in customer user object for profile/notification usage
        try {
            return userRepo.getOne(customerId);
        } catch (Exception e) {
            return null;
    }
}

    public DefaultTableModel getServiceHistoryTableModel(String search, String statusFilter) {
        // Create table columns for appointment/service history
        String[] columns = {"Appointment ID", "Date", "Time", "Service Type", "Description", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        try {
            // Read all appointments from file
            List<Appointment> appointments = appointmentRepo.getAll();

            // Convert search keyword to lowercase for case-insensitive search
            String keyword = search == null ? "" : search.trim().toLowerCase();

            for (Appointment appointment : appointments) {
                // Only show appointments that belong to the current customer
                if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                    continue;
                }

                // Get service details by using the service ID stored in the appointment
                Services service = getServiceById(appointment.getServiceId());

                String serviceType = service == null ? "Unknown Service" : service.getName();
                String description = service == null ? "-" : service.getDetails();
                String appointmentStatus = appointment.getStatusService().getDisplayAppointmentStatus();

                // Apply status filter chosen by customer
                boolean matchesStatus;
                if ("All".equalsIgnoreCase(statusFilter)) {
                    matchesStatus = true;
                } else if ("Assigned".equalsIgnoreCase(statusFilter)) {
                    matchesStatus = appointment.getStatusService() == AppointmentStatus.ASSIGNED;
                } else if ("Completed".equalsIgnoreCase(statusFilter)) {
                    matchesStatus = appointment.getStatusService() == AppointmentStatus.COMPLETED;
                } else {
                    matchesStatus = true;
                }

                // Apply keyword search on multiple fields
                boolean matchesSearch = keyword.isEmpty()
                        || appointment.getId().toLowerCase().contains(keyword)
                        || serviceType.toLowerCase().contains(keyword)
                        || description.toLowerCase().contains(keyword)
                        || appointmentStatus.toLowerCase().contains(keyword)
                        || appointment.getDate().toString().toLowerCase().contains(keyword);

                // Add row into table only if both search and filter conditions are satisfied
                if (matchesStatus && matchesSearch) {
                    Object[] row = {
                            appointment.getId(),
                            appointment.getDate(),
                            appointment.getTime(),
                            serviceType,
                            description,
                            appointmentStatus
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            // Show error dialog if loading appointment history fails
            DialogUtil.showErrorMessage("Load Error", "Failed to load appointment history");
        }

        return tableModel;
    }

    public DefaultTableModel getPaymentHistoryTableModel(String search, String yearFilter, String monthFilter) {
        // Create table columns for payment history
        String[] columns = {"Invoice ID", "Appointment ID", "Service Type", "Description", "Amount (RM)", "Transaction Type", "Payment Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        try {
            // Read all payment records from file
            List<PaymentRecord> paymentRecords = paymentRecordRepo.getAll();
            String keyword = search == null ? "" : search.trim().toLowerCase();

            for (PaymentRecord paymentRecord : paymentRecords) {

                // Only show records that have already been paid
                if (!paymentRecord.isHasPaid()) {
                    continue;
                }

                // Get appointment linked to the payment record
                Appointment appointment = appointmentRepo.getOne(paymentRecord.getAppointmentId());

                if (appointment == null) {
                    continue;
                }

                // Only show payment history for the current customer
                if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                    continue;
                }

                // Get service information for service type and description
                Services service = getServiceById(appointment.getServiceId());

                String serviceType = service == null ? "Unknown Service" : service.getName();
                String description = service == null ? "-" : service.getDetails();
                String amount = String.format("%.2f", paymentRecord.getAmount());

                // Show payment method as transaction type
                String transactionType = (paymentRecord.getPaymentMethod() == null || paymentRecord.getPaymentMethod().isBlank())
                        ? "-"
                        : paymentRecord.getPaymentMethod();

                // Format payment date safely if payment date exists
                String paymentDate = paymentRecord.getPaymentDateTime() == null
                        ? "-"
                        : paymentRecord.getPaymentDateTime().toLocalDate().toString();

                String paymentYear = paymentRecord.getPaymentDateTime() == null
                        ? "Unknown"
                        : String.valueOf(paymentRecord.getPaymentDateTime().getYear());

                String paymentMonth = paymentRecord.getPaymentDateTime() == null
                        ? "Unknown"
                        : Month.of(paymentRecord.getPaymentDateTime().getMonthValue()).name();

                // Apply year and month filter
                boolean matchesYear = "All".equalsIgnoreCase(yearFilter) || paymentYear.equalsIgnoreCase(yearFilter);
                boolean matchesMonth = "All".equalsIgnoreCase(monthFilter) || paymentMonth.equalsIgnoreCase(monthFilter);

                // Apply keyword search on payment-related fields
                boolean matchesSearch = keyword.isEmpty()
                        || paymentRecord.getId().toLowerCase().contains(keyword)
                        || paymentRecord.getAppointmentId().toLowerCase().contains(keyword)
                        || serviceType.toLowerCase().contains(keyword)
                        || description.toLowerCase().contains(keyword)
                        || amount.toLowerCase().contains(keyword)
                        || transactionType.toLowerCase().contains(keyword)
                        || paymentDate.toLowerCase().contains(keyword);

                // Add payment record into table if it matches all conditions
                if (matchesYear && matchesMonth && matchesSearch) {
                    Object[] row = {
                            paymentRecord.getId(),
                            paymentRecord.getAppointmentId(),
                            serviceType,
                            description,
                            amount,
                            transactionType,
                            paymentDate
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            // Show error dialog if loading payment history fails
            DialogUtil.showErrorMessage("Load Error", "Failed to load payment history");
        }

        return tableModel;
    }

    public DefaultComboBoxModel<String> getPaymentYearComboModel() {
        // Create dropdown model for available payment years
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            // Use set to avoid duplicate years
            Set<String> years = new LinkedHashSet<>();

            for (PaymentRecord paymentRecord : paymentRecordRepo.getAll()) {

                // Only include years from paid records
                if (!paymentRecord.isHasPaid()) continue;

                Appointment appointment = appointmentRepo.getOne(paymentRecord.getAppointmentId());

                if (appointment == null) continue;
                if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) continue;

                if (paymentRecord.getPaymentDateTime() != null) {
                    years.add(String.valueOf(paymentRecord.getPaymentDateTime().getYear()));
                }
            }

            // Add all available years into dropdown
            for (String year : years) {
                model.addElement(year);
            }
        } catch (Exception e) {
            // Keep "All" only if error happens
        }

        return model;
    }

    public DefaultComboBoxModel<String> getPaymentMonthComboModel() {
        // Create dropdown model for available payment months
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            // Use set to avoid duplicate month values
            Set<String> months = new LinkedHashSet<>();

            for (PaymentRecord paymentRecord : paymentRecordRepo.getAll()) {

                // Only include months from paid records
                if (!paymentRecord.isHasPaid()) continue;

                Appointment appointment = appointmentRepo.getOne(paymentRecord.getAppointmentId());

                if (appointment == null) continue;
                if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) continue;

                if (paymentRecord.getPaymentDateTime() != null) {
                    months.add(Month.of(paymentRecord.getPaymentDateTime().getMonthValue()).name());
                }
            }

            // Add all available months into dropdown
            for (String month : months) {
                model.addElement(month);
            }
        } catch (Exception e) {
            // Keep "All" only if error happens
        }

        return model;
    }

    public DefaultTableModel getFeedbackTableModel() {
        // Create table model for all feedback submitted by current customer
        String[] columns = {"Feedback ID", "Appointment ID", "Staff Rating", "Technician Rating", "Comment"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        try {
            List<Feedback> feedbacks = feedbackRepo.getAll();
            List<String> customerAppointmentIds = getCustomerAppointmentIds();

            for (Feedback feedback : feedbacks) {
                // Only show feedback that belongs to this customer's appointments
                if (customerAppointmentIds.contains(feedback.getAppointmentId())) {
                    Object[] row = {
                            feedback.getId(),
                            feedback.getAppointmentId(),
                            feedback.getStaffRating(),
                            feedback.getTechnicianRating(),
                            feedback.getComment()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Load Error", "Failed to load feedback");
        }

        return tableModel;
    }

    public DefaultComboBoxModel<String> getCompletedAppointmentComboModel() {
        // Create dropdown model for completed appointments that do not yet have feedback
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try {
            List<Appointment> appointments = appointmentRepo.getAll();
            List<Feedback> feedbacks = feedbackRepo.getAll();

            for (Appointment appointment : appointments) {
                boolean isOwnAppointment = appointment.getCustomerId().equalsIgnoreCase(customerId);
                boolean isCompleted = appointment.getStatusService() == AppointmentStatus.COMPLETED;

                // Check whether feedback has already been submitted for this appointment
                boolean feedbackExists = feedbacks.stream()
                        .anyMatch(feedback -> feedback.getAppointmentId().equalsIgnoreCase(appointment.getId()));

                // Only allow completed appointments without existing feedback
                if (isOwnAppointment && isCompleted && !feedbackExists) {
                    model.addElement(appointment.getId());
                }
            }
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Load Error", "Failed to load completed appointments");
        }

        return model;
    }

    public String[] getAppointmentPeopleDetails(String appointmentId) {
        try {
            // Get appointment first
            Appointment appointment = appointmentRepo.getOne(appointmentId);
            if (appointment == null) {
                return null;
            }

            // Retrieve staff and technician user objects
            User staff = userRepo.getOne(appointment.getStaffId());
            User technician = userRepo.getOne(appointment.getTechnicianId());

            // Format the text as ID | Name for display in UI
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

    public void submitFeedback(String appointmentId, String staffRatingText, String technicianRatingText, String comment) {
        try {
            // Validate all user inputs before creating feedback
            ValidationResult validationResult = new ValidationResult();

            Validator.required(validationResult, "Appointment ID", appointmentId);
            Validator.validateInteger(validationResult, "Staff Rating", staffRatingText);
            Validator.validateInteger(validationResult, "Technician Rating", technicianRatingText);

            if (validationResult.hasError()) {
                DialogUtil.showWarningMessage("Validation Error", validationResult.getErrors());
                return;
            }

            int staffRating = Integer.parseInt(staffRatingText);
            int technicianRating = Integer.parseInt(technicianRatingText);

            // Validate rating range between 1 and 5
            if (staffRating < 1 || staffRating > 5) {
                DialogUtil.showWarningMessage("Validation Error", "Staff rating must be between 1 and 5");
                return;
            }

            if (technicianRating < 1 || technicianRating > 5) {
                DialogUtil.showWarningMessage("Validation Error", "Technician rating must be between 1 and 5");
                return;
            }

            // Validate appointment exists
            Appointment appointment = appointmentRepo.getOne(appointmentId);
            if (appointment == null) {
                DialogUtil.showWarningMessage("Validation Error", "Appointment ID not found");
                return;
            }

            // Validate the appointment belongs to current customer
            if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                DialogUtil.showWarningMessage("Validation Error", "You can only provide feedback for your own appointment");
                return;
            }

            // Validate only completed appointments can receive feedback
            if (appointment.getStatusService() != AppointmentStatus.COMPLETED) {
                DialogUtil.showWarningMessage("Validation Error", "Only completed appointments can receive feedback");
                return;
            }

            // Validate one appointment can only have one feedback submission
            List<Feedback> feedbacks = feedbackRepo.getAll();
            boolean feedbackExists = feedbacks.stream()
                    .anyMatch(feedback -> feedback.getAppointmentId().equalsIgnoreCase(appointmentId));

            if (feedbackExists) {
                DialogUtil.showWarningMessage("Validation Error", "This appointment already has feedback. One appointment can only be submitted once.");
                return;
            }

            // Create feedback object and save into Feedback.txt
            Feedback feedback = new Feedback();
            feedback.setId(generateFeedbackId());
            feedback.setAppointmentId(appointmentId);
            feedback.setStaffRating(staffRating);
            feedback.setTechnicianRating(technicianRating);
            feedback.setComment(comment == null ? "" : comment.trim());

            feedbackRepo.create(feedback);

            DialogUtil.showInfoMessage("Success", "Feedback submitted successfully");
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Failed to submit feedback");
        }
    }

    public void loadProfileIntoFields(JTextField nameField, JTextField phoneField) {
        try {
            // Load current customer profile details into UI text fields
            User user = userRepo.getOne(customerId);
            if (user != null) {
                nameField.setText(user.getName());
                phoneField.setText(user.getContactNumber());
            }
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Load Error", "Failed to load profile");
        }
    }

    public void updateProfile(String name, String phoneNumber) {
        try {
            // Validate updated profile information before saving
            ValidationResult validationResult = new ValidationResult();

            Validator.required(validationResult, "Name", name);
            Validator.validatePhone(validationResult, phoneNumber);

            if (validationResult.hasError()) {
                DialogUtil.showWarningMessage("Validation Error", validationResult.getErrors());
                return;
            }

            // Retrieve current user and update editable fields
            User user = userRepo.getOne(customerId);
            if (user == null) {
                DialogUtil.showErrorMessage("Error", "User not found");
                return;
            }

            user.setName(name);
            user.setContactNumber(phoneNumber);
            userRepo.update(user);

            DialogUtil.showInfoMessage("Success", "Profile updated successfully");
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Failed to update profile");
        }
    }

    private List<String> getCustomerAppointmentIds() throws FileCorruptedException {
        // Collect all appointment IDs that belong to the current customer
        List<String> ids = new ArrayList<>();
        List<Appointment> appointments = appointmentRepo.getAll();

        for (Appointment appointment : appointments) {
            if (appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                ids.add(appointment.getId());
            }
        }

        return ids;
    }

    private Services getServiceById(String serviceId) {
        try {
            // Find service object by ID from Services.txt
            return servicesRepo.getOne(serviceId);
        } catch (Exception e) {
            return null;
        }
    }

    private String generateFeedbackId() {
        try {
            // Generate unique feedback ID by checking existing records
            while (true) {
                String id = RandomIdGenerator.generateId("FB-", 3);
                Feedback existing = feedbackRepo.getOne(id);
                if (existing == null) {
                    return id;
                }
            }
        } catch (Exception e) {
            return RandomIdGenerator.generateId("FB-", 3);
        }
    }
}
