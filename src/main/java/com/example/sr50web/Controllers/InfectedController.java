package com.example.sr50web.Controllers;

import com.example.sr50web.Exceptions.UserNotFoundException;
import com.example.sr50web.Models.Infected;
import com.example.sr50web.Models.Role;
import com.example.sr50web.Models.User;
import com.example.sr50web.Services.InfectedServices;
import com.example.sr50web.Services.UserServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class InfectedController {

    @Autowired
    private InfectedServices service;
    @Autowired
    private UserServices userService;
    @GetMapping("/infected")
    public String newsList(Model model){
        List<Infected> list = service.allInfected();
        model.addAttribute("infected", new Infected());
        model.addAttribute("allInfected", list);
        return "infected";
    }

    @GetMapping("/infected/new")
    public String newInfected(Model model, HttpServletRequest request) throws UserNotFoundException {
        model.addAttribute("infected", new Infected());
        model.addAttribute("pageTitle", "Add news");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                User temp = userService.get(cookie.getValue());
                if(temp.getRole().equals(Role.ADMIN)){
                    return "newInfected";
                }else{
                    return "redirect:/home";
                }
            }
            }
        }
        return "redirect:/home";
    }

    @PostMapping("/infected/save")
    public String saveNews(Infected infected, RedirectAttributes ra) {
        service.save(infected);
        ra.addFlashAttribute("message", "News have been saved");
        return "redirect:/infected";
    }

    @GetMapping("/infected/edit/{id}")
    public String editInfected(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        Infected infected = service.get(id);
        model.addAttribute("infected", infected);
        model.addAttribute("pageTitle",
                "Edit news (name:" + infected.getId() + ")");
        return "newInfected";
    }

    @GetMapping("/infected/delete/{id}")
    public String deleteNews(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        ra.addFlashAttribute("message", "News have been deleted");
        return "redirect:/infected";
    }
}