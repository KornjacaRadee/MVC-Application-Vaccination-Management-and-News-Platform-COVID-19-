package com.example.sr50web.repo;

import com.example.sr50web.Models.Infected;
import java.util.List;
public interface IInfectedNewsRepository {
    public List<Infected> findAllInfected();
    public Infected findInfectedById(Integer id);

    public int save(Infected infectedNews);

    public int update(Infected infectedNews);

    public int delete(Integer id);
}
