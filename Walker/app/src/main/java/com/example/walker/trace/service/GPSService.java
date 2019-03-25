package com.example.walker.trace.service;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;
import com.example.walker.app.MyApplication;
import com.example.walker.trace.MsgObservable;
import com.example.walker.trace.RunConstants;
import com.example.walker.trace.adapter.mLocationListener;
import com.example.walker.trace.base.BaseService;
import com.example.walker.trace.bean.InfoBean;
import com.example.walker.trace.bean.PositionBean;
import com.example.walker.trace.utils.ThreadPoolUtils;
import com.orhanobut.logger.Logger;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.example.walker.trace.RunConstants.FIRST_LOCATION_MSG;
import static com.example.walker.trace.RunConstants.RUN_FIRST_LOCATION_MSG;
import static com.example.walker.trace.RunConstants.RUN_LOCATION_MSG;


public class GPSService extends BaseService<GPSSerView, GPSSerPresenter> implements GPSSerView {

    static final int HASH_CODE = 1;
    public static boolean sShouldStopService = false;
    public static boolean isRunStop = true;
    public static Disposable sSubscription;
    private LocationManager locationManager;
    public static int mGpsSatelliteNumber = 0;

    int onStart(Intent intent, int flags, int startId) {
        startForeground(HASH_CODE, new Notification());
        if (sShouldStopService) stopService();
        else startService();
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void setOutDoorRunListener() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mSerPersenter.getLatLng(locationManager);
        if (location != null) {
            Message msg = Message.obtain();
            msg.what = FIRST_LOCATION_MSG;
            msg.obj = location;
            MsgObservable.getInstance().sendMsgs(msg);
            Logger.d(LTAG, "bestProvider : get the location success!");
        } else {
            Logger.d(LTAG, "bestProvider : get the location failed!");
        }


        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000, 2, locationGpsListener);
            locationManager.addGpsStatusListener(statusListener);
        } else {

        }
    }


    void startService() {
        if (sShouldStopService) return;
        if (sSubscription != null && !sSubscription.isDisposed()) return;
        setOutDoorRunListener();
        sSubscription = Observable
                .interval(3, TimeUnit.SECONDS)
                .doOnDispose(() -> {
                }).subscribe(count -> {
                });

    }


    public static void stopService() {
        sShouldStopService = true;
        if (sSubscription != null) sSubscription.dispose();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return onStart(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        onStart(intent, 0, 0);
        return null;
    }

    void onEnd(Intent rootIntent) {
        startService(new Intent(MyApplication.getInstance(), GPSService.class));
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        onEnd(rootIntent);
    }

    @Override
    protected GPSSerPresenter createPresenter() {
        return new GPSSerPresenter();
    }


    @Override
    public void onDestroy() {
        onEnd(null);
    }

    @Override
    public void onRespondError(String message) {

    }



    @SuppressLint("MissingPermission")
    private GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                // first locate
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Logger.i(LTAG, "第一次定位");
                    break;
                // change state
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    // get current state
                    GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    mGpsSatelliteNumber = count;
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    Logger.i(LTAG, "start locate");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    mGpsSatelliteNumber = 0;
                    Logger.i(LTAG, "end locate");
                    break;
            }
        }

        ;
    };


    public static Location preLocation;
    private static PositionBean oncePosition;
    private static ArrayList<LatLng> mLocationList = new ArrayList<>();

    private mLocationListener locationGpsListener = new mLocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Logger.w(LTAG, "locationListener : onStatusChanged ,  status : " + status);
            if (status != LocationProvider.AVAILABLE) {
            }
        }

        @Override
        public void onLocationChanged(final Location mlocation) {
            if (mlocation == null) {
                return;
            }
            if (!isRunStop) {
                ThreadPoolUtils.getInstance().addTask(new Runnable() {
                    @Override
                    public void run() {
                        if (preLocation != null) {
                            double latitude = mlocation.getLatitude();
                            double longitude = mlocation.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            latLng = mSerPersenter.gpsToBaidu(latLng);
                            mlocation.setLatitude(latLng.latitude);
                            mlocation.setLongitude(latLng.longitude);


                            if (preLocation.getLatitude() != latLng.latitude
                                    || preLocation.getLongitude() != latLng.longitude) {

                                oncePosition = mSerPersenter.getMoveGpsData(preLocation,
                                        mlocation);
                                double totalDistance = mSerPersenter
                                        .getDoubleToDouble(oncePosition.distance);
                                mLocationList.add(latLng);

                                InfoBean info =
                                        new InfoBean(InfoBean.GET_ONCE_LOCATION,
                                                mLocationList, oncePosition, mlocation);
                                Message msg = Message.obtain();
                                msg.what = RUN_LOCATION_MSG;
                                msg.obj = info;
                                MsgObservable.getInstance().sendMsgs(msg);
                            }

                        } else {

                            double latitude = mlocation.getLatitude();
                            double longitude = mlocation.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            latLng = mSerPersenter.gpsToBaidu(latLng);
                            mlocation.setLatitude(latLng.latitude);
                            mlocation.setLongitude(latLng.longitude);

                            mLocationList.add(latLng);
                            Message msg = Message.obtain();
                            msg.what = RUN_FIRST_LOCATION_MSG;
                            msg.obj = mlocation;
                            MsgObservable.getInstance().sendMsgs(msg);
                        }

                        preLocation = mlocation;
                    }

                });

            } else {
                preLocation = null;
                if (mLocationList.size() > 0) {
                    mLocationList.clear();
                }
                Message msg = Message.obtain();
                msg.what = FIRST_LOCATION_MSG;
                msg.obj = mlocation;
                MsgObservable.getInstance().sendMsgs(msg);
            }
        }
    };
}
