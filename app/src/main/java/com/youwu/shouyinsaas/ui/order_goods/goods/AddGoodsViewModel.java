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
 * 2022/07/22
 */

public class AddGoodsViewModel extends BaseViewModel<DemoRepository> {

    public SingleLiveEvent<String> TopName = new SingleLiveEvent<>();
    public ObservableField<Integer> type = new ObservableField<>();

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();


    //商品名称
    public SingleLiveEvent<String> GoodsName = new SingleLiveEvent<>();

    //商品名称
    public SingleLiveEvent<String> GoodsImage= new SingleLiveEvent<>();
    //商品单位
    public SingleLiveEvent<String> GoodsCompany = new SingleLiveEvent<>();
    //商品库存
    public SingleLiveEvent<String> GoodsStock = new SingleLiveEvent<>();
    //商品售价
    public SingleLiveEvent<String> GoodsPrice = new SingleLiveEvent<>();
    //商品进价
    public SingleLiveEvent<String> GoodsPurchasePrice = new SingleLiveEvent<>();
    //商品描述
    public SingleLiveEvent<String> GoodsDescribe = new SingleLiveEvent<>();
    //商品图片
    public SingleLiveEvent<String> main_image = new SingleLiveEvent<>();



    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();

        //商品状态
        public SingleLiveEvent<String> text_state = new SingleLiveEvent<>();
    }
    //生成
    public ObservableField<String> RandomCode = new ObservableField<>();

    //商品分类列表
    public SingleLiveEvent<ArrayList<GroupBean>> GoodsList = new SingleLiveEvent<>();
    //门店群组列表
    public SingleLiveEvent<ArrayList<GroupBean>> groupList = new SingleLiveEvent<>();
    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    public AddGoodsViewModel(@NonNull Application application, DemoRepository repository) {
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

    //保存的点击事件
    public BindingCommand AddOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });


    /**
     * 获取商品分类
     */
    public void goods_category() {
        model.GOODS_CATEGORY()
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

                            GoodsList.setValue(list);
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
     * 添加商品
     * @param category_id   分类id
     * @param group_id      门店群组id
     * @param status        1 开启 2 关闭
     */
    public void AddGoods(int category_id, int group_id, String status) {
        KLog.e("添加商品上传参数：\n" ,
                "name:"+GoodsName.getValue()+",goods_code:"+RandomCode.get()+",main_image:"+",unit:"+GoodsCompany.getValue()+",category_id:"+category_id+",stock:"+GoodsStock.getValue()+",sale_price:"+GoodsPrice.getValue()
        +",cost_price:"+GoodsPurchasePrice.getValue()+",group_id:"+group_id+",details:"+GoodsDescribe.getValue()+",status:"+status);
        //name:测试,goods_code:435211798,main_image:,unit:个,category_id:1,stock:99,sale_price:10,cost_price:8,group_id:12,details:测试11,status:1
        model.ADD_GOODS(GoodsName.getValue(),RandomCode.get(),GoodsImage.getValue(),GoodsCompany.getValue(),category_id+"",GoodsStock.getValue(),GoodsPrice.getValue(),GoodsPurchasePrice.getValue(),group_id+"",GoodsDescribe.getValue(),status)
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
                            IntegerEvent.setValue(2);

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
