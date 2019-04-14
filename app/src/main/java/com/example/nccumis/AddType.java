package com.example.nccumis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AddType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        //使用Spinner
        Spinner notify = (Spinner)findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(
                this, R.array.type_array, android.R.layout.simple_spinner_item );
        nAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        notify.setAdapter(nAdapter);


    }
}
