package com.kelompokmcs.tournal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.R;

import java.util.ArrayList;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>{
    private Context context;
    private ArrayList<Agenda> agendaList;

    public AgendaAdapter(Context context, ArrayList<Agenda> agendaList){
        this.context = context;
        this.agendaList = agendaList;
    }

    @NonNull
    @Override
    public AgendaAdapter.AgendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_agenda,parent,false);
        return new AgendaAdapter.AgendaViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaViewHolder holder, int position) {
        Agenda agendaItem = agendaList.get(position);

        holder.tvAgendaTitle.setText(agendaItem.getAgendaTitle());
        holder.tvStartTime.setText(agendaItem.getStartTime());
        holder.tvEndTime.setText(agendaItem.getEndTime());
    }

    @Override
    public int getItemCount() {
        return agendaList.size();
    }

    public class AgendaViewHolder extends RecyclerView.ViewHolder {
        TextView tvAgendaTitle, tvStartTime, tvEndTime;
        public AgendaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAgendaTitle = itemView.findViewById(R.id.tv_agenda_title);
            tvStartTime = itemView.findViewById(R.id.tv_agenda_start_time);
            tvEndTime = itemView.findViewById(R.id.tv_agenda_end_time);
        }
    }
}
