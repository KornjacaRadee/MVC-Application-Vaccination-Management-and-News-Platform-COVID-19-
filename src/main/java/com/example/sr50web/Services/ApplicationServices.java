package com.example.sr50web.Services;

import com.example.sr50web.Models.*;
import com.example.sr50web.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ApplicationServices {
    @Autowired
    private ApplicationRepository repository;
    public List<Applicat> listAll(){
        return (List<Applicat>) repository.findAllApplications();
    }

    public List<Applicat> listAllByPatient(String searchTerm){
        return (List<Applicat>) repository.findAllApplicationsByUser(searchTerm);
    }

    public void save(Applicat applicat) {
        if(repository.findApplicationById(applicat.getId()) != null){
            repository.update(applicat);
        }   else{
            repository.save(applicat);
        }
    }

    public List<Applicat> searchApplications(String query) {
        return repository.searchApplications(query);
    }
    public List<Applicat> listApplicationsByUserId(Integer id){

        return (List<Applicat>) repository.findApplicatiosUserId(id);
    }

    public Applicat get(Integer id) {
        Applicat result = repository.findApplicationById(id);
        return result;
    }

    public void delete(Integer id) {
        repository.delete(id);
    }
}
