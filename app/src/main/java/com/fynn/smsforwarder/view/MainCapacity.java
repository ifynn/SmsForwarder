package com.fynn.smsforwarder.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseActivityCapacity;
import com.fynn.smsforwarder.base.BasePresenter;
import com.fynn.smsforwarder.base.Model;

/**
 *
 * @author fynn
 * @date 2018/2/4
 */

public class MainCapacity extends BaseActivityCapacity {

    private Fragment[] mFragments;

    private ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mActivity.getSupportActionBar().setTitle(R.string.title_home);
                    return true;

                case R.id.navigation_dashboard:
                    mActivity.getSupportActionBar().setTitle(R.string.title_dashboard);
                    return true;

                case R.id.navigation_notifications:
                    mActivity.getSupportActionBar().setTitle(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mFragments = new Fragment[]{
                HomeFragment.newInstance(),
                SmsFlowFragment.newInstance(),
                NotificationFragment.newInstance()
        };

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void initActions(Bundle savedInstanceState) {
        mViewPager.setAdapter(new FragmentAdapter(mActivity.getSupportFragmentManager()));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected Model createModel() {
        return null;
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }
}
