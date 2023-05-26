package com.example.sr50web.Services;

import com.example.sr50web.Models.Vaccine;
import com.example.sr50web.repo.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class VaccineService {

    @Autowired
    private VaccineRepository repository;

    public List<Vaccine> listAll() { return(List<Vaccine>) repository.findAllVaccines(); }

    public List<Vaccine> listAllQuanty() { return(List<Vaccine>) repository.findAllVaccinesQuanty(); }

    public void save(Vaccine vaccine) {
        if(repository.findVaccineById(vaccine.getId()) != null){
            repository.update(vaccine);
        } else{
            repository.save(vaccine);
        }
    }
    public List<Vaccine> getAllVaccinesSorted(String sort, String direction) {
        return repository.findSortedVaccines(sort, direction);
    }
    public List<Vaccine> searchVaccines(String query) {
        return repository.searchVaccines(query);
    }

    public Vaccine get(Integer id) {
        Vaccine vaccine = repository.findVaccineById(id);
        return vaccine;
    }

    public void delete(Integer id) { repository.delete(id); }
}
