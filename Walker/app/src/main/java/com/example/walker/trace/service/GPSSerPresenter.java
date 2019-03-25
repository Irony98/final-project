package com.example.walker.trace.service;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.walker.trace.base.BasePresenter;
import com.example.walker.trace.bean.PositionBean;

import java.util.List;

public class GPSSerPresenter extends BasePresenter <GPSSerView>{


    public LatLng gpsToBaidu(LatLng latLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(latLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }


    public PositionBean getMoveGpsData(Location preLocation, Location mlocation) {
        long pastTime = mlocation.getTime() - preLocation.getTime();
        float[] result = new float[3];

        LatLng start = new LatLng(preLocation.getLatitude(), preLocation.getLongitude());
        LatLng end = new LatLng(mlocation.getLatitude(), mlocation.getLongitude());
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude,
                result);
        double distance = result[0];
        double velocity = pastTime / (distance * 60);
        return new PositionBean().setCurrentTime(mlocation.getTime()).setDistance(distance)
                .setLatlng(end).setVelocity(velocity).setPreGapTime(pastTime)
                .setGpsSpeed(mlocation.getSpeed());

    }

    public double getDoubleToDouble(double distance) {
        try {
            String str = String.valueOf(distance);
            str = str.substring(0,
                    str.length() < str.indexOf(".") + 3 ? str.length() : str.indexOf(".") + 3);
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public Criteria getCriteria(){
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        return criteria;
    }

    @SuppressLint("MissingPermission")
    public Location getLatLng(LocationManager locationManager) {
        String provider;

        List<String> providerList = locationManager.getProviders(true);
        boolean isNet = providerList.contains(LocationManager.NETWORK_PROVIDER);
        boolean isGps = providerList.contains(LocationManager.GPS_PROVIDER);
        if (isNet) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (isGps) { // GPS Provider
            provider = LocationManager.GPS_PROVIDER;
        } else {
            provider = null;
        }

        if (provider != null) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                return null;
            } else {
                return location;
            }
        } else {
            return null;
        }
    }

}
