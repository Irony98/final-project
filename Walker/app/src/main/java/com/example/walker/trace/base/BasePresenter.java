package com.example.walker.trace.base;

import java.lang.ref.WeakReference;

public abstract class BasePresenter <T extends BaseView>{
    public WeakReference<T> ActView;

    public BasePresenter() {
        super();
    }

    protected T getView() {
        return ActView.get();
    }

    public boolean isViewAttached() {
        return ActView != null && ActView.get() != null;
    }

    public void attach(T view) {
        if (ActView == null) {
            ActView = new WeakReference<T>(view);
        }
    }

    public void detach() {
        if (ActView != null) {
            ActView.clear();
            ActView = null;
        }
    }

}
