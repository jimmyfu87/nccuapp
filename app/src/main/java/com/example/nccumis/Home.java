package com.example.nccumis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class Home extends AppCompatActivity {
    private Button addSpend;
    private Button checkSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        addSpend = (Button) findViewById(R.id.addSpend);
        checkSpend = (Button) findViewById(R.id.checkSpend);

    }
}
