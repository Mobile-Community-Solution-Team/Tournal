package com.kelompokmcs.tournal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder>{
    private Context context;
    private ArrayList<Agenda> agendaList;
    private OnItemClickListener onItemClickListener;

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
    public void onBindViewHolder(@NonNull AgendaViewHolder holder, final int position) {
        Agenda agendaItem = agendaList.get(position);

        holder.tvAgendaTitle.setText(agendaItem.getAgendaTitle());
        holder.tvStartTime.setText(parseDateToddMMMMyyyyhhmma(agendaItem.getStartTime()));
        holder.tvEndTime.setText(parseDateToddMMMMyyyyhhmma(agendaItem.getEndTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
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

    public void setAgendaList(ArrayList<Agenda> agendaList) {
        this.agendaList = agendaList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private String parseDateToddMMMMyyyyhhmma(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
            return new SimpleDateFormat("dd MMMM yyyy hh:mm a").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
