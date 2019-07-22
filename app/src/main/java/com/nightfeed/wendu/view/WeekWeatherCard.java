package com.nightfeed.wendu.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nightfeed.wendu.R;
import com.nightfeed.wendu.adapter.WeeklyCardAdapter;
import com.nightfeed.wendu.utils.ScreenUtils;

//代号 W
public class WeekWeatherCard extends FrameLayout {

    private int forecastDays;
    private RecyclerView weekly_recyclerview;//周天气列表
    private WeeklyCardAdapter weeklyAdapter;
    private WeekTemperatureView cWeekTemperatureView;
    private int type=-1;//0默认 1没空气 2没温度 3没空气和温度

    public WeekWeatherCard(Context context){
        super(context);
        init(context);
    }

    public WeekWeatherCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.week_weather_card,this);

        cWeekTemperatureView =findViewById(R.id.week_temp_line);


        weekly_recyclerview=(RecyclerView) findViewById(R.id.weekly_recycler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        weekly_recyclerview.setLayoutManager(linearLayoutManager);
        weekly_recyclerview.setNestedScrollingEnabled(false);

    }


    public void setData(){

//        int dailyCount = dailyForecast.getTemperatures().size();

//        int[] dailyMaxTemps = new int[dailyCount];
//        int[] dailyMinTemps  = new int[dailyCount];

        int width=getWidth();
        if(width==0){
            width= ScreenUtils.getScreenWidth(getContext());
        }

        if(weeklyAdapter==null||weekly_recyclerview.getAdapter()==null){

            ViewGroup.LayoutParams viewParams = weekly_recyclerview.getLayoutParams();
            viewParams.width= width/5 * forecastDays;
            weekly_recyclerview.setLayoutParams(viewParams);

//            weekly_recyclerview.setAdapter(weeklyAdapter=new WeeklyCardAdapter(getContext(),dailyForecast,width/5,type));

            //初始化周天气图适配器及各天的点击事件
            weeklyAdapter.setOnItemClickListener(new WeeklyCardAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    int position=weekly_recyclerview.getChildAdapterPosition(view);
//                    setHourlyScrollViewScroll(position);
                }
            });

        }else{
            weeklyAdapter.notifyDataSetChanged();
        }

//        for (int i = 0; i < dailyCount; i++) {
//
//            if(dailyForecast.getTemperatures().get(i) != null){
//                DailyTemperature dailyTemp = dailyForecast.getTemperatures().get(i);
//                dailyMinTemps[i] = WeatherUtil.getTemperature(dailyTemp.getMin());
//                dailyMaxTemps[i] = WeatherUtil.getTemperature(dailyTemp.getMax());
//            }
//        }

        //draw weekly line
//        drawWeeklyCurveView(dailyMaxTemps,dailyMinTemps,width);

    }

    /**
     * draw weekly curve view
     * @param maxTemps
     * @param minTemps
     */
    private void drawWeeklyCurveView(int[] maxTemps, int[] minTemps, int width){

        int lineWidth = width/5 * forecastDays;
        //weekly temperature
        ViewGroup.LayoutParams tempViewParams = (ViewGroup.LayoutParams) cWeekTemperatureView.getLayoutParams();
        tempViewParams.width=lineWidth;
        cWeekTemperatureView.setLayoutParams(tempViewParams);
        cWeekTemperatureView.drawCurve(maxTemps,minTemps);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
