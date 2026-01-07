package com.hawkdesk.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button; // NOVO IMPORT
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hawkdesk.app.R;
import com.hawkdesk.app.adapters.TicketAdapter;
import com.hawkdesk.app.database.TicketDAO;
import com.hawkdesk.app.models.Ticket;
import com.hawkdesk.app.utils.DateUtil;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TicketAdapter.OnTicketClickListener {

    private static final String TAG = "MainActivity";

    private TicketDAO ticketDAO;
    private int currentUserId = -1;
    private String currentUserRole = "USER"; // Estado para saber quem está logado
    private TicketAdapter adapter;
    private RecyclerView ticketsRecyclerView;
    private FloatingActionButton fabNewTicket;

    // Novas variáveis para os botões de filtro
    private Button myTicketsButton;
    private Button allTicketsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CORREÇÃO: LIGAR A TOOLBAR CUSTOMIZADA
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        ticketDAO = new TicketDAO(this);

        // 1. Configurar RecyclerView e Adapter
        ticketsRecyclerView = findViewById(R.id.ticketsRecyclerView);
        ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketAdapter(this);
        ticketsRecyclerView.setAdapter(adapter);

        // 2. Inicializar Botões de Filtro
        myTicketsButton = findViewById(R.id.myTicketsButton);
        allTicketsButton = findViewById(R.id.allTicketsButton);

        // 3. Inicialização e Listener do FAB
        fabNewTicket = findViewById(R.id.fabNewTicket);
        fabNewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNewTicket();
            }
        });

        // Listener (Exemplo de clique no filtro)
        myTicketsButton.setOnClickListener(v -> {
            // Lógica para alternar o filtro, se necessário. Por enquanto, só recarrega
            loadTickets();
        });

        // O restante da carga será feito no onResume
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCurrentUser(); // Lemos o ID e o Role
        setupFilterButtons(); // Adaptamos a UI
        loadTickets(); // Carregamos a lista
    }

    private void loadCurrentUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("HawkDeskPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userId", -1);
        currentUserRole = sharedPreferences.getString("userRole", "USER"); // Lê a função
    }

    /**
     * Adapta a visibilidade e o texto dos botões de filtro
     * com base no perfil do usuário logado (ADM ou USER).
     */
    private void setupFilterButtons() {
        boolean isAdmin = currentUserRole.equals("ADMIN");

        if (isAdmin) {
            // ADM: O botão principal vira "TODOS OS TICKETS"
            myTicketsButton.setText(R.string.all_tickets); // Altera o texto do botão principal
            allTicketsButton.setVisibility(View.GONE); // Mantém o outro botão escondido, usando apenas um
        } else {
            // USUÁRIO: O botão principal é "MEUS TICKETS"
            myTicketsButton.setText(R.string.my_tickets);
            allTicketsButton.setVisibility(View.GONE); // O usuário comum não precisa do filtro "Todos"
        }
    }

    private void loadTickets() {
        List<Ticket> tickets;
        boolean isAdmin = currentUserRole.equals("ADMIN");

        if (currentUserId > 0) {
            if (isAdmin) {
                // ADM: Carrega todos os tickets
                tickets = ticketDAO.getAllTickets();
            } else {
                // USUÁRIO: Carrega apenas os tickets do usuário logado
                tickets = ticketDAO.getTicketsByUserId(currentUserId);
            }
        } else {
            // Não logado
            tickets = new ArrayList<>();
        }

        if (tickets != null && !tickets.isEmpty()) {
            Log.d(TAG, "Tickets carregados com sucesso: " + tickets.size());
            adapter.setTickets(tickets);
        } else {
            Log.d(TAG, "Nenhum ticket encontrado.");
            Toast.makeText(this, "Nenhum chamado encontrado.", Toast.LENGTH_SHORT).show();
            adapter.setTickets(new ArrayList<>());
        }
    }

    private void navigateToNewTicket() {
        Intent intent = new Intent(this, NewTicketActivity.class);
        startActivity(intent);
    }

    // =========================================================
    // LÓGICA DO MENU (PERFIL E LOGOUT) E onTicketClick...
    // =========================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("HawkDeskPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(this, "Logout efetuado.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onTicketClick(Ticket ticket) {
        if (ticket == null || ticket.getId() == null) {
            Log.e(TAG, "Ticket nulo ou ID ausente.");
            Toast.makeText(this, "Erro: Ticket inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        String ticketId = String.valueOf(ticket.getId());

        if (ticketId != null && !ticketId.isEmpty() && !ticketId.equals("-1")) {
            Log.d(TAG, "Ticket clicado. Enviando ID: " + ticketId);

            Intent intent = new Intent(this, TicketDetailActivity.class);
            intent.putExtra("ticketId", ticketId);
            startActivity(intent);
        } else {
            Log.d(TAG, "ID do Ticket inválido para navegação: " + ticketId);
            Toast.makeText(this, "Erro ao carregar detalhes do chamado.", Toast.LENGTH_SHORT).show();
        }
    }
}