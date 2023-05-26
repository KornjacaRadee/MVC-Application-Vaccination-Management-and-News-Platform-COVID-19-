package com.example.sr50web.Controllers;
import com.example.sr50web.Services.ApplicationServices;
import com.example.sr50web.Services.PatientServices;
import com.example.sr50web.Services.UserServices;
import com.example.sr50web.repo.*;
import com.example.sr50web.Models.*;
import com.example.sr50web.Exceptions.UserNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserServices service;

    @Autowired
    private ApplicationServices applicationService;
    @Autowired
    private PatientServices patientService;

    @GetMapping("/user")
    public String showUsers(Model model){
        List<User> listUsers = service.listAll();
        model.addAttribute("allUsers", listUsers);
        return "user";
    }

    @GetMapping("/user/new")
    public String showAddUser(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("method", "/user/save");
        model.addAttribute("pageTitle", "Dodaj novog pacijenta");
        return "newUser";
    }

    @PostMapping("/user/save")
    public String save(User user, RedirectAttributes ra) throws UserNotFoundException {
        user.setRole(Role.PACIJENT);
        LocalDateTime dateTime = LocalDateTime.now();
        user.setRegistration(dateTime);
        service.save(user);
        User tempUser = service.get(user.getEmail());
        if(user.getRole() == Role.PACIJENT){
            Patient temp = new Patient();;
            temp.setUserId(tempUser.getId());
            patientService.save(temp);
        }
        ra.addFlashAttribute("message", "Korisnik je sacuvan");
        return "redirect:/user";
    }

    @PostMapping ("/user/update")
    public String update(User user, RedirectAttributes ra) throws UserNotFoundException {
        user.setAddress("adresicaaa");
        service.save(user);
        ra.addFlashAttribute("message", "Korisnik je apdejtovan");
        return "redirect:/user";
    }
    @GetMapping("/user/edit")
    public String EditUserWithCookie(HttpServletRequest request, Model model, RedirectAttributes ra){
        try{
            User temp = new User();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if(cookie.getValue().contains("@")){
                        temp = service.get(cookie.getValue());
                    }
                }
            }
            List <Applicat> tempApplication = applicationService.listApplicationsByUserId(temp.getId());
            model.addAttribute("applicat", new Applicat());
            model.addAttribute("applications", tempApplication);
            model.addAttribute("user", temp);
            model.addAttribute("method", "/users/update");
            return "newUser";

        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "Korisnik ne moze biti izmenjen");
            return "redirect:/user";
        }
    }
    @GetMapping("/user/edit/{id}")
    public String showEditUser(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try{
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("method", "/users/update");
            model.addAttribute("pageTitle", "Promeni korisnika (Email:" + user.getEmail() + ")");
            return "newUser";

        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "Korisnik ne moze biti izmenjen");
            return "redirect:/user";
        }
    }

    @GetMapping("application/delete/{id}")
    public String deleteApplication(@PathVariable("id") Integer id, RedirectAttributes ra){
        applicationService.delete(id);
        ra.addFlashAttribute("message", "Obrisan");
        return "redirect:/user/edit";

    }
    @GetMapping("user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
//        patientService.delete(id);
        ra.addFlashAttribute("message", "Korisnik je izrisan");
        return "redirect:/user";

    }

}
