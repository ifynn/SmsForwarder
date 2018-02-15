package com.fynn.smsforwarder.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by fynn on 2018/2/4.
 */

public abstract class BaseActivityCapacity<V, M extends Model, P extends BasePresenter<V, M>>
        implements ActivityCapacity, View.OnClickListener {

    protected AppCompatActivity mActivity;

    protected P mPresenter;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) activity;
        mActivity.setContentView(getContentViewId());

        createPresenterInternal();
        createModelInternal();

        initViews(savedInstanceState);
        initActions(savedInstanceState);
    }

    @LayoutRes
    protected abstract int getContentViewId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initActions(Bundle savedInstanceState);

    private void createPresenterInternal() {
        mPresenter = createPresenter();

        if (mPresenter == null) {
            return;
        }

        onCreatePresenter();
        mPresenter.attachView((V) this);
    }

    protected void onCreatePresenter() {
        // no-op
    }

    private void createModelInternal() {
        if (mPresenter == null) {
            return;
        }
        mPresenter.referTo(createModel());
    }

    protected abstract P createPresenter();

    protected abstract M createModel();

    public void startActivity(Class<ActivityCapacity> capacityClass) {
        Intent intent = new Intent(mActivity, BaseActivityImpl.class);
        intent.putExtra(ActivityCapacity.CAPACITY_NAME, capacityClass.getName());
        mActivity.startActivity(intent);
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    public <T extends View> T findViewById(@IdRes int id) {
        return mActivity.findViewById(id);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onActivityKeyDowned(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onActivityConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }
}
