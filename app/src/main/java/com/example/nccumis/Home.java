package com.example.nccumis;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Collections;

public class Home extends AppCompatActivity {
    private Button addSpend;
    private Button checkSpend;
    private Button signout;
    private Button backup;
    private Button restore;
    private Button cloud_backup;
    private Button cloud_restore;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 127;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE2 = 128;
    private boolean created=false;
    public  DriveServiceHelper driveServiceHelper;
    final int REQUEST_CODE_SIGN_IN_create=1;  //create
    final int REQUEST_CODE_SIGN_IN_update=2;  //update
    final int REQUEST_CODE_SIGN_IN_restore=3;  //restore
    public SharedPreferences setting;
    public  boolean isCreated=false;
    private static final String TAG = "Home";

    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        restoreSharepref();
        DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        cloud_backup=(Button) findViewById(R.id.cloud_backup);
        cloud_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean created=getSharedPreferences("sharepref",MODE_PRIVATE).getBoolean("Created",false);
//                if(isCreated){
//                    signIn(REQUEST_CODE_SIGN_IN_update);
//                }
//                else {
                    signIn(REQUEST_CODE_SIGN_IN_create);
//                    SharedPreferences setting=getSharedPreferences("sharepref",MODE_PRIVATE);
//                    setting.edit().putBoolean("Created",true);
//
//                }
            }
        });
        cloud_restore=(Button) findViewById(R.id.cloud_restore);
        cloud_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(REQUEST_CODE_SIGN_IN_restore);
            }
        });
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
    private void signIn(int askcode) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE_APPDATA))
                .requestIdToken("887086177375-sv0vfdd9p4dj9isghd5sg02hid9pu2dp.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), askcode);
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

                    //Toast.makeText(getApplicationContext(), "File Set", Toast.LENGTH_SHORT).show();
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        Toast.makeText(getApplicationContext(), "手機內存備份成功", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN_create:
                handleSignInResult_create(data);
                break;
//            case REQUEST_CODE_SIGN_IN_update:
//                handleSignInResult_update(data);
            case REQUEST_CODE_SIGN_IN_restore:
                handleSignInResult_restore(data);
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult_create(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                   // Toast.makeText(Home.this, "登錄成功", Toast.LENGTH_SHORT).show();

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("nccumis")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    driveServiceHelper = new DriveServiceHelper(googleDriveService);
                    driveServiceHelper.createorupdateFile();
                    Toast.makeText(Home.this, "備份成功", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Unable to sign in.", exception);
                    Toast.makeText(Home.this, "登錄失敗", Toast.LENGTH_SHORT).show();
                });
    }
    private void handleSignInResult_restore(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("nccumis")
                                    .build();
                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    driveServiceHelper = new DriveServiceHelper(googleDriveService);
                    driveServiceHelper.restore();
                    Toast.makeText(Home.this, "還原成功", Toast.LENGTH_SHORT).show();//若沒檔案仍會顯示還原成功

                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(Home.this, "登錄失敗", Toast.LENGTH_SHORT).show();
                });
    }
//    private void handleSignInResult_update(Intent result) {
//        GoogleSignIn.getSignedInAccountFromIntent(result)
//                .addOnSuccessListener(googleAccount -> {
//                    // Use the authenticated account to sign in to the Drive service.
//                    GoogleAccountCredential credential =
//                            GoogleAccountCredential.usingOAuth2(
//                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
//                    credential.setSelectedAccount(googleAccount.getAccount());
//                    Drive googleDriveService =
//                            new Drive.Builder(
//                                    AndroidHttp.newCompatibleTransport(),
//                                    new GsonFactory(),
//                                    credential)
//                                    .setApplicationName("nccumis")
//                                    .build();
//                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
//                    // Its instantiation is required before handling any onClick actions.
//                    driveServiceHelper = new DriveServiceHelper(googleDriveService);
//                   // String fileid=getSharedPreferences("sharepref",MODE_PRIVATE).getString("Dbfileid","0");
//                    driveServiceHelper.update();
//                    Toast.makeText(Home.this, "更新成功", Toast.LENGTH_SHORT).show();
//
//                })
//                .addOnFailureListener(exception -> {
//                    Toast.makeText(Home.this, "登錄失敗", Toast.LENGTH_SHORT).show();
//                });
//    }
    private void restoreSharepref(){
        SharedPreferences setting=getSharedPreferences("sharepref",MODE_PRIVATE);
        SharedPreferences.Editor editor=setting.edit();
        editor.putString("Dbfileid","0");
        editor.putBoolean("Created",false);
        editor.commit();
    }
}



