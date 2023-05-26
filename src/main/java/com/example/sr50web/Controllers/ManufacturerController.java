package com.example.sr50web.Controllers;
import com.example.sr50web.Models.*;
import com.example.sr50web.Services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ManufacturerController {

    @Autowired
    private ManufacturerServices service;

    @GetMapping("/manufacturer")
    public String showManufacturerList(Model model){
        List<Manufacturer> list = service.listAll();
        model.addAttribute("allManufacturers", list);
        return "manufacturer";
    }
    @GetMapping("/manufacturer/new")
    public String showNewForm(Model model){
        model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("pageTitle", "Dodaj novog proizvodjaca");
        return "newManufacturer";
    }
    @PostMapping("/manufacturer/save")
    public String saveManufacturer(Manufacturer manufacturer, RedirectAttributes ra) {
        service.save(manufacturer);
        ra.addFlashAttribute("message", "Proizvodjac je sacuvan");
        return "redirect:/manufacturer";
    }
    @GetMapping("/manufacturer/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        Manufacturer manufacturer = service.get(id);
        model.addAttribute("manufacturer", manufacturer);
        model.addAttribute("pageTitle",
                "Izmeni proizvodjaca (ime:"+ manufacturer.getName() +", drzava:" + manufacturer.getCountry() + ")");
        return "newManufacturer";
    }
    @GetMapping("/manufacturer/delete/{id}")
    public String deleteManufacturer(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        ra.addFlashAttribute("message", "Proizvodjac je izbrisan");
        return "redirect:/manufacturer";
    }
    @GetMapping("/error")
    public String handleError() {
        return "error";
    }
}