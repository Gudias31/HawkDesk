package com.hawkdesk.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hawkdesk.app.models.User;
import com.hawkdesk.app.models.UserRole;
// import com.hawkdesk.app.utils.PasswordUtil;

import static com.hawkdesk.app.database.DatabaseHelper.*;

public class UserDAO {
    // ... (Construtor e getUserByEmail permanecem iguais) ...
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * BUSCA REAL: Busca um usuário pelo email no banco de dados SQLite.
     * Assume que User.setRole() aceita String.
     */
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;

        String[] projection = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD_HASH,
                COLUMN_USER_ROLE
        };

        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        try (Cursor cursor = db.query(
                TABLE_USERS, projection, selection, selectionArgs, null, null, null
        )) {
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)));
                user.setPasswordHash(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD_HASH)));

                String roleString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE));

                user.setRole(roleString);
            }
        } catch (Exception e) {
            Log.e("UserDAO", "Erro ao buscar usuário por email: " + e.getMessage());
        } finally {
            db.close();
        }

        return user;
    }

    /**
     * NOVO MÉTODO: Busca apenas o NOME do usuário pelo ID.
     */
    public String getUserNameById(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = "Cliente Desconhecido";
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { userId };

        try (Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COLUMN_USER_NAME},
                selection, selectionArgs, null, null, null
        )) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
            }
        } catch (Exception e) {
            Log.e("UserDAO", "Erro ao buscar nome do usuário: " + e.getMessage());
        } finally {
            db.close();
        }
        return name;
    }


    /**
     * Insere um novo usuário. Lógica REAL de SQLite.
     */
    public long insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD_HASH, user.getPasswordHash());

        values.put(COLUMN_USER_ROLE, user.getRole());

        long newId = db.insert(TABLE_USERS, null, values);
        db.close();
        return newId;
    }
}
