package com.example.nccumis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LogIn extends AppCompatActivity {



    private CheckBox checkboxLogIn;
    private EditText et_passwordLogin;
    private Button btn_forgetPassword;
    private Button btn_registerAgain;
    private Button loginHome;
    private Button btn_backToHome;
    int RC_SIGN_IN=0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        signInButton=findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        checkboxLogIn = (CheckBox)findViewById(R.id.checkLogIn);
        et_passwordLogin =(EditText)findViewById(R.id.et_passwordLogin);
        btn_forgetPassword=(Button)findViewById(R.id.btn_forgetPassword);
        btn_registerAgain = (Button)findViewById(R.id.btn_registerAgain);
        loginHome = (Button) findViewById(R.id.loginHome);
        btn_backToHome =(Button)findViewById(R.id.btn_backToHome);

        //回上頁
        btn_backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        //切換到註冊頁面
        btn_registerAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToRegistr();
            }
        });

        //切換到驗證密碼頁面
        btn_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpToPasswordCheckEmail();
            }
        });


        //顯示隱藏密碼
        checkboxLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_passwordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    et_passwordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //登入首頁
        loginHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAccountPassword()){
                    jumpToHome();
                }
            }
        });

    }
//    protected void onStart(){
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if(account!=null){
//            startActivity(new Intent(LogIn.this,Home.class));
//        }
//
//    }
//切換到驗證密碼的頁面
    public void JumpToPasswordCheckEmail(){
        Intent intent= new Intent(LogIn.this,PasswordCheckEmail.class);
        startActivity(intent);
    }
    //切換到註冊頁面
    public void JumpToRegistr(){
        Intent intent= new Intent(LogIn.this, Register.class);
        startActivity(intent);
    }

    //////////確認使用者輸入帳密，待補////////////////
    public boolean checkAccountPassword(){
        return true;
    }

    public void jumpToHome(){
        Intent intent = new Intent(LogIn.this,MainActivity2.class);
        startActivity(intent);
    }


    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                //.requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                 //       new Scope(DriveScopes.DRIVE_APPDATA))
                //.requestIdToken("961476906062-83aqneaf6du0u3hmpfa0bn5qe740jv6n.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(LogIn.this,Home.class));
        } catch (ApiException e) {
            Log.w("Google signin error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LogIn.this,"登錄失敗",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CODE_SIGN_IN_create:
//                handleSignInResult_create(data);
//                break;
//            case REQUEST_CODE_SIGN_IN_update:
//                handleSignInResult_update(data);
//            case REQUEST_CODE_SIGN_IN_restore:
//                handleSignInResult_restore(data);
//            default:
//                break;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void handleSignInResult_create(Intent result) {
//        GoogleSignIn.getSignedInAccountFromIntent(result)
//                .addOnSuccessListener(googleAccount -> {
//                    Toast.makeText(LogIn.this, "登錄成功", Toast.LENGTH_SHORT).show();
//
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
//
//                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
//                    // Its instantiation is required before handling any onClick actions.
//                   driveServiceHelper = new DriveServiceHelper(googleDriveService);
//                   driveServiceHelper.saveFile();
//                   Toast.makeText(LogIn.this, "儲存成功", Toast.LENGTH_SHORT).show();
//
//                })
//                .addOnFailureListener(exception -> {
//                    Toast.makeText(LogIn.this, "登錄失敗", Toast.LENGTH_SHORT).show();
//                });
//    }
//    private void handleSignInResult_restore(Intent result) {
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
//                    driveServiceHelper.restore();
//                    Toast.makeText(LogIn.this, "還原成功", Toast.LENGTH_SHORT).show();
//
//                })
//                .addOnFailureListener(exception -> {
//                    Toast.makeText(LogIn.this, "登錄失敗", Toast.LENGTH_SHORT).show();
//                });
//    }
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
//                    driveServiceHelper.update();
//                    Toast.makeText(LogIn.this, "更新成功", Toast.LENGTH_SHORT).show();
//
//                })
//                .addOnFailureListener(exception -> {
//                    Toast.makeText(LogIn.this, "登錄失敗", Toast.LENGTH_SHORT).show();
//                });
//    }

}
