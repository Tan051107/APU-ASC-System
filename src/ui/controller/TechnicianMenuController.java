package ui.controller;

import java.time.LocalDate;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import enums.AppointmentStatus;
import models.Appointment;
import repositories.AppointmentRepository;

public class TechnicianMenuController {
    private final AppointmentRepository appointmentRepo;
    private final String loggedInTechnicianId;

    public TechnicianMenuController(String technicianId) {
        this.appointmentRepo = new AppointmentRepository();
        this.loggedInTechnicianId = technicianId;
    }

    public DefaultTableModel getAppointmentsTableModel() {
        List<Appointment> allAppointments = appointmentRepo.findAll();

        String[] columns = {"Appt ID", "Date", "Time", "Status", "Customer ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean isToday = (appt.getDate().equals(LocalDate.now()));
            if (isMyAppointment && isToday){
                Object[] rowData = {
                    appt.getId(),
                    appt.getDate().toString(),
                    appt.getTime().toString(),
                    appt.getStatusService().getDisplayAppointmentStatus(),
                    appt.getCustomerId()
                };
                tableModel.addRow(rowData);
            }
            
        }

        return tableModel;
    }

    public DefaultTableModel getAppointmentsTableModel(LocalDate filterDate, String searchQuery) {
        List<Appointment> allAppointments = appointmentRepo.findAll();

        String[] columns = {"Appt ID", "Date", "Time", "Status", "Customer ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        String query = (searchQuery == null) ? "" : searchQuery.trim().toLowerCase();

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean matchesDate = (filterDate == null) || appt.getDate().equals(filterDate);
            boolean matchesSearch = query.isEmpty() || 
                                    appt.getId().toLowerCase().contains(query) ||
                                    appt.getStatusService().name().toLowerCase().contains(query) ||
                                    appt.getCustomerId().toLowerCase().contains(query);

            if (isMyAppointment && matchesDate && matchesSearch) {
                Object[] rowData = {
                    appt.getId(),
                    appt.getDate().toString(),
                    appt.getTime().toString(),
                    appt.getStatusService().getDisplayAppointmentStatus(),
                    appt.getCustomerId()
                };
                tableModel.addRow(rowData);
            }
        }

        return tableModel;
    }

    public DefaultTableModel getHistoryAppointmentsTableModel() {
        List<Appointment> allAppointments = appointmentRepo.findAll();

        String[] columns = {"Appt ID", "Date", "Time", "Status", "Customer ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean isCompleted = appt.getStatusService() == AppointmentStatus.COMPLETED;
            if (isMyAppointment && isCompleted){
                Object[] rowData = {
                    appt.getId(),
                    appt.getDate().toString(),
                    appt.getTime().toString(),
                    appt.getStatusService().getDisplayAppointmentStatus(),
                    appt.getCustomerId()
                };
                tableModel.addRow(rowData);
            }
            
        }

        return tableModel;
    }

    public DefaultTableModel getHistoryAppointmentsTableModel(String searchQuery) {
        List<Appointment> allAppointments = appointmentRepo.findAll();

        String[] columns = {"Appt ID", "Date", "Time", "Status", "Customer ID"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        String query = (searchQuery == null) ? "" : searchQuery.trim().toLowerCase();

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean isCompleted = appt.getStatusService() == AppointmentStatus.COMPLETED;
            boolean matchesSearch = query.isEmpty() || 
                                    appt.getId().toLowerCase().contains(query) ||
                                    appt.getDate().toString().contains(query)||
                                    appt.getStatusService().name().toLowerCase().contains(query) ||
                                    appt.getCustomerId().toLowerCase().contains(query);

            if (isMyAppointment && isCompleted && matchesSearch ) {
                Object[] rowData = {
                    appt.getId(),
                    appt.getDate().toString(),
                    appt.getTime().toString(),
                    appt.getStatusService().getDisplayAppointmentStatus(),
                    appt.getCustomerId()
                };
                tableModel.addRow(rowData);
            }
        }

        return tableModel;
    }
}
