package com.fynn.smsforwarder.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by fynn on 2018/2/4.
 */

public class BasePresenter<V, M> implements Presenter<V, M> {

    protected Reference<V> mViewRef;

    protected M mModel;

    @Override
    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    @Override
    public V getView() {
        return mViewRef == null ? null : mViewRef.get();
    }

    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (mViewRef == null) {
            return;
        }

        mViewRef.clear();
        mViewRef = null;
    }

    @Override
    public void referTo(M model) {
        mModel = model;
    }

    public M getModel() {
        return mModel;
    }
}
