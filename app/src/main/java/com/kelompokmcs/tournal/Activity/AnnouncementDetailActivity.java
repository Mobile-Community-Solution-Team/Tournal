package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

public class AnnouncementDetailActivity extends AppCompatActivity {

    private TextView tvAnnouncementTitle,tvAnnouncementDesc, tvDateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);

        tvAnnouncementTitle = findViewById(R.id.tv_announcement_title);
        tvAnnouncementDesc = findViewById(R.id.tv_announcement_desc);
        tvDateAndTime = findViewById(R.id.tv_date_and_time);

        Announcement announcement = getIntent().getParcelableExtra("announcement");
        tvAnnouncementTitle.setText(announcement.getAnnouncementTitle());;
        tvAnnouncementDesc.setText(announcement.getAnnouncementDesc());
        tvDateAndTime.setText(announcement.getDateAndTime());
    }
}
