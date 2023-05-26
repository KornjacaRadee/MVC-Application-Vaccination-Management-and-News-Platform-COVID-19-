package com.example.sr50web.repo;

import com.example.sr50web.Models.Applicat;
import com.example.sr50web.Models.Infected;

import java.util.List;

public interface IApplicationRepository {
    public List<Applicat> findAllApplications();
    public Applicat findApplicationById(Integer id);
    public List<Applicat> findApplicatiosUserId(Integer id);

    public int save(Applicat applicat);

    public int update(Applicat applicat);

    public int delete(Integer id);
}
