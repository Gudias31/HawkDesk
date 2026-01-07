package com.hawkdesk.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hawkdesk.app.utils.PasswordUtil;
import com.hawkdesk.app.models.UserRole;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hawkdesk.db";
    // Versão incrementada para 4 para forçar a recriação.
    private static final int DATABASE_VERSION = 4; // <<--- VALOR ALTERADO PARA 4

    // ===================================================================================
    //  ... (Constantes de Coluna e Tabela) ...
    // ===================================================================================
    public static final String TABLE_USERS = "users";
    public static final String TABLE_TICKETS = "tickets";
    public static final String TABLE_COMMENTS = "comments";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD_HASH = "password_hash";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_ROLE = "role";

    public static final String COLUMN_TICKET_ID = "ticket_id";
    public static final String COLUMN_TICKET_USER_ID = "user_id";
    public static final String COLUMN_TICKET_TITLE = "title";
    public static final String COLUMN_TICKET_DESCRIPTION = "description";
    public static final String COLUMN_TICKET_STATUS = "status";
    public static final String COLUMN_TICKET_PRIORITY = "priority";
    public static final String COLUMN_TICKET_CATEGORY = "category";
    public static final String COLUMN_TICKET_CREATED_AT = "created_at";
    public static final String COLUMN_TICKET_UPDATED_AT = "updated_at";

    public static final String COLUMN_COMMENT_ID = "comment_id";
    public static final String COLUMN_COMMENT_TICKET_ID = "ticket_id";
    public static final String COLUMN_COMMENT_USER_ID = "user_id";
    public static final String COLUMN_COMMENT_MESSAGE = "message";
    public static final String COLUMN_COMMENT_TIMESTAMP = "timestamp";

    // ===================================================================================

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Criando tabelas...");

        // Tabela de Usuários
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL," +
                COLUMN_USER_PASSWORD_HASH + " TEXT NOT NULL," +
                COLUMN_USER_NAME + " TEXT," +
                COLUMN_USER_ROLE + " TEXT DEFAULT 'USER'" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Tabela de Tickets (omitido para brevidade, mas está correto)
        String CREATE_TICKETS_TABLE = "CREATE TABLE " + TABLE_TICKETS + " (" +
                COLUMN_TICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TICKET_USER_ID + " INTEGER NOT NULL," +
                COLUMN_TICKET_TITLE + " TEXT NOT NULL," +
                COLUMN_TICKET_DESCRIPTION + " TEXT," +
                COLUMN_TICKET_STATUS + " TEXT NOT NULL," +
                COLUMN_TICKET_PRIORITY + " TEXT NOT NULL," +
                COLUMN_TICKET_CATEGORY + " TEXT," +
                COLUMN_TICKET_CREATED_AT + " INTEGER," +
                COLUMN_TICKET_UPDATED_AT + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_TICKET_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(CREATE_TICKETS_TABLE);

        // Tabela de Comentários (omitido para brevidade, mas está correto)
        String CREATE_COMMENTS_TABLE = "CREATE TABLE " + TABLE_COMMENTS + " (" +
                COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_COMMENT_TICKET_ID + " INTEGER NOT NULL," +
                COLUMN_COMMENT_USER_ID + " INTEGER NOT NULL," +
                COLUMN_COMMENT_MESSAGE + " TEXT NOT NULL," +
                COLUMN_COMMENT_TIMESTAMP + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_COMMENT_TICKET_ID + ") REFERENCES " + TABLE_TICKETS + "(" + COLUMN_TICKET_ID + ")," +
                "FOREIGN KEY (" + COLUMN_COMMENT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(CREATE_COMMENTS_TABLE);

        // 🏆 INSERÇÃO DO USUÁRIO ADMINISTRADOR PADRÃO
        insertDefaultAdmin(db);
    }

    private void insertDefaultAdmin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // A senha padrão para o ADM será "admin123"
        String defaultPassword = "admin123";
        // ATENÇÃO: Se o PasswordUtil.hashPassword não estiver implementado, ele pode falhar aqui.
        String adminPasswordHash = PasswordUtil.hashPassword(defaultPassword);

        values.put(COLUMN_USER_EMAIL, "admin@hawkdesk.com");
        values.put(COLUMN_USER_PASSWORD_HASH, adminPasswordHash);
        values.put(COLUMN_USER_NAME, "Administrador");
        values.put(COLUMN_USER_ROLE, UserRole.ADMIN.name()); // Define como ADMIN

        long id = db.insert(TABLE_USERS, null, values);
        if (id > 0) {
            Log.i("DatabaseHelper", "Usuário Administrador padrão inserido com sucesso.");
        } else {
            Log.e("DatabaseHelper", "Falha ao inserir usuário Administrador padrão.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Atualizando tabelas (DROP/RECREATE)...");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}