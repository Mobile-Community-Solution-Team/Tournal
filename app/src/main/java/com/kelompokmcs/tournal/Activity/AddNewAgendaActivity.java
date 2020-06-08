package com.kelompokmcs.tournal.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.kelompokmcs.tournal.API.APIRequest;
import com.kelompokmcs.tournal.API.VolleySingleton;
import com.kelompokmcs.tournal.Listener.RequestResult;
import com.kelompokmcs.tournal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewAgendaActivity extends AppCompatActivity implements RequestResult {

    private Button btnAddAgenda;
    private CardView cvGetLocation;
    private TextInputEditText etAgendaTitle, etStartDate, etStartTime, etEndDate, etEndTime, etLocation;
    private Toolbar toolbar;
    private APIRequest apiRequest;
    private LinearLayout loadingLayout;
    private int REQUEST_CODE = 1;
    private int groupId;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay, startHour, startMinute, endHour, endMinute;
    private double selectedLat, selectedLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_agenda);

        cvGetLocation = findViewById(R.id.cv_get_location);
        btnAddAgenda = findViewById(R.id.btn_add_agenda);
        etAgendaTitle = findViewById(R.id.et_agenda_title);
        etStartDate = findViewById(R.id.et_start_date);
        etStartTime = findViewById(R.id.et_start_time);
        etEndDate = findViewById(R.id.et_end_date);
        etEndTime = findViewById(R.id.et_end_time);
        etLocation = findViewById(R.id.et_location);
        loadingLayout = findViewById(R.id.loading_layout);
        toolbar = findViewById(R.id.toolbar);
        apiRequest = new APIRequest(this);

        groupId = getIntent().getIntExtra("group_id",-1);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Calendar c = Calendar.getInstance();
        startYear = c.get(Calendar.YEAR);
        startMonth = c.get(Calendar.MONTH);
        startDay = c.get(Calendar.DAY_OF_MONTH);
        endYear = startYear;
        endMonth = startMonth;
        endDay = startDay;
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);
        endHour = startHour;
        endMinute = startMinute;

        cvGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddNewAgendaActivity.this,SelectLocationActivity.class);
                startActivityForResult(i,REQUEST_CODE);
            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewAgendaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startYear = i;
                        startMonth = i1;
                        startDay = i2;
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewAgendaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        endYear = i;
                        endMonth = i1;
                        endDay = i2;
                        etEndDate.setText(parseDateToddMMMMyyyy(endYear,endMonth,endDay));
                    }
                },endYear,endMonth,endDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewAgendaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        startHour = i;
                        startMinute = i1;
                        etStartTime.setText(startHour+":"+startMinute);
                    }
                },startHour,startMinute,true);
                timePickerDialog.show();
            }
        });

        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewAgendaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        endHour = i;
                        endMinute = i1;
                        etEndTime.setText(endHour+":"+endMinute);
                    }
                },endHour,endMinute,true);
                timePickerDialog.show();
            }
        });

        btnAddAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String agendaTitle = etAgendaTitle.getText().toString();
                String startDate = etStartDate.getText().toString();
                String startTime = etStartTime.getText().toString();
                String endDate = etEndDate.getText().toString();
                String endTime = etEndTime.getText().toString();
                String location = etLocation.getText().toString();
                validateAddAgendaForm(agendaTitle,startDate,startTime,endDate,endTime,location);
            }
        });
    }

    private void validateAddAgendaForm(String agendaTitle, String startDate, String startTime, String endDate, String endTime, String location) {
        clearErrorMessage();

        if(agendaTitle.isEmpty()){
            etAgendaTitle.requestFocus();
            etAgendaTitle.setError("Agenda Title must be filled");
        }
        else if(startDate.isEmpty()){
            etStartDate.requestFocus();
            etStartDate.setError("Start Date must be filled");
        }
        else if(startTime.isEmpty()){
            etStartTime.requestFocus();
            etStartTime.setError("Start Time must be filled");
        }
        else if(endDate.isEmpty()){
            etEndDate.requestFocus();
            etEndDate.setError("End Date must be filled");
        }
        else if(endTime.isEmpty()){
            etEndTime.requestFocus();
            etEndTime.setError("End Time must be filled");
        }
        else if(location.isEmpty()){
            etLocation.requestFocus();
            etLocation.setError("Location must be selected");
        }
        else{
            loadingLayout.setVisibility(View.VISIBLE);
            try {
                String date1 = startYear+"-"+(startMonth+1)+"-"+startDay+" "+startHour+":"+startMinute;
                String date2 = endYear+"-"+(endMonth+1)+"-"+endDay+" "+endHour+":"+endMinute;
                JsonObjectRequest request = apiRequest.addAgenda(groupId,agendaTitle,selectedLat,selectedLng,date1,date2);
                VolleySingleton.getInstance(AddNewAgendaActivity.this).addToRequestQueue(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearErrorMessage() {
        etAgendaTitle.setError(null);
        etStartDate.setError(null);
        etStartTime.setError(null);
        etEndDate.setError(null);
        etEndTime.setError(null);
        etLocation.setError(null);
    }

    private String parseDateToddMMMMyyyy(int startYear, int startMonth, int startDay) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(startYear+"-"+startMonth+"-"+startDay);
            return new SimpleDateFormat("dd MMMM yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            selectedLat = data.getDoubleExtra("lat",0.0);
            selectedLng = data.getDoubleExtra("lng",0.0);
            etLocation.setText("Lat = "+selectedLat+"\nLng = "+selectedLng);
        }
    }

    @Override
    public void notifySuccess(String requestType, JSONObject response) {
        if(requestType.equals("addAgenda")){
            Toast.makeText(this, "Success to add agenda", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        }
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void notifyError(String requestType, VolleyError error) {
        if(requestType.equals("addAgenda")){
            Toast.makeText(this, "Failed to add agenda", Toast.LENGTH_LONG).show();
        }
        loadingLayout.setVisibility(View.GONE);
    }
}
