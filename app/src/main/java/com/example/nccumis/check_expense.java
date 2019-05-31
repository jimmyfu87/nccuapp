package com.example.nccumis;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class check_expense extends AppCompatActivity {
    private final int startDate = -1;
    private final int endDate = 1;

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
    private List<String> typeName = new ArrayList<String>();

    private Button lastPage;
    private Button switchAccount;
    private EditText dateStart_input;
    private EditText dateEnd_input;
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
    private XAxis xAxis;
    private YAxis yAxis;

    private List<Expense> select_expense = new ArrayList<Expense>();

    private ListView TypeListView;
    private List<Integer> numberArray = new ArrayList<Integer>();
    private List<String> nameArray = new ArrayList<String>();
    private List<String> percentageArray = new ArrayList<String>();
    private List<Integer> totalArray = new ArrayList<Integer>();

    private List<String> bookArray = new ArrayList<String>();
    private List<String> selectBooks = new ArrayList<String>();

    private boolean[] checked ;
    private  String[] checking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_check);
        //一開始預設統計所有帳本資料
        setBookArray();
        initSelectBooks();
        checked=new boolean[bookArray.size()];
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
        dateStart_input = (EditText)findViewById(R.id.dateStart_input);
        dateStart_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(startDate);
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
                    showDatePickDlg(startDate);
                    dateStart_input.setInputType(InputType.TYPE_NULL);        // disable soft input
                }

            }
        });
        //結束日期
        dateEnd_input = (EditText)findViewById(R.id.dateEnd_input);
        dateEnd_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(endDate);
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
                    showDatePickDlg(endDate);
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
                    dbmanager.close();
                    setExpenseData(select_expense);
                    setList();
                    setListViewHeightBasedOnChildren(TypeListView);
                    //setPieChart();
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
                //setPieChart();
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
                select_expense=dbmanager.fetchExpenseWithbook(start_date,end_date,selectBooks);
                dbmanager.close();
                setExpenseData(select_expense);
                setList();
                setListViewHeightBasedOnChildren(TypeListView);
  //              setPieChart();
            }
        });

        //圖表
        setExpenseData(select_expense);
        //setPieChart();


        //ListView 類別項目、類別名稱、類別佔總額%、類別金額
        TypeListView = (ListView)findViewById(R.id.TypeListView);
        setList();
        setListViewHeightBasedOnChildren(TypeListView);
    }

    public void showDatePickDlg(final int checknum) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(check_expense.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                // -1是startDate  1是EndDate
                if(checknum == startDate){
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
                                //checkedStatusList.set(which, isChecked);
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
            if(this.typeName.contains(getTypeName)){
                replacePosition = this.typeName.indexOf(getTypeName);
                replacePrice = this.getPriceData.get(replacePosition) + getPrice;
                this.getPriceData.set(replacePosition, replacePrice);
            } else{
                this.typeName.add(getTypeName);
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
        this.typeName.clear();
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

    public void initListData(){
        for(int i = 0; i < this.getPriceData.size();i++){
            int index = i+1;
            this.numberArray.add(index);
            this.nameArray.add(this.typeName.get(i));
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


//    public void setPieChart(){
//   List<PieEntry> pieEntries = new ArrayList<>();
//        double selectDateTotalPrice = (double)countSelectDateTotalPrice(this.getPriceData);
//        for(int i = 0; i < getPriceData.size(); i++){
//            if((getPriceData.get(i)/selectDateTotalPrice)<0.05)
//                pieEntries.add(new PieEntry(getPriceData.get(i) , ""));
//            else
//             pieEntries.add(new PieEntry(getPriceData.get(i) , typeName.get(i)));
//
//        }
//    for(int i = 0; i < getPriceData.size(); i++){
//             pieEntries.add(new PieEntry(getPriceData.get(i) , typeName.get(i)));
//         }

 //       PieDataSet dataSet = new PieDataSet(pieEntries , "類別");
//        dataSet.setSliceSpace(3f);           //设置饼状Item之间的间隙
//        dataSet.setValueTextSize(16f);
//        dataSet.setColors(this.COLORFUL_COLORS);
 //       PieData data = new PieData(dataSet);

 //       PieChart expenseChart = (PieChart) findViewById(R.id.expense_chart);
 //       expenseChart.getDescription().setEnabled(false);
 //       expenseChart.setHighlightPerTapEnabled(true);
  //      expenseChart.setEntryLabelTextSize(16f);
  //      expenseChart.setRotationAngle(90);
   //     expenseChart.animateXY(800, 800);
   //     expenseChart.setData(data);
   //     expenseChart.setUsePercentValues(true);
   //     expenseChart.invalidate();
   //     expenseChart.setCenterText("Total\n"+countSelectDateTotalPrice(this.getPriceData));
   //     expenseChart.setCenterTextSize(20);

   // }
   public void setLineChart(){

       LineChart expenseChart = (LineChart) findViewById(R.id.expense_chart);
      //LineDataSet dataSet = new LineDataSet();

       //XAxis xAxis =LineChart.getXAxis();
       //YAxis yAxis = LineChart.getYAxis();
       xAxis.setDrawAxisLine(true);
       xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴位置
       xAxis.setDrawGridLines(true);//不绘制网格线
       xAxis.setDrawLabels(true);//绘制该轴标签
       xAxis.setTextColor(Color.RED);// 设置轴标签的颜色。
       xAxis.setTextSize(11f);//设置轴标签的文字大小。
       xAxis.setValueFormatter(new IndexAxisValueFormatter() {
           public String formarvalue(float value, AxisBase axis) {
               int i = (int) value;
               return "数据"+i;
           }
       });
       xAxis.setLabelCount(5);




       //dataSet.setValueTextSize(16f);
       //dataSet.setColors(this.COLORFUL_COLORS);
       //LineData data = new LineData(dataSet);

       expenseChart.getDescription().setEnabled(false);
       expenseChart.setHighlightPerTapEnabled(true);
      // expenseChart.setEntryLabelTextSize(16f);
       //expenseChart.setRotationAngle(90);
       expenseChart.animateXY(800, 800);
      // expenseChart.setData(data);
       //expenseChart.setUsePercentValues(true);
       expenseChart.invalidate();
       //expenseChart.setCenterText("Total\n"+countSelectDateTotalPrice(this.getPriceData));
       //expenseChart.setCenterTextSize(20);

   }

    //暫存日期
    public void setdateInfo(int startOrEnd, int year, int month, int day){
        if(startOrEnd==this.startDate){
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
        setdateInfo(this.startDate,Integer.parseInt(year), Integer.parseInt(month),1);
        set_start_dateformat(Integer.parseInt(year),Integer.parseInt(month),1);

        if(month == "2"){
            int intYear = Integer.parseInt(year);
            if ((intYear % 4 == 0 && intYear % 100 != 0) || (intYear % 400 == 0 && intYear % 3200 != 0)){
                this.dateEnd_input.setText(year+"年"+month+"月"+29+"日");
                setdateInfo(this.endDate,Integer.parseInt(year), Integer.parseInt(month),29);
                set_end_dateformat(Integer.parseInt(year),Integer.parseInt(month),29);

            }else {
                this.dateEnd_input.setText(year+"年"+month+"月"+28+"日");
                setdateInfo(this.endDate,Integer.parseInt(year), Integer.parseInt(month),28);
                set_end_dateformat(Integer.parseInt(year),Integer.parseInt(month),28);
            }
        }else if(oddMonth.contains(month)){
            this.dateEnd_input.setText(year+"年"+month+"月"+31+"日");
            setdateInfo(this.endDate,Integer.parseInt(year), Integer.parseInt(month),31);
            set_end_dateformat(Integer.parseInt(year),Integer.parseInt(month),31);
        }else {
            this.dateEnd_input.setText(year+"年"+month+"月"+30+"日");
            setdateInfo(this.endDate,Integer.parseInt(year), Integer.parseInt(month),30);
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
        setdateInfo(this.startDate,Integer.parseInt(year), 1,1);
        set_start_dateformat(Integer.parseInt(year),1,1);

        this.dateEnd_input.setText(year + "年" + 12 + "月" + 31+"日");
        setdateInfo(this.endDate,Integer.parseInt(year), 12,31);
        set_end_dateformat(Integer.parseInt(year),12,31);
        this.dateEnd_input.setError(null);
    }

    //檢查輸入日期是否有誤
    public boolean checkDateInput(View view){
        if(this.yearStart > this.yearEnd || this.monthStart > monthEnd || this.dayStart > dayEnd){
            this.dateEnd_input.setError("結束日期小於開始日期");
            Snackbar.make(view,"結束日期小於開始日期，請重新修改",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        this.dateEnd_input.setError(null);
        return true;
    }


    public void jumpToHome(){
        Intent intent = new Intent(check_expense.this,Home.class);
        startActivity(intent);
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
