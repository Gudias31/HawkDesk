package com.hawkdesk.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hawkdesk.app.R;
import com.hawkdesk.app.database.UserDAO;
import com.hawkdesk.app.models.User;
import com.hawkdesk.app.utils.PasswordUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton; // Variável para o botão de Registro
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar DAO
        userDAO = new UserDAO(this);

        // Inicializar Views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton); // **CORREÇÃO: Inicializa o botão de registro**

        // Listener do botão de Login
        loginButton.setOnClickListener(v -> attemptLogin());

        // **CORREÇÃO: Listener do botão de Registro**
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Preencha todos os campos.");
            return;
        }

        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            // Verifica a senha
            if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                saveUserSession(user);
                navigateToMain();
            } else {
                showError("Senha incorreta.");
            }
        } else {
            showError("Usuário não encontrado.");
        }
    }

    private void saveUserSession(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("HawkDeskPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("userId", user.getId());
        editor.putString("userName", user.getName());
        editor.putString("userEmail", user.getEmail());
        editor.putString("userRole", user.getRole().toString());
        editor.apply();
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void navigateToRegister() {
        // **ATENÇÃO: Este método exige a criação da RegisterActivity**
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showError(String message) {
        Toast.makeText(this, "Erro: " + message, Toast.LENGTH_LONG).show();
    }
}