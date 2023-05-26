package com.example.sr50web.Controllers;

import com.example.sr50web.Exceptions.UserNotFoundException;
import com.example.sr50web.Models.Manufacturer;
import com.example.sr50web.Models.User;
import com.example.sr50web.Services.UserServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String showLogin(Model model){
        model.addAttribute("manufacturer", new Manufacturer());
        model.addAttribute("pageTitle", "Dodaj novog proizvodjaca");
        return "index";
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
                return "index";
            }if (sessionID.isEmpty()) {
                sessionID = generateSessionID();
                setSessionCookie(response, temp.getEmail());
            }
            setLoggedInUser(sessionID, temp.getEmail());
            return "redirect:/user";
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
