package com.example.vhl2.bandapp3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    private DatabaseReference myRoster;
    final int ADD_POINTS_CODE = 2;
    private BandMember currentUser;

    GridLayout gridLayout;
    List<BandMember> roster;
    ArrayAdapter<BandMember> arrayAdapter;
    ListView listView;


    /**
     * creates a gridlayour and sets up a firebase database reference that can and creates a long clicks
     * for them
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(1);
        gridLayout.setBackgroundColor(Color.WHITE);

        roster = new ArrayList<BandMember>();
        listView = new ListView(this);
        GridLayout.Spec listViewRowSpec = GridLayout.spec(0);
        GridLayout.Spec listViewColSpec = GridLayout.spec(0, GridLayout.FILL);
        GridLayout.LayoutParams listViewLayoutParams = new GridLayout.LayoutParams(listViewRowSpec, listViewColSpec);
        listView.setLayoutParams(listViewLayoutParams);

        // In the Database all band members are children of Users
        myRoster = FirebaseDatabase.getInstance().getReference("Users");
        BandMember Vince = new BandMember("Vinent Lombardi","zags", true, "sophomore",
                "vlombardi", 25, "saxophone");
        BandMember Max = new BandMember("Maxwell Sherman","1234", false, "sophomore",
                "mSherman", 7, "percussion");
        BandMember Brian = new BandMember("Brian", "1234", false, "Junior", "BrianRocks", 35,  "bass");

//          addMember(Vince);
//          addMember(Max);
//          addMember(Brian);

        Intent intent = getIntent();
        currentUser = (BandMember) intent.getSerializableExtra("currentUser");
        Toast.makeText(this, "welcome " + currentUser.getUserName(), Toast.LENGTH_SHORT);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            /**
             * checks if the items state has changed
             * @param actionMode
             * @param i
             * @param l
             * @param b
             */
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                int numSelected = listView.getCheckedItemCount();
                actionMode.setTitle(numSelected + " selected");
            }

            /**
             * inflates the menu for mainActivity
             * @param actionMode
             * @param menu
             * @return
             */
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.cam_menu, menu);
                return true;
            }

            /**
             * stub
             * @param actionMode
             * @param menu
             * @return
             */
            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            /**
             * Cam menu for removing users
             * @param actionMode
             * @param menuItem
             * @return
             */
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                int menuId = menuItem.getItemId();


                switch (menuId) {
                    case R.id.deleteMenuAction:
                        if (currentUser.getAdmin()) {
                            for (int x = roster.size() - 1; x >= 0; x--) {
                                if (listView.isItemChecked(x)) {
                                    String instrument = roster.get(x).getInstrument();
                                    String name = roster.get(x).getUserName();
                                    myRoster.child(instrument).child(name).removeValue();
                                    roster.remove(x);
                                }
                            }
                            Collections.sort(roster);
                            arrayAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "you cannot delete users", Toast.LENGTH_SHORT).show();
                        }
                }

                actionMode.finish();
                return true;
            }

            /**
             * stub
             * @param actionMode
             */
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            /**
             * This method takes in BandMember information and puts them in an arraylist
             * Which then is hooked up to a listView which displays all of our current band members
             * @param dataSnapshot
             * @param s
             */
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot child : dataSnapshot.getChildren()){
                   // for(DataSnapshot snapshot : child.getChildren()) {

                        BandMember member = child.getValue(BandMember.class);
                        roster.add(member);

                        arrayAdapter.notifyDataSetChanged();
                    //}
                }
                //Log.d(TAG, "onChildAdded: " + dataSnapshot.getChildrenCount());

            }



            /**
             * this code removes a section of data from the database and then adds it back
             * in a new sorted order along with a new element.
             * @param dataSnapshot
             * @param s
             */
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: changed!");
                boolean exists = true;

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    BandMember member = child.getValue(BandMember.class);

                    if (exists){
                        exists = false;
                        String instrument = member.getInstrument();
                        for(int i = roster.size() - 1; i >= 0; i--){
                            if(roster.get(i).getInstrument().equals(instrument)){
                                roster.remove(i);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    roster.add(member);

                }
                Collections.sort(roster);
                arrayAdapter.notifyDataSetChanged();
            }

            /**
             * stub
             * @param dataSnapshot
             */
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: ");

            }

            /**
             * stub
             * @param dataSnapshot
             * @param s
             */
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            /**
             * stub
             * @param databaseError
             */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.d(TAG, "onCancelled: ");
            }
        };

        myRoster.addChildEventListener(childEventListener);
        arrayAdapter = new ArrayAdapter<BandMember>(this, android.R.layout.simple_list_item_activated_1, roster);
        listView.setAdapter(arrayAdapter);
        gridLayout.addView(listView);
        setContentView(gridLayout);
    }

//    /**
//     * adds a member to one of the sub databases based on its membership
//     * @param member incoming band member
//     */
//    public void addMember(BandMember member){
//        String userId = member.getUserName();
//        String userSection = member.getInstrument();
//        switch(userSection){
//            case "saxophone" :
//                myRoster.child("saxophone").child(userId).setValue(member);
//                break;
//            case "percussion" :
//                myRoster.child("percussion").child(userId).setValue(member);
//                break;
//            case "clarinet" :
//                myRoster.child("clarinet").child(userId).setValue(member);
//                break;
//            case "flute" :
//                myRoster.child("flute").child(userId).setValue(member);
//                break;
//            case "bass" :
//                myRoster.child("bass").child(userId).setValue(member);
//                break;
//            case "trumpet" :
//                myRoster.child("trumpet").child(userId).setValue(member);
//                break;
//            case "trombone" :
//                myRoster.child("trombone").child(userId).setValue(member);
//                break;
//            case "sousaphone" :
//                myRoster.child("sousaphone").child(userId).setValue(member);
//                break;
//            default:
//                Log.e(TAG, "addMember: invalid entry");
//                break;
//        }
//    }

    /**
     * creates the main_menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * adds menuItems
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch(menuId) {
            case R.id.addMenuItem:
                if(currentUser.getAdmin()) {
                    Intent intent = new Intent(MainActivity.this, AddPointsActivity.class);
                    startActivityForResult(intent, ADD_POINTS_CODE);
                }else{
                    Toast.makeText(this, "you are not allowed to add points", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.clearMenu:
                if(currentUser.getAdmin()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertBuilder.setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            myRoster.removeValue();
                        }
                    });
                    alertBuilder.setNegativeButton(R.string.no_button, null);
                    alertBuilder.setTitle(R.string.alert_title).setMessage(R.string.clear_warning);
                    alertBuilder.show();
                    return true;
                } else {
                    Toast.makeText(this, "you are not aloud to wipe out the database", Toast.LENGTH_SHORT).show();
                }
            default:
                Log.e(TAG, "onOptionsItemSelected: Invalid item selected");
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *updates listView after setting points
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == ADD_POINTS_CODE)&&(resultCode == Activity.RESULT_OK)){
            arrayAdapter.notifyDataSetChanged();
        }
    }
}
