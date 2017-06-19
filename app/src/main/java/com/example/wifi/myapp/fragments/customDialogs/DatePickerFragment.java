package com.example.wifi.myapp.fragments.customDialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.wifi.myapp.R;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int option;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.option = bundle.getInt("option");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        month += 1;

        if(this.option == 1) {
            EditText editTextStartDate = (EditText) getActivity().findViewById(R.id.editTextStartDate);
            editTextStartDate.setText(String.valueOf(year + "-" + month + "-" + day));
        }else{
            EditText editTextEndDate = (EditText) getActivity().findViewById(R.id.editTextEndDate);
            editTextEndDate.setText(String.valueOf(year + "-" + month + "-" + day));
        }
    }


}
