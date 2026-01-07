package com.hawkdesk.app.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hawkdesk.app.R;
import com.hawkdesk.app.adapters.CommentAdapter;
import com.hawkdesk.app.database.TicketDAO;
import com.hawkdesk.app.database.CommentDAO;
import com.hawkdesk.app.models.Comment;
import com.hawkdesk.app.models.Ticket;
import com.hawkdesk.app.models.Ticket.TicketStatus;
import com.hawkdesk.app.models.Ticket.TicketPriority;
import com.hawkdesk.app.utils.DateUtil;

import java.util.Date;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TicketDetailActivity extends AppCompatActivity {

    // DECLARAÇÃO COMPLETA DAS VARIÁVEIS DA CLASSE (Resolve os erros de 'cannot find symbol')
    private TextView ticketTitleTextView;
    private TextView statusTextView;
    private TextView priorityTextView;
    private TextView categoryTextView;
    private TextView descriptionTextView;
    private TextView userNameTextView;
    private TextView createdAtTextView;
    private Button updateStatusButton;
    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Button addCommentButton;

    private CommentAdapter commentAdapter;
    private TicketDAO ticketDAO;
    private CommentDAO commentDAO;
    private Ticket currentTicket;
    private String currentUserId;
    private String currentUserName;
    private boolean isAdmin;

    private static final String WHATSAPP_NUMBER = "5511999999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        // Setup ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.ticket_details);
        }

        // Get user info
        SharedPreferences sharedPreferences = getSharedPreferences("HawkDeskPrefs", MODE_PRIVATE);
        int userIdInt = sharedPreferences.getInt("userId", -1);
        currentUserId = String.valueOf(userIdInt);
        currentUserName = sharedPreferences.getString("userName", "Usuário");
        String userRole = sharedPreferences.getString("userRole", "USER");
        isAdmin = userRole.equals("ADMIN");

        // Initialize DAOs
        ticketDAO = new TicketDAO(this);
        commentDAO = new CommentDAO(this);

        // INICIALIZAÇÃO COMPLETA DAS VIEWS (Ligando ao XML)
        ticketTitleTextView = findViewById(R.id.ticketTitleTextView);
        statusTextView = findViewById(R.id.statusTextView);
        priorityTextView = findViewById(R.id.priorityTextView);
        categoryTextView = findViewById(R.id.categoryTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        createdAtTextView = findViewById(R.id.createdAtTextView);
        updateStatusButton = findViewById(R.id.updateStatusButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        addCommentButton = findViewById(R.id.addCommentButton);

        // Setup RecyclerView for comments
        commentAdapter = new CommentAdapter(this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Get ticket ID from intent
        String ticketIdStr = getIntent().getStringExtra("ticketId");

        if (ticketIdStr == null || ticketIdStr.isEmpty() || ticketIdStr.equals("-1")) {
            Toast.makeText(this, "Erro ao carregar ticket: ID inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Converte para int
        int ticketId = Integer.parseInt(ticketIdStr);

        // Load ticket details
        loadTicketDetails(ticketId);

        // Setup button listeners
        updateStatusButton.setOnClickListener(v -> showUpdateStatusDialog());
        addCommentButton.setOnClickListener(v -> addComment());

        // Show update status button if admin
        if (isAdmin) {
            updateStatusButton.setVisibility(View.VISIBLE);
        } else {
            updateStatusButton.setVisibility(View.GONE);
        }
    }

    private void loadTicketDetails(int ticketId) {
        String ticketIdString = String.valueOf(ticketId);
        currentTicket = ticketDAO.getTicketById(ticketIdString);

        if (currentTicket == null) {
            Toast.makeText(this, "Ticket não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display ticket information
        ticketTitleTextView.setText(currentTicket.getTitle());
        statusTextView.setText(currentTicket.getStatus().toString());
        priorityTextView.setText(currentTicket.getPriority().toString());
        categoryTextView.setText("Categoria: " + currentTicket.getCategory());
        descriptionTextView.setText(currentTicket.getDescription());
        userNameTextView.setText("Criado por ID: " + currentTicket.getUserId());
        createdAtTextView.setText("Criado em: " + DateUtil.formatDateTime(currentTicket.getCreatedAt()));

        // Load comments
        loadComments(currentTicket.getId());
    }

    private void loadComments(String ticketId) {
        // Implementação real da carga de comentários
        // Correção de tipo: Envia INT para o DAO, conforme o CommentDAO.java
        List<Comment> comments = commentDAO.getCommentsByTicketId(Integer.parseInt(ticketId));
        commentAdapter.setComments(comments);
        commentsRecyclerView.scrollToPosition(commentAdapter.getItemCount() - 1);
    }

    private void addComment() {
        String message = commentEditText.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Comentário não pode ser vazio.", Toast.LENGTH_SHORT).show();
            return;
        }

        Comment newComment = new Comment();
        newComment.setTicketId(currentTicket.getId());
        newComment.setUserId(currentUserId);
        newComment.setMessage(currentUserName + ": " + message);
        newComment.setTimestamp(new Date());

        long commentId = commentDAO.insertComment(newComment);
        if (commentId > 0) {
            commentEditText.setText("");
            Toast.makeText(this, "Comentário adicionado.", Toast.LENGTH_SHORT).show();
            loadComments(currentTicket.getId()); // Recarrega a lista
        } else {
            Toast.makeText(this, "Erro ao adicionar comentário.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateStatusDialog() {
        // Lógica de atualização de status (visível apenas para ADM)
        String[] statuses = {"OPEN", "IN_PROGRESS", "WAITING", "CLOSED"};
        new AlertDialog.Builder(this)
                .setTitle("Atualizar Status do Chamado")
                .setItems(statuses, (dialog, which) -> {
                    Ticket.TicketStatus newStatus = Ticket.TicketStatus.valueOf(statuses[which]);
                    currentTicket.setStatus(newStatus);
                    long rowsAffected = ticketDAO.updateTicket(currentTicket);

                    if (rowsAffected > 0) {
                        Toast.makeText(this, "Status atualizado para " + newStatus.name(), Toast.LENGTH_SHORT).show();
                        loadTicketDetails(Integer.parseInt(currentTicket.getId())); // Recarrega os detalhes
                    } else {
                        Toast.makeText(this, "Erro ao atualizar status.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private int getStatusColor(TicketStatus status) {
        // Lógica de cores...
        return Color.GRAY;
    }

    private int getPriorityColor(TicketPriority priority) {
        // Lógica de cores...
        return Color.GRAY;
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