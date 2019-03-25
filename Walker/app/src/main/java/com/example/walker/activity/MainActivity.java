package com.example.walker.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.example.walker.R;


import com.example.walker.step.UpdateUiCallBack;
import com.example.walker.step.service.StepService;
import com.example.walker.step.utils.SharedPreferencesUtils;
import com.example.walker.view.StepArcView;
import com.example.walker.view.KiloView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_data;
    private StepArcView cc;
    private KiloView dd;
    private TextView tv_set;
    private TextView tv_isSupport;
    private SharedPreferencesUtils sp;
    private boolean isBind = false;

    private void assignViews() {
        tv_data = (TextView) findViewById(R.id.tv_data);
        cc = (StepArcView) findViewById(R.id.cc);
        dd=(KiloView) findViewById(R.id.dd);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_isSupport = (TextView) findViewById(R.id.tv_isSupport);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        initData();
        //addListener();
    }


    private void initData() {
        sp = new SharedPreferencesUtils(this);
        //get plan, set default goal 10000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "10000");
        //set current step zero.
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        dd.setCurrentCount(0);
        tv_isSupport.setText("Counting...");
        setupService();
    }


    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }


    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "10000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());
            dd.setCurrentCount(stepService.getStepCount());
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                    dd.setCurrentCount(stepCount);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                startActivity(new Intent(this, SetPlanActivity.class));
                break;
            case R.id.tv_data:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.bt_trail:
                startActivity(new Intent(this, TraceActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }
}
