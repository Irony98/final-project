package com.example.walker.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.example.walker.R;
import  java.text.DecimalFormat;

public class KiloView extends View {

    private float borderWidth = dipToPx(1);

    private float numberTextSize = 0;

    private String stepNumber = "0";

    private String kilo = "0.0";

    private String calory="0";

    public KiloView(Context context) {
        super(context);


    }

    public KiloView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KiloView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = (getWidth()) / 2;

        RectF rectF = new RectF(0 + borderWidth, borderWidth, 2 * centerX - borderWidth, 2 * centerX - borderWidth);


        drawSubline(canvas);
        drawTextKiloString(canvas, centerX);
        drawTextCalString(canvas,centerX);
    }

    private void drawSubline(Canvas canvas) {
        Paint paint = new Paint();

        paint.setColor(getResources().getColor(R.color.yellow));

        paint.setStrokeJoin(Paint.Join.ROUND);

        paint.setStrokeCap(Paint.Cap.SQUARE);

        paint.setStyle(Paint.Style.STROKE);

        paint.setAntiAlias(true);

        paint.setStrokeWidth(borderWidth);

        canvas.drawLine(getWidth()/32, getHeight()/4, getWidth()/32*31, getHeight()/4, paint);
        canvas.drawLine(getWidth()/32, getHeight()/5*3, getWidth()/32*31, getHeight()/5*3, paint);
    }




    private void drawTextKiloString(Canvas canvas, float centerX) {

        /******draw lable of kilo******/

        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dipToPx(16));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(getResources().getColor(R.color.black));
        String stepString = "You have been walking:";
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, getWidth()/16+bounds.width()/2, getHeight() / 8+ bounds.height() + getFontHeight(numberTextSize), vTextPaint);

        /******draw number of kilo******/
        String curren_kilo=kilo+"  KM";
        vTextPaint.getTextBounds(curren_kilo,0,curren_kilo.length(),bounds);
        canvas.drawText(curren_kilo, getWidth()/16*15-bounds.width()/2, getHeight() / 8+ bounds.height() + getFontHeight(numberTextSize), vTextPaint);
    }

    private void drawTextCalString(Canvas canvas, float centerX) {

        /******draw lable of cal******/

        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dipToPx(16));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(getResources().getColor(R.color.black));
        String stepString = "Burn Calorie:";
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, getWidth()/16+bounds.width()/2, getHeight() / 5*3 - bounds.height() - getFontHeight(numberTextSize), vTextPaint);

        /******draw number of cal******/
        String curren_cal=calory+"  kcal";
        vTextPaint.getTextBounds(curren_cal,0,curren_cal.length(),bounds);
        canvas.drawText(curren_cal, getWidth()/16*15-bounds.width()/2, getHeight() / 5*3 - bounds.height() - getFontHeight(numberTextSize), vTextPaint);
    }


    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        paint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        return bounds_Number.height();
    }

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public void setCurrentCount(int currentCount){
        float show_kilo=currentCount*0.0008176f;
        DecimalFormat   f  =   new DecimalFormat("##0.0");
        kilo=f.format(show_kilo);
        calory=String.valueOf((int)(currentCount*0.05628f));
        invalidate();
    }


}
