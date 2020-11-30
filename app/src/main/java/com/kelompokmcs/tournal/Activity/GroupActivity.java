package com.kelompokmcs.tournal.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.API.VolleySingleton;
import com.kelompokmcs.tournal.Adapter.AgendaAdapter;
import com.kelompokmcs.tournal.Adapter.AnnouncementAdapter;
import com.kelompokmcs.tournal.Database.DBHelper;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GroupActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RequestResult {

    private TabLayout tabLayout;
    private LinearLayout emptyAnnouncementLayout, emptyAgendaLayout;
    private TextView tvGroupName, tvDateRange, tvSeeAllAnouncement, tvSeeAllAgenda;
    private CardView cvShare, cvAddAnnouncement, cvAddAgenda;
    private Toolbar toolbar;
    private RecyclerView rvLatestAnnouncement, rvNextAgenda;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout loadingLayout;
    private FloatingActionButton fabChat;
    private AgendaAdapter agendaAdapter;
    private AnnouncementAdapter announcementAdapter;
    private DBTransaction dbTransaction;
    private APIRequest apiRequest;
    private int groupId;
    private int REQUEST_CODE = 1;
    private Group groupData;

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
        cvShare = findViewById(R.id.cv_share);
        cvAddAnnouncement = findViewById(R.id.cv_add_announcement);
        cvAddAgenda = findViewById(R.id.cv_add_agenda);
        emptyAgendaLayout = findViewById(R.id.empty_agenda_layout);
        emptyAnnouncementLayout = findViewById(R.id.empty_announcement_layout);
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        loadingLayout = findViewById(R.id.loading_layout);
        fabChat = findViewById(R.id.fab_chat);
        apiRequest = new APIRequest(this);
        dbTransaction = new DBTransaction(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        groupId = getIntent().getIntExtra("group_id",-1);
        groupData = dbTransaction.getGroupDataById(groupId);

        tvGroupName.setText(groupData.getGroupName());
        tvDateRange.setText(parseDateToddMMMMyyyy(groupData.getStartDate()) + " - " + parseDateToddMMMMyyyy(groupData.getEndDate()));

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadingLayout.setVisibility(View.VISIBLE);
        //dapatkan data terbaru dari server
        updateData();

        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupActivity.this,ChatActivity.class);
                i.putExtra("groupId",groupId);
                i.putExtra("groupName",groupData.getGroupName());
                startActivity(i);
            }
        });

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

        cvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupActivity.this,ShareActivity.class);
                i.putExtra("groupCode",groupData.getGroupCode());
                i.putExtra("groupPass",groupData.getGroupPass());
                startActivity(i);
            }
        });

        cvAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupActivity.this,AddNewAnnouncementActivity.class);
                i.putExtra("group_id",groupId);
                startActivityForResult(i,REQUEST_CODE);
            }
        });

        cvAddAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupActivity.this,AddNewAgendaActivity.class);
                i.putExtra("group_id",groupId);
                startActivityForResult(i,REQUEST_CODE);
            }
        });
    }

    private void updateData() {
        try {
            JsonObjectRequest request= apiRequest.getAgendaInformation(groupId);
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupLatestAnnouncementRecyclerView() {
        final ArrayList<Announcement> announcementArrayList = announcementLimit3Items();
        if(announcementArrayList.isEmpty()){
            emptyAnnouncementLayout.setVisibility(View.VISIBLE);
            rvLatestAnnouncement.setVisibility(View.GONE);
        }
        else{
            emptyAnnouncementLayout.setVisibility(View.GONE);
            rvLatestAnnouncement.setVisibility(View.VISIBLE);

            announcementAdapter = new AnnouncementAdapter(this,announcementArrayList);
            rvLatestAnnouncement.setLayoutManager(new LinearLayoutManager(this));
            rvLatestAnnouncement.setAdapter(announcementAdapter);
            rvLatestAnnouncement.setNestedScrollingEnabled(false);

            announcementAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent i = new Intent(GroupActivity.this, AnnouncementDetailActivity.class);
                    i.putExtra("announcement",announcementArrayList.get(position));
                    i.putExtra("groupName",groupData.getGroupName());
                    startActivity(i);
                }
            });
        }
    }

    private void setupNextAgendaRecyclerView() {
        final ArrayList<Agenda> agendaArrayList = agendaLimit3Items();
        if(agendaArrayList.isEmpty()){
            emptyAgendaLayout.setVisibility(View.VISIBLE);
            rvNextAgenda.setVisibility(View.GONE);
        }
        else{
            emptyAgendaLayout.setVisibility(View.GONE);
            rvNextAgenda.setVisibility(View.VISIBLE);

            agendaAdapter = new AgendaAdapter(this,agendaArrayList);
            rvNextAgenda.setLayoutManager(new LinearLayoutManager(this));
            rvNextAgenda.setAdapter(agendaAdapter);
            rvNextAgenda.setNestedScrollingEnabled(false);

            agendaAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent i = new Intent(GroupActivity.this, AgendaDetailActivity.class);
                    i.putExtra("agenda",agendaArrayList.get(position));
                    startActivity(i);
                }
            });
        }
    }

    private ArrayList<Announcement> announcementLimit3Items() {
        try {
            return new ArrayList<Announcement>(dbTransaction.getAnnouncementById(groupId).subList(0,3));
        }catch (Exception e){
            e.printStackTrace();
            return dbTransaction.getAnnouncementById(groupId);
        }
    }

    private ArrayList<Agenda> agendaLimit3Items() {
        //dapatkan 3 item agenda yang mendekati waktu sekarang
        ArrayList<Agenda> agendaArrayList = dbTransaction.getAgendaById(groupId);
        ArrayList<Agenda> agendaArrayListResult = new ArrayList<>();

        int size = 0;
        for (Agenda agenda : agendaArrayList) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(agenda.getStartTime());
                if(size < 3 && Calendar.getInstance().getTime().before(date)){
                    agendaArrayListResult.add(agenda);
                    size++;
                }
                else if(size >= 3){
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return agendaArrayListResult;
    }

    private String parseDateToddMMMMyyyy(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            return new SimpleDateFormat("dd MMMM yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        updateData();
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("getAgendaInformation")){
            dbTransaction.deleteAgendaTable(groupId);
            ArrayList<Agenda> agendaArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = response.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject agendaDatas = jsonArray.getJSONObject(i);
                    int agendaId = agendaDatas.getInt("agenda_id");
                    int groupId = agendaDatas.getInt("group_id");
                    String agendaTitle = agendaDatas.getString("agenda_title");
                    String agendaDesc = agendaDatas.getString("agenda_desc");
                    double startLat = agendaDatas.getDouble("start_lat");
                    double startLng = agendaDatas.getDouble("start_lng");
                    double startAlt = agendaDatas.getDouble("start_alt");
                    double endLat = agendaDatas.getDouble("end_lat");
                    double endLng = agendaDatas.getDouble("end_lng");
                    double endAlt = agendaDatas.getDouble("end_alt");
                    String startTime = agendaDatas.getString("start_time");
                    String endTime = agendaDatas.getString("end_time");

                    agendaArrayList.add(new Agenda(agendaId,groupId,"",agendaTitle,agendaDesc,startLat,startLng,startAlt,endLat,endLng,endAlt,startTime,endTime));
                }

                dbTransaction.addAgendaDatas(agendaArrayList);
                JsonObjectRequest request = apiRequest.getAnnouncementInformation(groupId);
                VolleySingleton.getInstance(this).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getAnnouncementInformation")){
            dbTransaction.deleteAnnouncementTable(groupId);
            ArrayList<Announcement> announcementArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = response.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject announcementDatas = jsonArray.getJSONObject(i);
                    int announcementId = announcementDatas.getInt("id");
                    int groupId = announcementDatas.getInt("group_id");
                    String announcementTitle = announcementDatas.getString("announcement_title");
                    String announcementDesc = announcementDatas.getString("announcement_desc");
                    String dateAndTime = announcementDatas.getString("date_and_time");
                    String userName = announcementDatas.getString("user_name");
                    String userEmail = announcementDatas.getString("user_email");
                    String userPhoto = announcementDatas.getString("user_photo");

                    announcementArrayList.add(new Announcement(announcementId,groupId,announcementTitle,announcementDesc,dateAndTime,userName,userEmail,userPhoto));
                }

                dbTransaction.addAnnouncementDatas(announcementArrayList);
                notifyDataHasChangeToRecyclerView();

                swipeRefreshLayout.setRefreshing(false);
                loadingLayout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        Toast.makeText(this, "Failed to get updated agenda and announcement", Toast.LENGTH_LONG).show();

        notifyDataHasChangeToRecyclerView();

        swipeRefreshLayout.setRefreshing(false);
        loadingLayout.setVisibility(View.GONE);
    }

    private void notifyDataHasChangeToRecyclerView() {
        ArrayList<Agenda> agendaArrayList = agendaLimit3Items();
        ArrayList<Announcement> announcementArrayList = announcementLimit3Items();

        if(agendaArrayList.isEmpty() || agendaAdapter == null){
            setupNextAgendaRecyclerView();
        }
        else
        {
            agendaAdapter.setAgendaList(agendaArrayList);
            agendaAdapter.notifyDataSetChanged();
        }

        if(announcementArrayList.isEmpty() || announcementAdapter == null){
            setupLatestAnnouncementRecyclerView();
        }
        else{
            announcementAdapter.setAnnouncementList(announcementArrayList);
            announcementAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            loadingLayout.setVisibility(View.VISIBLE);
            updateData();
        }
    }
}
