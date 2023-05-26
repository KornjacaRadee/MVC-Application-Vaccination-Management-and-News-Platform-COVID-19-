package com.example.sr50web.Controllers;

import com.example.sr50web.Exceptions.UserNotFoundException;
import com.example.sr50web.Models.Manufacturer;
import com.example.sr50web.Models.Role;
import com.example.sr50web.Models.User;
import com.example.sr50web.Models.Vaccine;
import com.example.sr50web.Services.ManufacturerServices;
import com.example.sr50web.Services.UserServices;
import com.example.sr50web.Services.VaccineService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class VaccineController {

    @Autowired
    private VaccineService service;

    @Autowired
    private ManufacturerServices manufacturerServices;
    @Autowired
    private UserServices userServices;

    @GetMapping("/vaccine")
    public String showVaccines(Model model){
        List<Vaccine> list = service.listAll();
        model.addAttribute("allVaccines", list);
        return "vaccine";
    }

    @GetMapping("/vaccinetable")
    public String VaccineTable(Model model, HttpServletRequest request, @RequestParam(name="searchBy", required = false) String searchBy, @RequestParam(name = "orderBy", required = false) String orderBy) throws UserNotFoundException {
        if(searchBy == null || orderBy == null){
            searchBy = "id";
            orderBy = "asc";
        }
        List<Vaccine> list = service.getAllVaccinesSorted(searchBy,orderBy);
        model.addAttribute("allVaccines", list);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                    User temp = userServices.get(cookie.getValue());
                    if(!temp.getRole().equals(Role.valueOf("PACIJENT"))){
                        return "vaccinesTable";
                    }else{
                        return "redirect:/home";
                    }
                }
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/vaccine/new")
    public String showAddVaccine(Model model){
        List<Manufacturer> manufacturers = manufacturerServices.listAll();

        model.addAttribute("vaccine", new Vaccine());
        model.addAttribute("manufacturers", manufacturers);
        return "newVaccine";
    }

    @PostMapping("/vaccine/save")
    public String save(Vaccine vaccine, RedirectAttributes ra) {

        Manufacturer temp = manufacturerServices.get(vaccine.getManufacturerId());
        vaccine.setManufacturer(temp);
        service.save(vaccine);
        ra.addFlashAttribute("message", "Vakcina je sacuvana");
        return "redirect:/vaccinetable";
    }

    @GetMapping("/vaccine/edit/{id}")
    public String showEditVaccine(@PathVariable("id") Integer id, Model model, HttpServletRequest request, RedirectAttributes ra) throws UserNotFoundException {
        List<Manufacturer> manufacturers = manufacturerServices.listAll();
        Vaccine vaccine = service.get(id);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                    User temp = userServices.get(cookie.getValue());
                    if(temp.getRole().equals(Role.valueOf("ADMIN"))){
                        model.addAttribute("dissic", "readonly");
                        model.addAttribute("vaccine", vaccine);
                        model.addAttribute("manufacturers", manufacturers);
                        model.addAttribute("manufacturer", vaccine.getManufacturer());
                        return "newVaccine";
                    }else if(temp.getRole().equals(Role.valueOf("ZAPOSLENI"))){
                        return "redirect:/home";
                    }
                }
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/vaccine/info/{id}")
    public String showInfoVaccine(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        List<Manufacturer> manufacturers = manufacturerServices.listAll();
        Vaccine vaccine = service.get(id);
        model.addAttribute("dissic", "readonly");
        model.addAttribute("vaccine", vaccine);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("manufacturer", vaccine.getManufacturer());
        return "onlyVaccine";
    }

    @GetMapping("/vaccine/delete/{id}")
    public String deleteVax(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        ra.addFlashAttribute("message", "Vakcina je izbrisana");
        return "redirect:/vaccine";
    }

}