package ui.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import enums.AppointmentStatus;
import enums.UserType;
import exceptions.FileCorruptedException;
import mapper.AppointmentMapper;
import mapper.CustomerCarMapper;
import mapper.UserMapper;
import models.Appointment;
import models.CustomerCar;
import models.User;
import repositories.CrudRepository;

public class TechnicianMenuController {
    /* private final AppointmentRepository appointmentRepo; */
    private final CrudRepository<Appointment> appointmentRepo;
    private final CrudRepository<User> userRepo;
    private final CrudRepository<CustomerCar> carRepo;

    private final String loggedInTechnicianId;

    public TechnicianMenuController(String technicianId) {
        this.appointmentRepo = new CrudRepository<>("txt_files/Appointment.txt", new AppointmentMapper());
        this.userRepo = new CrudRepository<>("txt_files/User.txt", new UserMapper());
        this.carRepo = new CrudRepository<>("txt_files/CustomerCar.txt", new CustomerCarMapper());
        this.loggedInTechnicianId = technicianId;
    }

    public DefaultTableModel getAppointmentsTableModel() {
        List<Appointment> allAppointments = new ArrayList<>();
        try {
            allAppointments = appointmentRepo.getAll();
        } catch (FileCorruptedException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        String[] columns = {"Appt ID","Plate Number", "Date", "Time", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean isToday = (appt.getDate().equals(LocalDate.now()));

            if (!isMyAppointment || !isToday) {
                continue;
            }
            try {
                CustomerCar apptCar = findCarByID(appt.getCarId());
                String plate = (apptCar != null && apptCar.getCarPlate() != null) ? apptCar.getCarPlate() : "N/A";

                Object[] rowData = {
                    appt.getId(),
                    plate,  
                    appt.getDate().toString(),
                    appt.getTime().toString(),
                    appt.getStatusService().getDisplayAppointmentStatus()
                };
                tableModel.addRow(rowData);

            } catch (Exception e) {
                System.err.println("Error reading car for appointment " + appt.getId() + ": " + e.getMessage());
            }
        }

        return tableModel;
    }

    public DefaultTableModel getAppointmentsTableModel(LocalDate filterDate, String searchQuery) {
        List<Appointment> allAppointments = new ArrayList<>();
        try {
            allAppointments = appointmentRepo.getAll();
        } catch (FileCorruptedException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        String[] columns = {"Appt ID","Plate Number", "Date", "Time", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        String query = (searchQuery == null) ? "" : searchQuery.trim().toLowerCase();

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean matchesDate = (filterDate == null) || appt.getDate().equals(filterDate);
            if (!isMyAppointment || !matchesDate) {
                continue;
            }
            
            try {
                CustomerCar apptCar = findCarByID(appt.getCarId());
                
                String plate = (apptCar.getCarPlate() != null) ? apptCar.getCarPlate() : "";

                boolean matchesSearch = query.isEmpty() || 
                                        appt.getId().toLowerCase().contains(query) ||
                                        appt.getStatusService().name().toLowerCase().contains(query) ||
                                        plate.toLowerCase().contains(query); 

                if (matchesSearch) {
                    Object[] rowData = {
                        appt.getId(),
                        plate,  
                        appt.getDate().toString(),
                        appt.getTime().toString(),
                        appt.getStatusService().getDisplayAppointmentStatus() 
                    };
                    tableModel.addRow(rowData);
                }
                
            } catch (Exception e) {
                System.err.println("Error reading car for appointment " + appt.getId() + ": " + e.getMessage());
            }
        }

        return tableModel;
    }

    public DefaultTableModel getHistoryAppointmentsTableModel() {
        List<Appointment> allAppointments = new ArrayList<>();
        try {
            allAppointments = appointmentRepo.getAll();
        } catch (FileCorruptedException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        String[] columns = {"Appt ID","Plate Number", "Date", "Time", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean isCompleted = appt.getStatusService() == AppointmentStatus.COMPLETED;
            if (!isMyAppointment || !isCompleted) {
                continue;
            }
            try {
                CustomerCar apptCar = findCarByID(appt.getCarId());
                String plate = (apptCar != null && apptCar.getCarPlate() != null) ? apptCar.getCarPlate() : "N/A";

                Object[] rowData = {
                    appt.getId(),
                    plate,  
                    appt.getDate().toString(),
                    appt.getTime().toString(),
                    appt.getStatusService().getDisplayAppointmentStatus()
                };
                tableModel.addRow(rowData);

            } catch (Exception e) {
                System.err.println("Error reading car for appointment " + appt.getId() + ": " + e.getMessage());
            }
            
        }

        return tableModel;
    }

    public DefaultTableModel getHistoryAppointmentsTableModel(String searchQuery) {
        List<Appointment> allAppointments = new ArrayList<>();
        try {
            allAppointments = appointmentRepo.getAll();
        } catch (FileCorruptedException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        String[] columns = {"Appt ID","Plate Number", "Date", "Time", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        String query = (searchQuery == null) ? "" : searchQuery.trim().toLowerCase();

        for (Appointment appt : allAppointments) {
            boolean isMyAppointment = appt.getTechnicianId().equals(loggedInTechnicianId);
            boolean isCompleted = appt.getStatusService() == AppointmentStatus.COMPLETED;
            if (!isMyAppointment || !isCompleted) {
                continue;
            }
            try {
                CustomerCar apptCar = findCarByID(appt.getCarId());
                String plate = (apptCar.getCarPlate() != null) ? apptCar.getCarPlate() : "";

                boolean matchesSearch = query.isEmpty() || 
                                        appt.getId().toLowerCase().contains(query) ||
                                        appt.getStatusService().name().toLowerCase().contains(query) ||
                                        plate.toLowerCase().contains(query);

                if (matchesSearch) {
                    Object[] rowData = {
                        appt.getId(),
                        plate,  
                        appt.getDate().toString(),
                        appt.getTime().toString(),
                        appt.getStatusService().getDisplayAppointmentStatus() 
                    };
                    tableModel.addRow(rowData);
                }
                
            } catch (Exception e) {
                System.err.println("Error reading car for appointment " + appt.getId() + ": " + e.getMessage());
            }
        }

        return tableModel; 
    }

    public Appointment findAppointmentById(String id) {
        try {
            return appointmentRepo.getOne(id);
        } catch (FileCorruptedException e) {
            System.err.println("Error reading appointments: " + e.getMessage());
            return null;
        }
    }

    public User findCustomerById(String id) throws FileCorruptedException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        User foundUser = userRepo.getOne(id);
        if (foundUser != null && foundUser.getUserType() == UserType.CUSTOMER) {
            return foundUser;
        }
        
        return null; 
    }

    public User findStaffById(String id) throws FileCorruptedException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        User foundUser = userRepo.getOne(id);
        
        if (foundUser != null && foundUser.getUserType() == UserType.COUNTER_STAFF) {
            return foundUser;
        }
        
        return null;
    }

    public CustomerCar findCarByID(String id) throws FileCorruptedException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        CustomerCar foundCar = carRepo.getOne(id);
        
        if (foundCar != null) {
            return foundCar;
        }
        
        return null;
    }


    // View Appointment Controllers
    public void completeAppointment(Appointment appointmentToEdit, JDialog popupForm) {
        try {
            if (appointmentToEdit.getStatusService() == AppointmentStatus.COMPLETED) {
                JOptionPane.showMessageDialog(popupForm, 
                    "This appointment is already marked as completed.", 
                    "Already Completed", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            appointmentToEdit.setStatusService(AppointmentStatus.COMPLETED);

            appointmentRepo.update(appointmentToEdit);

            JOptionPane.showMessageDialog(popupForm, 
                                        String.format("Successfully completed appointment %s.", appointmentToEdit.getId()),
                                        "Updated Successfully", 
                                        JOptionPane.INFORMATION_MESSAGE);

            popupForm.dispose();

        } catch (exceptions.NotFoundException | exceptions.FileCorruptedException e) {
            JOptionPane.showMessageDialog(popupForm, 
                "Failed to update appointment: " + e.getMessage(), 
                "Update Error", 
                JOptionPane.ERROR_MESSAGE); 
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(popupForm, 
                "Encountered an unexpected error when updating the appointment.", 
                "System Error", 
                JOptionPane.ERROR_MESSAGE);
            // logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
