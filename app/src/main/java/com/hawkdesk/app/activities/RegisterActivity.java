package com.hawkdesk.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hawkdesk.app.R;
import com.hawkdesk.app.database.UserDAO;
import com.hawkdesk.app.models.User;
import com.hawkdesk.app.models.UserRole;
import com.hawkdesk.app.utils.PasswordUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText; // Campo para o número de telefone (WhatsApp)
    private EditText passwordEditText;
    private Button registerButton;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDAO = new UserDAO(this);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText); // Inicializa o novo campo
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim(); // Obtém o número
        String password = passwordEditText.getText().toString().trim();

        // 1. Validação: Todos os campos são obrigatórios
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Verificar se o usuário já existe
        if (userDAO.getUserByEmail(email) != null) {
            Toast.makeText(this, "Usuário já existe. Tente fazer login.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Criar e preencher o objeto User
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setWhatsappNumber(phone); // Salva o número
        newUser.setRole(UserRole.USER.name());

        // 4. Hash da Senha e Inserção no DB
        String hashedPassword = PasswordUtil.hashPassword(password);
        newUser.setPasswordHash(hashedPassword);

        long userId = userDAO.insertUser(newUser);

        if (userId > 0) {
            Toast.makeText(this, "Registro bem-sucedido! Faça login.", Toast.LENGTH_LONG).show();
            navigateToLogin();
        } else {
            Toast.makeText(this, "Erro ao registrar usuário. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}