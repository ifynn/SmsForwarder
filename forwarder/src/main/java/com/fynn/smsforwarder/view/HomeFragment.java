package com.fynn.smsforwarder.view;

import android.os.Bundle;
import android.widget.TextView;

import com.fynn.smsforwarder.R;
import com.fynn.smsforwarder.base.BaseFragment;
import com.fynn.smsforwarder.business.DefaultPresenter;
import com.fynn.smsforwarder.model.SmsStorageModel;

/**
 * @author fynn
 */
public class HomeFragment extends BaseFragment<BaseView, SmsStorageModel, DefaultPresenter>
        implements BaseView {

    private TextView tvCount;

    private ViewInteraction interaction;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(ViewInteraction interaction) {
        HomeFragment fragment = new HomeFragment();
        fragment.interaction = interaction;
        fragment.setRetainInstance(true);
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
        tvCount.setText(mPresenter.getDbRecordCount() + "");
    }

    @Override
    protected DefaultPresenter createPresenter() {
        return new DefaultPresenter();
    }

    @Override
    protected SmsStorageModel createModel() {
        return interaction.getModel();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser) {
            return;
        }

        if (tvCount != null) {
            tvCount.setText(mPresenter.getDbRecordCount() + "");
        }
    }
}
