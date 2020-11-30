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
import android.widget.Toast;

import com.kelompokmcs.tournal.Activity.AnnouncementDetailActivity;
import com.kelompokmcs.tournal.Adapter.AnnouncementAdapter;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.OnItemClickListener;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AnnouncementFragment extends Fragment {

    private RecyclerView rvAnnouncement;
    private DBTransaction dbTransaction;
    private ArrayList<Announcement> announcementArrayList;
    private LinearLayout announcementLayout, emptyLayout;
    private AnnouncementAdapter adapter;
    public boolean sortFromNewest = true;
    public Date startDate, endDate;

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
        adapter = new AnnouncementAdapter(getContext(),announcementArrayList);
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

    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Announcement> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(announcementArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Announcement item : announcementArrayList) {
                    if (item.getAnnouncementTitle().toLowerCase().contains(filterPattern) || item.getAnnouncementDesc().toLowerCase().contains(constraint)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<Announcement> filteredLits = new ArrayList<>((List) filterResults.values);
            adapter.setAnnouncementList(filteredLits);
            adapter.notifyDataSetChanged();
        }
    };

    public void changeSortValue(boolean sortFromNewest){
        this.sortFromNewest = sortFromNewest;

        if(sortFromNewest){
            for(int i=0;i<announcementArrayList.size()-1;i++){
                for(int j=i+1;j<announcementArrayList.size();j++){
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(announcementArrayList.get(i).getDateAndTime());
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(announcementArrayList.get(j).getDateAndTime());
                        if(date1.before(date2)){
                            Announcement announcement = announcementArrayList.get(i);
                            announcementArrayList.set(i,announcementArrayList.get(j));
                            announcementArrayList.set(j,announcement);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            for(int i=0;i<announcementArrayList.size()-1;i++){
                for(int j=i+1;j<announcementArrayList.size();j++){
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(announcementArrayList.get(i).getDateAndTime());
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(announcementArrayList.get(j).getDateAndTime());
                        if(date1.after(date2)){
                            Announcement announcement = announcementArrayList.get(i);
                            announcementArrayList.set(i,announcementArrayList.get(j));
                            announcementArrayList.set(j,announcement);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        adapter.setAnnouncementList(announcementArrayList);
        adapter.notifyDataSetChanged();
    }
}
