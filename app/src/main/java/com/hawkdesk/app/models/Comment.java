package com.hawkdesk.app.models;

import java.util.Date;

public class Comment {
    private String id;
    private String ticketId;
    private String userId;
    private String message; // O campo da mensagem
    private Date timestamp; // O campo do timestamp

    // Construtor vazio
    public Comment() {}

    // Getters
    public String getId() { return id; }
    public String getTicketId() { return ticketId; }
    public String getUserId() { return userId; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }

    // Setters (Corrigidos para resolver os erros)
    public void setId(String id) { this.id = id; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setMessage(String message) { this.message = message; } // 🏆 CORRIGIDO
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; } // 🏆 CORRIGIDO
}