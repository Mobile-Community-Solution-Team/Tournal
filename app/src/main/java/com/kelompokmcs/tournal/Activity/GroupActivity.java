package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kelompokmcs.tournal.Adapter.AgendaAdapter;
import com.kelompokmcs.tournal.Adapter.AnnouncementAdapter;
import com.kelompokmcs.tournal.Database.DBHelper;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TextView tvGroupName, tvDateRange, tvSeeAllAnouncement, tvSeeAllAgenda;
    private RecyclerView rvLatestAnnouncement, rvNextAgenda;
    private DBTransaction dbTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        rvLatestAnnouncement = findViewById(R.id.rv_latest_announcement);
        rvNextAgenda = findViewById(R.id.rv_latest_next_agenda);
        tvGroupName = findViewById(R.id.tv_group_name);
        tvDateRange = findViewById(R.id.tv_date_range);
        tvSeeAllAnouncement = findViewById(R.id.tv_see_all_announcement);
        tvSeeAllAgenda = findViewById(R.id.tv_see_all_agenda);
        dbTransaction = new DBTransaction(this);

        int groupId = getIntent().getIntExtra("group_id",-1);
        final Group groupData = dbTransaction.getGroupDataById(groupId);
        tvGroupName.setText(groupData.getGroupName());

        setupLatestAnnouncementRecyclerView();
        setupNextAgendaRecyclerView();

        tvSeeAllAnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupActivity.this,AgendaAndAnnouncementActivity.class);
                i.putExtra("tab",0);
                i.putExtra("groupName",groupData.getGroupName());
                i.putExtra("groupId",groupData.getGroupId());
                startActivity(i);
            }
        });

        tvSeeAllAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupActivity.this,AgendaAndAnnouncementActivity.class);
                i.putExtra("tab",1);
                i.putExtra("groupName",groupData.getGroupName());
                i.putExtra("groupId",groupData.getGroupId());
                startActivity(i);
            }
        });
    }

    private void setupLatestAnnouncementRecyclerView() {
        ArrayList<Announcement> announcementArrayList = new ArrayList<>();
        announcementArrayList.add(new Announcement(1,1,"Announcement Title", "Announcement Desc","12 Januari 2020 12:00","chandra","@gmail.com","www.chandra"));
        AnnouncementAdapter adapter = new AnnouncementAdapter(this,announcementArrayList);
        rvLatestAnnouncement.setLayoutManager(new LinearLayoutManager(this));
        rvLatestAnnouncement.setAdapter(adapter);
        rvLatestAnnouncement.setNestedScrollingEnabled(false);
    }

    private void setupNextAgendaRecyclerView() {
        ArrayList<Agenda> agendaArrayList = new ArrayList<>();
        agendaArrayList.add(new Agenda(1,1,"Agenda Title","Agenda Desc", 0.0,0.0,0.0,"12 Januari 2020 12:00","12 Januari 2020 14:00"));
        AgendaAdapter adapter = new AgendaAdapter(this,agendaArrayList);
        rvNextAgenda.setLayoutManager(new LinearLayoutManager(this));
        rvNextAgenda.setAdapter(adapter);
        rvNextAgenda.setNestedScrollingEnabled(false);
    }

}
