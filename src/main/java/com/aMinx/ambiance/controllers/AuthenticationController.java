package com.aMinx.ambiance.controllers;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.data.UserDAO;
import com.aMinx.ambiance.models.User;
import com.aMinx.ambiance.models.dto.LoginFormDTO;
import com.aMinx.ambiance.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AuthenticationController {

    @Autowired
    UtilitiesService utilitiesService;

    @Autowired
    UserDAO userDAO;

    @GetMapping("/register")
    public String createRegistration(Model model) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                      Errors errors, HttpServletRequest request,
                                      Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }

        User existingUser = userDAO.findByUsername(registerFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userDAO.save(newUser);
        utilitiesService.setUserInSession(request.getSession(), newUser);
        return "redirect:/user/dashboard";
    }


    @GetMapping("/login")
    public String createLogin(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                               Errors errors, HttpServletRequest request,
                               Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }
        User user = userDAO.findByUsername(loginFormDTO.getUsername());
        if (user == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }
        String password = loginFormDTO.getPassword();
        if (!user.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }
        utilitiesService.setUserInSession(request.getSession(), user);
        return "redirect:/user/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
