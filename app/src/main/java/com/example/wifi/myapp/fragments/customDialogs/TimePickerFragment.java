package com.example.wifi.myapp.fragments.customDialogs;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.wifi.myapp.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int option;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.option = bundle.getInt("option");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        if(option == 1) {
            EditText editTextStartDate = (EditText) getActivity().findViewById(R.id.editTextStartDate);
            String date = editTextStartDate.getText().toString();
            editTextStartDate.setText(date + " " + String.valueOf(hourOfDay + ":" + minute + ":00"));
        }else{
            EditText editTextEndDate = (EditText) getActivity().findViewById(R.id.editTextEndDate);
            String date = editTextEndDate.getText().toString();
            editTextEndDate.setText(date + " " + String.valueOf(hourOfDay + ":" + minute + ":00"));
        }
    }


}
