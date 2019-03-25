package com.example.walker.trace.adapter;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.example.walker.trace.bean.PositionBean;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LatAdapter extends BaseAdapter {

    private Context context;
    private List<PositionBean> mPointList;

    public LatAdapter(Context context, List<PositionBean> mPointList) {
        super();
        this.context = context;
        this.mPointList = mPointList;

    }

    @Override
    public int getCount() {
        return mPointList == null ? 0 : mPointList.size();
    }

    @Override
    public PositionBean getItem(int position) {
        return mPointList == null ? null : mPointList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(context);
        }
        PositionBean item = getItem(position);

        ((TextView) convertView).setTextSize(10f);

        ((TextView) convertView).setText("Distance :" + getDoubleToStr(item.distance) + ",Speed :"
                + getDoubleToStr(item.velocity) + ",Time : " + item.preGapTime + "ms" + ",GpsSpeed : "
                + getDoubleToStr(item.gpsSpeed));
        return convertView;
    }

    private String getDoubleToStr(double distance) {
        String str = String.valueOf(distance);
        return str.substring(0,
                str.length() < str.indexOf(".") + 3 ? str.length() : str.indexOf(".") + 3);
    }

    public void update(List<PositionBean> mPointList) {
        this.mPointList = mPointList;
        notifyDataSetChanged();
    }
}

