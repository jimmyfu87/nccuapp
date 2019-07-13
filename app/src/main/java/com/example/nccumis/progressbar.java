package com.example.nccumis;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class progressbar  extends AppCompatActivity {
    ProgressBar  pb1;
    int progress = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pb1 = (ProgressBar) findViewById(R.id.pb1);
    }
    public void add(View v){
        progress+=10;
        pb1.setVisibility(progress<100? View.VISIBLE:View.GONE);
        pb1.setProgress(progress);
    }
}
