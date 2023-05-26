package com.example.sr50web.repo;
import com.example.sr50web.Models.Patient;
import java.util.List;

public interface IPatientRepository {
    public Patient findPatientById(Integer id);

    public List<Patient> findAll();

    public int save(Patient patient);

    public int update(Patient patient);

    public int delete(Integer id);
}
