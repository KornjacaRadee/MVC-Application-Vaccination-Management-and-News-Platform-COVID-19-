package com.example.sr50web.Models;


import java.time.LocalDateTime;
public class Patient {

        private int userId;
        private Boolean vaccinated;
        private int received;
        private LocalDateTime lastdose;
        public User user;

    public Patient(int userId, Boolean vaccinated, int received, LocalDateTime lastdose, User user) {
        this.userId = userId;
        this.vaccinated = vaccinated;
        this.received = received;
        this.lastdose = lastdose;
        this.user = user;
    }

    public Patient() {
    }

    public Patient(int userId,User user) {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(Boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public LocalDateTime getLastdose() {
        return lastdose;
    }

    public void setLastdose(LocalDateTime lastdose) {
        this.lastdose = lastdose;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "userId=" + userId +
                ", vaccinated=" + vaccinated +
                ", received=" + received +
                ", lastdose=" + lastdose +
                ", user=" + user +
                '}';
    }
}
