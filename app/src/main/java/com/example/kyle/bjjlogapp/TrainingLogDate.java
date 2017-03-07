package com.example.kyle.bjjlogapp;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrainingLogDate extends AppCompatActivity {

    //List<String> subList = new ArrayList<>();
    ListView mTrainingView;
    DatabaseHelper myDb;
    Button btnDelete;
    String date;
    int itemPosition;

    List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();

    String[] names = new String[]{"Submissions Hit","Submissions Tapped To","Hours Trained"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_log_date);
        mTrainingView = (ListView) findViewById(R.id.training_listView);
        myDb = new DatabaseHelper(this);

        btnDelete = (Button) findViewById(R.id.delete_button);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            date = extras.getString("Date");
            setTitle(date);
        }
        DeleteData();

        String[] from = new String[] {"rowid", "col_1"};
        createViewText();


        final SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
                android.R.layout.simple_list_item_2,
                from,
                new int[] {android.R.id.text1, android.R.id.text2 });

        mTrainingView.setAdapter(adapter);
        mTrainingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                itemPosition = position;

                if (itemPosition == 0 || itemPosition == 1) {
                    ShowAlertDialogWithListview();
                } else if (itemPosition == 2) {
                    ShowDialogWithNumbers();
                }

            }
        });

    }

    //consider making this a variable method argument so it can be called with submissions
    public void ShowDialogWithNumbers() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Hours Trained?");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for OK button here
                String hours= input.getEditableText().toString();
                //String hours = Integer.toString(whichButton);
                myDb.insertData(date, itemPosition,hours);
                finish();
                startActivity(getIntent());
              //MainActivity.mActivity.finish();
                MainActivity.mActivity.recreate();
            }
        });

        alert.show();

    }


    public List createViewText() {

        String[] lastmessage = new String[]{"Input all caught submissions","Input all submissions you tapped to","Input hours trained"};

        if(myDb.exists(date) == true) {
            for(int i= 0; i< 3; i++){
                if(myDb.getColumnValue(date, i) != null){
                    lastmessage[i] = myDb.getColumnValue(date, i);
                }

           }
        }

        for (int i = 0; i < names.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rowid", "" + names[i]);
            map.put("col_1", "" + lastmessage[i]);
            fillMaps.add(map);


        }
            return fillMaps;

    }

    public void DeleteData() {
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData("3");
                        if(deletedRows > 0)
                            Toast.makeText(TrainingLogDate.this,"Data deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(TrainingLogDate.this,"Data not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void ShowAlertDialogWithListview()
    {
        //String mValue = date;
        List<String> mSubmissions = new ArrayList<String>();
        mSubmissions.add("Keylock");
        mSubmissions.add("Kimura");
        mSubmissions.add("Armbar");
        mSubmissions.add("RNC");
        mSubmissions.add("Triangle");
        mSubmissions.add("Straight Ankle");
        //Create sequence of items
        final CharSequence[] submissions = mSubmissions.toArray(new String[mSubmissions.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Submissions");
        dialogBuilder.setItems(submissions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = submissions[item].toString();  //Selected item in listview
                //+2 stops it from over writing the id or the year
                myDb.insertData(date, itemPosition,selectedText);

                finish();
                startActivity(getIntent());
                //MainActivity.mActivity.finish();
                MainActivity.mActivity.recreate();
            }

        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training, menu);
        return true;
    }

    public void viewAll() {
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            //show message if there is no data
            showMessage("Error","Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            //last goes to next line
            buffer.append("Id :" + res.getString(0)+"\n");
            buffer.append("Date :" + res.getString(1)+"\n");
            buffer.append("Subs Hit :" + res.getString(2)+"\n");
            buffer.append("Subs Caught :" + res.getString(3)+"\n");
            buffer.append("Hours :" + res.getString(4)+"\n\n");;

        }
        //Show all data
        showMessage("Data", buffer.toString());
    }



    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //can cancel after it's used
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
