package com.youwu.shouyinsaas.ui.money;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.money.bean.SalesOverviewBean;
import com.youwu.shouyinsaas.ui.money.bean.XiaoShouGaiKuangBean;
import com.youwu.shouyinsaas.utils_view.RxToast;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.youwu.shouyinsaas.app.AppApplication.toPrettyFormat;

/**
 * 2022/06/01
 */

public class SalesOverviewViewModel extends BaseViewModel<DemoRepository> {


    public ObservableField<SalesOverviewBean> entity = new ObservableField<>();
    //状态的绑定
    public ObservableField<Integer> state = new ObservableField<>();

    //早餐的绑定
    public ObservableField<String> morning_start_time = new ObservableField<>();
    public ObservableField<String> morning_end_time = new ObservableField<>();

    //午餐的绑定
    public ObservableField<String> afternoon_start_time = new ObservableField<>();
    public ObservableField<String> afternoon_end_time = new ObservableField<>();

    //晚餐的绑定
    public ObservableField<String> evening_start_time = new ObservableField<>();
    public ObservableField<String> evening_end_time = new ObservableField<>();

    //状态的绑定
    public ObservableField<String> StartTime = new ObservableField<>();
    public ObservableField<String> EndTime = new ObservableField<>();
    public ObservableField<String> fatalism = new ObservableField<>();


    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //饼状图的绑定
    public SingleLiveEvent<SalesOverviewBean> entity_List = new SingleLiveEvent<>();


    public ObservableField<XiaoShouGaiKuangBean> BeanObservableField = new ObservableField<>();

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();



    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public SalesOverviewViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);

    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });
    //查看明细的点击事件
    public BindingCommand ViewDetailsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //跳转到销售单据
            startActivity(SalesDocumentActivity.class);
        }
    });

    //今日的点击事件
    public BindingCommand TodayOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            state.set(1);
            IntegerEvent.setValue(7);
        }
    });
    //本周的点击事件
    public BindingCommand ThisWeekOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            state.set(2);
            IntegerEvent.setValue(8);
        }
    });
    //本月的点击事件
    public BindingCommand ThisMonthOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            state.set(3);
            IntegerEvent.setValue(9);
        }
    });
    //自定义的点击事件
    public BindingCommand ThisQuarterOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            IntegerEvent.setValue(10);
        }
    });

    //搜索的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(11);
        }
    });

    //早餐开始时间的点击事件
    public BindingCommand MorningStartTimeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //早餐结束时间的点击事件
    public BindingCommand MorningEndTimeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //午餐开始时间的点击事件
    public BindingCommand AfternoonStartTimeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });
    //午餐结束时间的点击事件
    public BindingCommand AfternoonEndTimeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });
    //晚餐开始时间的点击事件
    public BindingCommand EveningStartTimeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);
        }
    });
    //晚餐结束时间的点击事件
    public BindingCommand EveningEndTimeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(6);
        }
    });

    /**
     * 获取销售概况
     * @param type  1.今日 2.本周 3.本月 4.本季度
     */
    public void sales_situation(String type) {
        String store_id= AppApplication.spUtils.getString("StoreId");
        model.SALES_SITUATION(type,store_id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {
                        if (response.isOk()){
                            String JsonData = new Gson().toJson(response.data);

                            SalesOverviewBean salesOverviewBean = JSON.parseObject(toPrettyFormat(JsonData), SalesOverviewBean.class);
                            entity.set(salesOverviewBean);
                            entity_List.setValue(salesOverviewBean);
                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 销售概况
     *
     * @param start                     开始日期
     * @param end                       结束日期
     */
    public void new_sales_info(String start,String end) {
        String store_id= AppApplication.spUtils.getString("StoreId");
        model.NEW_SALES_INFO(start,end,morning_start_time.get(),morning_end_time.get(),afternoon_start_time.get(),afternoon_end_time.get(),evening_start_time.get(),evening_end_time.get())
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {
                        if (response.isOk()){

                                String JsonData = new Gson().toJson(response.data);
                                KLog.d("JsonData:"+JsonData.length());
                                KLog.d("销售概况返回信息："+JsonData);
                                if (JsonData.length()!=2){
                                XiaoShouGaiKuangBean xiaoShouGaiKuangBean=JSON.parseObject(toPrettyFormat(JsonData), XiaoShouGaiKuangBean.class);

                                BeanObservableField.set(xiaoShouGaiKuangBean);

                                }



                        }else {
                            if (response.code==-1){
                                XiaoShouGaiKuangBean data=new XiaoShouGaiKuangBean();
                                BeanObservableField.set(data);
                            }
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }


    /**
     * 核销
     * @param order_sn
     * @param activity
     */
    public void close_order(String order_sn, final Activity activity) {

        model.CLOSE_ORDER(order_sn)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<Object>>() {
                    @Override
                    public void onNext(BaseBean<Object> response) {

                        if (response.isOk()){
                            RxToast.showTipToast(activity,"核销成功！");

                        }else {
                            RxToast.normal(response.getMessage());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        //关闭对话框
                        dismissDialog();
                        if (throwable instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) throwable).message);
                        }
                    }
                    @Override
                    public void onComplete() {
                        //关闭对话框
                        dismissDialog();
                    }
                });
    }
}
