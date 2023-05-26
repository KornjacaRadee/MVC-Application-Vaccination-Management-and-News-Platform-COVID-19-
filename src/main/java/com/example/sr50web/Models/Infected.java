package com.example.sr50web.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
public class Infected {

    private int id;
    private int infected;
    private int tested;
    private int hospitalized;
    private int respirator;
    private int allInfected;
    private LocalDate dateTime;

    public Infected(int id, int infected, int tested, int hospitalized, int respirator, int allInfected, LocalDate dateTime) {
        this.id = id;
        this.infected = infected;
        this.tested = tested;
        this.hospitalized = hospitalized;
        this.respirator = respirator;
        this.allInfected = allInfected;
        this.dateTime = dateTime;
    }

    public Infected() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInfected() {
        return infected;
    }

    public void setInfected(int infected) {
        this.infected = infected;
    }

    public int getTested() {
        return tested;
    }

    public void setTested(int tested) {
        this.tested = tested;
    }

    public int getHospitalized() {
        return hospitalized;
    }

    public void setHospitalized(int hospitalized) {
        this.hospitalized = hospitalized;
    }

    public int getRespirator() {
        return respirator;
    }

    public void setRespirator(int respirator) {
        this.respirator = respirator;
    }

    public int getAllInfected() {
        return allInfected;
    }

    public void setAllInfected(int allInfected) {
        this.allInfected = allInfected;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Infected{" +
                "id=" + id +
                ", infected=" + infected +
                ", tested=" + tested +
                ", hospitalized=" + hospitalized +
                ", respirator=" + respirator +
                ", allInfected=" + allInfected +
                ", dateTime=" + dateTime +
                '}';
    }
}
