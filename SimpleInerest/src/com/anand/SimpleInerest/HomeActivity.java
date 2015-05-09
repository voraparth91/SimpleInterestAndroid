package com.anand.SimpleInerest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends Activity {
    private Calendar cal;
    private EditText amount;
    private EditText roi;
    private EditText startDate;
    private EditText endDate;
    private EditText etDays;

    private Button pickStart;
    private Button pickEnd;
    private Button calculate;
    private TextView statusInterest;
    private TextView statusTotal;
    private CheckBox checkDays;

    private int year;
    private int Eyear;
    private int month;
    private int Emonth;
    private int day;
    private int Eday;

    static final int START_DATE_DIALOG_ID = 123;
    static final int END_DATE_DIALOG_ID = 321;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initializeAllViews();
        initializeDatePickers();

        addListenerOnButton();
        setCalculateButton();
    }

    public void setCalculateButton() {
        calculate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getEditableText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Amount", Toast.LENGTH_LONG).show();
                    amount.requestFocus();
                    return;
                }
                if (roi.getEditableText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Rate Of Interest in Percentage", Toast.LENGTH_LONG).show();
                    roi.requestFocus();
                    return;
                }
                double P = Double.parseDouble(amount.getEditableText().toString().trim());
                double R = Double.parseDouble(roi.getEditableText().toString().trim());

                int D = 0;
                if(checkDays.isChecked()){
                    if (etDays.getEditableText().toString().trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter Number Of Days to be Charged", Toast.LENGTH_LONG).show();
                        etDays.requestFocus();
                        return;
                    }
                    D = Integer.parseInt(etDays.getEditableText().toString().trim());
                    Toast.makeText(getApplicationContext(), "Days to be Charged is " + D, Toast.LENGTH_LONG).show();
                    double I = (P * R * D) / 36500;
                    double T = P + I;
                    String interestStr = String.format("Interest to be colleted is INR %.2f", I);
                    statusInterest.setText(interestStr);
                    String totalStr = String.format("Total to be colleted is INR %.2f", T);
                    statusTotal.setText(totalStr);
                }else {
                    Date myStartDate;
                    Date myEndDate;
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        myStartDate = df.parse(startDate.getEditableText().toString().trim());
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Please Enter a Valid Start Date", Toast.LENGTH_LONG).show();
                        startDate.requestFocus();
                        return;
                    }

                    try {
                        myEndDate = df.parse(endDate.getEditableText().toString().trim());
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Please Enter a Valid End Date", Toast.LENGTH_LONG).show();
                        endDate.requestFocus();
                        return;
                    }
                    if (myEndDate.getTime() < myStartDate.getTime()) {
                        Toast.makeText(getApplicationContext(), "End Date should be greater than Start Date", Toast.LENGTH_LONG).show();
                        endDate.requestFocus();
                        return;
                    }

                    long diff = myEndDate.getTime() - myStartDate.getTime();
                    D = (int) (diff / 1000L / 60L / 60L / 24L);
                    Toast.makeText(getApplicationContext(), "Days to be Charged is " + D, Toast.LENGTH_LONG).show();
                    etDays.setText(String.valueOf(D));
                    double I = (P * R * D) / 36500;
                    double T = P + I;
                    String interestStr = String.format("Interest to be colleted is INR %.2f", I);
                    statusInterest.setText(interestStr);
                    String totalStr = String.format("Total to be colleted is INR %.2f", T);
                    statusTotal.setText(totalStr);
                }
            }
        });
    }

    public void initializeDatePickers() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        Eyear = cal.get(Calendar.YEAR);
        Emonth = cal.get(Calendar.MONTH);
        Eday = cal.get(Calendar.DAY_OF_MONTH);
    }

    public void initializeAllViews() {
        amount = (EditText) findViewById(R.id.etAmount);
        roi = (EditText) findViewById(R.id.etInterest);
        startDate = (EditText) findViewById(R.id.etStart);
        endDate = (EditText) findViewById(R.id.etEnd);
        pickStart = (Button) findViewById(R.id.buttonStart);
        pickEnd = (Button) findViewById(R.id.buttonEnd);
        calculate = (Button) findViewById(R.id.buttonCalculate);
        statusInterest = (TextView) findViewById(R.id.statusInterest);
        statusTotal = (TextView) findViewById(R.id.statusTotal);
        checkDays = (CheckBox) findViewById(R.id.checkDays);
        etDays = (EditText) findViewById(R.id.etDays);
        etDays.setEnabled(false);
        checkDays.setChecked(false);
        checkDays.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etDays.setEnabled(true);
                } else {
                    etDays.setEnabled(false);
                }
            }
        });

    }

    public void addListenerOnButton() {
        pickStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(START_DATE_DIALOG_ID);
            }
        });
        pickEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(END_DATE_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
            case END_DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, EdatePickerListener,
                        Eyear, Emonth, Eday);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            startDate.setText(new StringBuilder().append(day)
                    .append("/").append(month + 1).append("/").append(year)
                    .append(" "));
        }
    };

    private DatePickerDialog.OnDateSetListener EdatePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            Eyear = selectedYear;
            Emonth = selectedMonth;
            Eday = selectedDay;

            endDate.setText(new StringBuilder().append(Eday)
                    .append("/").append(Emonth + 1).append("/").append(Eyear)
                    .append(" "));
        }
    };
}
