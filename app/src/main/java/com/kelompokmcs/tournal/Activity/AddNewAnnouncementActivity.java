package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.API.VolleySingleton;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewAnnouncementActivity extends AppCompatActivity implements RequestResult {

    private TextInputEditText etAnnouncementTitle, etAnnouncementDesc;
    private Button btnAddAnnouncement;
    private Toolbar toolbar;
    private LinearLayout loadingLayout;
    private APIRequest apiRequest;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_announcement);

        etAnnouncementTitle = findViewById(R.id.et_announcement_title);
        etAnnouncementDesc = findViewById(R.id.et_announcement_desc);
        btnAddAnnouncement = findViewById(R.id.btn_add_announcement);
        toolbar = findViewById(R.id.toolbar);
        loadingLayout = findViewById(R.id.loading_layout);
        apiRequest = new APIRequest(this);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        groupId = getIntent().getIntExtra("group_id",-1);

        btnAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String announcementTitle = etAnnouncementTitle.getText().toString();
                String announcementDesc = etAnnouncementDesc.getText().toString();

                validateAddAnnouncementForm(announcementTitle,announcementDesc);
            }
        });
    }

    private void validateAddAnnouncementForm(String announcementTitle, String announcementDesc) {

        clearErrorMessage();

        if(announcementTitle.isEmpty()){
            etAnnouncementTitle.requestFocus();
            etAnnouncementTitle.setError("Announcement Title must be filled");
        }
        else if(announcementDesc.isEmpty()){
            etAnnouncementDesc.requestFocus();
            etAnnouncementDesc.setError("Announcement Description must be filled");
        }
        else{
            loadingLayout.setVisibility(View.VISIBLE);
            try {
                GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(AddNewAnnouncementActivity.this);
                JsonObjectRequest request = apiRequest.addAnnouncement(groupId,announcementTitle,announcementDesc,user.getId());
                VolleySingleton.getInstance(AddNewAnnouncementActivity.this).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearErrorMessage() {
        etAnnouncementTitle.setError(null);
        etAnnouncementDesc.setError(null);
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("addAnnouncement")){
            Toast.makeText(this, "Success to add announcement", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        }
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        if(requestType.equals("addAnnouncement")){
            Toast.makeText(this, "Failed to add announcement", Toast.LENGTH_LONG).show();
        }
        loadingLayout.setVisibility(View.GONE);
    }
}
