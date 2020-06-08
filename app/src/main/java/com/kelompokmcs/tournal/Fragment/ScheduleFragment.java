package com.kelompokmcs.tournal.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelompokmcs.tournal.Activity.AgendaDetailActivity;
import com.kelompokmcs.tournal.Activity.GroupActivity;
import com.kelompokmcs.tournal.Adapter.AgendaInScheduleAdapter;
import com.kelompokmcs.tournal.Database.DBHelper;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.R;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleFragment extends Fragment{

    private TextView tvSelectedDate;
    private RecyclerView rvSchedule;
    private LinearLayout emptyLayout;
    private ArrayList<Agenda> allAgendas;
    private DBTransaction dbTransaction;
    private AgendaInScheduleAdapter adapter;
    private CollapsibleCalendar collapsibleCalendar;
    private Handler mHandler;
    private Thread thread;
    private final int START_PROGRESS = 100;
    private final int UPDATE_PROGRESS = 101;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();

        tvSelectedDate = rootView.findViewById(R.id.tv_selected_date);
        rvSchedule = rootView.findViewById(R.id.rv_schedule);
        collapsibleCalendar= rootView.findViewById(R.id.calendarView);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        dbTransaction = new DBTransaction(getContext());


        allAgendas = dbTransaction.getAllAgenda();
        setupRecyclerView();
        showAgendaForFirstTime();
        addEvenTag();

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDayChanged() {

            }

            @Override
            public void onClickListener() {

            }

            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                tvSelectedDate.setText(parseDateToddMMMMyyyy(day.getYear(),day.getMonth(),day.getDay()));
                showAgendaOfSelectedDay(agendaOfSelectedDay(day.getYear(),day.getMonth(),day.getDay()));
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });
    }

    private void setupRecyclerView() {
        ArrayList<Agenda> agendaArrayList = new ArrayList<>();
        adapter = new AgendaInScheduleAdapter(getContext(),agendaArrayList);
        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSchedule.setAdapter(adapter);
    }

    private void showAgendaForFirstTime() {
        tvSelectedDate.setText(parseDateToddMMMMyyyy(collapsibleCalendar.getYear(),collapsibleCalendar.getMonth(),collapsibleCalendar.getTodayItemPosition()));
        showAgendaOfSelectedDay(agendaOfSelectedDay(collapsibleCalendar.getYear(),collapsibleCalendar.getMonth(),collapsibleCalendar.getTodayItemPosition()));
    }

    private void addEvenTag() {
        for (Agenda agenda : allAgendas){
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(agenda.getStartTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                collapsibleCalendar.addEventTag(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Agenda> agendaOfSelectedDay(int year, int month, int day) {
        ArrayList<Agenda> agendaArrayList = new ArrayList<>();
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(year+"-"+(month+1)+"-"+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Agenda agenda : allAgendas) {
            try {
                Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(agenda.getStartTime());
                if(date2.equals(date1)){
                    agendaArrayList.add(agenda);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return agendaArrayList;
    }

    private void showAgendaOfSelectedDay(ArrayList<Agenda> agendaArrayList){
        if(agendaArrayList.isEmpty()){
            rvSchedule.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
        else{
            rvSchedule.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            adapter.setAgendaList(agendaArrayList);
            adapter.notifyDataSetChanged();
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent i = new Intent(getActivity(), AgendaDetailActivity.class);
                    i.putExtra("agenda",agendaArrayList.get(position));
                    startActivity(i);
                }
            });
        }
    }

    private String parseDateToddMMMMyyyy(int i, int i1, int i2) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(i+"-"+(i1+1)+"-"+i2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }
}
