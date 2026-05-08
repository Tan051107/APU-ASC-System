package ui.controller;

import java.time.LocalDate;
import java.util.List;

import models.Appointment;
import services.AppointmentService;
import ui.pages.Manager.AppointmentReports;
import utils.DialogUtil;

public class AppointmentReportsController {
    private final AppointmentReports panel;

    private final AppointmentService appointmentService = new AppointmentService();

    public AppointmentReportsController(AppointmentReports panel){
        this.panel = panel;
        initListeners();
        loadMonthlyAppointments();
        loadYearlyAppointments();
        loadCompletedAppointments();
        loadAssignedAppointments();
        loadCancelledAppointments();
    }

    private void initListeners() {
        panel.closeButton.addActionListener(e -> panel.dispose());
    }

    public void loadMonthlyAppointments(){
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
            String totalString = Integer.toString(total);
            panel.monthTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Monthly Total Appointment Count!");
        }
    }

    public void loadYearlyAppointments(){
        try {
            List<Appointment> allAppointments = appointmentService.getAllAppointments();

            int total = 0;
            LocalDate today = LocalDate.now();
            int currentYear = today.getYear();

            for (Appointment appointment : allAppointments){
                if (appointment.getDate().getYear() == currentYear){
                    total++;
                }
            }
            String totalString = Integer.toString(total);
            panel.yearTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Yearly Total Appointment Count!");
        }
    }

    public void loadCompletedAppointments(){
        try {
            List<Appointment> allAppointments = appointmentService.getAllAppointments();

            int total = 0;

            for (Appointment appointment : allAppointments){
                if (appointment.getStatusService().getDisplayAppointmentStatus().equals("Completed")){
                    total++;
                }
            }
            String totalString = Integer.toString(total);
            panel.completeTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Completed Appointment Count!");
        }
    }

    public void loadAssignedAppointments(){
        try {
            List<Appointment> allAppointments = appointmentService.getAllAppointments();

            int total = 0;

            for (Appointment appointment : allAppointments){
                if (appointment.getStatusService().getDisplayAppointmentStatus().equals("Assigned")){
                    total++;
                }
            }
            String totalString = Integer.toString(total);
            panel.assignedTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Completed Appointment Count!");
        }
    }

    public void loadCancelledAppointments(){
        try {
            List<Appointment> allAppointments = appointmentService.getAllAppointments();

            int total = 0;

            for (Appointment appointment : allAppointments){
                if (appointment.getStatusService().getDisplayAppointmentStatus().equals("Cancelled")){
                    total++;
                }
            }
            String totalString = Integer.toString(total);
            panel.cancelledTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Completed Appointment Count!");
        }
    }
}
