package com.example.walker.trace;
import android.os.Trace;

import java.util.LinkedList;
import java.util.List;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.example.walker.trace.base.BasePresenter;
import com.example.walker.trace.bean.PositionBean;

public class TraceActPresenter extends BasePresenter<TraceActView> implements TraceActModelListener{
    private TraceActView traceActView;
    public TraceActPresenter(TraceActView traceActView) {
        super();
        this.traceActView = traceActView;
    }

    /*
    Tranform GPS coordinate into Baidu Map coordinate
     */
    public LatLng gpsToBaidu(LatLng data) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordType.GPS);
        converter.coord(data);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    /*
    Define and set the status of the map
     */
    public MapStatusUpdate getMapStatusUpdate(LinkedList<PositionBean> pointsList) {
        if (pointsList != null && pointsList.size() > 0) {
            LatLng lastLatLng = pointsList.get(pointsList.size() - 1).latlng;
            MapStatus mMapStatus = new MapStatus.Builder().target(lastLatLng).build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            return mMapStatusUpdate;
        } else {
            return null;
        }
    }

}
