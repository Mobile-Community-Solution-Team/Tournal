package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.kelompokmcs.tournal.Fragment.InsertCodeFragment;
import com.kelompokmcs.tournal.Fragment.InsertPasswordFragment;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;

public class JoinGroupActivity extends AppCompatActivity implements InsertCodeFragment.passGroupDataListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        loadFragment(new InsertCodeFragment());
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.frame_layout,fragment).
                    commit();
            return true;
        }
        return false;
    }

    @Override
    public void passGroupData(Group group) {
        InsertPasswordFragment fragment = new InsertPasswordFragment();
        fragment.setGroupData(group);
        loadFragment(fragment);
    }

}
