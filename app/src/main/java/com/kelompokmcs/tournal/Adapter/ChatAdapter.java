package com.kelompokmcs.tournal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.kelompokmcs.tournal.Model.Chat;
import com.kelompokmcs.tournal.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Chat> chatArrayList;

    public ChatAdapter(Context context, ArrayList<Chat> chatArrayList){
        this.context = context;
        this.chatArrayList = chatArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(context).inflate(R.layout.item_layout_other_chat,parent,false);
                return new ViewHolder0(view);
            case 1:
                View view1 = LayoutInflater.from(context).inflate(R.layout.item_layout_my_chat,parent,false);
                return new ViewHolder1(view1);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = chatArrayList.get(position);

        switch (holder.getItemViewType()) {
            case 0:
                ((ViewHolder0) holder).tvName.setText(chat.getName());
                ((ViewHolder0) holder).tvMessage.setText(chat.getMessage());
                ((ViewHolder0) holder).tvTime.setText(chat.getTime());
                break;

            case 1:
                ((ViewHolder1) holder).tvMessage.setText(chat.getMessage());
                ((ViewHolder1) holder).tvTime.setText(chat.getTime());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatArrayList.get(position).getUserId().equals(GoogleSignIn.getLastSignedInAccount(context).getId())){
            return 1;
        }
        else{
            return 0;
        }
       
    }

    private class ViewHolder0 extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        TextView tvName, tvMessage, tvTime;
        public ViewHolder0(View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.iv_person_photo);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        public ViewHolder1(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    public void setChatArrayList(ArrayList<Chat> chatArrayList) {
        this.chatArrayList = chatArrayList;
    }
}
