package com.example.kyle.bjjlogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainPieSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences SP;
    Spinner spinner;
    String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pie_settings);

        // Spiner element
        spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        List<String> items = new ArrayList<String>();
        items.add("SubsHit");
        items.add("SubsHitBy");

        // Creating adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(adapter);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("getData",selection);
                startActivity(intent);
                //Toast.makeText(v.getContext(), selection, Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }

    ;

    @Override
    public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
        // On selecting a spinner item
        selection = parentView.getItemAtPosition(position).toString();
        SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SP.edit().putInt("last index", position).commit();

        // Showing selected spinner item
        //Toast.makeText(parentView.getContext(), "Selected: " + selection, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void onResume() {
        super.onResume();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (SP != null) {
            int pos = SP.getInt("last index", 0);

            spinner.setSelection(pos);
        }
    }
}
/*
    public updatePieChart() {

    }
    */

