package com.kelompokmcs.tournal.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.Database.DBTransaction;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.Model.Group;
import com.kelompokmcs.tournal.R;
import com.kelompokmcs.tournal.API.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateGroupActivity extends AppCompatActivity implements RequestResult {

    private TextInputEditText etGroupName, etGroupLocation, etStartDate, etEndDate, etGroupPassword, etConfirmGroupPassword;
    private Button btnCreateGroup;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    private DBTransaction dbTransaction;
    private APIRequest apiRequest;
    private Toolbar toolbar;
    private LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        etGroupName = findViewById(R.id.et_group_name);
        etGroupLocation = findViewById(R.id.et_group_location);
        etStartDate =  findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etGroupPassword = findViewById(R.id.et_group_pass);
        etConfirmGroupPassword = findViewById(R.id.et_confirm_group_pass);
        btnCreateGroup = findViewById(R.id.btn_create_group);
        toolbar = findViewById(R.id.toolbar);
        loadingLayout = findViewById(R.id.loading_layout);
        dbTransaction = new DBTransaction(this);
        apiRequest = new APIRequest(this);

        final Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR);
        startMonth = c.get(Calendar.MONTH);
        startDay = c.get(Calendar.DAY_OF_MONTH);
        endYear = startYear;
        endMonth = startMonth;
        endDay = startDay;

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateGroupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startYear = i;
                        startMonth = i1;
                        startDay = i2;
                        checkStartDateAndEndDate();
                        etStartDate.setText(parseDateToddMMMMyyyy(startYear,startMonth,startDay));
                    }
                }, startYear,startMonth,startDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateGroupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        endYear = i;
                        endMonth = i1;
                        endDay = i2;
                        checkStartDateAndEndDate();
                        etEndDate.setText(parseDateToddMMMMyyyy(endYear,endMonth,endDay));
                    }
                }, endYear,endMonth,endDay);
                Calendar startDate = Calendar.getInstance();
                startDate.set(startYear,startMonth,startDay);
                datePickerDialog.getDatePicker().setMinDate(startDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = etGroupName.getText().toString();
                String groupLocation = etGroupLocation.getText().toString();
                String startDate = startYear+"-"+startMonth+"-"+startDay;
                String endDate = endYear+"-"+endMonth+"-"+endDay;
                String groupPassword = etGroupPassword.getText().toString();
                String confirmGroupPassword = etConfirmGroupPassword.getText().toString();

                validateCreateGroupForm(groupName,groupLocation,startDate,endDate,groupPassword,confirmGroupPassword);
            }
        });
    }

    private void checkStartDateAndEndDate() {
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startYear+"-"+startMonth+"-"+startDay);
            Date endDate =  new SimpleDateFormat("yyyy-MM-dd").parse(endYear+"-"+endMonth+"-"+endDay);

            if(startDate.after(endDate)){
                //swipe startDate dan endDate jika startDate setelah endDate
                int a = startYear;
                int b = startMonth;
                int c = startDay;
                startYear = endYear;
                startMonth = endMonth;
                startDay = endDay;
                endYear = a;
                endMonth = b;
                endDay = c;
                etStartDate.setText(parseDateToddMMMMyyyy(startYear,startMonth,startDay));
                etEndDate.setText(parseDateToddMMMMyyyy(endYear,endMonth,endDay));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String parseDateToddMMMMyyyy(int i, int i1, int i2) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(i+"-"+i1+"-"+i2);
            return new SimpleDateFormat("dd MMMM yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void validateCreateGroupForm(String groupName, String groupLocation, String startDate, String endDate, String groupPassword, String confirmGroupPassword) {

        clearErrorMessage();

        if(groupName.isEmpty()){
            etGroupName.requestFocus();
            etGroupName.setError("Group name must be filled");
        }
        else if(groupLocation.isEmpty()){
            etGroupLocation.requestFocus();
            etGroupLocation.setError("Activity location must be filled");
        }
        else if(startDate.isEmpty()){
            Toast.makeText(this, "Please select start date", Toast.LENGTH_SHORT).show();
            etStartDate.setError("Please select start date");
        }
        else if(endDate.isEmpty()){
            Toast.makeText(this, "Please select end date", Toast.LENGTH_SHORT).show();
            etStartDate.setError("Please select end date");
        }
        else if(groupPassword.isEmpty()){
            etGroupPassword.requestFocus();
            etGroupPassword.setError("Password must be filled");
        }
        else if(groupPassword.length() < 8){
            etGroupPassword.requestFocus();
            etGroupPassword.setError("Password must be at least 8 character");
        }
        else if(!groupPassword.equals(confirmGroupPassword)){
            etConfirmGroupPassword.requestFocus();
            etConfirmGroupPassword.setError("Confirmation password must be the same with password");
        }
        else{
            loadingLayout.setVisibility(View.VISIBLE);
            try {
                GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(CreateGroupActivity.this);
                JsonObjectRequest request = apiRequest.createNewGroupRequest(user.getId(),groupName,groupLocation, startDate, endDate, groupPassword);
                VolleySingleton.getInstance(CreateGroupActivity.this).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearErrorMessage() {
        etGroupName.setError(null);
        etGroupLocation.setError(null);
        etStartDate.setError(null);
        etEndDate.setError(null);
        etGroupPassword.setError(null);
        etConfirmGroupPassword.setError(null);
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("createNewGroup")){
            try {
                //jika berhasil, maka akan mendapatkan response berupa data dari group yang barusan dibuat
                JSONArray jsonArray = response.getJSONArray("response");
                JSONObject groupDatas = jsonArray.getJSONObject(0);

                int groupId = groupDatas.getInt("group_id");
                String groupCode = groupDatas.getString("group_code");
                String groupName = groupDatas.getString("group_name");
                String groupLocation = groupDatas.getString("group_location");
                String startDate = groupDatas.getString("start_date");
                String endDate = groupDatas.getString("end_date");
                String groupPass = groupDatas.getString("group_pass");

                Group newGroup = new Group(groupId,groupCode,groupName,groupLocation,startDate,endDate,groupPass);

                subscribeToTopic(groupId);

                dbTransaction.addNewGroup(newGroup);

                Toast.makeText(CreateGroupActivity.this,"Successfull Create a Group",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(CreateGroupActivity.this, GroupActivity.class);
                i.putExtra("group_id",groupId);
                startActivity(i);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void notifyError(String requestType, VolleyError error) {
        if(requestType.equals("createNewGroup")){
            Toast.makeText(CreateGroupActivity.this, "Failed to create new group", Toast.LENGTH_SHORT).show();
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
