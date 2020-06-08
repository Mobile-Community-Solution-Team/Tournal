package com.kelompokmcs.tournal.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelompokmcs.tournal.Activity.GroupActivity;
import com.kelompokmcs.tournal.Adapter.GroupListAdapter;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private RecyclerView rvGroupList;
    private DBTransaction dbTransaction;
    private ArrayList<Group> groupArrayList;
    private NestedScrollView groupListLayout;
    private LinearLayout emptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();
        rvGroupList = rootView.findViewById(R.id.rv_group_list);
        groupListLayout = rootView.findViewById(R.id.group_list_layout);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        dbTransaction = new DBTransaction(getContext());

        groupArrayList =  dbTransaction.getGroupList();
        if(groupArrayList.isEmpty()){
            emptyLayout.setVisibility(View.VISIBLE);
            groupListLayout.setVisibility(View.GONE);
        }
        else{
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        GroupListAdapter adapter = new GroupListAdapter(getContext(),groupArrayList);
        rvGroupList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroupList.setAdapter(adapter);
        rvGroupList.setNestedScrollingEnabled(false);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), GroupActivity.class);
                i.putExtra("group_id",groupArrayList.get(position).getGroupId());
                startActivity(i);
            }
        });
    }
}
