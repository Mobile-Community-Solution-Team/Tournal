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

public class AgendaInScheduleAdapter extends RecyclerView.Adapter<AgendaInScheduleAdapter.AgendaViewHolder> {
    private Context context;
    private ArrayList<Agenda> agendaList;
    private OnItemClickListener onItemClickListener;

    public AgendaInScheduleAdapter(Context context, ArrayList<Agenda> agendaList){
        this.context = context;
        this.agendaList = agendaList;
    }

    public void setAgendaList(ArrayList<Agenda> agendaList) {
        this.agendaList = agendaList;
    }

    @NonNull
    @Override
    public AgendaInScheduleAdapter.AgendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_agenda_in_schedule,parent,false);
        return new AgendaInScheduleAdapter.AgendaViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaInScheduleAdapter.AgendaViewHolder holder, final int position) {
        Agenda agendaItem = agendaList.get(position);

        holder.tvAgendaTitle.setText(agendaItem.getAgendaTitle());
        holder.tvStartTime.setText(parseDateTohhmma(agendaItem.getStartTime()));
        holder.tvEndTime.setText(parseDateTohhmma(agendaItem.getEndTime()));
        holder.tvGroupName.setText(agendaItem.getGroupName());

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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class AgendaViewHolder extends RecyclerView.ViewHolder {
        TextView tvAgendaTitle, tvStartTime, tvEndTime, tvGroupName;
        public AgendaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAgendaTitle = itemView.findViewById(R.id.tv_agenda_title);
            tvStartTime = itemView.findViewById(R.id.tv_agenda_start_time);
            tvEndTime = itemView.findViewById(R.id.tv_agenda_end_time);
            tvGroupName = itemView.findViewById(R.id.tv_group_name);
        }
    }

    private String parseDateTohhmma(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
            return new SimpleDateFormat("hh:mm a").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
