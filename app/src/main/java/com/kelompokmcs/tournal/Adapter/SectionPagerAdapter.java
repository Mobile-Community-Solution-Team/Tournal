package com.kelompokmcs.tournal.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kelompokmcs.tournal.Fragment.AgendaFragment;
import com.kelompokmcs.tournal.Fragment.AnnouncementFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0 :
                fragment = new AnnouncementFragment();
                break;
            case 1:
                fragment = new AgendaFragment();
                break;
            default :
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "Announcement";
            case 1 :
                return "Agenda";
        }
        return null;
    }
}
