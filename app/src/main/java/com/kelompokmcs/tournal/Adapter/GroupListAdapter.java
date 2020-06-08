package com.kelompokmcs.tournal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private Context context;
    private ArrayList<Group> groupList;
    private OnItemClickListener onItemClickListener;

    public GroupListAdapter(Context context, ArrayList<Group> groupList){
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_group,parent,false);
        return new GroupViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, final int position) {
        Group groupItem = groupList.get(position);

        holder.tvGroupnName.setText(groupItem.getGroupName());
        holder.tvDateRange.setText(parseDateToddMMMMyyyy(groupItem.getStartDate()) + " - " + parseDateToddMMMMyyyy(groupItem.getEndDate()));
        holder.tvGroupLocation.setText(groupItem.getGroupLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupnName, tvDateRange, tvGroupLocation;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupnName = itemView.findViewById(R.id.tv_group_name);
            tvDateRange = itemView.findViewById(R.id.tv_date_range);
            tvGroupLocation = itemView.findViewById(R.id.tv_group_location);
        }
    }

    private String parseDateToddMMMMyyyy(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }

}
