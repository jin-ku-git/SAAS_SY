package com.youwu.shouyinsaas.utils_view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.youwu.shouyinsaas.R;


public class CalendarDayRelativeLayout extends RelativeLayout {
    public CalendarDayRelativeLayout(Context context) {
        this(context, null);
    }

    public CalendarDayRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void isDurationSat(boolean isSaturday) {
        this.setBackground(getResources().getDrawable(R.drawable.appoint_calendar_sat_bg));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void isDurationSun(boolean isSunday) {
        this.setBackground(getResources().getDrawable(R.drawable.appoint_calendar_sun_bg));
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void isETime(boolean etime) {
//        this.setBackgroundResource(getResources().getDrawable(R.drawable.));

        this.setBackground(getResources().getDrawable(R.drawable.appoint_calendar_sat_bg));
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void isSTime(boolean stime) {
//        this.setBackground(getResources().getDrawable(R.mipmap.appoint_calendar_start_bg));
        this.setBackground(getResources().getDrawable(R.drawable.appoint_calendar_sun_bg));
    }

    /**
     * 同一天
     * */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void isSameDay(){
        this.setBackground(getResources().getDrawable(R.drawable.appoint_calendar_same_bg));
    }
}