package com.example.walker.step.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;



public class StepCountModeDispatcher {
    private Context context;
    private boolean hasSensor;

    public StepCountModeDispatcher(Context context) {
        this.context = context;
        hasSensor = isSupportStepCountSensor();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isSupportStepCountSensor() {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isSupportStepCountSensor(Context context) {
        // 获取传感器管理器的实例
        SensorManager sensorManager = (SensorManager) context
                .getSystemService(context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        return countSensor != null || detectorSensor != null;
    }
}
