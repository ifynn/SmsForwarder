package com.fynn.smsforwarder.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fynn on 2018/2/4.
 */

public abstract class BaseFragment<V, M extends Model, P extends BasePresenter<V, M>> extends Fragment {

    protected P mPresenter;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int id = getContentViewId();
        if (id <= 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        rootView = inflater.inflate(id, container, false);

        createPresenterInternal();
        createModelInternal();

        initViews(savedInstanceState);
        initActions(savedInstanceState);

        return rootView;
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

    @SuppressWarnings("TypeParameterUnusedInFormals")
    public <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }
}
