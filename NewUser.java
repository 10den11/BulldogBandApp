package com.example.vhl2.bandapp3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class NewUser extends AppCompatActivity {

    final String TAG = "NewUser: ";

    private final String NEW_USER_CODE = "newMember";
    private BandMember user;
    private Spinner classes;
    private Spinner section;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        section = (Spinner) findViewById(R.id.section);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sections,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        section.setAdapter(adapter);

        classes = (Spinner) findViewById(R.id.classes);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.classes,
                android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classes.setAdapter(adapter2);




        Intent intent = getIntent();

        if (savedInstanceState != null) {
               user = (BandMember) savedInstanceState.getSerializable("newMember");
               setInformation();
        } else if (intent.getExtras().getBoolean("redo")) {
            user = (BandMember) intent.getSerializableExtra("newMember");
            setInformation();
        } else {
            user = new BandMember();
        }

    }

    private void setInformation(){
        EditText userNameText = (EditText) findViewById(R.id.userName);
        EditText nameText = (EditText) findViewById(R.id.name);
        EditText passwordText = (EditText) findViewById(R.id.password);
        Spinner year = (Spinner) findViewById(R.id.classes);
        Spinner instrument = (Spinner) findViewById(R.id.section);
        EditText passwordText2 = (EditText) findViewById(R.id.passwordCheck);

        nameText.setText(user.getName());
        userNameText.setText(user.getUserName());
        passwordText.setText(user.getPassword());
        passwordText2.setText(user.getPassword());
        String userSection = user.getInstrument();
        String userYear = user.getYear();
        switch(userSection){
            case "saxophone" :
                instrument.setSelection(0);
                break;
            case "trumpet" :
                instrument.setSelection(1);
                break;
            case "percussion" :
                instrument.setSelection(2);
                break;
            case "flute" :
                instrument.setSelection(3);
                break;
            case "clarinet" :
                instrument.setSelection(4);
                break;
            case "trombone" :
                instrument.setSelection(5);
                break;
            case "bass" :
                instrument.setSelection(6);
                break;
            case "sousaphone" :
                instrument.setSelection(7);
                break;
            default:
                Log.e(TAG, "addMember: invalid entry");
                break;
        }

        switch(userYear){
            case "freshman":
                year.setSelection(0);
                break;
            case "sophomore":
                year.setSelection(1);
                break;
            case "junior":
                year.setSelection(2);
                break;
            case "senior":
                year.setSelection(3);
                break;
            case "director":
                year.setSelection(4);
                break;
            default:
                year.setSelection(0);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NEW_USER_CODE, user);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        EditText userNameText = (EditText) findViewById(R.id.userName);
        EditText nameText = (EditText) findViewById(R.id.name);
        EditText passwordText = (EditText) findViewById(R.id.password);
        EditText passwordText2 = (EditText) findViewById(R.id.passwordCheck);
        Spinner year = (Spinner) findViewById(R.id.classes);
        String name = nameText.getText().toString();
        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();
        DatabaseReference myRoster = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseDatabase db = myRoster.getDatabase();
        Query ref = db.getReference("parent").orderByChild("childNode").equalTo(userName);



        switch (menuId) {
            case R.id.doneButton: // TODO user validation
                if((!userName.equals("") && (!password.equals("")))) {
                    if(password.equals(passwordText2.getText().toString())) {

                        Intent intent = new Intent();
                        user.setName(name);
                        user.setPassword(password);
                        user.setUserName(userName);
                        user.setYear(classes.getSelectedItem().toString());
                        user.setInstrument(section.getSelectedItem().toString());

                        Log.d(TAG, "onOptionsItemSelected: " + user.toString());
                        intent.putExtra(NEW_USER_CODE, user);
                        setResult(Activity.RESULT_OK, intent);
                        NewUser.this.finish();
                        return true;
                    }else{
                        Toast.makeText(this, "the passwords you entered do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "your user name and password cannot be blank", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                Intent dud = new Intent();
                dud.putExtra(NEW_USER_CODE, user);  //test
                setResult(0, dud);
                NewUser.this.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }











}
