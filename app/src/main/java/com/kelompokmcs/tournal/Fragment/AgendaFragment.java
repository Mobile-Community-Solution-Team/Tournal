package com.kelompokmcs.tournal.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelompokmcs.tournal.Adapter.AgendaAdapter;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.R;

import java.util.ArrayList;

public class AgendaFragment extends Fragment {

    private RecyclerView rvAgenda;
    private DBTransaction dbTransaction;
    private ArrayList<Agenda> agendaArrayList;
    private LinearLayout agendaLayout, emptyLayout;

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
        rvAgenda = rootView.findViewById(R.id.rv_agenda);
        agendaLayout = rootView.findViewById(R.id.agenda_layout);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        dbTransaction = new DBTransaction(getContext());

        agendaArrayList = dbTransaction.getAgendaById(getActivity().getIntent().getIntExtra("groupId",-1));

        if(agendaArrayList.isEmpty()){
            emptyLayout.setVisibility(View.VISIBLE);
            agendaLayout.setVisibility(View.GONE);
        }
        else{
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {

        AgendaAdapter adapter = new AgendaAdapter(getContext(),agendaArrayList);
        rvAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAgenda.setAdapter(adapter);
    }
}
