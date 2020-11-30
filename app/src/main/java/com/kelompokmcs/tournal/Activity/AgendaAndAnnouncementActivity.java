package com.kelompokmcs.tournal.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kelompokmcs.tournal.Adapter.SectionPagerAdapter;
import com.kelompokmcs.tournal.Fragment.AgendaFragment;
import com.kelompokmcs.tournal.Fragment.AnnouncementFragment;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

public class AgendaAndAnnouncementActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TextView toolbarTitle, tvSort, tvFilter;
    private SectionPagerAdapter pagerAdapter;
    private FloatingActionButton fabMain, fabSort, fabFilter;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private Boolean isOpen = false;
    private static final int FILTER_REQUEST_CODE = 1, SORT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_and_announcement);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tvSort = findViewById(R.id.tv_sort);
        tvFilter = findViewById(R.id.tv_filter);
        fabMain = findViewById(R.id.fabMain);
        fabSort = findViewById(R.id.fab_sort);
        fabFilter = findViewById(R.id.fab_filter);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarTitle.setText(getIntent().getStringExtra("groupName"));
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    tvSort.setVisibility(View.INVISIBLE);
                    tvFilter.setVisibility(View.INVISIBLE);
                    fabSort.startAnimation(fab_close);
                    fabFilter.startAnimation(fab_close);
                    fabMain.startAnimation(fab_anticlock);
                    fabSort.setClickable(false);
                    fabFilter.setClickable(false);
                    isOpen = false;
                } else {
                    tvSort.setVisibility(View.VISIBLE);
                    tvFilter.setVisibility(View.VISIBLE);
                    fabSort.startAnimation(fab_open);
                    fabFilter.startAnimation(fab_open);
                    fabMain.startAnimation(fab_clock);
                    fabSort.setClickable(true);
                    fabFilter.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean showPreviousAgenda =  ((AgendaFragment) pagerAdapter.getRegisteredFragment(1)).showPreviousAgenda;
                boolean showNextAgenda = ((AgendaFragment) pagerAdapter.getRegisteredFragment(1)).showNextAgenda;
                Intent i = new Intent(AgendaAndAnnouncementActivity.this,FilterActivity.class);
                i.putExtra("showPreviousAgenda",showPreviousAgenda);
                i.putExtra("showNextAgenda",showNextAgenda);
                startActivityForResult(i,FILTER_REQUEST_CODE);
            }
        });

        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sortFromNewest =  ((AnnouncementFragment) pagerAdapter.getRegisteredFragment(0)).sortFromNewest;
                boolean previousNextAgenda = ((AgendaFragment) pagerAdapter.getRegisteredFragment(1)).previousNextAgenda;
                Intent i = new Intent(AgendaAndAnnouncementActivity.this,SortActivity.class);
                i.putExtra("sortFromNewest",sortFromNewest);
                i.putExtra("previousNextAgenda",previousNextAgenda);
                startActivityForResult(i,SORT_REQUEST_CODE);
            }
        });

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        pagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("tab",0));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(viewPager.getCurrentItem() == 0){
                    ((AnnouncementFragment) pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).getFilter().filter(s);
                }
                else{
                    ((AgendaFragment) pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).getFilter().filter(s);
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK){
            boolean updatedShowPreviousAgenda = data.getBooleanExtra("showPreviousAgenda",false);
            boolean updatedShowNextAgenda = data.getBooleanExtra("showNextAgenda",false);

            ((AgendaFragment) pagerAdapter.getRegisteredFragment(1)).changeFilterValue(updatedShowPreviousAgenda,updatedShowNextAgenda);
        }
        else if(requestCode == SORT_REQUEST_CODE && resultCode == RESULT_OK){
            boolean updatedSortFromNewest = data.getBooleanExtra("sortFromNewest",false);
            boolean previousNextAgenda = data.getBooleanExtra("previousNextAgenda",false);
            ((AnnouncementFragment) pagerAdapter.getRegisteredFragment(0)).changeSortValue(updatedSortFromNewest);
            ((AgendaFragment) pagerAdapter.getRegisteredFragment(1)).changeSortValue(previousNextAgenda);
        }
    }
}
