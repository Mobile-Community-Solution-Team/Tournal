package com.kelompokmcs.tournal.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.API.VolleySingleton;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity implements RequestResult {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private APIRequest apiRequest;
    private DBTransaction dbTransaction;
    private LinearLayout loadingLayout;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButton = findViewById(R.id.sign_in_button);
        apiRequest = new APIRequest(this);
        dbTransaction = new DBTransaction(this);
        loadingLayout = findViewById(R.id.loading_layout);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent i = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(i,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //tampilkan loading layout
            loadingLayout.setVisibility(View.VISIBLE);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            JsonObjectRequest request = apiRequest.addNewUser(account.getId(),account.getDisplayName(),account.getEmail(), String.valueOf(account.getPhotoUrl()));
            VolleySingleton.getInstance(this).addToRequestQueue(request);

        } catch (ApiException e) {
            Toast.makeText(this, "Failed to sign in. Please try again", Toast.LENGTH_SHORT).show();
            //hilangkan loading layout
            loadingLayout.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("addNewUser")){
            try {
                //jika belum pernah memakai aplikasi Tournal, maka tidak perlu mengambil data user dari server
                if(response.getString("response").equals("Success add new user data")){
                    Intent i = new Intent(SignInActivity.this,IntroActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(response.getString("response").equals("Sign in using exist user data")){
                    //sebaliknya jika sudah pernah, maka akan dilakukan sync data user
                    JsonObjectRequest request = apiRequest.getGroupInformationForUser(account.getId());
                    VolleySingleton.getInstance(this).addToRequestQueue(request);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getGroupInformationForUser")){
            ArrayList<Group> groupArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = response.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject groupDatas = jsonArray.getJSONObject(i);

                    int groupId = groupDatas.getInt("group_id");
                    String groupCode = groupDatas.getString("group_code");
                    String groupName = groupDatas.getString("group_name");
                    String groupLocation = groupDatas.getString("group_location");
                    String startDate = groupDatas.getString("start_date");
                    String endDate = groupDatas.getString("end_date");
                    String groupPass = groupDatas.getString("group_pass");

                    subscribeToTopic(groupId);
                    groupArrayList.add(new Group(groupId,groupCode,groupName,groupLocation,startDate,endDate,groupPass));
                }
                dbTransaction.addGroupDatas(groupArrayList);
                JsonObjectRequest request = apiRequest.getAnnouncementInformationForUser(account.getId());
                VolleySingleton.getInstance(this).addToRequestQueue(request);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getAnnouncementInformationForUser")){
            ArrayList<Announcement> announcementArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = response.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject announcementDatas = jsonArray.getJSONObject(i);
                    int announcementId = announcementDatas.getInt("id");
                    int groupId = announcementDatas.getInt("group_id");
                    String announcementTitle = announcementDatas.getString("announcement_title");
                    String announcementDesc = announcementDatas.getString("announcement_desc");
                    String dateAndTime = announcementDatas.getString("date_and_time");
                    String userName = announcementDatas.getString("user_name");
                    String userEmail = announcementDatas.getString("user_email");
                    String userPhoto = announcementDatas.getString("user_photo");

                    announcementArrayList.add(new Announcement(announcementId,groupId,announcementTitle,announcementDesc,dateAndTime,userName,userEmail,userPhoto));
                }

                dbTransaction.addAnnouncementDatas(announcementArrayList);
                JsonObjectRequest request = apiRequest.getAgendaInformationForUser(account.getId());
                VolleySingleton.getInstance(this).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getAgendaInformationForUser")){
            ArrayList<Agenda> agendaArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = response.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject agendaDatas = jsonArray.getJSONObject(i);
                    int agendaId = agendaDatas.getInt("agenda_id");
                    int groupId = agendaDatas.getInt("group_id");
                    String agendaTitle = agendaDatas.getString("agenda_title");
                    String agendaDesc = agendaDatas.getString("agenda_desc");
                    double startLat = agendaDatas.getDouble("start_lat");
                    double startLng = agendaDatas.getDouble("start_lng");
                    double startAlt = agendaDatas.getDouble("start_alt");
                    double endLat = agendaDatas.getDouble("end_lat");
                    double endLng = agendaDatas.getDouble("end_lng");
                    double endAlt = agendaDatas.getDouble("end_alt");
                    String startTime = agendaDatas.getString("start_time");
                    String endTime = agendaDatas.getString("end_time");

                    agendaArrayList.add(new Agenda(agendaId,groupId,"",agendaTitle,agendaDesc,startLat,startLng,startAlt,endLat,endLng,endAlt,startTime,endTime));
                }
                dbTransaction.addAgendaDatas(agendaArrayList);

                Intent i = new Intent(SignInActivity.this,IntroActivity.class);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        if(requestType.equals("addNewUser")){
            Toast.makeText(this, "Failed to sign in. Please try again", Toast.LENGTH_SHORT).show();
        }
        else if(requestType.equals("getGroupInformationForUser")){
            Toast.makeText(this, "Failed to get group information", Toast.LENGTH_SHORT).show();
        }
        else if(requestType.equals("getAnnouncementInformationForUser")){
            Toast.makeText(this, "Failed to get announcement information", Toast.LENGTH_SHORT).show();
        }
        else if(requestType.equals("getAgendaInformationForUser")){
            Toast.makeText(this, "Failed to get agenda information", Toast.LENGTH_SHORT).show();
        }
        //hilangkan loading layout
        loadingLayout.setVisibility(View.GONE);
        //logout kembali jika tidak terkoneksi dengan server
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SignInActivity.this, gso);
        mGoogleSignInClient.signOut();

    }

    private void subscribeToTopic(int groupId) {
        //notification for 1 group
        FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(groupId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe Failed";
                        }
                        Log.d("Notification", msg);
                    }});
    }
}
