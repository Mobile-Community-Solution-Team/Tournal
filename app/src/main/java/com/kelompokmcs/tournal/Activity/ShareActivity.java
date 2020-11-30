package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.kelompokmcs.tournal.R;

public class ShareActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvGroupCode, tvGroupPass;
    private Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        toolbar = findViewById(R.id.toolbar);
        tvGroupCode = findViewById(R.id.tv_group_code);
        tvGroupPass = findViewById(R.id.tv_group_pass);
        btnShare = findViewById(R.id.btn_share);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        tvGroupCode.setText(getIntent().getStringExtra("groupCode"));
        tvGroupPass.setText(getIntent().getStringExtra("groupPass"));

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String shareSub = "Invite Your Friends to Join This Group";
                String shareBody = "Hi, Friends. I want to invite you to download Tournal App and join to my group using this credential:\n\nGroup Code = "+getIntent().getStringExtra("groupCode")+"\nGroup Password = "+getIntent().getStringExtra("groupPass");
                i.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                i.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(i,"Share Using"));
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
