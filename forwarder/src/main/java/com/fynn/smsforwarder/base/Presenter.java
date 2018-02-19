package com.fynn.smsforwarder.base;

/**
 * Created by fynn on 2018/2/4.
 */

public interface Presenter<V, M> {

    /**
     * 关联 view 角色
     */
    void attachView(V view);

    /**
     * 获取 view 角色
     *
     * @return
     */
    V getView();

    /**
     * 是否已关联 view 角色
     *
     * @return
     */
    boolean isViewAttached();

    /**
     * 取消关联 view 角色
     */
    void detachView();

    void referTo(M model);
}
