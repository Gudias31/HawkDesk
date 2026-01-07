package com.hawkdesk.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hawkdesk.app.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView userRoleTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Meu Perfil");
        }

        // 1. Inicializar Views
        userNameTextView = findViewById(R.id.userNameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userRoleTextView = findViewById(R.id.userRoleTextView);
        logoutButton = findViewById(R.id.logoutButton);

        // 2. Carregar Dados do Usuário (SharedPreferences)
        SharedPreferences sharedPreferences = getSharedPreferences("HawkDeskPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Não Logado");
        String userEmail = sharedPreferences.getString("userEmail", "Desconhecido");
        String userRole = sharedPreferences.getString("userRole", "CLIENTE");

        // 3. Exibir Dados
        userNameTextView.setText(getString(R.string.profile_name, userName));
        userEmailTextView.setText(getString(R.string.profile_email, userEmail));
        userRoleTextView.setText(getString(R.string.profile_role, userRole));


        // 4. Configurar Logout
        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        // Limpa a sessão
        SharedPreferences sharedPreferences = getSharedPreferences("HawkDeskPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Volta para a tela de Login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(this, "Logout efetuado com sucesso.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
