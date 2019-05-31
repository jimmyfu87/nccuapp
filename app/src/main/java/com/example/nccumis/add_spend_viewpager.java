package com.example.nccumis;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

public class add_spend_viewpager extends AppCompatActivity {
    ViewPager pager;
    ArrayList<View> pagerList;
    int indexPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spend_viewpager);
        pager = (ViewPager) findViewById(R.id.pager);

        LayoutInflater li = getLayoutInflater().from(this);
        View v1 = li.inflate(R.layout.expense_add,null);
        View v2 = li.inflate(R.layout.income_add,null);
        pagerList = new ArrayList<View>();
        pagerList.add(v1);
        pagerList.add(v2);

        pager.setAdapter(new myViewPagerAdapter(pagerList));
        pager.setCurrentItem(0);


        indexPage = pager.getCurrentItem();

        //updatepage();
    }

    private void updatepage(){
        startActivity(new Intent(add_spend_viewpager.this, pagerList.get(indexPage).getClass()));
    }
}
