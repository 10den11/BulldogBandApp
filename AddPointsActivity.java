package com.example.vhl2.bandapp3;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddPointsActivity extends AppCompatActivity {
    private DataSnapshot dataSnapshot;
    private DatabaseReference myRoster;
    private List<BandMember> userList;
    private List<CheckBox> userUI;
    private static String TAG = "AddPointsActivity";

    GridLayout gridLayout;
    ScrollView scrollView;
    Spinner pointSpinner;
    EditText otherAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_points);
        Intent intent = getIntent();

        myRoster = FirebaseDatabase.getInstance().getReference("Users");
        userList = new ArrayList<>();
        userUI = new ArrayList<>();

        myRoster.addValueEventListener(new ValueEventListener() {
            GridLayout.Spec rowSpec;
            GridLayout.Spec colSpec;
            GridLayout.LayoutParams layoutParams;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k = 0;
                if(userList.size() == 0 && userUI.size() == 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for(DataSnapshot snapshot : postSnapshot.getChildren()) {
                            userList.add(snapshot.getValue(BandMember.class));
                            Log.d(TAG, "onDataChange: " + userList.toString());
                            userUI.add(new CheckBox(AddPointsActivity.this));
                            rowSpec = GridLayout.spec(k + 2, GridLayout.FILL);
                            colSpec = GridLayout.spec(0, 2);
                            layoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);
                            userUI.get(k).setLayoutParams(layoutParams);
                            userUI.get(k).setText(userList.get(k).getName());
                            gridLayout.addView(userUI.get(k));
                            k++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "onCreate: " + userList.toString());
        gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(2);
        gridLayout.setBackgroundColor(Color.WHITE);

        GridLayout.Spec rowSpec = GridLayout.spec(0, GridLayout.FILL);
        GridLayout.Spec colSpec = GridLayout.spec(0, GridLayout.FILL);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);

        scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.TRANSPARENT);


        TextView pointText = new TextView(this);
        pointText.setText("Select Point Value");
        pointText.setLayoutParams(layoutParams);
        pointText.setTextSize(20);
        pointText.setTextColor(Color.BLACK);
        gridLayout.addView(pointText);

        pointSpinner = new Spinner(this);
        List<String> pointSpinnerList = new ArrayList<>();
        pointSpinnerList.add("Men's Game");
        pointSpinnerList.add("Women's Game");
        pointSpinnerList.add("Other");
        ArrayAdapter<String> pointAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item, android.R.id.text1, pointSpinnerList);
        pointSpinner.setAdapter(pointAdapter);
        gridLayout.addView(pointSpinner);

        TextView otherText = new TextView(this);
        otherText.setTextColor(Color.BLACK);
        otherText.setText("If Other Enter Amount");
        otherText.setTextSize(20);
        gridLayout.addView(otherText);

        otherAmount = new EditText(this);
        otherAmount.setHint("# of Points");
        otherAmount.setTextColor(Color.BLACK);
        otherAmount.setTextSize(20);
        rowSpec = GridLayout.spec(1);
        colSpec = GridLayout.spec(1, GridLayout.FILL);
        layoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);
        otherAmount.setLayoutParams(layoutParams);
        otherAmount.setInputType(Configuration.KEYBOARD_12KEY);
        gridLayout.addView(otherAmount);


        scrollView.addView(gridLayout);

        setContentView(scrollView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pointsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.selectAll:
                for(int k = 0; k < userUI.size(); k++) {
                    userUI.get(k).setChecked(true);
                }
                return true;
            case R.id.deSelectAll:
                for(int k = 0; k < userUI.size(); k++) {
                    userUI.get(k).setChecked(false);
                }
                return true;
            case R.id.addPoints:
                int points;
                switch(pointSpinner.getSelectedItemPosition()) {
                    case 0:
                        points = 7;
                        break;
                    case 1:
                        points = 14;
                        break;
                    case 2:
                        points = Integer.parseInt(otherAmount.getText().toString());
                        break;
                    default:
                        points = 0;
                }
                for(int k = 0; k < userUI.size(); k++) {
                    if(userUI.get(k).isChecked()) {
                        userList.get(k).addPoints(points);

                        myRoster.child(userList.get(k).getInstrument()).child(userList.get(k).getUserName()).setValue(userList.get(k));
                    }
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

