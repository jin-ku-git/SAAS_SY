package com.youwu.shouyinsaas.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.youwu.shouyinsaas.data.DemoRepository;
import com.youwu.shouyinsaas.ui.handover.HandoverViewModel;
import com.youwu.shouyinsaas.ui.handover.SaleGoodsListViewModel;
import com.youwu.shouyinsaas.ui.login.LoginViewModel;
import com.youwu.shouyinsaas.ui.main.MainViewModel;
import com.youwu.shouyinsaas.ui.money.CouponPushViewModel;
import com.youwu.shouyinsaas.ui.money.SalesDocumentViewModel;
import com.youwu.shouyinsaas.ui.money.CashierViewModel;
import com.youwu.shouyinsaas.ui.money.RechargeRecordViewModel;
import com.youwu.shouyinsaas.ui.money.SalesOverviewViewModel;
import com.youwu.shouyinsaas.ui.money.VipRechargeViewModel;
import com.youwu.shouyinsaas.ui.money.WriteOffViewModel;
import com.youwu.shouyinsaas.ui.network.NetWorkViewModel;
import com.youwu.shouyinsaas.ui.order_goods.ConfirmOrderViewModel;
import com.youwu.shouyinsaas.ui.order_goods.NewOrderGoodsViewModel;
import com.youwu.shouyinsaas.ui.order_goods.OrderGoodsViewModel;
import com.youwu.shouyinsaas.ui.order_goods.OrderSettlementViewModel;
import com.youwu.shouyinsaas.ui.order_goods.SellOutViewModel;
import com.youwu.shouyinsaas.ui.order_goods.StocktakingViewModel;
import com.youwu.shouyinsaas.ui.order_goods.TakeOrderViewModel;
import com.youwu.shouyinsaas.ui.order_goods.goods.AddGoodsViewModel;
import com.youwu.shouyinsaas.ui.order_goods.goods.AddMealViewModel;
import com.youwu.shouyinsaas.ui.order_goods.goods.EditSaleGoodsViewModel;
import com.youwu.shouyinsaas.ui.set_up.MoreViewModel;
import com.youwu.shouyinsaas.ui.set_up.SetUpViewModel;
import com.youwu.shouyinsaas.ui.set_up.SettingsPrintViewModel;
import com.youwu.shouyinsaas.ui.set_up.StoreSetUpViewModel;
import com.youwu.shouyinsaas.ui.vip.AddVipViewModel;
import com.youwu.shouyinsaas.ui.vip.SouSuoVipViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by goldze on 2019/3/26.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final DemoRepository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository());
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, DemoRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NetWorkViewModel.class)) {
            return (T) new NetWorkViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {//登录
            return (T) new LoginViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {//首页
            return (T) new MainViewModel(mApplication, mRepository);
        } else if (modelClass.isAssignableFrom(SouSuoVipViewModel.class)) {//搜索VIP
            return (T) new SouSuoVipViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SetUpViewModel.class)) {//通用设置
            return (T) new SetUpViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(RechargeRecordViewModel.class)) {//充值记录
            return (T) new RechargeRecordViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(AddVipViewModel.class)) {//添加会员
            return (T) new AddVipViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(HandoverViewModel.class)) {//交接班
            return (T) new HandoverViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SaleGoodsListViewModel.class)) {//销售商品列表
            return (T) new SaleGoodsListViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(CashierViewModel.class)) {//结算
            return (T) new CashierViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(VipRechargeViewModel.class)) {//会员充值
            return (T) new VipRechargeViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SalesDocumentViewModel.class)) {//销售单据
            return (T) new SalesDocumentViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(OrderGoodsViewModel.class)) {//申请订货
            return (T) new OrderGoodsViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(NewOrderGoodsViewModel.class)) {//新建订货
            return (T) new NewOrderGoodsViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(ConfirmOrderViewModel.class)) {//确认订货
            return (T) new ConfirmOrderViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(OrderSettlementViewModel.class)) {//订货结算
            return (T) new OrderSettlementViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(CouponPushViewModel.class)) {//优惠券推送
            return (T) new CouponPushViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(WriteOffViewModel.class)) {//核销
            return (T) new WriteOffViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(StocktakingViewModel.class)) {//盘点
            return (T) new StocktakingViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SellOutViewModel.class)) {//沽清
            return (T) new SellOutViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SalesOverviewViewModel.class)) {//销售概况
            return (T) new SalesOverviewViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(TakeOrderViewModel.class)) {//取单
            return (T) new TakeOrderViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(SettingsPrintViewModel.class)) {//打印设置
            return (T) new SettingsPrintViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(AddGoodsViewModel.class)) {//添加商品
            return (T) new AddGoodsViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(AddMealViewModel.class)) {//添加套餐
            return (T) new AddMealViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(MoreViewModel.class)) {//更多页面
            return (T) new MoreViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(EditSaleGoodsViewModel.class)) {//销售商品列表
            return (T) new EditSaleGoodsViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(StoreSetUpViewModel.class)) {//门店设置
            return (T) new StoreSetUpViewModel(mApplication, mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
