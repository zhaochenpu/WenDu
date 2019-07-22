package com.nightfeed.wendu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.nightfeed.wendu.utils.ScreenUtils;

public class AQITextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private float mRadius;
    private float strokeWidth;
    private float strokeBoundary;

    public AQITextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        strokeWidth= ScreenUtils.dip2px(context,0.48f);
        mPaint.setStrokeWidth(strokeWidth);
        strokeBoundary=strokeWidth/2;
        mRadius= ScreenUtils.dip2px(context,2.4f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if(width==0||height==0){
            return;
        }
        canvas.drawRoundRect(new RectF(strokeBoundary, strokeBoundary, width-strokeBoundary, height-strokeBoundary), mRadius, mRadius, mPaint);
    }

    public void setData(String text, int color){
        mPaint.setColor(color);
        setTextColor(color);
        setText(text);
    }
}
