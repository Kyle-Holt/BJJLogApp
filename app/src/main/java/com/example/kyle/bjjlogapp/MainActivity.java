package com.example.kyle.bjjlogapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    DatabaseHelper myDb;
    float averageKimuraHit;
    float averageKeylockHit;
    float averageArmbarHit;
    float averageRNCHit;
    float averageTriangleHit;
    float averageStraightAnkleHit;
    ArrayList<String> labels;
    ArrayList<String> newLabels;
    String subViewSelection;
    PieChart pieChart;

    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requires context, calls the constructor
        myDb = new DatabaseHelper(this);
        this.mActivity = this;
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setIcon(R.mipmap.ic_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        subViewSelection= getIntent().getStringExtra("getData");

        //getSubData(value);

        if (subViewSelection == null) {
            getSubData("subsHit");
        } else {
            Log.d(subViewSelection,"VALUE");
            getSubData(subViewSelection);
        }

/*
        if (value.equals("subsHit") || value.equals("subsHitBy")) {
            getSubData(value);
        } else {
            getSubData("subsHit");
        }
        */

        pieChart = (PieChart) findViewById(R.id.chart);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, MainPieSettings.class));
                finish();
            }
        });

        //Though averages are not required for the pie chart, they also do not change the values of the chart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(averageKimuraHit, 0));
        entries.add(new Entry(averageKeylockHit, 1));
        entries.add(new Entry(averageArmbarHit, 2));
        entries.add(new Entry(averageRNCHit, 3));
        entries.add(new Entry(averageTriangleHit, 4));
        entries.add(new Entry(averageStraightAnkleHit, 5));

        labels = new ArrayList<String>();
        labels.add("Kimura");
        labels.add("Keylock");
        labels.add("Armbar");
        labels.add("RNC");
        labels.add("Triangle");
        labels.add("Straight Ankle");

      PieDataSet dataset = new PieDataSet(entries, "");
      PieData data = new PieData(labels, dataset);
        ArrayList<Entry> newEntries = entries;
        newLabels = labels;

        List<Entry> toRemove = new ArrayList<>();
        for (Entry e : entries) {
            if(e.getVal() == 0) {
                toRemove.add(e);
            }
        }

        for (Entry e: toRemove) {
            newLabels.remove(newEntries.indexOf(e));
            newEntries.remove(e);

        }

        dataset = new PieDataSet(newEntries, "");
        data = new PieData(newLabels, dataset);

        int [] colorSet = {Color.parseColor("#EB97FF"), Color.parseColor("#FF9797"), +
                Color.parseColor("#97F0FF"), Color.parseColor("#FFD297"), Color.parseColor("#F9FF97"), Color.parseColor("#B1FF97")};
        dataset.setColors(colorSet);
        data.setValueTextSize(15f);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(10f);
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleRadius(10f);
        pieChart.setUsePercentValues(true);
        pieChart.setData(data);
        pieChart.setOnChartValueSelectedListener(this);

        Legend l = pieChart.getLegend();
        if (subViewSelection == null) {
            l.setCustom(new int[] {Color.TRANSPARENT}, new String[] {"SubsHit"});
        } else {
            l.setCustom(new int[] {Color.TRANSPARENT}, new String[] {subViewSelection});
        }
        l.setTextSize(20f);

        pieChart.animateY(2000);

    }

    public void getSubData(String hit) {
        //if we have an object known as submission, we can use a name column and possibly have this as one line of code
        averageKimuraHit = myDb.getColumnAverage("Kimura", hit);
        averageKeylockHit = myDb.getColumnAverage("Keylock", hit);
        averageArmbarHit = myDb.getColumnAverage("Armbar", hit);
        averageRNCHit = myDb.getColumnAverage("RNC", hit);
        averageTriangleHit = myDb.getColumnAverage("Triangle", hit);
        averageStraightAnkleHit = myDb.getColumnAverage("Straight Ankle", hit);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            if (myDb.doesDatabaseExist(this) == true) {
                startActivity(new Intent(this, StatsActivity.class));
            }
    //getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment, mFragment).commit();
            return true;
        }
        if (id == R.id.calendar_add) {
            startActivity(new Intent(this, CalendarAct.class));
            return true;
        }

        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        int columnValue = 1;

        String title = labels.get(e.getXIndex());
        Log.d("Selected: " + title + ", dataSet: " + dataSetIndex, "HIT ME UP");
        if (subViewSelection != null) {
            if (subViewSelection.equals("SubsHit")) {
                columnValue = 0;
                float subLastDay = changeFromAverage(title, columnValue);
                Toast toast = Toast.makeText(this, "Average " + title + " per day: " +Float.toString(subLastDay), Toast.LENGTH_SHORT);
                toast.show();
            } else if (subViewSelection.equals("SubsHitBy")) {
                columnValue = 1;
                float subLastDay = changeFromAverage(title, columnValue);
                Toast toast = Toast.makeText(this, "Average " + title + " caught in per day: " +Float.toString(subLastDay), Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }


    public float changeFromAverage(String sub, int columnPosition) {
        float change = 0;
        String lastDay = myDb.getLastestDate();
        int lastSubs = myDb.getSubmissionPerDay(sub, columnPosition, lastDay);
        Log.d(Integer.toString(lastSubs), "SUB");
        Log.d(Float.toString(averageStraightAnkleHit), "SUB");

        //consider making an object called submissions and extend it for each submission type
        if(sub.equals("Armbar")) {
            //change = roundTwoDecimals(((lastSubs/averageArmbarHit) - 1)*100);
            change = averageArmbarHit;
        } else if(sub.equals("Kimura")) {
           // change = roundTwoDecimals(((lastSubs/averageKimuraHit) - 1)*100);
            change = averageKimuraHit;
        } else if(sub.equals("Keylock")) {
          //  change = roundTwoDecimals(((lastSubs/averageKeylockHit) - 1)*100);
            change = averageKeylockHit;
        } else if(sub.equals("RNC")) {
            //change = roundTwoDecimals(((lastSubs/averageRNCHit) - 1)*100);
            change = averageRNCHit;
        } else if(sub.equals("Triangle")) {
            //change = roundTwoDecimals(((lastSubs/averageTriangleHit) - 1)*100);
            change = averageTriangleHit;
        } else if(sub.equals("Straight Ankle")) {
           // change = roundTwoDecimals(((lastSubs / averageStraightAnkleHit) - 1) * 100);
            change = averageStraightAnkleHit;
        }

        return change;
    }

    float roundTwoDecimals(float d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Float.valueOf(twoDForm.format(d));
    }

    @Override
    public void onNothingSelected() {

    }

}
