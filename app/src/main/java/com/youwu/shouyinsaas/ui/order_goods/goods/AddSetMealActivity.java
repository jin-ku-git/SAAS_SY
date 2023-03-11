package com.youwu.shouyinsaas.ui.order_goods.goods;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityAddMealBinding;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityListRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.ui.main.popup.ChoiceMealDialog;
import com.youwu.shouyinsaas.ui.main.popup.TipsDialog;
import com.youwu.shouyinsaas.ui.set_up.bean.SystemMessageEvent;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;


/**
 * 添加套餐页面
 * 2022/07/23
 */
public class AddSetMealActivity extends BaseActivity<ActivityAddMealBinding, AddMealViewModel> {


    //商品recyclerveiw的适配器
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    ArrayList<GroupBean> groupList=new ArrayList<>();
    int group_position=0;
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_add_meal;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AddMealViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(AddMealViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://选择套餐
                        ChoiceMealDialog dialog_cabinet =new ChoiceMealDialog(AddSetMealActivity.this,CabinetEntityList);

                        dialog_cabinet.setOnBeanListener(new ChoiceMealDialog.OnHeatListener() {
                            @Override
                            public void onHeat(ArrayList<CommunityBean> bean,double total_price) {
                                CabinetEntityList.clear();
                                CabinetEntityList.addAll(bean);
                                initRecyclerView();
                                viewModel.total_price.set(subZeroAndDot(total_price+""));
                            }
                        });

                        new  XPopup.Builder(AddSetMealActivity.this)
                                .maxWidth((int) (widths * 0.9))
                                .maxHeight((int) (height*0.8))
                                .autoOpenSoftInput(false)
                                .asCustom(dialog_cabinet)
                                .show();
                        break;
                    case 2://点击保存
                        add_goods();
                        break;
                    case 3://添加成功
                        final TipsDialog tipsDialog =new TipsDialog(AddSetMealActivity.this,"添加成功");
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
                                    viewModel.GoodsName.set("");
                                    viewModel.GoodsCompany.set("");
                                    viewModel.GoodsDescribe.set("");
                                    viewModel.total_price.set("0");
                                    CabinetEntityList.clear();
                                    initRecyclerView();
                                    tipsDialog.dismiss();
                                    int sign = 1;
                                    EventBus.getDefault().post(new SystemMessageEvent(sign,""));

                                }
                            }
                        });

                        new  XPopup.Builder(AddSetMealActivity.this)
                                .maxWidth((int) (widths * 0.5))
                                .maxHeight((int) (height*0.6))
                                .autoOpenSoftInput(false)
                                .asCustom(tipsDialog)
                                .show();
                        break;

                }
            }
        });
        //门店群组回调
        viewModel.groupList.observe(this, new Observer<ArrayList<GroupBean>>() {
            @Override
            public void onChanged(ArrayList<GroupBean> groupBeans) {
                groupList=groupBeans;
                List<String> list=new ArrayList<>();
                for (int i=0;i<groupBeans.size();i++){
                    list.add(groupBeans.get(i).getName());
                }
                binding.spinner.setItems(list);
            }
        });
    }



    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        viewModel.total_price.set("0");
        viewModel.uc.text_state.setValue("开启");

        viewModel.getGoodsGroup(AppApplication.spUtils.getString("StoreId"));

//为未下拉的菜单项设置点击事件
        binding.spinner.setOnItemSelectedListener(new com.jaredrummler.materialspinner.MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(com.jaredrummler.materialspinner.MaterialSpinner view, int position, long id, Object item) {
                RxToast.normal("Clicked " + item);
                group_position=position;
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
     * 商品列表
     */
    private void initRecyclerView() {
        //创建adapter
        mCabinetListRecycleAdapter = new CommunityListRecycleAdapter(this, CabinetEntityList);
        //给RecyclerView设置adapter
        binding.goodsRecyclerView.setAdapter(mCabinetListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.goodsRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(6, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.goodsRecyclerView.getItemDecorationCount()==0) {
            binding.goodsRecyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }


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
        String JsonData = new Gson().toJson(CabinetEntityList);
        viewModel.AddPackage(groupList.get(group_position).getId(),status,JsonData);
    }
}
