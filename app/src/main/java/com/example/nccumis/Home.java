package com.example.nccumis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    private Button addSpend;
    private Button checkSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //到記帳
        addSpend = (Button) findViewById(R.id.addSpend);
        addSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.expense_add);
            }
        });

        //到查帳
        checkSpend = (Button) findViewById(R.id.checkSpend);
        checkSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.income_add);
            }
        });
    }
}
