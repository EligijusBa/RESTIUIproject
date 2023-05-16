package com.balukonis.app.universitymicroserviceUI.controller;

import com.balukonis.app.universitymicroserviceUI.model.Student;
import com.balukonis.app.universitymicroserviceUI.model.Students;
import com.balukonis.app.universitymicroserviceUI.model.University;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/students")
public class RestUIController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;


    @GetMapping("/{id}")
    public String viewStudentInfo(@PathVariable("id") int id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Student student = restTemplate.getForObject("http://localhost:8081/students/" + id, Student.class);
        model.addAttribute("student", student);
        return "ui/studentInfo";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String editStudent(@PathVariable("id") int id, Model model) {
        Student student = restTemplate.getForObject("http://localhost:8081/students/" + id, Student.class);
        University[] universities = restTemplate.getForObject("http://localhost:8081/universities/", University[].class);
        model.addAttribute("student", student);
        model.addAttribute("universities", universities);
        return "ui/editStudent";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStudent(@PathVariable("id") int id, @ModelAttribute("student") Student student) {
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Student> requestEntity = new HttpEntity<>(student, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "http://localhost:8081/students/" + id,
                HttpMethod.PUT,
                requestEntity,
                Void.class
        );

        return "redirect:/ui/students/" + id;
    }

    @GetMapping("")
    public String getAllStudents(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:8081/students/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {
                }
        );
        List<Student> students = response.getBody();
        model.addAttribute("students", students);
        return "/ui/students";
    }

    @GetMapping("/new")
    public String showStudentForm(Model model) {
        University[] universitiesArray = restTemplate.getForObject("http://localhost:8081/universities/", University[].class);
        List<University> universities = Arrays.asList(universitiesArray);
        model.addAttribute("universities", universities);
        model.addAttribute("student", new Student());
        return "/ui/createStudent";
    }

    @PostMapping("/new")
    public String createStudent(@ModelAttribute("student") @Valid Student student,
                                BindingResult result,
                                RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "/ui/createStudent";
        }

        University university = restTemplate.getForObject(
                "http://localhost:8081/universities/{id}", University.class, student.getUniversityId());
        student.setUniversity(university);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Student> request = new HttpEntity<>(student, headers);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:8081/students/new", request, Student.class);
        attributes.addFlashAttribute("message", "Student successfully created!");
        return "redirect:/ui/students";
    }
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes attributes) {
        restTemplate.delete("http://localhost:8081/students/{id}", id);
        attributes.addFlashAttribute("message", "Student successfully deleted!");
        return "redirect:/ui/students";
    }
}
