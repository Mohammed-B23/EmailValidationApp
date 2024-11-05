package org.mb6.emailverification.models;

public class EmailValidationResult {

    private String email;
    private String status;

    public EmailValidationResult(String email, String status) {
        this.email = email;
        this.status = status;
    }

    // Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
