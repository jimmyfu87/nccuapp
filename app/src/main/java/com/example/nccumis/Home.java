package com.example.nccumis;

import android.Manifest;
import android.app.backup.BackupManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Home extends AppCompatActivity {
    private Button addSpend;
    private Button checkSpend;
    private Button signout;
    private Button backup;
    private Button restore;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 127;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE2 = 128;

    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        backup = (Button) findViewById(R.id.backup);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BackupManager bm = new BackupManager(Home.this);
//                bm.dataChanged();
//                jumpToadd_expense();
//                  checkpermission();
                backUp();
            }
        });
        restore=(Button) findViewById(R.id.restore);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restore();
            }
        });

        signout = (Button) findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }

        });

        //到記帳
        addSpend = (Button) findViewById(R.id.addSpend);
        addSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToadd_expense();
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
    }

    public void jumpToadd_expense() {
        Intent intent = new Intent(Home.this, add_expense.class);
        startActivity(intent);
    }

    public void jumpTocheck_expense() {
        Intent intent = new Intent(Home.this, check_expense.class);
        startActivity(intent);
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(Home.this, LogIn.class));
                        // ...
                    }
                });
    }

    public void backUp() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        else {
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    String currentDBPath = "/data/com.example.nccumis/databases/App.db";
                    String backupDBPath = "App.db";
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(sd, backupDBPath);

                    Toast.makeText(getApplicationContext(), "File Set", Toast.LENGTH_SHORT).show();
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Toast.makeText(getApplicationContext(), "Backup is successful to SD card", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if(grantResults[0]== PackageManager.PERMISSION_DENIED){
            return;
        }
        else {
            if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
                backUp();
            } else if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE2) {
                restore();
            } else {
                // Permission Denied

            }
        }
    }

    public void restore() {
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
                    Toast.makeText(getApplicationContext(), "File Set", Toast.LENGTH_SHORT).show();

                    if (backupDB.exists()) {
                        FileChannel src = new FileInputStream(backupDB).getChannel();
                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Toast.makeText(getApplicationContext(), "Restore successful", Toast.LENGTH_SHORT).show();
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
}



