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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CustomerController {

    private final String customerId;

    private final CrudRepository<Appointment> appointmentRepo =
            new CrudRepository<>("txt_files/Appointment.txt", new AppointmentMapper());

    private final CrudRepository<Feedback> feedbackRepo =
            new CrudRepository<>("txt_files/Feedback.txt", new FeedbackMapper());

    private final CrudRepository<User> userRepo =
            new CrudRepository<>("txt_files/User.txt", new UserMapper());

    private final CrudRepository<Services> servicesRepo =
            new CrudRepository<>("txt_files/Services.txt", new ServicesMapper());

    public CustomerController(String customerId) {
        this.customerId = customerId;
    }

    public DefaultTableModel getServiceHistoryTableModel(String search, String statusFilter) {
        String[] columns = {"Appointment ID", "Date", "Time", "Service Type", "Description", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        try {
            List<Appointment> appointments = appointmentRepo.getAll();
            String keyword = search == null ? "" : search.trim().toLowerCase();

            for (Appointment appointment : appointments) {
                if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                    continue;
                }

                Services service = getServiceById(appointment.getServiceId());
                String serviceType = service == null ? "Unknown Service" : service.getServiceName();
                String serviceDescription = service == null ? "-" : service.getServiceDetails();
                String appointmentStatus = appointment.getStatusService().getDisplayAppointmentStatus();

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

                boolean matchesSearch = keyword.isEmpty()
                        || appointment.getId().toLowerCase().contains(keyword)
                        || serviceType.toLowerCase().contains(keyword)
                        || serviceDescription.toLowerCase().contains(keyword)
                        || appointmentStatus.toLowerCase().contains(keyword)
                        || appointment.getDate().toString().toLowerCase().contains(keyword);

                if (matchesStatus && matchesSearch) {
                    Object[] row = {
                            appointment.getId(),
                            appointment.getDate(),
                            appointment.getTime(),
                            serviceType,
                            serviceDescription,
                            appointmentStatus
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Load Error", "Failed to load appointment history");
        }

        return tableModel;
    }

    public DefaultTableModel getPaymentHistoryTableModel(String search, String yearFilter, String monthFilter) {
        String[] columns = {"Appointment ID", "Service Type", "Description", "Amount (RM)", "Status", "Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        try {
            List<Appointment> appointments = appointmentRepo.getAll();
            String keyword = search == null ? "" : search.trim().toLowerCase();

            for (Appointment appointment : appointments) {
                if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                    continue;
                }

                Services service = getServiceById(appointment.getServiceId());
                String serviceType = service == null ? "Unknown Service" : service.getServiceName();
                String description = service == null ? "-" : service.getServiceDetails();
                String amount = service == null ? "0.00" : String.format("%.2f", service.getServicePrice());
                String status = appointment.getStatusService().getDisplayAppointmentStatus();

                String appointmentYear = String.valueOf(appointment.getDate().getYear());
                String appointmentMonth = Month.of(appointment.getDate().getMonthValue()).name();

                boolean matchesYear = "All".equalsIgnoreCase(yearFilter) || appointmentYear.equals(yearFilter);
                boolean matchesMonth = "All".equalsIgnoreCase(monthFilter) || appointmentMonth.equalsIgnoreCase(monthFilter);

                boolean matchesSearch = keyword.isEmpty()
                        || appointment.getId().toLowerCase().contains(keyword)
                        || serviceType.toLowerCase().contains(keyword)
                        || description.toLowerCase().contains(keyword)
                        || amount.toLowerCase().contains(keyword)
                        || appointment.getDate().toString().toLowerCase().contains(keyword);

                if (matchesYear && matchesMonth && matchesSearch) {
                    Object[] row = {
                            appointment.getId(),
                            serviceType,
                            description,
                            amount,
                            status,
                            appointment.getDate()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Load Error", "Failed to load payment history");
        }

        return tableModel;
    }

    public DefaultComboBoxModel<String> getPaymentYearComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            Set<String> years = new LinkedHashSet<>();
            for (Appointment appointment : appointmentRepo.getAll()) {
                if (appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                    years.add(String.valueOf(appointment.getDate().getYear()));
                }
            }
            for (String year : years) {
                model.addElement(year);
            }
        } catch (Exception e) {
            // keep All only
        }

        return model;
    }

    public DefaultComboBoxModel<String> getPaymentMonthComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("All");

        try {
            Set<String> months = new LinkedHashSet<>();
            for (Appointment appointment : appointmentRepo.getAll()) {
                if (appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                    months.add(Month.of(appointment.getDate().getMonthValue()).name());
                }
            }
            for (String month : months) {
                model.addElement(month);
            }
        } catch (Exception e) {
            // keep All only
        }

        return model;
    }

    public DefaultTableModel getFeedbackTableModel() {
        String[] columns = {"Feedback ID", "Appointment ID", "Staff Rating", "Technician Rating", "Comment"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        try {
            List<Feedback> feedbacks = feedbackRepo.getAll();
            List<String> customerAppointmentIds = getCustomerAppointmentIds();

            for (Feedback feedback : feedbacks) {
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
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try {
            List<Appointment> appointments = appointmentRepo.getAll();
            List<Feedback> feedbacks = feedbackRepo.getAll();

            for (Appointment appointment : appointments) {
                boolean isOwnAppointment = appointment.getCustomerId().equalsIgnoreCase(customerId);
                boolean isCompleted = appointment.getStatusService() == AppointmentStatus.COMPLETED;
                boolean feedbackExists = feedbacks.stream()
                        .anyMatch(feedback -> feedback.getAppointmentId().equalsIgnoreCase(appointment.getId()));

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
            Appointment appointment = appointmentRepo.getOne(appointmentId);
            if (appointment == null) {
                return null;
            }

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

    public void submitFeedback(String appointmentId, String staffRatingText, String technicianRatingText, String comment) {
        try {
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

            if (staffRating < 1 || staffRating > 5) {
                DialogUtil.showWarningMessage("Validation Error", "Staff rating must be between 1 and 5");
                return;
            }

            if (technicianRating < 1 || technicianRating > 5) {
                DialogUtil.showWarningMessage("Validation Error", "Technician rating must be between 1 and 5");
                return;
            }

            Appointment appointment = appointmentRepo.getOne(appointmentId);
            if (appointment == null) {
                DialogUtil.showWarningMessage("Validation Error", "Appointment ID not found");
                return;
            }

            if (!appointment.getCustomerId().equalsIgnoreCase(customerId)) {
                DialogUtil.showWarningMessage("Validation Error", "You can only provide feedback for your own appointment");
                return;
            }

            if (appointment.getStatusService() != AppointmentStatus.COMPLETED) {
                DialogUtil.showWarningMessage("Validation Error", "Only completed appointments can receive feedback");
                return;
            }

            List<Feedback> feedbacks = feedbackRepo.getAll();
            boolean feedbackExists = feedbacks.stream()
                    .anyMatch(feedback -> feedback.getAppointmentId().equalsIgnoreCase(appointmentId));

            if (feedbackExists) {
                DialogUtil.showWarningMessage("Validation Error", "This appointment already has feedback. One appointment can only be submitted once.");
                return;
            }

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

    public User getCustomerUser() {
        try {
            return userRepo.getOne(customerId);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getCustomerAppointmentIds() throws FileCorruptedException {
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
            return servicesRepo.getOne(serviceId);
        } catch (Exception e) {
            return null;
        }
    }

    private String generateFeedbackId() {
        try {
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
