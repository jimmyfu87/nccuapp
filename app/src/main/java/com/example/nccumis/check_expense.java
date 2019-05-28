package com.example.nccumis;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class check_expense extends AppCompatActivity {
    private final int START_DATE = -1;
    private final int END_DATE = 1;

    //預設25種
    private static final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31),
            Color.rgb(179, 100, 53),Color.rgb(220, 200, 50), Color.rgb(100, 100, 100), Color.rgb(70, 10, 20),
            Color.rgb(8, 10, 25), Color.rgb(42, 109, 130),Color.rgb(217, 80, 138), Color.rgb(254, 149, 7),
            Color.rgb(254, 247, 120), Color.rgb(106, 167, 134),Color.rgb(53, 194, 209),Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),Color.rgb(191, 134, 134), Color.rgb(179, 48, 80),
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157)
    };

    private List<Integer> getPriceData = new ArrayList<Integer>();
    private List<String> getTypeName = new ArrayList<String>();

    private Button lastPage;
    private Button switchAccount;
    private TextView dateStart_input;
    private TextView dateEnd_input;
    private Button searchButton;
    private Button searchthisMonth;
    private Button searchthisYear;
    private String start_date,end_date;
    private int yearStart = 0;
    private int monthStart = 0;
    private int dayStart = 0;
    private int yearEnd = 0;
    private int monthEnd = 0;
    private int dayEnd = 0;

    private List<Expense> select_expense = new ArrayList<Expense>();

    private ListView TypeListView;
    private List<Integer> numberArray = new ArrayList<Integer>();
    private List<String> nameArray = new ArrayList<String>();
    private List<String> percentageArray = new ArrayList<String>();
    private List<Integer> totalArray = new ArrayList<Integer>();

    private List<String> bookArray = new ArrayList<String>();
    private ArrayList<String> selectBooks = new ArrayList<String>();

    private boolean[] checked;

    private Intent getPreSavedData;
    private Bundle saveBag;

    ViewPager pager;
    ArrayList<View> pagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_check);

//        pager = (ViewPager) findViewById(R.id.pager);
//
//        LayoutInflater li = getLayoutInflater().from(this);
//        View v1 = li.inflate(R.layout.income_check,null);
//        pagerList = new ArrayList<View>();
//        pagerList.add(v1);
//
//        pager.setAdapter(new myViewPagerAdapter(pagerList));
//        pager.setCurrentItem(0);

        dateStart_input = (TextView) findViewById(R.id.dateStart_input);
        dateEnd_input = (TextView) findViewById(R.id.dateEnd_input);
        TypeListView = (ListView)findViewById(R.id.TypeListView);

        //自動傳入存回資料
        getPreSavedData = getIntent();
        saveBag = getPreSavedData.getExtras();

        if(saveBag != null){
            start_date = saveBag.getString("startDate");
            end_date = saveBag.getString("endDate");
            System.out.println("查帳："+start_date+", "+end_date);
            selectBooks = saveBag.getStringArrayList("selectBooks");

            dateStart_input.setText(resetDateformat(START_DATE,start_date));
            dateEnd_input.setText(resetDateformat(END_DATE,end_date));

            DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
            dbmanager.open();
            select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
            System.out.println(select_expense.size()+", "+selectBooks.size());
            dbmanager.close();
            setExpenseData(select_expense);
            setList();
            setListViewHeightBasedOnChildren(TypeListView);
            setPieChart();
        }else {
            //圖表
            setExpenseData(select_expense);
            setPieChart();

            //ListView 類別項目、類別名稱、類別佔總額%、類別金額
            setList();
            setListViewHeightBasedOnChildren(TypeListView);
        }
        //一開始預設統計所有帳本資料
        setBookArray();
        initSelectBooks();

        //上一頁
        lastPage = (Button)findViewById(R.id.lastPage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToHome();
            }
        });

        //切換帳戶
        switchAccount = (Button) findViewById(R.id.switchAccount);
        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiDialogEvent();
            }
        });

        //起始日期
        dateStart_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(START_DATE);
                    dateStart_input.setInputType(InputType.TYPE_NULL);      // disable soft input
                    return true;
                }
                return false;
            }
        });
        dateStart_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(START_DATE);
                    dateStart_input.setInputType(InputType.TYPE_NULL);        // disable soft input
                }

            }
        });
        //結束日期
        dateEnd_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(END_DATE);
                    dateEnd_input.setInputType(InputType.TYPE_NULL);        // disable soft input
                    return true;
                }
                return false;
            }
        });
        dateEnd_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(END_DATE);
                    dateEnd_input.setInputType(InputType.TYPE_NULL);        // disable soft input
                }

            }
        });

        // 搜尋按鈕
        this.searchButton = (Button) findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDateInput(v)){
                    //Expense 資料庫
                    clearList();
                    DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                    dbmanager.open();
                   // select_expense=dbmanager.fetchExpense(start_date,end_date);           //可直接調用select_expense的資訊
                    //System.out.println("Size of select books"+selectBooks.size());
                    select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
                    if(select_expense.isEmpty()){
                        Snackbar.make(v,"查詢金額為零",Snackbar.LENGTH_SHORT).show();
                    }
                    dbmanager.close();
                    setExpenseData(select_expense);
                    setList();
                    setListViewHeightBasedOnChildren(TypeListView);
                    setPieChart();
                }
            }
        });
        //搜尋本月
        searchthisMonth = (Button)findViewById(R.id.searchThisMonth_btn);
        searchthisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSetDateForMonthSearch();
                //Expense 資料庫
                clearList();
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                dbmanager.open();
                select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
                dbmanager.close();
                setExpenseData(select_expense);
                setList();
                setListViewHeightBasedOnChildren(TypeListView);
                setPieChart();
            }
        });

        //搜尋本年
        searchthisYear = (Button)findViewById(R.id.searchThisYear_btn);
        searchthisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSetDateForYearSearch();
                //Expense 資料庫
                clearList();
                DatabaseManager dbmanager=new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
                dbmanager.open();
                dbmanager.close();
                select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
                dbmanager.close();
                setExpenseData(select_expense);
                setList();
                setListViewHeightBasedOnChildren(TypeListView);
                setPieChart();
            }
        });


        //listview 切換頁面至流水帳
        TypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jumpTocheck_expense_detail(position);
            }
        });
    }

    public void showDatePickDlg(final int checknum) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(check_expense.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                // -1是startDate  1是EndDate
                if(checknum == START_DATE){
                    check_expense.this.dateStart_input.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
                    set_start_dateformat(year,monthOfYear,dayOfMonth);
                }else{
                    check_expense.this.dateEnd_input.setText(year + "年" + monthOfYear + "月" + dayOfMonth+"日");
                    set_end_dateformat(year,monthOfYear,dayOfMonth);
                }
                setdateInfo(checknum,year,monthOfYear,dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void multiDialogEvent(){
        this.selectBooks.clear();
//        final List<Boolean> checkedStatusList = new ArrayList<>();
//        for(String s : bookArray){
//            checkedStatusList.add(false);
//        }
        new AlertDialog.Builder(check_expense.this)
                .setMultiChoiceItems(bookArray.toArray(new String[bookArray.size()]), checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                                checked.set(which, isChecked);
                            }
                        })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuilder sb = new StringBuilder();
                        boolean isEmpty = true;
                        for(int i = 0; i < checked.length; i++){
                            if(checked[i]){
                                sb.append(bookArray.get(i));
                                sb.append(" ");
                                selectBooks.add(bookArray.get(i));
                                //System.out.println("Here"+bookArray.get(i));
                                isEmpty = false;
                            }
                        }
                        if(!isEmpty){
                            Toast.makeText(check_expense.this, "你選擇的是"+sb.toString(), Toast.LENGTH_SHORT).show();
//                            for(String s : bookArray){
//                                checkedStatusList.add(false);
//                            }
                        } else{
                            setBookArray();
                            initSelectBooks();
                            Toast.makeText(check_expense.this, "請勾選項目，系統已自動返回預設(統計所有帳本)", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .show();
    }

    public void setExpenseData(List<Expense> select_expense){
        int getPrice = 0;
        String getTypeName = "";
        int replacePosition = 0;
        int replacePrice = 0;

        for(int i = 0;i < select_expense.size();i++){
            getTypeName = select_expense.get(i).getType_name();
            getPrice = select_expense.get(i).getEx_price();
            if(this.getTypeName.contains(getTypeName)){
                replacePosition = this.getTypeName.indexOf(getTypeName);
                replacePrice = this.getPriceData.get(replacePosition) + getPrice;
                this.getPriceData.set(replacePosition, replacePrice);
            } else{
                this.getTypeName.add(getTypeName);
                this.getPriceData.add(getPrice);
            }
            //System.out.println(getTypeName+" ,"+getPriceData);
        }
    }



    public void setBookArray(){
        DatabaseManager dbmanager = new DatabaseManager(getApplicationContext());    //選取start_date到end_date的所有帳目，包裝成List<Expense>
        dbmanager.open();
        this.bookArray = dbmanager.fetchBook();           //可直接調用select_expense的資訊
        dbmanager.close();
        this.checked = new boolean[this.bookArray.size()];

        for(int i = 0; i < checked.length; i++){
            checked[i] = true;
        }
    }
    public void initSelectBooks(){
        for(int i = 0; i < bookArray.size(); i++) {
            this.selectBooks.add(bookArray.get(i));
        }
    }

    //統計前先清除List內的所有元素
    public void clearList(){
        this.select_expense.clear();
        this.getPriceData.clear();
        this.getTypeName.clear();
        this.numberArray.clear();
        this.nameArray.clear();
        this.percentageArray.clear();
        this.totalArray.clear();
    }

    //指定期間的支出總金額
    public int countSelectDateTotalPrice(List<Integer> getPriceData){
        //計算前先歸零
        int totalPrice = 0;
        for(int i = 0; i < getPriceData.size(); i++){
            totalPrice += getPriceData.get(i);
        }
        return totalPrice;
    }

    //計算該類別佔總額幾%
    public double countPercentage(double priceOfType , double total){
        if(total==0){
            return 0;
        }
        //System.out.println(priceOfType+", "+total+", "+priceOfType/total);
        return priceOfType/total*100;
    }

    public void sortListData(){
        ArrayList<Type> typelist = new ArrayList<Type>();
        for(int i = 0; i < this.getPriceData.size(); i++){
            typelist.add(new Type(this.getPriceData.get(i), this.getTypeName.get(i)));
            //System.out.println(typelist.get(i).getPrice()+", "+typelist.get(i).getTypeName());
        }
        ArrayList<Type> sortedTypelist = new ArrayList<Type>();
        //selection sort
        while(!typelist.isEmpty()){
            int index = 0;
            for(int i = 1; i < typelist.size(); i++){
                int largestPrice = typelist.get(0).getPrice();
                if(largestPrice < typelist.get(i).getPrice()){
                    largestPrice = typelist.get(i).getPrice();
                    index = i;
                }
            }
            Type type = new Type(typelist.get(index).getPrice(), typelist.get(index).getTypeName());
            sortedTypelist.add(type);
            typelist.remove(index);
        }

        //assgin sortedTypelist
        for(int i = 0; i < sortedTypelist.size(); i++){
            this.getPriceData.set(i ,sortedTypelist.get(i).getPrice());
            this.getTypeName.set(i ,sortedTypelist.get(i).getTypeName());
        }
    }

    public void initListData(){
        for(int i = 0; i < this.getPriceData.size();i++){
            int index = i+1;
            this.numberArray.add(index);
            this.nameArray.add(this.getTypeName.get(i));
            int selectDateTotalPrice = countSelectDateTotalPrice(this.getPriceData);
            double percentage = countPercentage(this.getPriceData.get(i), selectDateTotalPrice);
            DecimalFormat df = new DecimalFormat("##.0");
            percentage = Double.parseDouble(df.format(percentage));
            //System.out.println(selectDateTotalPrice+" ,"+percentage);
            this.percentageArray.add(percentage +" %");
            this.totalArray.add(this.getPriceData.get(i));
        }
        //System.out.println(this.getPriceData.size()+" ,"+this.typeName.size());
    }

    public void setList(){
        sortListData();
        initListData();
        ExpenseListAdapter Ex_adapter = new ExpenseListAdapter(this, this.numberArray, this.nameArray, this.percentageArray, this.totalArray);
        TypeListView.setAdapter(Ex_adapter);
    }

    /**
     * 動態設定ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public void setPieChart(){
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i = 0; i < getPriceData.size(); i++){
             pieEntries.add(new PieEntry(getPriceData.get(i) , getTypeName.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries , "類別");
        dataSet.setSliceSpace(3f);           //设置饼状Item之间的间隙
        dataSet.setValueTextSize(16f);
        dataSet.setColors(this.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart expenseChart = (PieChart) findViewById(R.id.expense_chart);
        expenseChart.getDescription().setEnabled(false);
        expenseChart.setHighlightPerTapEnabled(true);
        expenseChart.setEntryLabelTextSize(16f);
        expenseChart.setRotationAngle(90);
        expenseChart.animateXY(800, 800);
        expenseChart.setData(data);
        expenseChart.setUsePercentValues(true);
        expenseChart.invalidate();
        expenseChart.setCenterText("Total\n"+countSelectDateTotalPrice(this.getPriceData));
        expenseChart.setCenterTextSize(20);

    }

    //暫存日期
    public void setdateInfo(int startOrEnd, int year, int month, int day){
        if(startOrEnd==START_DATE){
            this.yearStart = year;
            this.monthStart = month;
            this.dayStart = day;
        }else{
            this.yearEnd = year;
            this.monthEnd = month;
            this.dayEnd = day;
        }
    }

    //根據當月自動設起訖日期
    public void autoSetDateForMonthSearch(){
        List<String> oddMonth = new ArrayList<String>();
        oddMonth.add("1");
        oddMonth.add("3");
        oddMonth.add("5");
        oddMonth.add("7");
        oddMonth.add("8");
        oddMonth.add("11");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String year = Integer.toString(cal.get(Calendar.YEAR));
        String month = Integer.toString(cal.get(Calendar.MONTH) +1);

        this.dateStart_input.setText(year + "年" + month + "月" + 1+"日");
        setdateInfo(START_DATE,Integer.parseInt(year), Integer.parseInt(month),1);
        set_start_dateformat(Integer.parseInt(year),Integer.parseInt(month),1);

        if(month == "2"){
            int intYear = Integer.parseInt(year);
            if ((intYear % 4 == 0 && intYear % 100 != 0) || (intYear % 400 == 0 && intYear % 3200 != 0)){
                this.dateEnd_input.setText(year+"年"+month+"月"+29+"日");
                setdateInfo(END_DATE,Integer.parseInt(year), Integer.parseInt(month),29);
                set_end_dateformat(Integer.parseInt(year),Integer.parseInt(month),29);

            }else {
                this.dateEnd_input.setText(year+"年"+month+"月"+28+"日");
                setdateInfo(END_DATE,Integer.parseInt(year), Integer.parseInt(month),28);
                set_end_dateformat(Integer.parseInt(year),Integer.parseInt(month),28);
            }
        }else if(oddMonth.contains(month)){
            this.dateEnd_input.setText(year+"年"+month+"月"+31+"日");
            setdateInfo(END_DATE,Integer.parseInt(year), Integer.parseInt(month),31);
            set_end_dateformat(Integer.parseInt(year),Integer.parseInt(month),31);
        }else {
            this.dateEnd_input.setText(year+"年"+month+"月"+30+"日");
            setdateInfo(END_DATE,Integer.parseInt(year), Integer.parseInt(month),30);
            set_end_dateformat(Integer.parseInt(year),Integer.parseInt(month),30);
        }
        this.dateEnd_input.setError(null);
        //System.out.println(this.dateinStart+" ,"+this.dateinEnd);
    }

    //根據當年自動設起訖日期
    public void autoSetDateForYearSearch(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String year = Integer.toString(cal.get(Calendar.YEAR));

        this.dateStart_input.setText(year + "年" + 1 + "月" + 1+"日");
        setdateInfo(START_DATE,Integer.parseInt(year), 1,1);
        set_start_dateformat(Integer.parseInt(year),1,1);

        this.dateEnd_input.setText(year + "年" + 12 + "月" + 31+"日");
        setdateInfo(END_DATE,Integer.parseInt(year), 12,31);
        set_end_dateformat(Integer.parseInt(year),12,31);
        this.dateEnd_input.setError(null);
    }

    //檢查輸入日期是否有誤
    public boolean checkDateInput(View view){
        if(this.yearStart > this.yearEnd || this.monthStart > monthEnd || this.dayStart > dayEnd){
            this.dateEnd_input.setError("結束日期小於開始日期");
            Snackbar.make(view,"結束日期小於開始日期，請重新修改",Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(dateStart_input.getText().toString().isEmpty()||dateEnd_input.getText().toString().isEmpty()){
            this.dateEnd_input.setError("開始或結束日期未填寫");
            Snackbar.make(view,"請填寫開始和結束日期",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        this.dateEnd_input.setError(null);
        return true;
    }


    public void jumpToHome(){
        Intent intent = new Intent(check_expense.this,Home.class);
        startActivity(intent);
    }

    public void jumpTocheck_expense_detail(int position){
        Intent intent = new Intent(check_expense.this, check_expense_detail.class);
        String typeName = nameArray.get(position);
        Bundle saveCheckExpenseData = new Bundle();
        saveCheckExpenseData.putString("typeName", typeName);
        saveCheckExpenseData.putString("startDate", start_date);
        saveCheckExpenseData.putString("endDate", end_date);
        saveCheckExpenseData.putStringArrayList("selectBooks", selectBooks);
        intent.putExtras(saveCheckExpenseData);
        startActivity(intent);
    }

    public String resetDateformat(int startOrEndDate, String date){
        String resetDate = "";
        int index = 0;

        for(String str : date.split("-")){
            resetDate += (Integer.parseInt(str) < 10) ? str.substring(1): str;
            if(index == 0){
                resetDate += "年";
                if(startOrEndDate == START_DATE){
                    this.yearStart = Integer.parseInt(str);
                }else{
                    this.yearEnd = Integer.parseInt(str);
                }
            } else if(index == 1){
                resetDate +="月";
                if(startOrEndDate == START_DATE){
                    this.monthStart = Integer.parseInt(str);
                }else{
                    this.monthEnd = Integer.parseInt(str);
                }
            }else if(index == 2){
                resetDate += "日";
                if(startOrEndDate == START_DATE){
                    this.dayStart = Integer.parseInt(str);
                }else{
                    this.dayEnd = Integer.parseInt(str);
                }
            }
            index++;
        }

        return resetDate;
    }

    public void set_start_dateformat(int year,int month,int day){
        String st_month;
        String st_day;
        if(month<10){
            st_month=Integer.toString(month);
            st_month="0"+st_month;
        }
        else{
            st_month=Integer.toString(month);
        }
        if(day<10){
            st_day=Integer.toString(day);
            st_day="0"+st_day;
        }
        else{
            st_day=Integer.toString(day);
        }
        start_date=year+"-"+st_month+"-"+st_day;
    }

    public void set_end_dateformat(int year,int month,int day){
        String st_month;
        String st_day;
        if(month<10){
            st_month=Integer.toString(month);
            st_month="0"+st_month;
        }
        else{
            st_month=Integer.toString(month);
        }
        if(day<10){
            st_day=Integer.toString(day);
            st_day="0"+st_day;
        }
        else{
            st_day=Integer.toString(day);
        }
        end_date=year+"-"+st_month+"-"+st_day;
    }
}
