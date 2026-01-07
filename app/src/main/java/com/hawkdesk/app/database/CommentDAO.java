package com.hawkdesk.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hawkdesk.app.models.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Importa todas as constantes do Helper
import static com.hawkdesk.app.database.DatabaseHelper.*;

public class CommentDAO {

    private static final String TAG = "CommentDAO";
    private DatabaseHelper dbHelper;

    public CommentDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Insere um novo comentário no banco de dados (LÓGICA REAL).
     */
    public long insertComment(Comment comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        long newId = -1;

        try {
            // Conversão de String (do Comment) para Int (para o DB)
            int ticketId = Integer.parseInt(comment.getTicketId());
            int userId = Integer.parseInt(comment.getUserId());

            values.put(COLUMN_COMMENT_TICKET_ID, ticketId);
            values.put(COLUMN_COMMENT_USER_ID, userId);
            values.put(COLUMN_COMMENT_MESSAGE, comment.getMessage()); // O campo correto
            values.put(COLUMN_COMMENT_TIMESTAMP, comment.getTimestamp().getTime());

            newId = db.insert(TABLE_COMMENTS, null, values);

        } catch (Exception e) {
            Log.e(TAG, "ERRO AO INSERIR COMENTÁRIO: " + e.getMessage(), e);
            newId = -1;
        } finally {
            db.close();
        }

        return newId;
    }

    /**
     * Converte um registro do Cursor em um objeto Comment.
     */
    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();

        comment.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMMENT_ID))));
        comment.setTicketId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMMENT_TICKET_ID))));
        comment.setUserId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMMENT_USER_ID))));
        comment.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT_MESSAGE)));
        comment.setTimestamp(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COMMENT_TIMESTAMP))));

        return comment;
    }

    /**
     * Busca todos os comentários para um ticket específico (LÓGICA REAL).
     * @param ticketId O ID do ticket (int).
     */
    public List<Comment> getCommentsByTicketId(int ticketId) { // 🏆 CORREÇÃO DE ASSINATURA: Agora espera INT
        List<Comment> comments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_COMMENT_TICKET_ID + " = ?";
        String[] selectionArgs = { String.valueOf(ticketId) };

        try (Cursor cursor = db.query(TABLE_COMMENTS, null, selection, selectionArgs, null, null, COLUMN_COMMENT_TIMESTAMP + " ASC")) {
            while (cursor.moveToNext()) {
                comments.add(cursorToComment(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar comentários: " + e.getMessage());
        } finally {
            db.close();
        }

        return comments;
    }
}