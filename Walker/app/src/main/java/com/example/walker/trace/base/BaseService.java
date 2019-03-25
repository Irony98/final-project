package com.example.walker.trace.base;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public abstract class BaseService<V extends BaseView, P extends BasePresenter<V>> extends Service implements BaseView{
    public BaseService() {
    }
    public final String LTAG = BaseService.this.getClass().getSimpleName();
    protected P mSerPersenter;
    public Handler handler;
    protected abstract P createPresenter();
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate() {
        super.onCreate();
        mSerPersenter = createPresenter();
        if (mSerPersenter != null) {
            // BasePersenter类的方法。主要用于将View用弱引用赋值给P层的View对象
            mSerPersenter.attach((V) this);
        }
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onDestroy() {
        if (mSerPersenter != null) {
            // BasePersenter类的方法。主要用于将View的引用清除。
            mSerPersenter.detach();
        }
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showToast(final String toast) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
