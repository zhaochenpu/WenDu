package com.nightfeed.wendu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightfeed.wendu.R;
import com.nightfeed.wendu.view.AQITextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 赵晨璞 on 2016/6/19.
 *
 */

public  class WeeklyCardAdapter extends RecyclerView.Adapter<WeeklyCardAdapter.WeeklyViewHolder> implements View.OnClickListener{

    private Context mContext;
//    private DailyForecast dailyForecast;
    private int width;
    private int grayColor;
    private int type;//0默认 1没空气 2没温度 3没空气和温度

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    public WeeklyCardAdapter(Context context, int width, int type) {
        mContext=context;
//        this.dailyForecast=dailyForecast;
        this.width=width;
        grayColor = Color.parseColor("#999999");
        this.type=type;
    }

    @Override
    public WeeklyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=null;
        view = LayoutInflater.from(mContext).inflate(R.layout.daily_weather_item, parent, false);


        ViewGroup.LayoutParams itemParams = (ViewGroup.LayoutParams) view.getLayoutParams();
        itemParams.width=width;
        view.setLayoutParams(itemParams);
        WeeklyViewHolder holder = new WeeklyViewHolder(view);

//        view.setOnClickListener(this);

        return holder;
    }


    @Override
    public void onBindViewHolder(WeeklyViewHolder holder, int position) {
        String columName = "";

//        DailyTemperature dailyTemp=dailyForecast.getTemperatures().get(position);
//
//        //daily skycon
//        DailySkycon dailySkycon=dailyForecast.getSkycons().get(position);
//        if(dailySkycon != null) {
//
//            holder.day_skycon.setImageResource(SkyconUtil.getWeatherIcon(dailySkycon.getValue()));
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            if(!TextUtils.isEmpty(dailyTemp.getDate())){
//                calendar.setTime(dateFormat.parse(dailyTemp.getDate()));
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if(position == 0){
//            columName = mContext.getString(R.string.yesterday);
//            holder.columNameView.setTextColor(grayColor);
//        }else if(position == 1){
//            columName = CommonUtil.getWeekDayName(null,mContext);
//        }else{
//            columName = CommonUtil.getWeekDayName(calendar,mContext);
//        }
//
//        holder.columNameView.setText(columName);
//        if(CommonUtil.SystemLanguageIsEN(mContext)){
//            holder.dateView.setText(TimeTranslator.toWeekDayDesc(calendar,"."));
//        }else{
//            holder.dateView.setText(TimeTranslator.toWeekDayDesc(calendar,"-")+"");
//        }
//
//        //daily aqi
//        if(type==0||type==2){
//            int aqi= (int) dailyForecast.getAqis().get(position).getAvg();
//            if(aqi==0){
//                holder.daily_aqi.setData("-", grayColor);
//            }else {
//                HazeLevel hazeLevel = WeatherUtil.getHazeLevel(mContext,aqi,true);
//                try {
//                    if(position == 0) { //动态设置skycon画笔颜色//
//                        holder.daily_aqi.setData(aqi +" " +hazeLevel.getDesc(), Color.parseColor(hazeLevel.getAqiColor().replace("#","#99")));
//                    }else {
//                        holder.daily_aqi.setData(aqi +" " +hazeLevel.getDesc(), Color.parseColor(hazeLevel.getAqiColor()));
//                    }
//                }catch (Exception e){
//                    holder.daily_aqi.setData(aqi +" " +hazeLevel.getDesc(),grayColor);
//                }
//            }
//        }
    }

    @Override
    public int getItemCount()
    {
        int count;
//        if(dailyForecast!=null){
//            count = dailyForecast.getTemperatures().size();
//        }else{
            count=0;
//
//        }
        return count;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }


    public class WeeklyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView columNameView,dateView;
        private ImageView day_skycon;
        private AQITextView daily_aqi;

        public WeeklyViewHolder(View view) {

            super(view);

            columNameView =  (TextView)view.findViewById(R.id.wday_name);

            dateView = (TextView)view.findViewById(R.id.wday_day);

            day_skycon=(ImageView)view.findViewById(R.id.day_skycon);

            daily_aqi=view.findViewById(R.id.daily_aqi);
        }
    }

}
