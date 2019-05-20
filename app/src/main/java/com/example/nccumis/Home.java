package com.example.nccumis;

import android.app.backup.BackupManager;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

    int RC_SIGN_IN=0;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
//        backup=(Button)findViewById(R.id.backup);
//        backup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BackupManager bm = new BackupManager(Home.this);
//                bm.dataChanged();
//                jumpToadd_expense();
//            }
//        });


        signout=(Button)findViewById(R.id.signout);
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



    public void jumpToadd_expense(){
        Intent intent = new Intent(Home.this,add_expense.class);
        startActivity(intent);
    }

    public void jumpTocheck_expense(){
        Intent intent = new Intent(Home.this,check_expense.class);
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
                        startActivity(new Intent(Home.this,LogIn.class));
                        // ...
                    }
                });
    }


}
