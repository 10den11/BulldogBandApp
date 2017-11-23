package com.example.dominicg.cloudapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    private DatabaseReference myRoster;

    GridLayout gridLayout;
    List<BandMember> roster;
    ArrayAdapter<BandMember> arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(1);
        gridLayout.setBackgroundColor(Color.WHITE);

        roster = new ArrayList<>();
        listView = new ListView(this);
        GridLayout.Spec listViewRowSpec = GridLayout.spec(0);
        GridLayout.Spec listViewColSpec = GridLayout.spec(0, GridLayout.FILL);
        GridLayout.LayoutParams listViewLayoutParams = new GridLayout.LayoutParams(listViewRowSpec, listViewColSpec);
        listView.setLayoutParams(listViewLayoutParams);

        // In the Database all band members are children of Users
        myRoster = FirebaseDatabase.getInstance().getReference("Users");
        BandMember Vince = new BandMember("Vinent Lombardi","1234", true, "sophmore",
                "vlombardi", 25, "saxophone");
        BandMember Max = new BandMember("Maxwell Sherman","1234", false, "sophmore",
                "mSherman", 25, "percussion");

        addMember(Vince);
        addMember(Max);

        ChildEventListener childEventListener = new ChildEventListener() {
            /**
             * This method takes in BandMember information and puts them in an arraylist
             * Which then is hooked up to a listView which displays all of our current band members
             * @param dataSnapshot
             * @param s
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                BandMember member = dataSnapshot.getValue(BandMember.class);
                roster.add(member);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        };

        myRoster.addChildEventListener(childEventListener);
        // Read from the database
//        myRoster.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                BandMember value = dataSnapshot.getValue(BandMember.class);
//                Log.d(TAG, "Value is: " + value.toString());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });



        arrayAdapter = new ArrayAdapter<BandMember>(this, android.R.layout.simple_list_item_1, roster);
        listView.setAdapter(arrayAdapter);
        gridLayout.addView(listView);
        setContentView(gridLayout);
    }

    /**
     * Push gives each value a key and setValue stores the objects in a database
     * @param member
     */
    public void addMember(BandMember member){
        //String UserId = member.getUserName();
        myRoster.push().setValue(member);
    }
}
