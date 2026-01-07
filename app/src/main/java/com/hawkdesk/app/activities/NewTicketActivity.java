package com.hawkdesk.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log; // 🏆 IMPORT CORRIGIDO/ADICIONADO AQUI!

import com.hawkdesk.app.R;
import com.hawkdesk.app.database.TicketDAO;
import com.hawkdesk.app.models.Ticket;
import com.hawkdesk.app.models.Ticket.TicketPriority;
import com.hawkdesk.app.models.Ticket.TicketStatus;

import java.util.Date;

public class NewTicketActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Spinner categorySpinner;
    private Spinner prioritySpinner;
    private Button createTicketButton;

    private TicketDAO ticketDAO;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Garante que o layout é carregado
        try {
            setContentView(R.layout.activity_new_ticket);
        } catch (Exception e) {
            Toast.makeText(this, "Erro fatal ao carregar layout: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.new_ticket);
        }

        // Get current user ID
        SharedPreferences sharedPreferences = getSharedPreferences("HawkDeskPrefs", MODE_PRIVATE);
        int userIdInt = sharedPreferences.getInt("userId", -1);
        currentUserId = String.valueOf(userIdInt);

        // Initialize DAO
        ticketDAO = new TicketDAO(this);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        createTicketButton = findViewById(R.id.createTicketButton);

        // 2. Setup Category Spinner (VERIFICAÇÃO DE STRINGS)
        try {
            String[] categories = {
                    getString(R.string.category_hardware),
                    getString(R.string.category_software),
                    getString(R.string.category_network),
                    getString(R.string.category_access),
                    getString(R.string.category_other)
            };
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, categories
            );
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar Categorias. Verifique strings.xml", Toast.LENGTH_LONG).show();
            Log.e("NewTicketActivity", "Spinner Category Error: ", e);
            // Continua a execução, mas o spinner pode estar vazio
        }


        // 3. Setup Priority Spinner (VERIFICAÇÃO DE STRINGS)
        try {
            String[] priorities = {
                    getString(R.string.priority_low),
                    getString(R.string.priority_medium),
                    getString(R.string.priority_high),
                    getString(R.string.priority_urgent)
            };
            ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, priorities
            );
            priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            prioritySpinner.setAdapter(priorityAdapter);
            prioritySpinner.setSelection(1); // Default to Medium
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar Prioridades. Verifique strings.xml", Toast.LENGTH_LONG).show();
            Log.e("NewTicketActivity", "Spinner Priority Error: ", e);
        }


        // Set button click listener
        createTicketButton.setOnClickListener(v -> createTicket());
    }

    private void createTicket() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Trata erro se o spinner não foi inicializado corretamente
        if (categorySpinner.getSelectedItem() == null || prioritySpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Erro: Não foi possível ler as opções. Tente reiniciar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = categorySpinner.getSelectedItem().toString();
        int priorityPosition = prioritySpinner.getSelectedItemPosition();

        // Validation
        if (currentUserId.equals("-1")) {
            Toast.makeText(this, "Erro: Não é possível criar ticket. Usuário não logado.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.isEmpty()) {
            titleEditText.setError("Título é obrigatório");
            titleEditText.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            descriptionEditText.setError("Descrição é obrigatória");
            descriptionEditText.requestFocus();
            return;
        }

        // Map priority position to enum (CORRIGIDO: Apenas 3 ENUMS no Ticket.java)
        TicketPriority priority;
        switch (priorityPosition) {
            case 0:
                priority = TicketPriority.LOW;
                break;
            case 1:
                priority = TicketPriority.MEDIUM;
                break;
            case 2:
                priority = TicketPriority.HIGH;
                break;
            case 3:
                // Se o usuário selecionou "URGENT", mapeia para HIGH
                priority = TicketPriority.HIGH;
                break;
            default:
                priority = TicketPriority.MEDIUM;
        }

        // Cria ticket usando setters
        Ticket ticket = new Ticket();
        Date now = new Date();

        ticket.setUserId(currentUserId);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority);
        ticket.setStatus(TicketStatus.OPEN); // Status inicial
        ticket.setCategory(category);
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);


        long ticketId = ticketDAO.insertTicket(ticket);

        if (ticketId > 0) {
            Toast.makeText(this, R.string.ticket_created, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.ticket_error, Toast.LENGTH_SHORT).show();
        }
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