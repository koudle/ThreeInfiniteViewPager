package com.koudle.threeinfiniteviewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koudle.threeinfiniteviewpagerlibrary.InfiniteViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kl on 17-3-12.
 */

public class MyInfiViewPagerAdapter<T> extends InfiniteViewPagerAdapter {
    private Context mContenxt;
    private List<View> mBufferView = new ArrayList<>();

    public MyInfiViewPagerAdapter(Context context, ViewPager viewPager){
        super(viewPager);
        mContenxt = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView textView1 =  mBufferView.size() >0 ? (TextView) mBufferView.remove(0) : null;
        if(textView1 == null) {
            textView1 = new TextView(mContenxt);
        }
        textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        InfiniteData data = (InfiniteData) getItemData(getRealPosition(position));
        String str = new String();
        for (int i= 0;i<99;i++) {
            str +=String.valueOf(data.mText);
        }

        textView1.setText(str);

        if(data.mText % 3 == 0){
            textView1.setBackgroundColor(Color.RED);
        }else if(data.mText % 3 == 1){
            textView1.setBackgroundColor(Color.BLUE);
        }else if(data.mText %3 == 2){
            textView1.setBackgroundColor(Color.GREEN);
        }
        container.addView(textView1);
        return textView1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mBufferView.add((View)object);
        container.removeView((View)object);
    }
}
