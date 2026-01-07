package com.hawkdesk.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hawkdesk.app.R;
import com.hawkdesk.app.models.Ticket;
import com.hawkdesk.app.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final Context context;
    private List<Ticket> tickets;
    private final OnTicketClickListener listener;

    /**
     * Define o listener de clique no Ticket.
     * Deve ser implementado pela Activity que usa este Adapter (ex: MainActivity)
     */
    public interface OnTicketClickListener {
        void onTicketClick(Ticket ticket);
    }

    /**
     * Construtor do Adapter.
     *
     * @param context Contexto da Activity (ex: this)
     */
    public TicketAdapter(Context context) {
        this.context = context;
        this.tickets = new ArrayList<>();
        // O listener é a própria Activity
        if (context instanceof OnTicketClickListener) {
            this.listener = (OnTicketClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " deve implementar OnTicketClickListener");
        }
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    /**
     * Método para atualizar a lista de tickets.
     *
     * @param newTickets A nova lista de tickets vindos do DB
     */
    public void setTickets(List<Ticket> newTickets) {
        this.tickets = newTickets;
        notifyDataSetChanged();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView titleTextView;
        private final TextView statusTextView;
        private final TextView priorityTextView;
        private final TextView dateTextView;
        private Ticket currentTicket;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.ticketTitleTextView);
            statusTextView = itemView.findViewById(R.id.ticketStatusTextView);
            priorityTextView = itemView.findViewById(R.id.ticketPriorityTextView);
            dateTextView = itemView.findViewById(R.id.ticketDateTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(Ticket ticket) {
            this.currentTicket = ticket;
            titleTextView.setText(ticket.getTitle());

            //  CORREÇÃO DE IDIOMA (STATUS)
            statusTextView.setText(context.getString(ticket.getStatus().getStringResId()));

            //  CORREÇÃO DE IDIOMA (PRIORIDADE)
            priorityTextView.setText(context.getString(ticket.getPriority().getStringResId()));

            dateTextView.setText(DateUtil.formatDateTime(ticket.getCreatedAt()));
        }

        @Override
        public void onClick(View v) {
            // Chama o listener na Activity
            if (listener != null) {
                listener.onTicketClick(currentTicket);
            }
        }
    }
}