package com.example.sr50web.Controllers;

import com.example.sr50web.Exceptions.UserNotFoundException;
import com.example.sr50web.Models.Infected;
import com.example.sr50web.Models.News;
import com.example.sr50web.Models.Role;
import com.example.sr50web.Models.User;
import com.example.sr50web.Services.InfectedServices;
import com.example.sr50web.Services.NewsServices;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsServices service;
    @Autowired
    private InfectedServices infectedServices;
    @Autowired
    private UserServices userServices;
    @GetMapping("/news")
    public String showNewsList(Model model){
        List<News> list = service.allNews();
        model.addAttribute("news", new News());
        model.addAttribute("allNews", list);
        return "news";
    }
    @GetMapping("/home")
    public String showNewsHome(Model model){
        List<News> list = service.allNews();
        model.addAttribute("allNews", list);
        List<Infected> infectedNews = infectedServices.allInfected();
        for(Infected infected : infectedNews){
            LocalDate today = LocalDate.now();
            if (infected.getDateTime().isEqual(today)) {
                model.addAttribute("style","visibility: visible; width: 50vw; margin: auto;");
                model.addAttribute("infected", infected.getInfected());
                model.addAttribute("hospitalized", infected.getHospitalized());
                model.addAttribute("tested", infected.getTested());
                model.addAttribute("respirator", infected.getRespirator());
                model.addAttribute("allinf", infected.getAllInfected());
            } else {

            }
        }

        model.addAttribute("news", new News());
        return "newsHome";
    }

    @GetMapping("/news/new")
    public String showNewForm(Model model, HttpServletRequest request) throws UserNotFoundException {
        model.addAttribute("news", new News());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                    User temp = userServices.get(cookie.getValue());
                    if(temp.getRole().equals(Role.valueOf("ADMIN"))){
                        return "newNews";
                    }else{
                        return "redirect:/home";
                    }
                }
            }
        }
        return "redirect:/home";
    }

    @PostMapping("/news/save")
    public String saveNews(News news, RedirectAttributes ra) {
        service.save(news);
        ra.addFlashAttribute("message", "News have been saved");
        return "redirect:/news";
    }

    @GetMapping("/news/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        News news = service.get(id);
        model.addAttribute("news", news);
        return "newNews";
    }

    @GetMapping("/news/delete/{id}")
    public String deleteNews(@PathVariable("id") Integer id, RedirectAttributes ra){
        service.delete(id);
        ra.addFlashAttribute("message", "News have been deleted");
        return "redirect:/news";
    }
}

