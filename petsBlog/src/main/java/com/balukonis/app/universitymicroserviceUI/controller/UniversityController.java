package com.balukonis.app.universitymicroserviceUI.controller;

import com.balukonis.app.universitymicroserviceUI.model.Student;
import com.balukonis.app.universitymicroserviceUI.model.University;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/ui/universities")
public class UniversityController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;


    @GetMapping("/{id}")
    public String viewUniversityInfo(@PathVariable("id") int id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        University university = restTemplate.getForObject("http://localhost:8081/universities/" + id, University.class);
        model.addAttribute("University", university);
        return "ui/universityInfo";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String editUniversity(@PathVariable("id") int id, Model model) {
        University university = restTemplate.getForObject("http://localhost:8081/universities/" + id, University.class);
        model.addAttribute("university", university);
        return "ui/editUniversity";
    }

    @PostMapping("/universities/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUniversity(@PathVariable("id") int id, @ModelAttribute("university") University university) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<University> requestEntity = new HttpEntity<>(university, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "http://localhost:8081/universities/" + id,
                HttpMethod.PUT,
                requestEntity,
                Void.class
        );

        return "redirect:/ui/universities/" + id;
    }

    @GetMapping("/")
    public String getAllUniversities(Model model) {
        ResponseEntity<List<University>> response = restTemplate.exchange(
                "http://localhost:8081/universities/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<University>>() {}
        );
        List<University> universities = response.getBody();
        model.addAttribute("universities", universities);
        return "ui/universities";
    }

    @GetMapping("/new")
    public String createUniversity(Model model) {
        University university = new University();
        model.addAttribute("university", university);
        return "ui/createUniversity";
    }

    @PostMapping("/new")
    public String createUniversity(@ModelAttribute("university") @Valid University university,
                                BindingResult result,
                                RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "/ui/createUniversity";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<University> request = new HttpEntity<>(university, headers);

        ResponseEntity<University> response = restTemplate.postForEntity(
                "http://localhost:8081/universities/new", request, University.class);
        attributes.addFlashAttribute("message", "University successfully created!");
        return "redirect:/ui/universities";
    }
    @PostMapping("/delete/{id}")
    public String deleteUniversity(@PathVariable("id") Long id, RedirectAttributes attributes) {
        restTemplate.delete("http://localhost:8081/universities/{id}", id);
        attributes.addFlashAttribute("message", "University successfully deleted!");
        return "redirect:/ui/university";
    }
}

