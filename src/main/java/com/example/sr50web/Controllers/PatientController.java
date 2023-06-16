package com.example.sr50web.Controllers;
import com.example.sr50web.Services.*;
import com.example.sr50web.Models.*;
import com.example.sr50web.Exceptions.*;
import com.example.sr50web.repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientServices service;
    @Autowired
    private UserServices userService;

    @GetMapping("/patient")
    public String allPatients(Model model) throws UserNotFoundException {
        List<Patient> list = service.getAllPatients();
        model.addAttribute("allPatients", list);
        return "patient";
    }

    @PostMapping("/patient/update")
    public String update(Patient patient, RedirectAttributes ra) {
        service.update(patient);
        ra.addFlashAttribute("message", "Patient has been updated");
        return "redirect:/patient";
    }

    @GetMapping("/patient/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        Patient patient = service.getPatientById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("pageTitle",
                "Edit patient (name:" + patient.getUser().getFirstName() + ")");
        return "newPatient";
    }

    @GetMapping("/patient/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        userService.delete(id);
        ra.addFlashAttribute("message", "Patient has been deleted");
        return "redirect:/patient";
    }
}

