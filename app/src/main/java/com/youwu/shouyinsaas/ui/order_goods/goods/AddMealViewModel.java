package com.youwu.shouyinsaas.ui.order_goods.goods;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.google.gson.Gson;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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

/**
 * 2022/07/23
 */

public class AddMealViewModel extends BaseViewModel<DemoRepository> {


    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();



    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();

        //商品状态
        public SingleLiveEvent<String> text_state = new SingleLiveEvent<>();
    }

    public ObservableField<String> total_price=new ObservableField<>();
    //生成
    public ObservableField<String> RandomCode = new ObservableField<>();
    //商品名称
    public ObservableField<String> GoodsName = new ObservableField<>();
    //商品单位
    public ObservableField<String> GoodsCompany = new ObservableField<>();
    //商品描述
    public ObservableField<String> GoodsDescribe = new ObservableField<>();


    //门店群组列表
    public SingleLiveEvent<ArrayList<GroupBean>> groupList = new SingleLiveEvent<>();

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    public AddMealViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);

    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });

    //生成的点击事件
    public BindingCommand NewOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Random ran =new Random();

            RandomCode.set(""+ran.nextInt((int)new Date().getTime()));
        }
    });


    //选择套餐的点击事件
    public BindingCommand SetMealOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);

        }
    });
    //保存的点击事件
    public BindingCommand AddOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });

    /**
     * 获取群组
     * @param store_id 门店id
     */
    public void getGoodsGroup(String store_id){

        model.GOODS_GROUP(store_id)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<ArrayList<GroupBean>>>() {
                    @Override
                    public void onNext(BaseBean<ArrayList<GroupBean>> response) {
                        if (response.isOk()){
                            String submitJsonData = new Gson().toJson(response.data);
                            ArrayList<GroupBean> list=  AppApplication.getObjectList(submitJsonData,GroupBean.class);

                            groupList.setValue(list);

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

    /**
     * 添加套餐
     * @param group_id
     * @param status
     * @param goods_list
     */
    public void AddPackage(int group_id, String status ,String goods_list) {
        KLog.e("添加商品上传参数：\n" ,
                "name:"+GoodsName.get()+",goods_code:"+RandomCode.get()+",unit:"+GoodsCompany.get()+",sale_price:"+total_price.get()
                        +",group_id:"+group_id+",desc:"+GoodsDescribe.get()+",status:"+status+",goods_list:"+goods_list);
        //name:测试,goods_code:435211798,main_image:,unit:个,category_id:1,stock:99,sale_price:10,cost_price:8,group_id:12,details:测试11,status:1
        model.ADD_PACKAGE(GoodsName.get(),RandomCode.get(),GoodsCompany.get(),total_price.get(),group_id+"",GoodsDescribe.get(),status,goods_list)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                    }
                })
                .subscribe(new DisposableObserver<BaseBean<ArrayList<GroupBean>>>() {
                    @Override
                    public void onNext(BaseBean<ArrayList<GroupBean>> response) {
                        if (response.isOk()){
                            String JsonData = new Gson().toJson(response.data);
                            KLog.d("返回数据："+JsonData);

                            IntegerEvent.setValue(3);
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
}
