package com.example.walker.trace;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.baidu.trace.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class LBStraceService extends Service {
    private String Tag="LBStraceService";

    private List<LatLng> points = new ArrayList<LatLng>();


    public LBStraceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText( this , " GPS模块正常 " , Toast.LENGTH_SHORT)
                    .show();
            return ;
        }

        Toast.makeText( this , " 请开启GPS！ " , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);

    }



}
