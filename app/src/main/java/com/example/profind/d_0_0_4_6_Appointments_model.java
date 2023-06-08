package com.example.profind;

public class d_0_0_4_6_Appointments_model {
    String clients_phone,professionals_phone,appointment_time,appointment_date,booking_time;

    public d_0_0_4_6_Appointments_model() {
    }

    public d_0_0_4_6_Appointments_model(String clients_phone, String professionals_phone,
                                        String appointment_time, String appointment_date,
                                        String booking_time) {
        this.clients_phone = clients_phone;
        this.professionals_phone = professionals_phone;
        this.appointment_time = appointment_time;
        this.appointment_date = appointment_date;
        this.booking_time = booking_time;
    }

    public String getClients_phone() {
        return clients_phone;
    }

    public void setClients_phone(String clients_phone) {
        this.clients_phone = clients_phone;
    }

    public String getProfessionals_phone() {
        return professionals_phone;
    }

    public void setProfessionals_phone(String professionals_phone) {
        this.professionals_phone = professionals_phone;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }
}
