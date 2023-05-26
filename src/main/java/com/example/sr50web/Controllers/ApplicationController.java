package com.example.sr50web.Controllers;
import com.example.sr50web.Exceptions.UserNotFoundException;
import com.example.sr50web.Models.*;
import com.example.sr50web.Services.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    private ApplicationServices service;

    @Autowired
    private UserServices userServices;

    @Autowired
    private PatientServices patientServices;

    @Autowired
    private VaccineService vaccinesService;





    @PostMapping("/application/save")
    public String saveApplicat(Applicat applicat, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {
        Applicat novo = applicat;
        User temp = new User();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                   temp  = userServices.get(cookie.getValue());
                }
            }
        }
        List <Patient>  lista = patientServices.getAllPatients();
        Patient patient1 = new Patient();
        for(Patient patient : lista){
            if(patient.getUserId() == temp.getId()){
                patient1 = patient;
            }
        }
        Vaccine tempVaccine = vaccinesService.get(novo.getVaccineId());

        novo.setDateTime(LocalDateTime.now());
        novo.setPatient(patient1);
        novo.setVaccine(tempVaccine);
        service.save(novo);

        ra.addFlashAttribute("message", "Proizvodjac je sacuvan");
        return "redirect:/manufacturer";
    }
    @GetMapping("/application/new")
    public String showAddUser(Model model){
        List<Vaccine> vaccines = vaccinesService.listAllQuanty();
        model.addAttribute("vaccines", vaccines);
        model.addAttribute("applicat", new Applicat());
        model.addAttribute("method", "/application/save");
        return "makeApplication";
    }



}