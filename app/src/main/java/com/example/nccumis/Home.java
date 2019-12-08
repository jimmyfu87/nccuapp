package com.example.nccumis;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nccumis.com.example.nccumis.onlineshopping.onlineShoppingPath;
import com.example.nccumis.com.example.nccumis.onlineshopping.wishpool_channel;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Button addSpend;
    private Button checkSpend;
    private Button signout;
    private Button backup;
    private Button restore;
    private Button cloud_backup;
    private Button cloud_restore;
    private Button wishpool;
    private Button onlineShopping;
    private Button shopping;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean created=false;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 127;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE2 = 128;
    public  DriveServiceHelper driveServiceHelper;
    final int REQUEST_CODE_SIGN_IN_create=1;  //create
    final int REQUEST_CODE_SIGN_IN_update=2;  //update
    final int REQUEST_CODE_SIGN_IN_restore=3;  //restore
    public SharedPreferences setting;
    private static final String TAG = "Home";
    public  boolean isCreated=false;
    private Spinner spn_homeBook;
    private SQLiteDatabase database;
    private boolean detail = false;
    private int saveDetailId =0;
    private ProgressBar PB;
    private TextView PB_expense;
    private TextView PB_left;
    private Button btn_showPB;

    private String i_price,i_note,i_date,i_type_name,i_book_name;
    private List<String> selectBook = new ArrayList<String>();//要查的帳本
    public List<String> dbBookData = new ArrayList<String>();//接資料庫資料
    private List<Integer> getPriceData = new ArrayList<Integer>();
    private List<String> bookArray = new ArrayList<String>();
    private List<Expense> select_expense = new ArrayList<Expense>();
    private List<Income> select_income = new ArrayList<Income>();
    private List<Book> select_BookAttribute = new ArrayList<Book>();

    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    private  String dateinStart;
    private String dateinEnd;
    private TextView member_id;
    private int startBudget = 0;
    private int expense = 0;
    private int income = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if(sp.getString("member_id",null)!=null) {
                Intent intent = new Intent(this, AutocrawlService.class);
                startService(intent);
            }
        }
        toolbar = findViewById(R.id.head_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("iWant購");
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        autoSetFirstandEndMonth();

//        btn_showPB=(Button)findViewById(R.id.btn_showPB);



        PB_left=(TextView)findViewById(R.id.PB_left);
        PB_expense =(TextView)findViewById(R.id.PB_expense);
        PB=(ProgressBar)findViewById(R.id.PB);
        PB.setMax(100);


        //Spinner ArrayAdapter 初始化
        initBook();     //抓所有資料庫的帳本名稱

        ArrayAdapter<String> bookList = new ArrayAdapter<String>(Home.this,
                android.R.layout.simple_spinner_dropdown_item, dbBookData);

        spn_homeBook=(Spinner)findViewById(R.id.spn_homeBook);
        spn_homeBook.setAdapter(bookList);

        //取回book的值
        spn_homeBook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_book_name = spn_homeBook.getSelectedItem().toString();
                setBook(i_book_name);   //設定現在選取的帳本
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                dbmanager.open();
                select_expense = dbmanager.fetchExpenseWithbook("0000-01-01","9999-12-31",selectBook);
                select_income = dbmanager.fetchIncomeWithbook("0000-01-01","9999-12-31",selectBook);
                select_BookAttribute = dbmanager.fetchBookallattribute(selectBook);
                dbmanager.close();
                String book_id=spn_homeBook.getSelectedItem().toString();

                SharedPreferences SP = getSharedPreferences("Book", MODE_PRIVATE);
                SharedPreferences.Editor editor = SP.edit();
                editor.putString("book_id",book_id);
                editor.putInt("book_position",position);
                editor.commit();

                int amountReamin = 0;
                for(int i = 0; i < select_BookAttribute.size(); i++){
                    if(select_BookAttribute.get(i).getName().equals(i_book_name)){
                        startBudget = select_BookAttribute.get(i).getAmount_start();
                        amountReamin = select_BookAttribute.get(i).getAmount_remain();
                        break;

                    }
                }
                countExpenseAndIncome();
                PB_expense.setText(Integer.toString(expense));       // 帳本花費（總支出）
                PB_left.setText(Integer.toString(amountReamin));    //  帳本剩餘
                PB.setProgress(Math.round(countPercentage()));
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String today=year+"-"+month+"-"+day;
                for(int i = 0; i < select_BookAttribute.size(); i++){
                    if(isDate2Bigger(today,select_BookAttribute.get(i).getEnd_date())&&select_BookAttribute.get(i).getClosed()==0){
                        String message="";
                        String selection="";
                        if(select_BookAttribute.get(i).getAmount_remain()>=0){
                            message="的結餘為+";
                            selection="來去放縱自己吧XDD";
                        }
                        else{
                            message="的結餘為";
                            selection="省著點下次再買吧QQ";
                        }
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Home.this);
                        builder.setMessage(select_BookAttribute.get(i).getName()+message+select_BookAttribute.get(i).getAmount_remain()+"元")
                                .setPositiveButton(selection, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(select_BookAttribute.get(i).getAmount_remain()>=0) {
                                            if (checklogin()) {
                                                dbmanager.open();
                                                dbmanager.updateClosed(select_BookAttribute.get(i).getId(), 1);
                                                dbmanager.close();
                                                editor.putInt("book_position", 0);
                                                editor.commit();
                                                jumpToWishpool();
                                            } else {
                                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Home.this);
                                                builder.setMessage("請先登入")
                                                        .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent = new Intent(Home.this, LogIn.class);
                                                                Home.this.startActivity(intent);
                                                            }
                                                        })
                                                        .setNegativeButton("等等再登入", null)
                                                        .create()
                                                        .show();

                                            }
                                        }
                                        else{
                                            dbmanager.open();
                                            dbmanager.updateClosed(select_BookAttribute.get(i).getId(), 1);
                                            dbmanager.close();
                                            editor.putInt("book_position", 0);
                                            editor.commit();
                                            refresh();
                                        }


                                    }
                                })
                                .setNegativeButton("回首頁", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbmanager.open();
                                        dbmanager.updateClosed(select_BookAttribute.get(i).getId(), 1);
                                        dbmanager.close();
                                        editor.putInt("book_position", 0);
                                        editor.commit();
                                        refresh();
                                    }

                                })
                                .create()
                                .show();
                    }
                    break;
                }


        //到帳本管理
//        jumpToBook =(Button)findViewById(R.id.jumpToBook);
//        jumpToBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpToBookManage();
//            }
//
//        });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SharedPreferences SP2 = getSharedPreferences("Book", MODE_PRIVATE);
        spn_homeBook.setSelection(SP2.getInt("book_position",0));








//        expense.setText(Integer.toString(countTotalExpensePrice())); //支出
//        income.setText(Integer.toString(countTotalIncomePrice()));  //收入
//        remain.setText(Integer.toString(countRemain()));    //期間花費餘額

        //從add_book 或 add_type 返回 填過的資料自動傳入
        Intent getSaveData = getIntent();
        Bundle getSaveBag = getSaveData.getExtras();
        if(getSaveBag != null) {
            this.detail = getSaveBag.getBoolean("detail");
            i_price = getSaveBag.getString("amount");
            i_date = getSaveBag.getString("date");
            int bookPosition = bookList.getPosition(getSaveBag.getString("book"));
            spn_homeBook.setSelection(bookPosition);
            i_note = getSaveBag.getString("note");
            initBook();
            spn_homeBook.setAdapter(bookList);
        }

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

        //到購物商城
        onlineShopping = (Button) findViewById(R.id.onlineshopping);
        onlineShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checklogin()){
                    jumpToOnlineShoppingPath();
                }
                else{
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Home.this);
                    builder.setMessage("請先登入")
                            .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Home.this, LogIn.class);
                                    Home.this.startActivity(intent);
                                }
                            })
                            .setNegativeButton("等等再登入", null)
                            .create()
                            .show();

                }
            }
        });
        //到許願池
        wishpool = (Button) findViewById(R.id.wishpool);
        wishpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checklogin()){
                    jumpToWishpool();
                }
                else{
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Home.this);
                    builder.setMessage("請先登入")
                            .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Home.this, LogIn.class);
                                    Home.this.startActivity(intent);
                                }
                            })
                            .setNegativeButton("等等再登入", null)
                            .create()
                            .show();

                }

            }
        });
    }
//

    public void showPB(View view){

    }

    //點擊左側菜單的動作
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.setting){
            if(checklogin()){
                Intent intent = new Intent(Home.this,Settings.class);
                startActivity(intent);
            }
            else{
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Home.this);
                builder.setMessage("請先登入")
                        .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Home.this, LogIn.class);
                                Home.this.startActivity(intent);
                            }
                        })
                        .setNegativeButton("等等再登入", null)
                        .create()
                        .show();

            }
        }else if(id == R.id.login){
            Intent intent = new Intent(Home.this,LogIn.class);
            startActivity(intent);
        }else if(id == R.id.logout){
            SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("member_id",null);
            editor.commit();
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
        }else if(id == R.id.register){
            Intent intent = new Intent(Home.this,Register.class);
            startActivity(intent);
        }else if(id == R.id.bookManagement){
            Intent intent = new Intent(Home.this,BookManage.class);
            startActivity(intent);
        }else if(id == R.id.changepassword){
            if(checklogin()){
                Intent intent = new Intent(Home.this,password_change.class);
                startActivity(intent);
            }
            else{
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Home.this);
                builder.setMessage("請先登入")
                        .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Home.this, LogIn.class);
                                Home.this.startActivity(intent);
                            }
                        })
                        .setNegativeButton("等等再登入", null)
                        .create()
                        .show();

            }
        } else if(id == R.id.cloudBackup){
            signIn(REQUEST_CODE_SIGN_IN_create);
        }else if(id == R.id.cloudreturn){
            signIn(REQUEST_CODE_SIGN_IN_restore);
        }else if(id == R.id.phoneBackup){
            backUp();
        }else if(id == R.id.phone_restore){
            restore();
        }
        DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void signIn(int askcode) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE_APPDATA))
                // .requestIdToken("887086177375-ukp7vglcak9hk278dvuh87p3gdgr7qjd.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), askcode);
    }

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

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    //到帳本管理
    public void jumpToBookManage() {
        Intent intent = new Intent(Home.this, BookManage.class);
        startActivity(intent);
    }


    //到記帳
    public void jumpToadd_spend() {
        Intent intent = new Intent(Home.this, add_expense.class);
        startActivity(intent);
    }
    //到查帳
    public void jumpTocheck_expense() {
        Intent intent = new Intent(Home.this, check_expense.class);
        startActivity(intent);
    }
    //到購物商城
    public void jumpToOnlineShoppingPath() {
        startActivity(new Intent(Home.this, onlineShoppingPath.class));
    }
    //到許願池
    public void jumpToWishpool() {
        startActivity(new Intent(Home.this, com.example.nccumis.com.example.nccumis.onlineshopping.wishpool.class));
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
        String month = (cal.get(Calendar.MONTH) +1 < 10) ? "0"+ Integer.toString(cal.get(Calendar.MONTH) +1) : Integer.toString(cal.get(Calendar.MONTH) +1);

        this.dateinStart = year+"-"+month+"-01";
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

    }

    public void initBook(){
        this.dbBookData.clear();
        this.selectBook.clear();

        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());
        dbmanager.open();
        this.dbBookData = dbmanager.fetchunclosedBook();
        dbmanager.close();

        for(int i = 0; i<dbBookData.size(); i++){
            if(this.selectBook.contains(dbBookData.get(i))){
                continue;
            }else{
                this.selectBook.add(dbBookData.get(i));
            }
        }
    }

    //selectBook為丟進資料庫fetch的參數
    public void setBook(String book){
        this.selectBook.clear();
        this.selectBook.add(book);
    }


    //計算該帳本預算占幾%
    public float countPercentage(){
        if(startBudget+income == 0 || expense >= startBudget+income){
            return 100;
        }

        return ((float)expense/(startBudget+income))*100f;
    }

    public void countExpenseAndIncome(){
        expense = 0;
        income = 0;
        for(int i = 0; i < select_expense.size();i++){
            expense += select_expense.get(i).getEx_price();
        }
        for(int i= 0; i<select_income.size();i++){
            income += select_income.get(i).getIn_price();
        }
    }
    public static boolean isDate2Bigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date  dt1 = sdf.parse(str1);
            Date  dt2 = sdf.parse(str2);
            if (dt1.getTime() <= dt2.getTime()) {
                isBigger = false;
            } else if (dt1.getTime() > dt2.getTime()) {
                isBigger = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isBigger;
    }
    private void refresh() {
        finish();
        Intent intent = new Intent(Home.this, Home.class);
        startActivity(intent);
    }
    private boolean checklogin(){
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.getString("member_id",null)!=null){
            return true;
        }
        else {
            return false;
        }
    }
}