package com.example.sr50web.repo;
import com.example.sr50web.Models.*;

import java.util.List;
public interface IVaccineRepository {

    public List<Vaccine> findAllVaccines();
    public Vaccine findVaccineById(Integer id);
    public List<Vaccine> findVaccineByManufacturer(Manufacturer manufacturer);
    public List<Vaccine> searchVaccines(String query);
    public List<Vaccine> findSortedVaccines(String sort, String direction);
    public List<Vaccine> findAllVaccinesQuanty();

    public int save(Vaccine vaccine);

    public int update(Vaccine vaccine);

    public int delete(Integer id);
}
