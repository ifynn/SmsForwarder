package com.fynn.smsforwarder.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.TextView;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseActivityCapacity;
import com.fynn.smsforwarder.business.presenter.DefaultPresenter;
import com.fynn.smsforwarder.model.SmsStorageModel;

/**
 * @author fynn
 * @date 2018/2/4
 */

public class MainCapacity extends BaseActivityCapacity<BaseView, SmsStorageModel, DefaultPresenter>
        implements BaseView, ViewInteraction {

    private Fragment[] mFragments;

    private ViewPager mViewPager;
    private BottomNavigationView navigation;
    private TextView tvTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    tvTitle.setText(R.string.title_home);
                    break;

                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    tvTitle.setText(R.string.title_dashboard);
                    break;

                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);
                    tvTitle.setText(R.string.title_notifications);
                    break;

                default:
                    return false;
            }
            return true;
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mFragments = new Fragment[]{
                HomeFragment.newInstance(this),
                SmsFlowFragment.newInstance(this),
                NotificationFragment.newInstance(this)
        };

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{mActivity.getResources().getColor(R.color.qmui_config_color_gray_6),
                mActivity.getResources().getColor(R.color.colorPrimary)
        };
        ColorStateList csl = new ColorStateList(states, colors);
        navigation.setItemTextColor(csl);
        navigation.setItemIconTintList(csl);

        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void initActions(Bundle savedInstanceState) {
        mViewPager.setAdapter(new FragmentAdapter(mActivity.getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(navigation.getMenu().getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected DefaultPresenter createPresenter() {
        return new DefaultPresenter();
    }

    @Override
    protected SmsStorageModel createModel() {
        return new SmsStorageModel();
    }

    @Override
    public SmsStorageModel getModel() {
        return mPresenter.getModel();
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
