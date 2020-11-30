package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.kelompokmcs.tournal.R;

public class SortActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup rgSortAnnouncement, rgSortAgenda;
    private RadioButton rbAnnouncementNewest, rbAnnouncementOldest;
    private RadioButton rbPrevNext, rbNextPrev;
    private boolean sortFromNewest, previousNextAgenda;
    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        toolbar = findViewById(R.id.toolbar);
        rgSortAnnouncement = findViewById(R.id.rg_sort_announcement);
        rbAnnouncementNewest = findViewById(R.id.rb_sort_announcement_newest);
        rbAnnouncementOldest = findViewById(R.id.rb_sort_announcement_oldest);
        rgSortAgenda = findViewById(R.id.rg_sort_agenda);
        rbPrevNext = findViewById(R.id.rb_prev_next);
        rbNextPrev = findViewById(R.id.rb_next_prev);
        btnFinish = findViewById(R.id.btn_finish_sort);

        sortFromNewest = getIntent().getBooleanExtra("sortFromNewest",false);
        previousNextAgenda = getIntent().getBooleanExtra("previousNextAgenda",false);

        if(sortFromNewest){
            rbAnnouncementNewest.setChecked(true);
        }
        else if(!sortFromNewest){
            rbAnnouncementOldest.setChecked(true);
        }

        if(previousNextAgenda){
            rbPrevNext.setChecked(true);
        }
        else{
            rbNextPrev.setChecked(true);
        }

        rgSortAnnouncement.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_sort_announcement_newest :
                        sortFromNewest = true;
                        break;
                    case R.id.rb_sort_announcement_oldest :
                        sortFromNewest = false;
                        break;
                }
            }
        });

        rgSortAgenda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_prev_next:
                        previousNextAgenda = true;
                        break;
                    case R.id.rb_sort_announcement_oldest :
                        previousNextAgenda = false;
                        break;
                }
            }
        });

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("sortFromNewest",sortFromNewest);
                intent.putExtra("previousNextAgenda",previousNextAgenda);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
