package com.kelompokmcs.tournal.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kelompokmcs.tournal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FilterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private boolean showPreviousAgenda, showNextAgenda;
    private CheckBox cbShowPreviousAgenda, cbShowNextAgenda;
    private EditText etStartDate, etEndDate;
    private Button btnFinish;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay, startHour, startMinute, endHour, endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        toolbar = findViewById(R.id.toolbar);
        cbShowPreviousAgenda = findViewById(R.id.cb_previous_agenda);
        cbShowNextAgenda = findViewById(R.id.cb_next_agenda);
        btnFinish = findViewById(R.id.btn_finish_filter);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);

        showPreviousAgenda = getIntent().getBooleanExtra("showPreviousAgenda",false);
        showNextAgenda = getIntent().getBooleanExtra("showNextAgenda",false);

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

        if(showPreviousAgenda){
            cbShowPreviousAgenda.setChecked(true);
        }

        if(showNextAgenda){
            cbShowNextAgenda.setChecked(true);
        }

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        cbShowPreviousAgenda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               showPreviousAgenda = b;
            }
        });

        cbShowNextAgenda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showNextAgenda = b;
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("showPreviousAgenda",showPreviousAgenda);
                intent.putExtra("showNextAgenda",showNextAgenda);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
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

}
