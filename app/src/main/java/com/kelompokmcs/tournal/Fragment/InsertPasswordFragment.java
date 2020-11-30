package com.kelompokmcs.tournal.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.Activity.GroupActivity;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.Model.Agenda;
import com.kelompokmcs.tournal.Model.Announcement;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.Model.Symbol;
import com.kelompokmcs.tournal.R;
import com.kelompokmcs.tournal.API.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InsertPasswordFragment extends Fragment implements RequestResult {

    private TextView tvGroupName;
    private Button btnLetsGo;
    private EditText etGroupPassword;
    private String groupName;
    private int groupId;
    private LinearLayout loadingLayout;
    private APIRequest apiRequest;
    private DBTransaction dbTransaction;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_password, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        View rootView = getView();
        tvGroupName = rootView.findViewById(R.id.tv_group_name);
        etGroupPassword = rootView.findViewById(R.id.et_group_pass);
        btnLetsGo = rootView.findViewById(R.id.btn_lets_go);
        loadingLayout = rootView.findViewById(R.id.loading_layout);
        toolbar = rootView.findViewById(R.id.toolbar);
        apiRequest = new APIRequest(this);
        dbTransaction = new DBTransaction(getContext());

        tvGroupName.setText(groupName);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JsonObjectRequest request = apiRequest.verifyGroupPasswordRequest(GoogleSignIn.getLastSignedInAccount(getContext()).getId(), groupId, etGroupPassword.getText().toString());

                    loadingLayout.setVisibility(View.VISIBLE);
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setGroupData(Group groupData) {
        groupName = groupData.getGroupName();
        groupId = groupData.getGroupId();
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("verifyGroupPassword")){
            try {
                String status = response.getString("response");
                if(status.equals("verified")){
                    JsonObjectRequest request = apiRequest.getGroupInformation(groupId);
                    VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
                }
                else if(status.equals("Already Join This Group")){
                    Toast.makeText(getContext(), "You already join the group", Toast.LENGTH_LONG).show();
                    loadingLayout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getGroupInformation")){
            try {
                JSONArray jsonArray = response.getJSONArray("response");
                JSONObject groupDatas = jsonArray.getJSONObject(0);

                int groupId = groupDatas.getInt("group_id");
                String groupCode = groupDatas.getString("group_code");
                String groupName = groupDatas.getString("group_name");
                String groupLocation = groupDatas.getString("group_location");
                String startDate = groupDatas.getString("start_date");
                String endDate = groupDatas.getString("end_date");
                String groupPass = groupDatas.getString("group_pass");


                dbTransaction.addNewGroup(new Group(groupId,groupCode,groupName,groupLocation,startDate,endDate,groupPass));

                JsonObjectRequest request = apiRequest.getAnnouncementInformation(groupId);
                VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getAnnouncementInformation")){
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
                JsonObjectRequest request = apiRequest.getAgendaInformation(groupId);
                VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getAgendaInformation")){
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
                JsonObjectRequest request = apiRequest.getSymbolInformation(groupId);
                VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestType.equals("getSymbolInformation")){
            ArrayList<Symbol> symbolArrayList = new ArrayList<>();
            try {
                JSONArray jsonArray = response.getJSONArray("response");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject symbolDatas = jsonArray.getJSONObject(i);
                    int id = symbolDatas.getInt("id");
                    int groupId = symbolDatas.getInt("group_id");
                    double lat = symbolDatas.getDouble("lat");
                    double lng = symbolDatas.getDouble("lng");
                    double alt = symbolDatas.getDouble("alt");
                    int symbolId = symbolDatas.getInt("symbol_id");

                    symbolArrayList.add(new Symbol(id,groupId,lat,lng,alt,symbolId));
                }

                dbTransaction.addSymbolDatas(symbolArrayList);
                loadingLayout.setVisibility(View.GONE);

                subscribeToTopic(groupId);
                Toast.makeText(getContext(),"Successfull join to Group "+groupName,Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), GroupActivity.class);
                i.putExtra("group_id",groupId);
                startActivity(i);
                getActivity().finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        if(requestType.equals("verifyGroupPassword")){
            Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
            Log.e("text",error.toString());
        }
        else if(requestType.equals("getGroupInformation")){
            Toast.makeText(getContext(), "Failed to Get Group Information", Toast.LENGTH_SHORT).show();
            Log.e("text",error.toString());
        }
        else if(requestType.equals("getAnnouncementInformation")){
            Toast.makeText(getContext(), "Failed to Get Announcement Information"+ requestType, Toast.LENGTH_SHORT).show();
            Log.e("text",error.toString());
        }
        else if(requestType.equals("getAgendaInformation")){
            Toast.makeText(getContext(), "Failed to Get Agenda Information"+ requestType, Toast.LENGTH_SHORT).show();
            Log.e("text",error.toString());
        }
        loadingLayout.setVisibility(View.GONE);
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
