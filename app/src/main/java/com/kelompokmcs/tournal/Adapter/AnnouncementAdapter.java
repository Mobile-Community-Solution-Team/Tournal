package com.kelompokmcs.tournal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>{
    private Context context;
    private ArrayList<Announcement> announcementList;
    private OnItemClickListener onItemClickListener;

    public AnnouncementAdapter(Context context, ArrayList<Announcement> announcementList){
        this.context = context;
        this.announcementList = announcementList;
    }

    @NonNull
    @Override
    public AnnouncementAdapter.AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_announcement,parent,false);
        return new AnnouncementAdapter.AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.AnnouncementViewHolder holder, final int position) {
        Announcement announcementItem = announcementList.get(position);

        holder.tvAnnouncementTitle.setText(announcementItem.getAnnouncementTitle());
        holder.tvAnnouncementDesc.setText(announcementItem.getAnnouncementDesc());
        holder.tvDateAndTime.setText(announcementItem.getDateAndTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView tvAnnouncementTitle, tvAnnouncementDesc, tvDateAndTime;
        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnnouncementTitle = itemView.findViewById(R.id.tv_announcement_title);
            tvAnnouncementDesc = itemView.findViewById(R.id.tv_announcement_desc);
            tvDateAndTime = itemView.findViewById(R.id.tv_date_and_time);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

