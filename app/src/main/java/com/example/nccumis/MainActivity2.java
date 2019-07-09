package com.example.nccumis;

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
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Button addSpend;
    private Button checkSpend;
    private Button shopping;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.head_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("購智帳");
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //到記帳
        addSpend = (Button) findViewById(R.id.addSpend);
        addSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_spend();
            }
        });

        //到查帳
        checkSpend = (Button) findViewById(R.id.checkSpend);
        checkSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTocheck_expense();
            }
        });

        //到爬蟲
        shopping =(Button) findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { jumpToshopping();}

        });

    }

    //點擊左側菜單的動作
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.register){
            Intent intent = new Intent(MainActivity2.this,Register.class);
            startActivity(intent);
        }else if(id == R.id.login){
            Intent intent = new Intent(MainActivity2.this,LogIn.class);
            startActivity(intent);
        }else if(id == R.id.setting){
            Intent intent = new Intent(MainActivity2.this,Settings.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
//到記帳
    public void jumpToadd_spend() {
        Intent intent = new Intent(MainActivity2.this, add_expense.class);
        startActivity(intent);
    }
//到查帳
    public void jumpTocheck_expense() {
        Intent intent = new Intent(MainActivity2.this, check_expense.class);
        startActivity(intent);
    }
//到爬蟲
    public void jumpToshopping() {
    }
}
