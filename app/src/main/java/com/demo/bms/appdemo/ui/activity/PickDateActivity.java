package com.demo.bms.appdemo.ui.activity;

import android.os.Bundle;

import com.demo.bms.appdemo.R;
import com.demo.bms.appdemo.support.Constants;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by bms on 16-12-15.
 */

public class PickDateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_pick_date;
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar nextDay = Calendar.getInstance();
        nextDay.add(Calendar.DAY_OF_YEAR, 1);

        CalendarPickerView calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        assert calendarPickerView != null;
        calendarPickerView.init(Constants.Dates.birthday, nextDay.getTime())
                .withSelectedDate(Calendar.getInstance().getTime());

        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener()
        {
            @Override
            public void onDateSelected(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        calendarPickerView.setOnInvalidDateSelectedListener(date -> {
            if (date.after(new Date())) {
                showSnackBar(R.string.not_coming);
            } else {
                showSnackBar(R.string.not_born);
            }
        });

    }
}
