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
import android.widget.Filter;
import android.widget.LinearLayout;

import com.kelompokmcs.tournal.Activity.AgendaDetailActivity;
import com.kelompokmcs.tournal.Adapter.AgendaAdapter;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

public class AgendaFragment extends Fragment {

    private RecyclerView rvPreviousAgenda, rvNextAgenda;
    private DBTransaction dbTransaction;
    private ArrayList<Agenda> nextAgendaArrayList, previousAgendaArraylist;
    private LinearLayout previousAgendaLayout, nextAgendaLayout, emptyLayout;
    private AgendaAdapter previousAgendaAdapter, nextAgendaAdapter;
    public boolean showPreviousAgenda = true;
    public boolean showNextAgenda = true;
    public boolean previousNextAgenda = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();
        rvPreviousAgenda = rootView.findViewById(R.id.rv_previous_agenda);
        rvNextAgenda = rootView.findViewById(R.id.rv_next_agenda);
        previousAgendaLayout = rootView.findViewById(R.id.previous_agenda_layout);
        nextAgendaLayout = rootView.findViewById(R.id.next_agenda_layout);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        dbTransaction = new DBTransaction(getContext());
        previousAgendaArraylist = new ArrayList<>();
        nextAgendaArrayList = new ArrayList<>();

        findPreviousAndNextAgenda();


        if(previousAgendaArraylist.isEmpty()){
            previousAgendaLayout.setVisibility(GONE);
        }
        else{
            setupPreviousAgendaRecyclerView();
        }

        if(nextAgendaArrayList.isEmpty()){
            nextAgendaLayout.setVisibility(GONE);
        }
        else{
            setupNextAgendaRecyclerView();
        }

        if(previousAgendaArraylist.isEmpty() && nextAgendaArrayList.isEmpty()){
            previousAgendaLayout.setVisibility(GONE);
            nextAgendaLayout.setVisibility(GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void findPreviousAndNextAgenda() {
        ArrayList<Agenda> agendaArrayList = dbTransaction.getAgendaById(getActivity().getIntent().getIntExtra("groupId",-1));

        for (Agenda agenda : agendaArrayList) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(agenda.getStartTime());
                if(Calendar.getInstance().getTime().after(date)){
                    previousAgendaArraylist.add(agenda);
                }
                else{
                    nextAgendaArrayList.add(agenda);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupPreviousAgendaRecyclerView() {
        previousAgendaAdapter = new AgendaAdapter(getContext(),previousAgendaArraylist);
        rvPreviousAgenda.setNestedScrollingEnabled(false);
        rvPreviousAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPreviousAgenda.setAdapter(previousAgendaAdapter);
        previousAgendaAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), AgendaDetailActivity.class);
                i.putExtra("agenda",previousAgendaArraylist.get(position));
                startActivity(i);
            }
        });
    }

    private void setupNextAgendaRecyclerView() {
        nextAgendaAdapter = new AgendaAdapter(getContext(),nextAgendaArrayList);
        rvNextAgenda.setNestedScrollingEnabled(false);
        rvNextAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNextAgenda.setAdapter(nextAgendaAdapter);
        nextAgendaAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), AgendaDetailActivity.class);
                i.putExtra("agenda",nextAgendaArrayList.get(position));
                startActivity(i);
            }
        });
    }

    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Agenda> filteredAgendaList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredAgendaList.addAll(previousAgendaArraylist);
                filteredAgendaList.addAll(nextAgendaArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Agenda item : previousAgendaArraylist) {
                    if (item.getAgendaTitle().toLowerCase().contains(filterPattern)) {
                        filteredAgendaList.add(item);
                    }
                }
                for (Agenda item : nextAgendaArrayList) {
                    if (item.getAgendaTitle().toLowerCase().contains(filterPattern)) {
                        filteredAgendaList.add(item);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredAgendaList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<Agenda> filteredList = new ArrayList<>((List) filterResults.values);
            ArrayList<Agenda> filteredPreviousAgendaList = new ArrayList<>();
            ArrayList<Agenda> filteredNextAgendaList = new ArrayList<>();

            for (Agenda agenda : filteredList) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(agenda.getStartTime());
                    if(Calendar.getInstance().getTime().after(date)){
                        filteredPreviousAgendaList.add(agenda);
                    }
                    else{
                        filteredNextAgendaList.add(agenda);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(filteredPreviousAgendaList.isEmpty()){
                previousAgendaLayout.setVisibility(GONE);
            }
            else{
                previousAgendaLayout.setVisibility(View.VISIBLE);
                previousAgendaAdapter.setAgendaList(filteredPreviousAgendaList);
                previousAgendaAdapter.notifyDataSetChanged();
            }

            if(filteredNextAgendaList.isEmpty()){
                nextAgendaLayout.setVisibility(GONE);
            }
            else{
                nextAgendaLayout.setVisibility(View.VISIBLE);
                nextAgendaAdapter.setAgendaList(filteredNextAgendaList);
                nextAgendaAdapter.notifyDataSetChanged();
            }
        }
    };

    public void changeFilterValue(boolean showPreviousAgenda, boolean showNextAgenda){
        this.showPreviousAgenda = showPreviousAgenda;
        this.showNextAgenda = showNextAgenda;

        if(showNextAgenda){
           nextAgendaLayout.setVisibility(View.VISIBLE);
        }
        else{
            nextAgendaLayout.setVisibility(GONE);
        }

        if(showPreviousAgenda){
            previousAgendaLayout.setVisibility(View.VISIBLE);
        }
        else{
            previousAgendaLayout.setVisibility(GONE);
        }
    }

    public void changeSortValue(boolean previousNextAgenda){
        this.previousNextAgenda = previousNextAgenda;
    }
}
