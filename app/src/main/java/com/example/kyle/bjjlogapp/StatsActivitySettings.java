package com.example.kyle.bjjlogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class StatsActivitySettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences SP;
    Spinner spinner;
    Spinner spinnerSubs;
    String selection;
    String subChoice;
    String oldSelection;
    String oldSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_activity_settings);

        // Spiner element
        spinner = (Spinner) findViewById(R.id.spinner2);
        spinnerSubs = (Spinner) findViewById(R.id.spinner3);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        spinnerSubs.setOnItemSelectedListener(this);

        List<String> items = new ArrayList<String>();
        items.add("SubsHit");
        items.add("SubsHitBy");

        List<String> subs = new ArrayList<>();
        subs.add("Kimura");
        subs.add("Keylock");
        subs.add("Armbar");
        subs.add("Straight Ankle");
        subs.add("RNC");
        subs.add("Triangle");

        // Creating adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        ArrayAdapter<String> adapterSubs = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subs);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSubs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(adapter);
        spinnerSubs.setAdapter(adapterSubs);


        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                intent.putExtra("getData",selection);
                intent.putExtra("getSub",subChoice);
                startActivity(intent);
                Log.d(subChoice, "SPINNER2");
             //   Toast.makeText(v.getContext(), selection, Toast.LENGTH_LONG).show();
                StatsActivity.sActivity.finish();
                finish();

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        switch (parent.getId()) {
            case R.id.spinner2:
                selection = parent.getItemAtPosition(position).toString();
                SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SP.edit().putInt("last index", position).commit();
                Log.d(subChoice, "SUBCHOICE1");
                break;
            case R.id.spinner3:
                subChoice = parent.getItemAtPosition(position).toString();
                SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SP.edit().putInt("sub index", position).commit();
                Log.d(subChoice, "SUBCHOICE");
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void onResume() {
        super.onResume();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (SP != null) {
            int pos = SP.getInt("last index", 0);
            int posSub = SP.getInt("sub index", 0);

            spinner.setSelection(pos);
            spinnerSubs.setSelection(posSub);

        }
    }
}
