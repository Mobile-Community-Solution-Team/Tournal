package com.kelompokmcs.tournal.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelompokmcs.tournal.Activity.AnnouncementDetailActivity;
import com.kelompokmcs.tournal.Adapter.AnnouncementAdapter;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

import java.util.ArrayList;


public class AnnouncementFragment extends Fragment {

    private RecyclerView rvAnnouncement;
    private DBTransaction dbTransaction;
    private ArrayList<Announcement> announcementArrayList;
    private LinearLayout announcementLayout, emptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_announcement, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();
        rvAnnouncement = rootView.findViewById(R.id.rv_announcement);
        announcementLayout = rootView.findViewById(R.id.announcement_layout);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        dbTransaction = new DBTransaction(getContext());

        announcementArrayList = dbTransaction.getAnnouncementById(getActivity().getIntent().getIntExtra("groupId",-1));

        if(announcementArrayList.isEmpty()){
            emptyLayout.setVisibility(View.VISIBLE);
            announcementLayout.setVisibility(View.GONE);
        }
        else{
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        AnnouncementAdapter adapter = new AnnouncementAdapter(getContext(),announcementArrayList);
        rvAnnouncement.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAnnouncement.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), AnnouncementDetailActivity.class);
                i.putExtra("announcement",announcementArrayList.get(position));
                i.putExtra("groupName",getActivity().getIntent().getStringExtra("groupName"));
                startActivity(i);
            }
        });
    }
}
