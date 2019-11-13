package com.nightfeed.wendu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.nightfeed.wendu.R;
import com.nightfeed.wendu.utils.ScreenUtils;

/**
 * Created by squirrelRao on 16/6/23.
 */
public class WeekTemperatureView extends View {

    private int[] maxTemperatures = new int[0];
    private int[] minTemperatures = new int[0];

    private int mMax;
    private int mMin;
    //private float mMaxY;
    private float mMinY;
    private float lineHeight;

    private static final float SMOOTHNESS = 0.35f; // the higher the smoother, but don't go over 0.5

    private float density;
    //private TemperatureLineParam tempLineParam;

    public WeekTemperatureView(Context context) {
        super(context);
    }

    public WeekTemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekTemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeekTemperatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override //draw week temperature view
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);

        DisplayMetrics displayMetrics = this.getContext().getApplicationContext().getResources().getDisplayMetrics();
        density = displayMetrics.density;

        if(maxTemperatures.length<=0){
            return;
        }

        float xInterval = this.getWidth()/maxTemperatures.length;
        float x1 = xInterval/2;
        float y0 = this.getHeight(); //- (int) ScreenUtils.getValueByDensity(30, density)

        //float lineY0 = y0 - ScreenUtils.getValueByDensity(8f,density);
        lineHeight = y0 - ScreenUtils.dip2px(getContext(),40);
        //mMaxY=ScreenUtils.dip2px(getContext(),20);
        mMinY=y0-ScreenUtils.dip2px(getContext(),20);


//        if(maxTemperatures.length == 0 || tempLineParam == null || tempLineParam.getTempDistance() == 0){
//            return;
//        }
//        Log.e(Constant.TAG,maxTemperatures.length+"  week day height:"+this.getHeight()+"   lineHeight="+this.getHeight()/2+"  width:"+this.getWidth());
        //draw max temperature line
        this.drawTempLine(maxTemperatures,x1,xInterval,canvas,true);

        //draw min temperature line
        this.drawTempLine(minTemperatures,x1,xInterval,canvas,false);

    }

    /**
     * draw temp line
     * @param temps
     * @param x1
     * @param xInterval
     */
    private void drawTempLine(int[] temps, float x1, float xInterval, Canvas canvas, boolean isHighTemperature){

        if(temps == null || temps.length == 0){
            return;
        }

        float[] pointYs = new float[temps.length];
        float[] pointXs = new float[temps.length];

        TextPaint textPaint = new TextPaint();
        float textSize=ScreenUtils.dip2px(getContext(),12);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        float textmargin=ScreenUtils.dip2px(getContext(),8);
        for(int i = 0; i < temps.length; i++){
            String text=(int)temps[i]+"°";
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            float y;
            if(i==0){
                textPaint.setColor(ContextCompat.getColor(getContext(), R.color.textAA));
            }else if(i==1){
                textPaint.setColor(ContextCompat.getColor(getContext(),R.color.text33));
            }
            float x = x1 + xInterval * i;
//            Log.e(Constant.TAG," circle core x = "+x+"  y ="+y+"  temp="+temps[i]+ "y1="+y1+"  "+lineParam.getMinTemperature());
            y= this.getYByTemperature(temps[i]);

            if(isHighTemperature){

                canvas.drawText(text,x-bounds.width()/2,y-textmargin,textPaint);
            }else {
                canvas.drawText(text,x-bounds.width()/2,y+bounds.height()+textmargin,textPaint);

            }

            pointXs[i] = x;
            pointYs[i] = y;
        }

        //draw lines
        Path path = new Path();
        Paint paint = this.getTempLinePaint(new Paint());

        path.moveTo(pointXs[0],pointYs[0]);

//        for(int j = 0; j <= pointXs.length-2; j=j+2){
//
////            if(j == 0){
////                PathEffect effects = new DashPathEffect(new float[]{2,2,2,2},2);
////                paint.setPathEffect(effects);
////            }else{
////                paint = this.getTempLinePaint(new Paint());
////            }
//
//            if(j==pointXs.length-2){
//                path.quadTo((pointXs[j]+pointXs[j+1])/2,(pointYs[j]+pointYs[j+1])/2,pointXs[j+1],pointYs[j+1]);
//
//            }else{
//                path.quadTo(pointXs[j+1],pointYs[j+1],pointXs[j+2],pointYs[j+2]);
//
//            }
//        }
        float lX = 0, lY = 0;
        for(int j = 1; j <= pointXs.length-1; j++){
            if(j==pointXs.length-1){
                path.cubicTo(pointXs[j-1],pointYs[j-1],(pointXs[j-1]+pointXs[j])/2,(pointYs[j-1]+pointYs[j])/2,pointXs[j],pointYs[j]);
            }else{

            float x0 = pointXs[j-1] + lX;
            float y0 = pointYs[j-1] ;

            // second control point
            lX = (pointXs[j+1]-pointXs[j-1])/2*SMOOTHNESS;		// (lX,lY) is the slope of the reference line
            lY = (pointYs[j+1]-pointYs[j-1])/2*SMOOTHNESS;
            float x2 = pointXs[j]- lX;
            float y2 = pointYs[j];
                path.cubicTo(x0,y0,x2,y2,pointXs[j],pointYs[j]);
            }
        }
        canvas.drawPath(path, paint);

    }


    private float getYByTemperature(double temp){

        float y = (float) (mMinY-(temp-mMin)*lineHeight/(mMax-mMin));

        return y;
    }


    /**
     * @param paint
     * @return
     */
    private Paint getTempLinePaint(Paint paint) {

        paint.setStyle(Paint.Style.STROKE);
        int color = getContext().getResources().getColor(R.color.normally_gray);

//        paint.setColor(Color.argb(Color.alpha(alpha),Color.red(color),Color.green(color),Color.blue(color)));

        paint.setColor(color);

        paint.setStrokeWidth(ScreenUtils.dip2px(getContext(), 1.5f));
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        return paint;
    }

    /**
     * draw curve
     * @param maxTemps
     * @param minTemps
     */
    public void drawCurve(int[] maxTemps, int[] minTemps){

        this.maxTemperatures = maxTemps;
        this.minTemperatures = minTemps;

        if (maxTemps != null && maxTemps.length > 0) {
            mMax = maxTemps[0];
            for (int y : maxTemps) {
                if (y > mMax){
                    mMax = y;
                }
            }
        }
        if (minTemps != null && minTemps.length > 0) {
            mMin = minTemps[0];
            for (int y : minTemps) {
                if (y < mMin){
                    mMin = y;
                }
            }
        }

        this.invalidate();
    }

}

