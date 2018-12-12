package com.koudle.threeinfiniteviewpagerlibrary;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by kl on 17-3-12.
 * 总共有三个item，index为0，1，2，但是只有1总是可见的，0和2是用来实现无限滑动效果的
 */
public abstract class InfiniteViewPagerAdapter<T> extends PagerAdapter {

    private static final int INFINITE_COUNT_EXTRA = 2;
    private static final int REAL_SHOW_COUNT = 1  ;
    private int mPositionOffset = 0;
    public List<T> mVideoDataList = new ArrayList<>();
    private ReentrantLock mLock = new ReentrantLock();

    public InfiniteViewPagerAdapter(ViewPager viewPager){
        init(viewPager);
    }

    private void init(final ViewPager viewPager){
        if(viewPager == null) return;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滑动过程中
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    //表示要往前翻页
                    InfiniteViewPagerAdapter.this.leftSlide();
                    InfiniteViewPagerAdapter.this.notifyDataSetChanged();
                }else if(position == 2){
                    //表示要往后翻页
                    InfiniteViewPagerAdapter.this.rightSlide();
                    InfiniteViewPagerAdapter.this.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE){
                    viewPager.setCurrentItem(InfiniteViewPagerAdapter.this.getVisibleItemPosition(), false);
                }
            }
        });
    }

    //获取总共有多少个数据
    public int getDataCount(){
        return mVideoDataList.size();
    }

    //用于显示view的position
    private int getVisibleItemPosition(){
        return REAL_SHOW_COUNT;
    }

    @Override
    public int getCount() {
        //item的数量只能为0个或3个
        if (mVideoDataList.size() == 0) {
            return 0;
        } else {
            return getVisibleItemPosition() + INFINITE_COUNT_EXTRA;
        }
    }

    public void insertDataStart(ArrayList<T> dataList){
        if(dataList == null) return;
        if(dataList.size() == 0) return;

        try {
            mLock.lock();
            ArrayList<T> temp = removeDuplicate(dataList);
            mPositionOffset += temp.size();
            mVideoDataList.addAll(0,temp);
        }finally {
            mLock.unlock();
        }

    }

    public void addDataEnd(ArrayList<T> dataList){
        if(dataList == null) return;
        if(dataList.size() == 0) return;

        ArrayList<T> temp = removeDuplicate(dataList);
        mVideoDataList.addAll(temp);
    }

    private ArrayList<T> removeDuplicate(ArrayList<T> dataList ){
        Iterator<T> iterator = dataList.iterator();
        while (iterator.hasNext()){
            T temp = iterator.next();
            for (T data : mVideoDataList){
                if(temp.equals(data)){
                    iterator.remove();
                    break;
                }
            }
        }

        return dataList;
    }

    //往左滑
    public void leftSlide(){
        mPositionOffset -- ;
        if(mPositionOffset < 0){
            mPositionOffset += mVideoDataList.size();
        }
    }

    //往右划
    public void rightSlide(){
        mPositionOffset ++;
        if(mPositionOffset >= mVideoDataList.size()){
            mPositionOffset = 0;
        }
    }

    //获得真正要显示的数据
    public T getRealData(int position){
        int realPosition = getRealPosition(position);
        if(mVideoDataList.size() > realPosition){
            return mVideoDataList.get(realPosition);
        }else {
            return null;
        }
    }

    private int getRealPosition(int position){
        int realPosition = 0;
        if(position == 0){
            realPosition = mPositionOffset - 1;
            if(realPosition < 0){
                realPosition += mVideoDataList.size();
            }
        }else if(position == 1){
            realPosition = mPositionOffset;
        }else if(position == 2){
            realPosition = mPositionOffset + 1;
            if(realPosition >= mVideoDataList.size()){
                realPosition = 0;
            }
        }

        return realPosition;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
