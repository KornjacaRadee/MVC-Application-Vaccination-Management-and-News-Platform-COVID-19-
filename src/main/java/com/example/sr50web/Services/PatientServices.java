package com.example.sr50web.Services;

import com.example.sr50web.Models.Patient;
import com.example.sr50web.repo.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class PatientServices {

    @Autowired
    private IPatientRepository repository;

    public List<Patient> getAllPatients() {
        return(List<Patient>) repository.findAll(); }

    public Patient getPatientById(Integer id) {
        Patient patient = repository.findPatientById(id);
        return patient;
    }
    public void save(Patient patient) {

        repository.save(patient);
    }

    public void update(Patient patient){

        repository.update(patient);
    }



    public void delete(Integer id) { repository.delete(id); }
}
