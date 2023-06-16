package com.example.sr50web.Services;

import com.example.sr50web.Models.*;
import com.example.sr50web.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ManufacturerServices {
    @Autowired
    private IManufaturerRepository repository;
    public List<Manufacturer> listAll(){
        return (List<Manufacturer>) repository.findAllManufacturers();
    }

    public void save(Manufacturer manufacturer) {
        if(repository.findManucaturerById(manufacturer.getId()) != null){
            repository.update(manufacturer);
        }   else{
            repository.save(manufacturer);
        }
    }

    public Manufacturer get(Integer id) {
        Manufacturer manufacturer = repository.findManucaturerById(id);
        return manufacturer;
    }

    public void delete(Integer id) {
        repository.delete(id);
    }
}
