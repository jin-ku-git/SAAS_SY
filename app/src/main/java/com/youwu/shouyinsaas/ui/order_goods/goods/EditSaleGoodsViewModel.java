package com.youwu.shouyinsaas.ui.order_goods.goods;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.http.BaseBean;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 2022/08/01
 */

public class EditSaleGoodsViewModel extends BaseViewModel<DemoRepository> {


    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }
    //商品分类列表
    public SingleLiveEvent<ArrayList<GroupBean>> GoodsList = new SingleLiveEvent<>();

    public EditSaleGoodsViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);

    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
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



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
