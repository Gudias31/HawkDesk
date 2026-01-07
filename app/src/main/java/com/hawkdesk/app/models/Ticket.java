package com.hawkdesk.app.models;

import java.util.Date;
import androidx.annotation.StringRes; // ⬅️ IMPORT NECESSÁRIO
import com.hawkdesk.app.R; // ⬅️ IMPORT NECESSÁRIO

public class Ticket {

    // ===================================
    // ENUMS (CORRIGIDO: Usa R.string para a tradução)
    // ===================================

    public enum TicketStatus {
        OPEN(R.string.status_open),
        IN_PROGRESS(R.string.status_in_progress),
        WAITING(R.string.status_waiting),
        RESOLVED(R.string.status_resolved), // Adicionei o RESOLVED, se estiver faltando
        CLOSED(R.string.status_closed);

        private final int stringResId;

        TicketStatus(@StringRes int stringResId) {
            this.stringResId = stringResId;
        }

        public int getStringResId() { // Novo método para obter o ID do recurso
            return stringResId;
        }
    }

    public enum TicketPriority {
        LOW(R.string.priority_low),
        MEDIUM(R.string.priority_medium),
        HIGH(R.string.priority_high),
        URGENT(R.string.priority_urgent);

        private final int stringResId;

        TicketPriority(@StringRes int stringResId) {
            this.stringResId = stringResId;
        }

        public int getStringResId() { // Novo método para obter o ID do recurso
            return stringResId;
        }
    }

    // ===================================
    // FIELDS
    // ===================================
    private String id;
    private String userId;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private String category;
    private Date createdAt;
    private Date updatedAt;

    // ===================================
    // CONSTRUCTORS
    // ... (Mantive seus construtores como estavam)
    // ===================================

    public Ticket() {
    }

    public Ticket(String id, String title, String description, TicketStatus status, TicketPriority priority, String category, Date createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public Ticket(String id, String userId, String title, String description, TicketStatus status, TicketPriority priority, String category, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // ===================================
    // GETTERS AND SETTERS
    // ===================================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }

    public TicketPriority getPriority() { return priority; }
    public void setPriority(TicketPriority priority) { this.priority = priority; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}