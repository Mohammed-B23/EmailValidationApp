package org.mb6.emailverification.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;

import javax.naming.directory.InitialDirContext;
import javax.naming.directory.DirContext;
import javax.naming.directory.Attributes;
import javax.naming.NamingException;

@Service
public class EmailValidationService {

    private RestTemplate restTemplate = new RestTemplate();
    private final String API_KEY = "706e2d6757f24f05b39e271125706ae2";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    // Méthode pour valider l'email
//    public boolean validateEmail(String email) {
//        return isSyntaxValid(email) && hasValidDomain(email);
//    }// Remplace par ta clé API

    public String validateEmail(String email) {
        // Step 1: Basic Regex Validation
        if (!isSyntaxValid(email)) {
            return "Invalid Format";
        }

        // Step 2: Domain Check
        String domain = email.substring(email.indexOf("@") + 1);
        if (!isDomainValid(domain)) {
            return "Invalid Domain";
        }

        // Step 3: SMTP Verification using an external API like Debounce
//        boolean isValidSMTP = verifyWithExternalAPI(email);

        boolean isValidSMTP = verifyWithExternalAPI(email);
        return isValidSMTP ? "Valid Email" : "Invalid - Unreachable SMTP";
    }

//    private boolean isValidFormat(String email) {
//        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
//        return email.matches(emailRegex);
//    }

    private boolean isDomainValid(String domain) {
        try {
            InetAddress.getByName(domain);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private boolean verifyWithExternalAPI(String email) {
        String url = "https://emailvalidation.abstractapi.com/v1/?api_key=" + API_KEY + "&email=" + email;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());

            // Extract the "deliverability" status
            String deliverability = jsonResponse.getString("deliverability");

            // The "DELIVERABLE" status indicates that the email is valid with Abstract
            return "DELIVERABLE".equals(deliverability);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // If an error occurs, consider the email invalid
        }
    }



    // Vérifie la syntaxe de l'email
    private boolean isSyntaxValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Vérifie que le domaine de l'email a un enregistrement MX
    private boolean hasValidDomain(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        try {
            DirContext ctx = new InitialDirContext();
            Attributes attrs = ctx.getAttributes("dns:/" + domain, new String[] {"MX"});
            System.out.println("{hasValidDomain} ==> "+attrs);
            return attrs != null && attrs.size() > 0;
        } catch (NamingException e) {
            return false;
        }
    }

}

