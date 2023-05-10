package com.example.sr50web.Models;

import java.time.LocalDateTime;
public class Applicat {

    private int id;
    private LocalDateTime dateTime;
    private Vaccine vaccine;
    private Patient patient;

    public Applicat(int id, LocalDateTime dateTime, Vaccine vaccine, Patient patient) {
        this.id = id;
        this.dateTime = dateTime;
        this.vaccine = vaccine;
        this.patient = patient;
    }

    public Applicat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Applicat{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", vaccine=" + vaccine +
                ", patient=" + patient +
                '}';
    }
}
