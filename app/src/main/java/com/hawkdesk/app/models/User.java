package com.hawkdesk.app.models;

// ... (imports existentes)

public class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private String role; // "ADMIN" ou "USER" (String)
    private String whatsappNumber; //  NOVO CAMPO

    // Construtor Vazio
    public User() {}

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getWhatsappNumber() { return whatsappNumber; } //  NOVO GETTER

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; } // 🏆 NOVO SETTER
}
