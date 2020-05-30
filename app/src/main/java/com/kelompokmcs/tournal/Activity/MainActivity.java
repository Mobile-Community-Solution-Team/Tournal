package com.kelompokmcs.tournal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelompokmcs.tournal.Fragment.AccountFragment;
import com.kelompokmcs.tournal.Fragment.GroupFragment;
import com.kelompokmcs.tournal.Fragment.HomeFragment;
import com.kelompokmcs.tournal.Fragment.ScheduleFragment;
import com.kelompokmcs.tournal.R;

public class MainActivity extends AppCompatActivity {

    TextView tvName, tvGivenName,tvFamilyName,tvEmail, tvId;
    ImageView ivPersonPhoto;
    Button btnSignOut;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        loadFragment(new HomeFragment());

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.menu_group:
                        fragment = new GroupFragment();
                        break;
                    case R.id.menu_schedule:
                        fragment = new ScheduleFragment();
                        break;
                    case R.id.menu_account:
                        fragment = new AccountFragment();
                        break;
                }

                return loadFragment(fragment);
            }
        });

        GoogleSignInAccount signedInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(signedInAccount==null){
            Intent i = new Intent(MainActivity.this,SignInActivity.class);
            startActivity(i);
            finish();
            return;
        }
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
}
