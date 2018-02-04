package com.fynn.smsforwarder.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseActivityCapacity;
import com.fynn.smsforwarder.base.BasePresenter;
import com.fynn.smsforwarder.base.Model;

/**
 * Created by fynn on 2018/2/4.
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
        if (savedInstanceState == null) {
            mFragments = new Fragment[] {
                    PlusOneFragment.newInstance("1111", ""),
                    PlusOneFragment.newInstance("2222", ""),
                    PlusOneFragment.newInstance("3333", "")
            };
        } else {
            FragmentManager manager = mActivity.getSupportFragmentManager();
            mFragments = new Fragment[]{
                    manager.findFragmentByTag("1"),
                    manager.findFragmentByTag("2"),
                    manager.findFragmentByTag("3")
            };
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void initActions(Bundle savedInstanceState) {
        //go2Fragment(mFragments[0]);

        mViewPager.setAdapter(new FragmentAdapter(mActivity.getSupportFragmentManager()));
    }

    private void go2Fragment(Fragment fragment) {

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
