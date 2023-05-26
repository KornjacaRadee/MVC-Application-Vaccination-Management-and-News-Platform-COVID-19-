package com.example.sr50web.repo;

import com.example.sr50web.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface IManufaturerRepository {
    public Manufacturer findManucaturerById(Integer id);

    public List<Manufacturer> findAllManufacturers();

    public int save(Manufacturer manufacturer);

    public int delete(int id);

    public int update(Manufacturer manufacturer);


}

