package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnnouncementDetailActivity extends AppCompatActivity {

    private TextView tvAnnouncementTitle,tvAnnouncementDesc, tvDateAndTime, tvUserName, toolbarTitle;
    private ImageView ivUserPhoto;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);

        tvAnnouncementTitle = findViewById(R.id.tv_announcement_title);
        tvAnnouncementDesc = findViewById(R.id.tv_announcement_desc);
        tvDateAndTime = findViewById(R.id.tv_date_and_time);
        tvUserName = findViewById(R.id.tv_user_name);
        ivUserPhoto = findViewById(R.id.iv_user_photo);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarTitle.setText(getIntent().getStringExtra("groupName"));
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Announcement announcement = getIntent().getParcelableExtra("announcement");
        tvAnnouncementTitle.setText(announcement.getAnnouncementTitle());;
        tvAnnouncementDesc.setText(announcement.getAnnouncementDesc());
        tvDateAndTime.setText(parseDateToddMMMMyyyyhhmma(announcement.getDateAndTime()));
        tvUserName.setText(announcement.getUserName());
//        Glide.with(this).load(Uri.parse(announcement.getUserPhoto())).into(ivUserPhoto);
    }

    private String parseDateToddMMMMyyyyhhmma(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
            return new SimpleDateFormat("dd MMMM yyyy hh:mm a").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
