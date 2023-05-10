package com.example.sr50web.Models;

import java.time.LocalDate;

public class Request {

    private int id;
    private int amount;
    private String reason;
    private LocalDate date;
    private String status;
    private User employee;
    private Vaccine vaccine;
    private String comment;

    public Request(int id, int amount, String reason, LocalDate date, String status, User employee, Vaccine vaccine, String comment) {
        this.id = id;
        this.amount = amount;
        this.reason = reason;
        this.date = date;
        this.status = status;
        this.employee = employee;
        this.vaccine = vaccine;
        this.comment = comment;
    }

    public Request() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", amount=" + amount +
                ", reason='" + reason + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", employee=" + employee +
                ", vaccine=" + vaccine +
                ", comment='" + comment + '\'' +
                '}';
    }
}
