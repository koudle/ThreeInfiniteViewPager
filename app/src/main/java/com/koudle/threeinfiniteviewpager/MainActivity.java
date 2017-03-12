package com.koudle.threeinfiniteviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Created by kl on 17-3-12.
 */

public class MainActivity extends Activity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
    }

    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<InfiniteData> infiniteDataArrayList = new ArrayList<>();

        for(int i=0;i<10;i++){
            InfiniteData data = new InfiniteData(i);
            infiniteDataArrayList.add(data);
        }
        MyInfiViewPagerAdapter<InfiniteData> myPagerAdapter = new MyInfiViewPagerAdapter<>(MainActivity.this,viewPager);
        myPagerAdapter.addDataEnd(infiniteDataArrayList);

        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(1,false);
    }
}
