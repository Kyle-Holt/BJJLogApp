package com.example.kyle.bjjlogapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        Calendar calendarDate = Calendar.getInstance();
        int currentMonth = calendarDate.get(Calendar.MONTH);
        setTitle(getMonthForInt(currentMonth));
        //setTitle(getMonthForInt(currentMonth));
        //setTitle(calendarDate.MONTH);

        calendar = (CalendarView) findViewById(R.id.calendar_id);

        calendar.setShowWeekNumber(false);

        calendar.setFirstDayOfWeek(2);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int correctMonth = month+1;

                setTitle(getMonthForInt(correctMonth-1));
                Intent intent = new Intent(CalendarActivity.this, TrainingLogDate.class);
                intent.putExtra("Date", dayOfMonth + "/" + correctMonth + "/" + year);
                startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_stats) {
            startActivity(new Intent(this, StatsActivity.class));

            //getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment, mFragment).commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public int[] intersection(int[] a, int[] b) {

        List<Integer> resultList = new ArrayList<Integer>();
        for (int i = 0; i < a.length; i++) {
            int c = a[i];
            for (int j = 0; j < b.length; j++) {
                if(c == b[j] && (resultList.contains(c) == false)) {
                    resultList.add(c);
                }
            }
        }
        int[] result = new int[resultList.size()];
        Iterator<Integer> iterator = resultList.iterator();
        for (int i = 0; i < result.length; i++) {
            result[i] = iterator.next().intValue();
        }
        return result;
    }

}


