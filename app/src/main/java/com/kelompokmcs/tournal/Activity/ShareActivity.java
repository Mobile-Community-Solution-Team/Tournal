package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.kelompokmcs.tournal.R;

public class ShareActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvGroupCode, tvGroupPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        toolbar = findViewById(R.id.toolbar);
        tvGroupCode = findViewById(R.id.tv_group_code);
        tvGroupPass = findViewById(R.id.tv_group_pass);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvGroupCode.setText(getIntent().getStringExtra("groupCode"));
        tvGroupPass.setText(getIntent().getStringExtra("groupPass"));
    }
}
