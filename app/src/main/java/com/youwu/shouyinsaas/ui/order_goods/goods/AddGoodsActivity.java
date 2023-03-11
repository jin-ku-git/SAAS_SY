package com.youwu.shouyinsaas.ui.order_goods.goods;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lxj.xpopup.XPopup;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;

import com.youwu.shouyinsaas.databinding.ActivityAddGoodsBinding;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.ui.main.popup.TipsDialog;
import com.youwu.shouyinsaas.ui.set_up.bean.SystemMessageEvent;
import com.youwu.shouyinsaas.utils_view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;


/**
 * 添加商品页面
 * 2022/07/22
 */
public class AddGoodsActivity extends BaseActivity<ActivityAddGoodsBinding, AddGoodsViewModel> {



    ArrayList<GroupBean> goodsList=new ArrayList<>();
    ArrayList<GroupBean> groupList=new ArrayList<>();

    int goods_position=0;
    int store_position=0;

    private int type=0;
    private CommunityBean data;

    @Override
    public void initParam() {
        super.initParam();
        type=getIntent().getIntExtra("type",0);
        data= (CommunityBean) getIntent().getSerializableExtra("CommunityBean");
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_add_goods;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AddGoodsViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(AddGoodsViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://
                        add_goods();
                        break;
                    case 2://添加商品成功
                        final TipsDialog tipsDialog =new TipsDialog(AddGoodsActivity.this,"添加成功");
                        tipsDialog.setOneText("返回首页");
                        tipsDialog.setTwoText("继续添加");

                        tipsDialog.setOnBeanListener(new TipsDialog.OnHeatListener() {
                            @Override
                            public void onHeat(int type) {
                                if (type==1){//
                                    int sign = 1;
                                    EventBus.getDefault().post(new SystemMessageEvent(sign,""));
                                    finish();
                                }else {
                                    viewModel.RandomCode.set("");
                                    viewModel.GoodsName.setValue("");
                                    viewModel.GoodsCompany.setValue("");
                                    viewModel.GoodsDescribe.setValue("");
                                    viewModel.GoodsStock.setValue("0");
                                    viewModel.GoodsPrice.setValue("0");
                                    viewModel.GoodsPurchasePrice.setValue("0");
                                    tipsDialog.dismiss();
                                    int sign = 1;
                                    EventBus.getDefault().post(new SystemMessageEvent(sign,""));

                                }
                            }
                        });

                        new  XPopup.Builder(AddGoodsActivity.this)
                                .maxWidth((int) (widths * 0.5))
                                .maxHeight((int) (height*0.6))
                                .autoOpenSoftInput(false)
                                .asCustom(tipsDialog)
                                .show();
                        break;
                }
            }
        });

        //分类回调
        viewModel.GoodsList.observe(this, new Observer<ArrayList<GroupBean>>() {
            @Override
            public void onChanged(ArrayList<GroupBean> groupBeans) {
                goodsList=groupBeans;
                List<String> list=new ArrayList<>();
                int pon=0;
                for (int i=0;i<groupBeans.size();i++){
                    list.add(groupBeans.get(i).getName());
                    if (data!=null&&data.getCategory_name().equals(groupBeans.get(i).getName())){
                        pon=i;
                    }
                }
                binding.spinner.setItems(list);
                binding.spinner.setSelectedIndex(pon);

            }
        });
        //门店群组回调
        viewModel.groupList.observe(this, new Observer<ArrayList<GroupBean>>() {
            @Override
            public void onChanged(ArrayList<GroupBean> groupBeans) {
                groupList=groupBeans;
                List<String> list=new ArrayList<>();
                int pon=0;
                for (int i=0;i<groupBeans.size();i++){
                    list.add(groupBeans.get(i).getName());
                    if (data!=null&&data.getCategory_name().equals(groupBeans.get(i).getName())){
                        pon=i;
                    }
                }
                binding.StoreSpinner.setItems(list);
                binding.StoreSpinner.setSelectedIndex(pon);
            }
        });
    }


    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        viewModel.main_image.setValue("");

        String JsonData = new Gson().toJson(data);
        KLog.d("解析数据："+JsonData);
        viewModel.type.set(type);
        if (type==1){
            viewModel.TopName.setValue("编辑商品");
        }
        if (data!=null){
            viewModel.GoodsName.setValue(data.getGoods_name());
            viewModel.GoodsStock.setValue(data.getStock()+"");
            viewModel.GoodsPrice.setValue(data.getGoods_price()+"");
        }


        /**
         * 获取商品分类
         */
        viewModel.goods_category();

        viewModel.getGoodsGroup(AppApplication.spUtils.getString("StoreId"));


        viewModel.uc.text_state.setValue("开启");

//为未下拉的菜单项设置点击事件
        binding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(com.jaredrummler.materialspinner.MaterialSpinner view, int position, long id, Object item) {
                RxToast.normal("Clicked " + item);
                goods_position=position;
            }
        });
        binding.StoreSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                RxToast.normal("StoreSpinner: " + item);
                store_position=position;
            }
        });

        binding.switchBtnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchBtnStatus.isChecked()) {
                    viewModel.uc.text_state.setValue("开启");
                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.main_color)));
                } else {
                    viewModel.uc.text_state.setValue("关闭");
                    binding.switchBtnStatus.setBackColor(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.gray_a9)));
                }
            }
        });


    }


    /**
     * 添加商品
     */
    private void add_goods() {
        String status;
        if (binding.switchBtnStatus.isChecked()){
            status="1";
        }else {
            status="2";
        }
        viewModel.AddGoods(goodsList.get(goods_position).getId(),groupList.get(store_position).getId(),status);
    }




}
