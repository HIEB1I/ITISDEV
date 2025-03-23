package com.mobdeve.sustainabite;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;
import java.util.Calendar;

public class DOE_date_handler {

    private final Context context;
    private final EditText editText;
    private final Calendar calendar;

    public DOE_date_handler(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
        this.calendar = Calendar.getInstance();

        this.editText.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            editText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}

