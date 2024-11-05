package org.mb6.emailverification.controller;

import org.mb6.emailverification.models.EmailValidationResult;
import org.mb6.emailverification.services.EmailValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins = "*") // Autorise CORS uniquement pour ce contrôleur
public class EmailController {

    @Autowired
    private EmailValidationService emailValidationService;

    @PostMapping("/validate")
    public ResponseEntity<List<EmailValidationResult>> validateEmails(@RequestBody List<String> emails) {
        List<EmailValidationResult> results = new ArrayList<>();
        for (String email : emails) {
            String status = emailValidationService.validateEmail(email);
            results.add(new EmailValidationResult(email, status));
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping
    public String hello(){
        return "<h1> Hello World</h1>";
    }
}
