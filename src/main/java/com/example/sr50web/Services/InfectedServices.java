package com.example.sr50web.Services;

import com.example.sr50web.Models.Infected;
import com.example.sr50web.repo.InfectedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InfectedServices {

    @Autowired
    private InfectedRepository repository;

    public List<Infected> allInfected() { return(List<Infected>) repository.findAllInfected(); }

    public void save(Infected infectedNews) {
        Integer todayCounty = 0;
        if(repository.findInfectedById(infectedNews.getId()) != null){
            repository.update(infectedNews);
        } else{
            List<Infected> infectedAll = repository.findAllInfected();
            for(Infected temp : infectedAll){
                if (temp.getDateTime().isEqual(LocalDate.now())){
                    todayCounty++;
                }
            }
            if(todayCounty == 0){
            repository.save(infectedNews);
            }
        }
    }

    public Infected get(Integer id) {
        Infected infectedNews = repository.findInfectedById(id);
        return infectedNews;
    }

    public void delete(Integer id) { repository.delete(id); }
}
