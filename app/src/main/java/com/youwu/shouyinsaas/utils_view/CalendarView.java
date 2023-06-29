package com.youwu.shouyinsaas.utils_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youwu.shouyinsaas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.goldze.mvvmhabit.utils.KLog;

/**
 * @Description: 可以选择时间范围的日历控件
 * @Author MengXY
 * @Emil mxy_2012_1@163.com
 * @Date 2019/1/8
 */
public class CalendarView extends LinearLayout implements View.OnClickListener{
    private TextView title;
    private RecyclerView recyclerView;
    private RelativeLayout layout_calendar_gonext;
    private RelativeLayout layout_calendar_goup;
    private LinearLayoutManager linearLayoutManager;

    private Calendar curDate = Calendar.getInstance();
    //从服务器获取的日期
    private Date dateFromServer;

    //外层主recyclerview的adapter
    private MainRvAdapter mainAdapter;
    private List<CalendarCell> months = new ArrayList<>();
    private Context context;

    //相关属性
    private int titleColor;
    private int titleSize;

    private int enableSelectColor;
    private int disableSeletColor;
    private int todayColor;
    private int todayEmptyColor;
    private int todayFillColor;

    /** 初始日期为当前日期前一年*/
    private String time;
    private long timeBefor;
    private long timeNow;

    private List<String> titles = new ArrayList<>();

    //点击的开始时间与结束时间
    private Date sDateTime;
    private Date eDateTime;
    private boolean isSelectingSTime = true;

    private HashMap<Integer, SubRvAdapter> allAdapters = new HashMap<>();


    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int maxSelect = 3;

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyCalendar);
        titleColor = ta.getColor(R.styleable.MyCalendar_titleColor, Color.WHITE);
        titleSize = (int) ta.getDimension(R.styleable.MyCalendar_titleSize, 15);
        enableSelectColor = ta.getColor(R.styleable.MyCalendar_dayInMonthColor, context.getResources().getColor(R.color.main_black));
        disableSeletColor = ta.getColor(R.styleable.MyCalendar_dayOutMonthcolor, context.getResources().getColor(R.color.gray_a9));
        todayColor = ta.getColor(R.styleable.MyCalendar_todayColor, Color.BLUE);
        todayEmptyColor = ta.getColor(R.styleable.MyCalendar_todayEmptycircleColor, Color.CYAN);
        todayFillColor = ta.getColor(R.styleable.MyCalendar_todayFillcircleColor, Color.CYAN);
        ta.recycle();
        this.context = context;
        init(context);
    }
    //该方法用于设置从服务器获取的时间，如果没有从服务器获取的时间将使用手机本地时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-(maxSelect-1));
        time = DateUtils.formatData(calendar.getTime(),Constant.TFORMATE_YMD);
        timeBefor = DateUtils.getDataTime(time);
        String now = DateUtils.formatData(new Date(),Constant.TFORMATE_YMD);
        timeNow = DateUtils.getDataTime(now);
//        LogUtils.e("之前日期："+time+"=="+timeBefor);
//        LogUtils.e("当前日期："+now+"=="+timeNow);

        curDate = DateUtils.strToCalendar(time, Constant.TFORMATE_YMD);
        dateFromServer = DateUtils.strToDate(time, Constant.TFORMATE_YMD);
    }

    private void init(Context context) {
        bindView(context);
        renderCalendar();
    }


    private void bindView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.appoint_calendarview, this, false);
        title = (TextView) view.findViewById(R.id.calendar_title);
        title.setTextColor(titleColor);
        title.setTextSize(titleSize);
        layout_calendar_gonext = view.findViewById(R.id.layout_calendar_gonext);
        layout_calendar_goup = view.findViewById(R.id.layout_calendar_goup);
        layout_calendar_gonext.setOnClickListener(this);
        layout_calendar_goup.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_rv);
        linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        addView(view);
    }

    public void renderCalendar() {
        months.clear();
        initTime();
        for (int i = 0; i < maxSelect; i++) {
            ArrayList<Date> cells = new ArrayList<>();
            if (i != 0) {
                curDate.add(Calendar.MONTH, 1);//后推一个月
            } else {
                curDate.add(Calendar.MONTH, 0);//当前月
            }
            Calendar calendar = (Calendar) curDate.clone();
            //将日历设置到当月第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            //获得当月第一天是星期几，如果是星期一则返回1此时1-1=0证明上个月没有多余天数
            int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //将calendar在1号的基础上向前推prevdays天。
            calendar.add(Calendar.DAY_OF_MONTH, -prevDays);
            //最大行数是6*7也就是，1号正好是星期六时的情况
            int maxCellcount = 6 * 7;
            while (cells.size() < maxCellcount) {
                cells.add(calendar.getTime());
                //日期后移一天
                calendar.add(calendar.DAY_OF_MONTH, 1);
            }
            months.add(new CalendarCell(i, cells));
        }
        for (int i = 0; i < months.size(); i++) {
            //title格式 2018年6月3日
            String title = (months.get(i).getCells().get(20).getYear() + 1900) +
                    "\t-\t" +
                    (months.get(i).getCells().get(20).getMonth() + 1);
            titles.add(title);
        }
        title.setText(titles.get(maxSelect-1));
        //只限定3个月，因此模拟给3个数值即可
        mainAdapter = new MainRvAdapter(R.layout.appoint_calendarview_item, months);
        recyclerView.setAdapter(mainAdapter);
        //recyclerview 的滚动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                title.setText(titles.get(linearLayoutManager.findLastVisibleItemPosition()));
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        recyclerView.scrollToPosition(maxSelect-1);
    }

    @Override
    public void onClick(View v) {
        int index = linearLayoutManager.findLastVisibleItemPosition();
        KLog.e("当前项"+index);
        switch (v.getId()){
            case R.id.layout_calendar_gonext:
                if(index < maxSelect-1){
                    recyclerView.scrollToPosition(index+1);
                    title.setText(titles.get(index+1));
                }
                break;
            case R.id.layout_calendar_goup:
                if(index > 0){
                    recyclerView.scrollToPosition(index-1);
                    title.setText(titles.get(index-1));
                }
                break;
        }

    }

    /**
     * 最外层水平recyclerview的adapter
     */
    private class MainRvAdapter extends BaseQuickAdapter<CalendarCell, BaseViewHolder> {

        public MainRvAdapter(int layoutResId, @Nullable List<CalendarCell> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final CalendarCell item) {
            if (((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).getLayoutManager() == null) {
                //RecyclerView不能都使用同一个LayoutManager
                GridLayoutManager manager = new GridLayoutManager(mContext, 7);
                //recyclerview嵌套高度不固定（wrap_content）时必须setAutoMeasureEnabled(true)，否则测量时控件高度为0
                manager.setAutoMeasureEnabled(true);
                ((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).setLayoutManager(manager);
            }
            SubRvAdapter subRvAdapter = null;
            if (allAdapters.get(helper.getPosition()) == null) {
                subRvAdapter = new SubRvAdapter(R.layout.calendar_text_day, item.getCells());
                allAdapters.put(helper.getPosition(), subRvAdapter);
                ((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).setAdapter(subRvAdapter);
            } else {
                subRvAdapter = allAdapters.get(helper.getPosition());
                ((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).setAdapter(subRvAdapter);
            }
            //item 点击事件响应
            subRvAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Date date = item.getCells().get(position);
                    if(date.getTime() >= timeBefor && date.getTime()<= timeNow ){
                        if (isSelectingSTime) {
                            //正在选择开始时间
                            selectSDate(item.getCells().get(position));
                        } else {
                            //正在选择结束时间
                            selectEDate(item.getCells().get(position));
                        }
                    }
                    //更新所有的adapter，比如今天6月，需要更新6、7、8三个月份不同adapter
                    Iterator iterator = allAdapters.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        ((SubRvAdapter) entry.getValue()).notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void selectSDate(Date date) {
        if (sDateTime != null && eDateTime != null) {
            sDateTime = date;
            notifyDateSelectChanged();
        } else {
            sDateTime = date;
            notifyDateSelectChanged();
        }
        eDateTime = null;
        isSelectingSTime = false;
        /** 当前没有选择结束时间*/
        if(this.calendaSelListener != null){
            calendaSelListener.selectStatus(false);
        }
    }

    public void selectEDate(Date date) {
        if (sDateTime != null) {
            if (date.getTime() >= sDateTime.getTime()) {
                eDateTime = date;
                isSelectingSTime = true;
                notifyDateSelectChanged();
            }else {
                eDateTime = sDateTime;
                sDateTime = date;
                isSelectingSTime = true;
                notifyDateSelectChanged();
            }
            /** 选择完成*/
            if(this.calendaSelListener != null){
                calendaSelListener.selectStatus(true);
            }
        }

    }

    /**
     * 通知开始时间跟结束时间均改变
     */
    public void notifyDateSelectChanged() {
        if (mETimeSelectListener != null && eDateTime != null) {
            mETimeSelectListener.onETimeSelect(eDateTime);
        }
        if (mSTimeSelectListener != null && sDateTime != null) {
            mSTimeSelectListener.onSTimeSelect(sDateTime);
        }
    }


    private class SubRvAdapter extends BaseQuickAdapter<Date, BaseViewHolder> {

        public SubRvAdapter(int layoutResId, @Nullable List<Date> data) {
            super(layoutResId, data);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void convert(BaseViewHolder helper, Date date) {
            helper.setIsRecyclable(false);//不让recyclerview进行复用，复用会出问题
            ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setEmptyColor(todayEmptyColor);
            ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setFillColor(todayFillColor);
            int day = date.getDate();
            //设置文本
            ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setText(String.valueOf(day));
            //设置颜色
            if(date.getTime() >= timeBefor && date.getTime()<= timeNow ){
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setTextColor(enableSelectColor);
            }else {
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setTextColor(disableSeletColor);
            }

            //更改选中文字颜色
            if(sDateTime != null && eDateTime != null){
                if(date.getTime()>sDateTime.getTime() && date.getTime()<eDateTime.getTime()){
                    ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).isSelected(true);
                    helper.getView(R.id.calendar_day_rl).setBackgroundColor(getResources().getColor(R.color.date_duration_bg));
                }
            }
            /****************************/
            if (eDateTime != null && date.getTime() == eDateTime.getTime()) {
                //结束时间
                if(eDateTime.equals(sDateTime)){
                    ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isSameDay();
                }else {
                    ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isETime(true);
                }
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).isETime(true);

            }
            if (sDateTime != null && date.getTime() == sDateTime.getTime()) {
                //开始时间
                if (eDateTime != null) {
                    if(eDateTime.equals(sDateTime)) {
                        ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isSameDay();
                    }else {
                        ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isSTime(true);
                    }
                    ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).isSTime(true);
                } else {
                    ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isDurationSun(true);
                    ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).isSTime(true);
                }
            }

            /*****************************************/
            if(date.getTime() == timeNow){
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setToday(true);
            }
        }
    }

    private class CalendarCell {
        private int position;
        ArrayList<Date> cells;

        public CalendarCell(int position, ArrayList<Date> cells) {
            this.position = position;
            this.cells = cells;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public ArrayList<Date> getCells() {
            return cells;
        }

        public void setCells(ArrayList<Date> cells) {
            this.cells = cells;
        }
    }


    //开始时间的选择监听
    public interface CalendarSTimeSelListener {
        void onSTimeSelect(Date date);
    }

    private CalendarSTimeSelListener mSTimeSelectListener;

    public void setSTimeSelListener(CalendarSTimeSelListener li) {
        mSTimeSelectListener = li;
    }

    //结束时间的监听事件
    public interface CalendatEtimSelListener {
        void onETimeSelect(Date date);
    }



    private CalendaSelListener calendaSelListener;

    /**选择日期完整性*/
    public interface CalendaSelListener{
        void selectStatus(boolean isOk);
    }

    public void setCalendaSelListener(CalendaSelListener calendaSelListener) {
        this.calendaSelListener = calendaSelListener;
    }

    private CalendatEtimSelListener mETimeSelectListener;

    public void setETimeSelListener(CalendatEtimSelListener li) {
        mETimeSelectListener = li;
    }

}