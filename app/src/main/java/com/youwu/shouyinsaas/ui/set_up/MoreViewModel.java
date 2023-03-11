package com.youwu.shouyinsaas.ui.set_up;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.handover.HandoverActivity;
import com.youwu.shouyinsaas.ui.money.SalesDocumentActivity;
import com.youwu.shouyinsaas.ui.money.SalesOverviewActivity;
import com.youwu.shouyinsaas.ui.money.WriteOffActivity;
import com.youwu.shouyinsaas.ui.order_goods.OrderGoodsActivity;
import com.youwu.shouyinsaas.ui.order_goods.SellOutActivity;
import com.youwu.shouyinsaas.ui.order_goods.StocktakingActivity;

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
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 2022/08/01
 */

public class MoreViewModel extends BaseViewModel<DemoRepository> {



    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public MoreViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);

    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });

    //销售单据的点击事件
    public BindingCommand SalesDocumentsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SalesDocumentActivity.class);
        }
    });
    //网络订单的点击事件
    public BindingCommand NetworkOrderOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("敬请期待");
        }
    });
    //核销的点击事件
    public BindingCommand WriteOffOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("敬请期待");
//            startActivity(WriteOffActivity.class);
        }
    });
    //新增商品的点击事件
    public BindingCommand AddGoodsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("敬请期待");
//            startActivity(AddGoodsActivity.class);
        }
    });
    //新增套餐的点击事件
    public BindingCommand AddSetMealOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("敬请期待");
//            startActivity(AddSetMealActivity.class);
        }
    });
    //编辑商品的点击事件
    public BindingCommand EditGoodsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("敬请期待");
//            startActivity(EditSaleGoodsActivity.class);
        }
    });
    //沽清的点击事件
    public BindingCommand SellOutOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SellOutActivity.class);
        }
    });
    //订货的点击事件
    public BindingCommand OrderGoodsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(OrderGoodsActivity.class);
        }
    });
    //盘点的点击事件
    public BindingCommand InventoryOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(StocktakingActivity.class);
        }
    });
    //交接班的点击事件
    public BindingCommand HandoverOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            RxToast.normal("敬请期待");




            startActivity(HandoverActivity.class);
        }
    });
    //通用设置的点击事件
    public BindingCommand SetUpOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SetUpActivity.class);
        }
    });
    //销售概况的点击事件
    public BindingCommand SalesOverviewOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SalesOverviewActivity.class);
        }
    });
    //打印设置的点击事件
    public BindingCommand SetPrintOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(SettingsPrintActivity.class);
        }
    });
    //门店设置的点击事件
    public BindingCommand StoreSetUpOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(StoreSetUpActivity.class);
        }
    });
    //核销的点击事件
    public BindingCommand hexiaoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(WriteOffActivity.class);
        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
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
