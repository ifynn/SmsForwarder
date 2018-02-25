package com.fynn.smsforwarder.view;

import android.os.Bundle;
import android.widget.TextView;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseFragment;
import com.fynn.smsforwarder.base.BasePresenter;
import com.fynn.smsforwarder.base.Model;
import com.fynn.smsforwarder.common.db.SmsDbHelper;
import com.fynn.smsforwarder.model.consts.Consts;

import org.fynn.appu.util.LogU;

/**
 * @author fynn
 */
public class HomeFragment extends BaseFragment {

    private TextView tvCount;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvCount = (TextView) findViewById(R.id.tv_count);
    }

    @Override
    protected void initActions(Bundle savedInstanceState) {
        Consts.sSmsCount.set(SmsDbHelper.get().getCount());
        tvCount.setText(Consts.sSmsCount + "");
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected Model createModel() {
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogU.e("onHiddenChanged", hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser) {
            return;
        }

        if (tvCount != null) {
            tvCount.setText(Consts.sSmsCount + "");
        }
    }
}
