package com.youwu.shouyinsaas.rili;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.databinding.PopupwindowHotelDateBinding;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2018/3/2.
 */

public class DatePopupWindow extends PopupWindow {

    private Activity activity;
    private final PopupwindowHotelDateBinding mBinding;

    private Date mSetDate;
    private int startGroupPosition=-1;
    private int endGroupPosition=-1;
    private int startchildPosition=-1;
    private int endchildPosition=-1;
    //当天在列表中的子索引
    private int c_stratChildPosition=-1;
    private DateAdapter mDateAdapter;
    private List<String> mSelectList;
    private List<DateInfo> mList;
    private String currentDate;
    private DateOnClickListener mOnClickListener;

    int time_type = 0;//0是预约开始具体时间，1是预约结束具体时间
    private TimePickerView  pvTime;//时间选择器

    /**
     *
     * @param context
     * @param date 日期 格式2019-03-02
     */
    public DatePopupWindow(final Activity context, String date){
        this.activity=context;
        this.currentDate=date;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.popupwindow_hotel_date,null,false);
        int screenHeight= (int) (AppUtil.getDisplayMetrics(context).displayHeight/1.5);
        this.setContentView(mBinding.getRoot());
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.dialogWindowAnim);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(new ShareDismissListener());
        backgroundAlpha(activity,1f);//调整背景透明度

        mBinding.timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_type = 0;
                initTimes();
            }
        });
        mBinding.timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_type = 1;
                initTimes();
            }
        });

        mBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    mOnClickListener.getDate(mList,startGroupPosition,startchildPosition,endGroupPosition,endchildPosition,mBinding.timeStart.getText().toString(),mBinding.timeEnd.getText().toString());
                }

                DatePopupWindow.this.dismiss();
            }
        });

        initView();

        initTimePicker();

    }

    //选择日期
    private void initTimes() {
        pvTime.show(); //弹出自定义时间选择器
    }
    private String getTimes(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:00");
        return format.format(date);
    }
    private void initTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */

        Time t = new Time();  // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow();  // 取得系统时间。
        int year = t.year;//年
        int month = t.month;//月
        int monthDay = t.monthDay;//日


        Log.i("0", "Calendar获取当前日期" + year + "年" + month + "月" + monthDay + "日");
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(year, month-2, monthDay);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year, month, monthDay);
        //时间选择器 ，自定义布局
        pvTime = new TimePickerBuilder(activity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (time_type == 0) {
                    mBinding.timeStart.setText(getTimes(date));
                } else if (time_type == 1) {
                    mBinding.timeEnd.setText(getTimes(date));
                }


            }
        })
                .isDialog(true)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })

                .setContentTextSize(18)
                .setType(new boolean[]{false, false, false, true, false, false})//true显示 false不显示
                .setLabel("年", "月", "日", ":00", "分", "秒")
                .setLineSpacingMultiplier(1.8f)
//                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setDividerColor(0xFF24AD9D)
                .setDividerColor(Color.WHITE)
                .setItemVisibleCount(4)
                .build();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        params.leftMargin = 0;
        params.rightMargin = 0;
        ViewGroup contentContainer = pvTime.getDialogContainerLayout();
        contentContainer.setLayoutParams(params);
        pvTime.getDialog().getWindow().setGravity(Gravity.BOTTOM);//可以改成Bottom
        pvTime.getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    public void initView() {


        mList=new ArrayList<>();
        //关闭动画
        ((DefaultItemAnimator) mBinding.rv.getItemAnimator()).setSupportsChangeAnimations(false);
        //当前日期转date
        SimpleDateFormat ymd_sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(currentDate==null){
                new Throwable("please set one start time");
                return;
            }
            mSetDate = ymd_sdf.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //修改日历对象的日期
        Calendar c = Calendar.getInstance();
        c.setTime(mSetDate);
        //获取月份 月份是从0开始需要+1
        int firstM= c.get(Calendar.MONTH)+1;
        //日期
        int days=c.get(Calendar.DATE);
        //周几
        int week=c.get(Calendar.DAY_OF_WEEK);
        //获取当前这个月最大天数
        int maxDys=c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startDate=0;
        //日   一   二   三   四   五   六
        //-    -   当前
        //需要求上周末的日期(周末到周一的日期)填补
        if("1".equals(week+"")){
            //今天是否周末
            startDate=days;
        }else {
            //获取上周末
            startDate= Integer.parseInt(CalendarUtil.getPreviousWeekSundayByDate(ymd_sdf.format(mSetDate)).split("-")[2]);

        }
        DateInfo info = new DateInfo();
        List<DayInfo> dayList=new ArrayList<>();
        info.setDate(c.get(Calendar.YEAR)+ "年" + firstM + "月");
        //根据周末日期开始计算到结尾日期的天数
        //当小于当前日期时，是不可选，setEnable(false)

        //如果上周末是大于当前的，说明上周末是上个月的
        //如何判断呢，1，当前日期小于周末则是上个月 2，其他情况是当前日期肯定会大于周末
        if(days<startDate){
            //计算上个月在本周日历的日期
            //获取上月最后一天的日期
            String previousMonthLastDate= CalendarUtil.getPreviousMonthEndByDate(ymd_sdf.format(mSetDate));
            int previousMonthLastDay= Integer.parseInt(previousMonthLastDate.split("-")[2]);
            LogUtil.debugE("previousMonthLastDay",previousMonthLastDay+"");
            //从上个月周日日期算起，到上个月最后一天的日期
            for (int i=startDate;i<=previousMonthLastDay;i++){
                DayInfo dayInfo=new DayInfo() ;
                dayInfo.setName(i+"");
                dayInfo.setEnable(false);
                dayInfo.setDate(previousMonthLastDate.split("-")[0]+"-"+previousMonthLastDate.split("-")[1]);
                dayList.add(dayInfo);
            }
            //计算完后，回归为1号，计算当前这个月日期
            startDate=1;
        }
        //计算当前月的天数
        for (int i=startDate ; i<=maxDys;i++){
            DayInfo dayInfo=new DayInfo() ;
            dayInfo.setName(i+"");
            dayInfo.setDate(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+i);
            int c_year= Integer.parseInt(currentDate.split("-")[0]);
            int c_month= Integer.parseInt(currentDate.split("-")[1]);
            int c_day= Integer.parseInt(currentDate.split("-")[2]);
            if(c_year==c.get(Calendar.YEAR) && c_month==(c.get(Calendar.MONTH)+1) && c_day==i){
                dayInfo.setName("今天");
                c_stratChildPosition=dayList.size();
            }
            if(c_year==c.get(Calendar.YEAR) && c_month==(c.get(Calendar.MONTH)+1) && c_day+1==i){
                dayInfo.setName("明天");
            }
            if(i<days){
                dayInfo.setEnable(false);
            }else {
                dayInfo.setEnable(true);
            }
            dayList.add(dayInfo);
        }
        info.setList(dayList);
        mList.add(info);
        //获取下12个月的数据
        for (int i=1 ;i<3;i++){
            //当前月份循环加1
            c.add(Calendar.MONTH,-1);
            DateInfo nextInfo = new DateInfo();
            List<DayInfo> nextdayList=new ArrayList<>();
            int maxDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
            nextInfo.setDate(c.get(Calendar.YEAR)+ "年" + (c.get(Calendar.MONTH)+1) + "月");
            //周几
            int weeks= CalendarUtil.getWeekNoFormat(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-01")-1;
            LogUtil.debugE("---",weeks+"");
            //根据该月的第一天，星期几，填充上个月的空白日期
            for (int t=0;t<weeks;t++){
                DayInfo dayInfo=new DayInfo() ;
                dayInfo.setName("");
                dayInfo.setEnable(false);
                dayInfo.setDate("");
                nextdayList.add(dayInfo);
            }
            //该月的所有日期
            for (int j=0;j<maxDays;j++){
                DayInfo dayInfo=new DayInfo() ;
                dayInfo.setName((j+1)+"");
                dayInfo.setEnable(true);
                dayInfo.setDate(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+(j+1));
                nextdayList.add(dayInfo);
            }
            nextInfo.setList(nextdayList);
            mList.add(nextInfo);
            LogUtil.debugE("--",c.getActualMaximum(Calendar.DAY_OF_MONTH)+"-"+c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH));
        }
        //倒叙
        Collections.reverse(mList);

        LogUtil.debugE("test","firstM="+firstM+"--days="+days+"--maxDays="+maxDys+"--WEEK="+week+"---本周日日期："+ CalendarUtil.getCurrentSunday());

        LinearLayoutManager manager=new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.rv.setLayoutManager(manager);
        mDateAdapter = new DateAdapter(mList);
        mBinding.rv.setAdapter(mDateAdapter);
        ((LinearLayoutManager) mBinding.rv.getLayoutManager()).scrollToPositionWithOffset(mList.size()-1, 0);

        initDecoration();

    }
    //设置日历标明今天明天
    public void setDefaultSelect(){
        if(c_stratChildPosition==-1)return;
        String date=mList.get(0).getList().get(c_stratChildPosition).getDate();
        LogUtil.debugE("zuixina---",date);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date curDate=null;
        try {
             curDate=sdf.parse(FormatDate(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(curDate==null)return;
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DATE,1);

        int year= Integer.parseInt(date.split("-")[0]);
        int month= Integer.parseInt(date.split("-")[1]);
        if(year==calendar.get(Calendar.YEAR) && month==calendar.get(Calendar.MONTH)+1 && c_stratChildPosition<mList.get(0).getList().size()-1){
            setInit(0,c_stratChildPosition+1,0,c_stratChildPosition+2,mBinding.timeStart.getText().toString(),mBinding.timeEnd.getText().toString());
        }else {
            for (int i=0;i<mList.get(1).getList().size();i++){
                if(!TextUtils.isEmpty(mList.get(1).getList().get(i).getDate())){
                    setInit(0,c_stratChildPosition,1,i,mBinding.timeStart.getText().toString(),mBinding.timeEnd.getText().toString());
                    break;

                }
            }
        }
        LogUtil.debugE("zusixin---",calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE));

//
    }

    //2018-03-23
    private String FormatDate(String date){
        if(TextUtils.isEmpty(date))return "";
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(date.split("-")[0]);
        stringBuffer.append("-");
        stringBuffer.append(date.split("-")[1].length()<2?"0"+date.split("-")[1] : date.split("-")[1]);
        stringBuffer.append("-");
        stringBuffer.append(date.split("-")[2].length()<2?"0"+date.split("-")[2] : date.split("-")[2]);
        return stringBuffer.toString();
    }

    public void setDateOnClickListener(DateOnClickListener mlListener){
        mOnClickListener=mlListener;
    }

      public void setInit(int startGroup,int startchild,int endGroup,int endchild ,String time_start,String time_end){
          startGroupPosition=startGroup;
          startchildPosition=startchild;
          mList.get(startGroup).getList().get(startchild).setStatus(1);
          endGroupPosition=endGroup;
          endchildPosition=endchild;
          mList.get(endGroup).getList().get(endchild).setStatus(2);
          mDateAdapter.notifyDataSetChanged();
          getoffsetDate(mList.get(startGroupPosition).getList().get(startchildPosition).getDate(),
                  mList.get(endGroupPosition).getList().get(endchildPosition).getDate(),true);
          mBinding.rv.scrollToPosition(startGroup);
          mBinding.timeStart.setText(time_start);
          mBinding.timeEnd.setText(time_end);
      }

    /**
     * 添加悬浮布局
     */
    private void initDecoration() {
        SectionDecoration decoration = SectionDecoration.Builder
                .init(activity,new PowerGroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //获取组名，用于判断是否是同一组
                        if (mList.size() > position) {
                            return mList.get(position).getDate();
                        }
                        return null;
                    }
                    @Override
                    public View getGroupView(int position) {
                        //获取自定定义的组View
                        if (mList.size() > position) {
                            View view = activity.getLayoutInflater().inflate(R.layout.item_select_date_group, null, false);
                            ((TextView) view.findViewById(R.id.tv_date)).setText(mList.get(position).getDate());
                            return view;
                        } else {
                            return null;
                        }
                    }
                })
                //设置高度
                .setGroupHeight(dip2px(activity, 40))
                .build();
        mBinding.rv.addItemDecoration(decoration);
    }
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    private class DateAdapter extends BaseQuickAdapter<DateInfo, BaseViewHolder> {

        public DateAdapter(@Nullable List<DateInfo> data) {
            super(R.layout.adapter_hotel_select_date,data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final DateInfo item) {
            RecyclerView rv= helper.getView(R.id.rv_date);
            GridLayoutManager manager =new GridLayoutManager(activity,7);
            rv.setLayoutManager(manager);
            final TempAdapter groupAdapter=new TempAdapter(item.getList());
            rv.setAdapter(groupAdapter);
            groupAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    showToast(item.getList().get(position).getDate());
                    if(!item.getList().get(position).isEnable())return;
                    if(TextUtils.isEmpty(item.getList().get(position).getName()))return;
                    if(TextUtils.isEmpty(item.getList().get(position).getDate()))return;
                    int status=item.getList().get(position).getStatus();
                    if(status==0 && startGroupPosition==-1 && startchildPosition==-1 && item.getList().get(position).isEnable()){
                        //入住
                        item.getList().get(position).setStatus(1);
                        adapter.notifyItemChanged(position);
                        startGroupPosition=helper.getAdapterPosition();
                        startchildPosition=position;
                        mBinding.tvTime.setText("请选择结束时间");
                        return;
                    }
                    //离开
                    if(status==0 && endGroupPosition==-1 && endchildPosition==-1){
                        LogUtil.debugE("--",startGroupPosition+"-"+startchildPosition);
                        int offset= Integer.parseInt(CalendarUtil.getTwoDay(item.getList().get(position).getDate()
                                ,mList.get(startGroupPosition).getList().get(startchildPosition).getDate()));
                        //判断该离开日期是否比入住时间还小，是则重新设置入住时间。
                        if(offset<0){
                            //刷新上一个入住日期
                            mList.get(startGroupPosition).getList().get(startchildPosition).setStatus(0);
                            mDateAdapter.notifyItemChanged(startGroupPosition);
                            //设置新的入住日期
                            item.getList().get(position).setStatus(1);
                            startGroupPosition=helper.getAdapterPosition();
                            startchildPosition=position;
                            adapter.notifyItemChanged(position);
                            mBinding.tvTime.setText("请选择结束时间");
                            return;
                        }
                        //离开
                        item.getList().get(position).setStatus(2);
                        adapter.notifyItemChanged(position);
                        endGroupPosition=helper.getAdapterPosition();
                        endchildPosition=position;
                        getoffsetDate(mList.get(startGroupPosition).getList().get(startchildPosition).getDate(),
                                mList.get(endGroupPosition).getList().get(endchildPosition).getDate(),true);
                        return;
                    }
                    //重置入住和离开时间，设置入住时间
                    if(status==0 && endGroupPosition!=-1 && endchildPosition!=-1 && startchildPosition!=-1 && startGroupPosition!=-1){
                        //重置入住和离开
                        mList.get(startGroupPosition).getList().get(startchildPosition).setStatus(0);
                        mList.get(endGroupPosition).getList().get(endchildPosition).setStatus(0);
                        mDateAdapter.notifyItemChanged(startGroupPosition);
                        mDateAdapter.notifyItemChanged(endGroupPosition);
                        //重置选择间区的状态
                        getoffsetDate(mList.get(startGroupPosition).getList().get(startchildPosition).getDate(),
                                mList.get(endGroupPosition).getList().get(endchildPosition).getDate(),false);
                        //设置入住
                        item.getList().get(position).setStatus(1);
                        adapter.notifyItemChanged(position);
                        startGroupPosition=helper.getAdapterPosition();
                        startchildPosition=position;
                        endGroupPosition=-1;
                        endchildPosition=-1;
                        mBinding.tvTime.setText("请选择结束时间");
                        return;
                    }
                }
            });

        }
    }


    /**
     *设置起始时间和结束时间的选中标识，或者设置不选中
     * @param startDate
     * @param endDate
     * @param status 选中设置为true 设置不选中false
     */
    private void getoffsetDate(String startDate, String endDate, boolean status){
        mSelectList = new ArrayList<>();
        int daysOffset= Integer.parseInt(CalendarUtil.getTwoDay(endDate,startDate));
        if(daysOffset<0)return ;
        mBinding.tvTime.setText("共"+(daysOffset)+"天");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        DayInfo info=mList.get(startGroupPosition).getList().get(startchildPosition);
        try {
            c.setTime(sdf.parse(info.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //根据2个时间的相差天数去循环
        for (int i=0;i<daysOffset;i++){
            //下一天（目标天）
            c.add(Calendar.DATE,1);
            //改天的日期（目标天）
            String d=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE);
            //循环group列表
            for (int j=0;j<mList.size();j++){
                //获取该月的随机一个dayInfo
                DayInfo dayInfo= mList.get(j).getList().get(mList.get(j).getList().size()-1);
                boolean isCheck=false;
                LogUtil.debugE("--",dayInfo.getDate()+"--"+dayInfo.getDate().split("-")[0]+"-"+(c.get(Calendar.YEAR)+"--"
                        +dayInfo.getDate().split("-")[1]+"--"+(c.get(Calendar.MONTH)+1)));
                //判断该天是否和目标天是否是同一个月
                if(!TextUtils.isEmpty(dayInfo.getDate()) && Integer.valueOf(dayInfo.getDate().split("-")[0])==(c.get(Calendar.YEAR))
                        && Integer.valueOf(dayInfo.getDate().split("-")[1])==((c.get(Calendar.MONTH)+1))) {
                    //是同一个月，则循环该月多有天数
                    for (int t = 0; t < mList.get(j).getList().size(); t++) {
                        //找到该月的日期与目标日期相同，存在，设置选择标记
                        if (mList.get(j).getList().get(t).getDate().equals(d)) {
                            mList.get(j).getList().get(t).setSelect(status);
                            isCheck=true;
                            break;
                        }
                    }
                }
                if(isCheck){
                    mDateAdapter.notifyItemChanged(j);
                    break;
                }
            }
        }


    }

    private class TempAdapter extends BaseQuickAdapter<DayInfo,BaseViewHolder>{
        public TempAdapter(@Nullable List<DayInfo> data) {
            super(R.layout.adapter_hotel_select_date_child,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DayInfo item) {
            helper.setText(R.id.tv_date,item.getName());
            LogUtil.debugE("---",item.isEnable()+"");

            //默认
            if(item.getStatus()==0){
                if(item.isSelect()){
                    //选中
                    helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_status).setVisibility(View.GONE);
                    ((TextView)helper.getView(R.id.tv_date)).setTextColor(activity.getResources().getColor(R.color.white));
//                    (helper.getView(R.id.ll_bg)).setBackgroundColor(activity.getResources().getColor(R.color.title_bg));
//                    (helper.getView(R.id.ll_bg)).setBackgroundResource(R.drawable.yuan_main);
                    (helper.getView(R.id.ll_bg)).setBackgroundResource(R.drawable.example_4_continuous_selected_bg_middle);
                }else {
                    //没选中状态
                    helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
                    helper.getView(R.id.tv_status).setVisibility(View.GONE);
                    ((TextView)helper.getView(R.id.tv_date)).setTextColor(activity.getResources().getColor(R.color.black));
                    (helper.getView(R.id.ll_bg)).setBackgroundColor(activity.getResources().getColor(R.color.white));
                }

            }else if(item.getStatus()==1){
                //入住
                helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_status,"开始");
                helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                ((TextView)helper.getView(R.id.tv_status)).setTextColor(activity.getResources().getColor(R.color.white));
                ((TextView)helper.getView(R.id.tv_date)).setTextColor(activity.getResources().getColor(R.color.white));
//                (helper.getView(R.id.ll_bg)).setBackgroundColor(activity.getResources().getColor(R.color.title_bg));
//                (helper.getView(R.id.ll_bg)).setBackgroundResource(R.drawable.yuan_main);
                (helper.getView(R.id.ll_bg)).setBackgroundResource(R.drawable.example_4_continuous_selected_bg_start);
            }else if(item.getStatus()==2){
                //离店
                helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_status,"结束");
                helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                ((TextView)helper.getView(R.id.tv_status)).setTextColor(activity.getResources().getColor(R.color.white));
                ((TextView)helper.getView(R.id.tv_date)).setTextColor(activity.getResources().getColor(R.color.white));
//                (helper.getView(R.id.ll_bg)).setBackgroundColor(activity.getResources().getColor(R.color.title_bg));
                (helper.getView(R.id.ll_bg)).setBackgroundResource(R.drawable.example_4_continuous_selected_bg_end);
            }
            //设置当前日期前的样式，没选中，并状态为0情况下
            if(!item.isSelect() && item.getStatus()==0) {
                if (!item.isEnable()) {
                    //无效
                    TextView textView = helper.getView(R.id.tv_date);
                    textView.setTextColor(activity.getResources().getColor(R.color.main_white_f8));
                } else {
                    TextView textView = helper.getView(R.id.tv_date);
                    textView.setTextColor(activity.getResources().getColor(R.color.black));
                }
            }
        }
    }

    public DatePopupWindow create(View view){
        this.showAtLocation(view, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
        return this;
    }
    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    class ShareDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(activity,1f);
        }
    }

    public interface DateOnClickListener{
        public void getDate(List<DateInfo> list, int startGroupPosition, int startChildPosition, int endGroupPosition, int endChildPosition,String timeStart,String timeEnd);
    }


}
