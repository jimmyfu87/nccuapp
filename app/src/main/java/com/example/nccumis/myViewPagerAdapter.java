package com.example.nccumis;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class myViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> mListViews;

    public myViewPagerAdapter(ArrayList<View> mListViews){
        this.mListViews = mListViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        View view = mListViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
