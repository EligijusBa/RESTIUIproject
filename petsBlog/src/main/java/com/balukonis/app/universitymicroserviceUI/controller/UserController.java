package com.balukonis.app.universitymicroserviceUI.controller;

import com.balukonis.app.universitymicroserviceUI.model.User;
import com.balukonis.app.universitymicroserviceUI.repository.UserRepository;
import com.balukonis.app.universitymicroserviceUI.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public String create(User user, Model model){
        logger.info("user: " + user);
        logger.info("created user: {}", userService.registerNewUserAccount(user));
        return "redirect:/login";

    }
    @GetMapping("/register")
    public String show(Model model){
        logger.info("invoking new user form");

        model.addAttribute("user", new User());
        return "register";
    }
    @GetMapping("/user/{id}/view")
    public String view(@PathVariable int id, Model model){
        logger.info("User with id: {}",id);

        model.addAttribute("user", userRepository.findById(id).get());

        return "user/view";
    }

    @GetMapping(value = "/meta/user/{id}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User userInJson(@PathVariable int id, Model model){
        return userRepository.findById(id).get();
    }
    @GetMapping(value = "/meta/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<User> metaInJson(@PathVariable int id){
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

}
