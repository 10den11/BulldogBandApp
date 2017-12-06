package com.example.vhl2.bandapp3;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private BandMember editMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        editMember = (BandMember) intent.getSerializableExtra("currentMember");

        final EditText editName = (EditText) findViewById(R.id.EditNameText);
        final EditText newPassword = (EditText) findViewById(R.id.EditPasswordText);
        final EditText repeatPassword = (EditText) findViewById(R.id.EditRepeatPasswordText);
        final Spinner sectionSpinner = (Spinner) findViewById(R.id.sectionSpinner);
        final Spinner classSpinner = (Spinner) findViewById(R.id.classSpinner);
        Button finishButton = findViewById(R.id.updateSettingsButton);

        editName.setText(editMember.getName());

        switch(editMember.getInstrument()) {
            case "saxophone":
                sectionSpinner.setSelection(0);
                break;
            case "trumpet":
                sectionSpinner.setSelection(1);
                break;
            case "percussion":
                sectionSpinner.setSelection(2);
                break;
            case "flute":
                sectionSpinner.setSelection(3);
                break;
            case "clarinet":
                sectionSpinner.setSelection(4);
                break;
            case "trombone":
                sectionSpinner.setSelection(5);
                break;
            case "bass":
                sectionSpinner.setSelection(6);
                break;
            case "sousaphone":
                sectionSpinner.setSelection(7);
        }

        switch (editMember.getYear()) {
            case "freshman":
                classSpinner.setSelection(0);
                break;
            case "sophomore":
                classSpinner.setSelection(1);
                break;
            case "junior":
                classSpinner.setSelection(2);
                break;
            case "senior":
                classSpinner.setSelection(3);
                break;
            case "director":
                classSpinner.setSelection(4);
        }

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!newPassword.getText().toString().equals("")) {
                    if(!newPassword.getText().toString().equals(repeatPassword.getText().toString())) {
                        Toast.makeText(SettingsActivity.this, "Passwords do not match",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        editMember.setPassword(newPassword.getText().toString());
                    }
                }
                editMember.setName(editName.getText().toString());
                editMember.setInstrument(sectionSpinner.getSelectedItem().toString());
                editMember.setYear(classSpinner.getSelectedItem().toString());

                Intent settingsIntent = new Intent();
                settingsIntent.putExtra("changedInfoMember", editMember);
                setResult(Activity.RESULT_OK, settingsIntent);
                finish();

            }
        });


    }
}