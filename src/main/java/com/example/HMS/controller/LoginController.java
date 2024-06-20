package com.example.HMS.controller;


import com.example.HMS.domain.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    @Autowired
    private WebController webController;

    public LoginController(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        try {
            Resource resource = resourceLoader.getResource("classpath:users.json");
            List<User> users = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<User>>() {});
            Optional<User> matchedUser = users.stream()
                    .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                    .findFirst();
            if (matchedUser.isPresent()) {
                User user = matchedUser.get();
                if (user.getRole().equals("doctor")) {
                    return "redirect:/doctors/";
                } else {
                  return webController.getPatients(email, model);
                }
            } else {
                return "login";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
