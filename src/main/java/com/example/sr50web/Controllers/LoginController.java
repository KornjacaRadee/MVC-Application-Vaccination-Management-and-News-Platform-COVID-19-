package com.example.sr50web.Controllers;

import com.example.sr50web.Exceptions.UserNotFoundException;
import com.example.sr50web.Models.Manufacturer;
import com.example.sr50web.Models.Role;
import com.example.sr50web.Models.User;
import com.example.sr50web.Services.UserServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginController {
    public static final String USER_KEY = "USER";

    private String url;
    @Autowired
    private UserServices service;

    @GetMapping("/loginn")
    public String loginpage(Model model){
        model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("pageTitle", "Dodaj novog proizvodjaca");
        return "login";
    }
    @RequestMapping("/")
    public String homepage2(){
        return "redirect:/home";
    }

    @GetMapping("/homepanel")
    public String showPanel(Model model,HttpServletRequest request) throws UserNotFoundException {

        User temp = new User();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getValue().contains("@")) {
                    temp = service.get(cookie.getValue());
                    break;
                }else{

                }
            }
            if(temp.getRole() == null){
                return "redirect:/loginn";
            }
            if(temp.getRole() == Role.ADMIN){
                model.addAttribute("adminstyle","visible");
                model.addAttribute("patientestyle","hidden");
                model.addAttribute("employeestyle","hidden");
            } else if (temp.getRole() == Role.ZAPOSLENI) {
                model.addAttribute("employeestyle","visible");
                model.addAttribute("adminstyle","hidden");
                model.addAttribute("patientestyle","hidden");
            }else{
                model.addAttribute("patientstyle","visible");
                model.addAttribute("adminstyle","hidden");
                model.addAttribute("employeestyle","hidden");

            }
        }


        return "mainloggedpanel";
    }
    @PostMapping("/loginn/save")
    public String Login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request , @CookieValue(value = "sessionID", defaultValue = "") String sessionID , HttpServletResponse response, Model model) throws UserNotFoundException {

        if (service.get(email, password) != null) {
            User temp = service.get(email, password);
            Boolean loggedIn = false;
            // aa
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getValue().contains(temp.getEmail())) {
                        loggedIn = true;
                        break;
                    }else{
                        loggedIn = false;
                    }
                }
            }
            // aa
            if (loggedIn == true) {
                model.addAttribute("error", "Korisnik je već ulogovan.");
                setSessionCookie(response, temp.getEmail());
                return "redirect:/homepanel";
            }if (sessionID.isEmpty()) {
                sessionID = generateSessionID();
                setSessionCookie(response, temp.getEmail());
            }
            setLoggedInUser(sessionID, temp.getEmail());
            return "redirect:/homepanel";
        } else {
            model.addAttribute("error", "Pogresan mail ili lozinka");
            return "index";
        }

    }



    private void setSessionCookie(HttpServletResponse response, String sessionID) {
        Cookie sessionCookie = new Cookie("sessionID", sessionID);
        sessionCookie.setMaxAge(3600);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    private String generateSessionID() {
        return UUID.randomUUID().toString();
    }

    private Map<String, String> loggedInUsers = new HashMap<>();

    private void setLoggedInUser(String sessionID, String email) {
        loggedInUsers.put(sessionID, email);
    }


}
