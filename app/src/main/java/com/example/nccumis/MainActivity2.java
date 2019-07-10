package com.example.nccumis;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Button addSpend;
    private Button checkSpend;
    private Button shopping;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 127;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE2 = 128;

    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    private  String dateinStart;
    private String dateinEnd;

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
        if(id == R.id.setting){
            Intent intent = new Intent(MainActivity2.this,Settings.class);
            startActivity(intent);
        }else if(id == R.id.login){
            Intent intent = new Intent(MainActivity2.this,LogIn.class);
            startActivity(intent);
        }else if(id == R.id.logout){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(MainActivity2.this, LogIn.class));
                            // ...
                        }
                    });
        }else if(id == R.id.register){
            Intent intent = new Intent(MainActivity2.this,Register.class);
            startActivity(intent);
        }else if(id == R.id.phone_restore){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE2);
            } else {
                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String currentDBPath = "/data/com.example.nccumis/databases/App.db";
                        String backupDBPath = "App.db";
                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);
                        //Toast.makeText(getApplicationContext(), "File Set", Toast.LENGTH_SHORT).show();

                        if (backupDB.exists()) {
                            FileChannel src = new FileInputStream(backupDB).getChannel();
                            FileChannel dst = new FileOutputStream(currentDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                            Toast.makeText(getApplicationContext(), "手機內存還原成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Not exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "沒有SD卡", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
    private void restoreSharepref(){
        SharedPreferences setting=getSharedPreferences("sharepref",MODE_PRIVATE);
        SharedPreferences.Editor editor=setting.edit();
        editor.putString("Dbfileid","0");
        editor.putBoolean("Created",false);
        editor.commit();
    }




    public void autoSetFirstandEndMonth(){
        List<String> oddMonth = new ArrayList<String>();
        oddMonth.add("1");
        oddMonth.add("3");
        oddMonth.add("5");
        oddMonth.add("7");
        oddMonth.add("8");
        oddMonth.add("10");
        oddMonth.add("12");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String year = Integer.toString(cal.get(Calendar.YEAR));
        String month = Integer.toString(cal.get(Calendar.MONTH) +1);

        this.dateinStart = year+"-"+month+"-"+1;
        if(month == "2"){
            int intYear = Integer.parseInt(year);
            if ((intYear % 4 == 0 && intYear % 100 != 0) || (intYear % 400 == 0 && intYear % 3200 != 0)){
                this.dateinEnd = year+"-"+month+"-"+29;
            }else {
                this.dateinEnd = year+"-"+month+"-"+28;
            }
        }else if(oddMonth.contains(month)){
            this.dateinEnd = year+"-"+month+"-"+31;
        }else {
            this.dateinEnd = year+"-"+month+"-"+30;
        }

        //System.out.println(this.dateinStart+" ,"+this.dateinEnd);
    }

}
