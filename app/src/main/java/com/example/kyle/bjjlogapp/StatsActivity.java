package com.example.kyle.bjjlogapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class StatsActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    DatabaseHelper myDb;
    Map<Date,Integer> m;
    Map<Date, Integer> m1;
    Collection entrySet;
    Iterator it;
    Date date;
    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    ArrayList<BarDataSet> dataSets = null;
    ArrayList<String> xAxis = new ArrayList<>();
    String subViewSelection;
    String subSelection;
    SharedPreferences SP;

    public static Activity sActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        myDb = new DatabaseHelper(this);

        this.sActivity = this;

        subViewSelection= getIntent().getStringExtra("getData");
        subSelection= getIntent().getStringExtra("getSub");

        Log.d(subViewSelection,"VALUE");
        Log.d(subSelection,"VALUE");

        BarChart chart = (BarChart) findViewById(R.id.chart);

        // need to sort the dates (the dates to plot), get submissions per day, plot, possibly hash?
        int subColumn = 0;

        if (subViewSelection != null) {
            if (subViewSelection.trim().equals("SubsHit")) {
                subColumn = 0;
                assignDates(subColumn);
                Log.d(Integer.toString(subColumn), "TESTDUM");
            } else if (subViewSelection.trim().equals("SubsHitBy")) {
                subColumn = 1;
                assignDates(subColumn);
                Log.d(Integer.toString(subColumn), "TESTDUM");
            } else {
                Log.d(subViewSelection, "TESTDUM");
            }
        } else {
            assignDates(subColumn);
        }


        datesToGraphOrdered();

        BarData data = new BarData(getXAxisValues(), getDataSet());
        data.setValueFormatter(new MyValueFormatter());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        chart.setDrawValueAboveBar(true);
        chart.setOnChartValueSelectedListener(this);
        chart.setHighlightPerTapEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                startActivity(new Intent(StatsActivity.this, StatsActivitySettings.class));
            }
        });

    }

    public void datesToGraphOrdered() {
        int counter = 0;
        while(it.hasNext()) {
            Map.Entry me = (Map.Entry)it.next();
            String value = me.getValue().toString();

            valueSet1.add(new BarEntry(Float.parseFloat(value),counter));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String date = formatter.format(me.getKey());
            String newDate = date.substring(0,5);
            xAxis.add(newDate);
            Log.d(Integer.toString(xAxis.size()), "XAXIS");

            counter++;
        }

    }

    public void assignDates(int columnValue) {
        //int columnValue = 0;
        int subsHit = 0;
        m = new HashMap<Date, Integer>();
        for (int i = 0; i < myDb.getNumOfRows() ; i++) {
            //This needs a starting date
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String day = myDb.getRowAndDate(i+1);
            //String day = myDb.getRowAndDate(i);

            if (subSelection == null) {
                subsHit = myDb.getSubmissionPerDay("Kimura", columnValue, day);
            } else {
                subsHit = myDb.getSubmissionPerDay(subSelection, columnValue, day);
            }

            try {
                date = formatter.parse(day);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            m.put(date,subsHit);

        }
        m1 = new TreeMap<>(m);
        Log.d(Integer.toString(m1.size()), "TREE");
        entrySet = m1.entrySet();
        it = entrySet.iterator();

    }



    private ArrayList<BarDataSet> getDataSet() {

        BarDataSet barDataSet1;
        if (subSelection== null) {
            barDataSet1 = new BarDataSet(valueSet1, "Kimura/SubsHit");
            barDataSet1.setColor(Color.rgb(0, 155, 0));
        } else {
            barDataSet1 = new BarDataSet(valueSet1, subSelection + "/" + subViewSelection);
            barDataSet1.setColor(Color.rgb(0, 155, 0));
        }

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        //dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        return xAxis;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        xAxis = getXAxisValues();
        Toast toast = Toast.makeText(this, xAxis.get(e.getXIndex()), Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onNothingSelected() {

    }


    public class MyValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            String form;
            if(value == 0) {
                form = "";
            } else {
                form = Math.round(value)+"";
            }
            return form;
        }
    }
}
