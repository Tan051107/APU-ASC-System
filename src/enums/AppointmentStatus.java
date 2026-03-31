package enums;

public enum AppointmentStatus {
    ASSIGNED("Assigned"),
    COMPLETED("Completed");


    private final String displayAppointmentStatus;

    AppointmentStatus(String displayAppointmentStatus){
        this.displayAppointmentStatus = displayAppointmentStatus;
    }

    public String getDisplayAppointmentStatus() {
        return displayAppointmentStatus;
    }

    public static AppointmentStatus fromString (String appointmentStatusString){
        for (AppointmentStatus appointmentStatus : AppointmentStatus.values()){
            if(appointmentStatus.displayAppointmentStatus.equalsIgnoreCase(appointmentStatusString)){
                return appointmentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid Appointment Status selected");
    }
}
