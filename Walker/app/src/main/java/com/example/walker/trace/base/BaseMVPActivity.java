package com.example.walker.trace.base;
import android.os.Bundle;
import android.widget.Toast;

public abstract class BaseMVPActivity <V extends BaseView, P extends BasePresenter<V>> extends BaseActivity implements BaseView {

        public P mActPersenter;
        @SuppressWarnings("unchecked")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mActPersenter = createPresenter();
            if (mActPersenter != null) {
                mActPersenter.attach((V) this);
            }
        }
        protected abstract P createPresenter();

        @Override
        protected void onDestroy() {
            if (mActPersenter != null) {
                mActPersenter.detach();
            }
            super.onDestroy();
        }

        @Override
        public void onRespondError(String message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
}
