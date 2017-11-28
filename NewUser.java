package com.example.vhl2.bandapp3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Spinner section = (Spinner) findViewById(R.id.section);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sections,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        section.setAdapter(adapter);

        Spinner classes = (Spinner) findViewById(R.id.classes);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.classes,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        section.setAdapter(adapter);

    }



}
