package com.hawkdesk.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hawkdesk.app.models.Ticket;
import com.hawkdesk.app.models.Ticket.TicketPriority;
import com.hawkdesk.app.models.Ticket.TicketStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hawkdesk.app.database.DatabaseHelper.*;

public class TicketDAO {

    private static final String TAG = "TicketDAO";
    private DatabaseHelper dbHelper;
    private UserDAO userDAO; // 🏆 NOVO: Para buscar o nome do cliente

    public TicketDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.userDAO = new UserDAO(context); // Inicializa o UserDAO
    }

    /**
     * Insere um novo ticket. (LÓGICA REAL)
     */
    public long insertTicket(Ticket ticket) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        long newId = -1;

        try {
            long createdAtTimestamp = ticket.getCreatedAt().getTime();
            long updatedAtTimestamp = ticket.getUpdatedAt().getTime();

            int userId = 1;
            try {
                userId = Integer.parseInt(ticket.getUserId());
            } catch (NumberFormatException e) {
                Log.e(TAG, "ID do utilizador inválido ao inserir ticket: " + ticket.getUserId());
            }

            // 1. DADOS OBRIGATÓRIOS (NOT NULL)
            values.put(COLUMN_TICKET_USER_ID, userId);
            values.put(COLUMN_TICKET_TITLE, ticket.getTitle());
            values.put(COLUMN_TICKET_STATUS, ticket.getStatus().name());
            values.put(COLUMN_TICKET_PRIORITY, ticket.getPriority().name());

            // 2. DADOS OPCIONAIS/AUTOMÁTICOS
            values.put(COLUMN_TICKET_DESCRIPTION, ticket.getDescription());
            values.put(COLUMN_TICKET_CATEGORY, ticket.getCategory());
            values.put(COLUMN_TICKET_CREATED_AT, createdAtTimestamp);
            values.put(COLUMN_TICKET_UPDATED_AT, updatedAtTimestamp);

            newId = db.insert(TABLE_TICKETS, null, values);

        } catch (Exception e) {
            Log.e(TAG, "ERRO FATAL AO INSERIR TICKET: " + e.getMessage(), e);
            newId = -1;
        } finally {
            db.close();
        }

        return newId;
    }

    /**
     * LÓGICA DE LEITURA: Converte o Cursor do SQLite em um objeto Ticket.
     */
    private Ticket cursorToTicket(Cursor cursor) {
        String id = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TICKET_ID)));
        String userId = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TICKET_USER_ID)));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TICKET_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TICKET_DESCRIPTION));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TICKET_CATEGORY));
        String statusStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TICKET_STATUS));
        String priorityStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TICKET_PRIORITY));
        long createdAtTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TICKET_CREATED_AT));

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setUserId(userId);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCategory(category);

        // Conversão de String para Enum (Obrigatório)
        ticket.setStatus(TicketStatus.valueOf(statusStr));
        ticket.setPriority(TicketPriority.valueOf(priorityStr));

        ticket.setCreatedAt(new Date(createdAtTimestamp));

        // 🏆 CORREÇÃO: Busca o nome do utilizador e define o título para (Nome - Assunto)
        String userName = userDAO.getUserNameById(userId);

        // Atualiza o título para o formato "Nome do Cliente: Assunto"
        ticket.setTitle(userName + ": " + title);

        return ticket;
    }


    // ===================================================================================
    // MÉTODOS DE BUSCA REAL (SUBSTITUINDO OS MOCKS)
    // ===================================================================================

    /**
     * Busca um Ticket por ID. (LÓGICA REAL)
     */
    public Ticket getTicketById(String ticketId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Ticket ticket = null;
        String selection = COLUMN_TICKET_ID + " = ?";
        String[] selectionArgs = { ticketId };

        try (Cursor cursor = db.query(TABLE_TICKETS, null, selection, selectionArgs, null, null, null)) {
            if (cursor.moveToFirst()) {
                ticket = cursorToTicket(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar ticket por ID: " + e.getMessage());
        } finally {
            db.close();
        }
        return ticket;
    }

    /**
     * Busca tickets por ID de Utilizador. (LÓGICA REAL)
     */
    public List<Ticket> getTicketsByUserId(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = COLUMN_TICKET_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        try (Cursor cursor = db.query(TABLE_TICKETS, null, selection, selectionArgs, null, null, COLUMN_TICKET_CREATED_AT + " DESC")) {
            while (cursor.moveToNext()) {
                tickets.add(cursorToTicket(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar tickets por ID de Utilizador: " + e.getMessage());
        } finally {
            db.close();
        }
        return tickets;
    }

    /**
     * Busca todos os tickets (Admin). (LÓGICA REAL)
     */
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(TABLE_TICKETS, null, null, null, null, null, COLUMN_TICKET_CREATED_AT + " DESC")) {
            while (cursor.moveToNext()) {
                tickets.add(cursorToTicket(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar todos os tickets: " + e.getMessage());
        } finally {
            db.close();
        }
        return tickets;
    }

    // O updateTicket(Ticket ticket) original permanece como um bom MOCK funcional
    public long updateTicket(Ticket ticket) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowsAffected = 0;

        try {
            values.put(COLUMN_TICKET_STATUS, ticket.getStatus().name());
            values.put(COLUMN_TICKET_PRIORITY, ticket.getPriority().name());
            values.put(COLUMN_TICKET_UPDATED_AT, new Date().getTime());

            String selection = COLUMN_TICKET_ID + " = ?";
            String[] selectionArgs = { ticket.getId() };

            rowsAffected = db.update(TABLE_TICKETS, values, selection, selectionArgs);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar ticket: " + e.getMessage(), e);
        } finally {
            db.close();
        }
        return rowsAffected;
    }
}