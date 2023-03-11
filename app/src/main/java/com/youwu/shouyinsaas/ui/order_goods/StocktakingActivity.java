package com.youwu.shouyinsaas.ui.order_goods;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.youwu.shouyinsaas.BR;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.app.AppViewModelFactory;
import com.youwu.shouyinsaas.databinding.ActivityStocktakingBinding;
import com.youwu.shouyinsaas.db.InventoryDao;
import com.youwu.shouyinsaas.fu_ping.ScreenManager;
import com.youwu.shouyinsaas.fu_ping.ShowShopingDisplay;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityListRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.ui.order_goods.adapter.ReasonAdapter;
import com.youwu.shouyinsaas.ui.order_goods.adapter.StocktakingRecycleAdapter;
import com.youwu.shouyinsaas.ui.order_goods.bean.ReasonBean;
import com.youwu.shouyinsaas.util.ScanUtils;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 盘点页面
 * 2022/05/28
 */

public class StocktakingActivity extends BaseActivity<ActivityStocktakingBinding, StocktakingViewModel> {

    //副屏
    private ScreenManager screenManager = ScreenManager.getInstance();
    //副屏
    private ShowShopingDisplay showShopingDisplay = null;

    //分类recyclerveiw的适配器
    private CommunityRecycleAdapter mCommunityRecycleAdapter;
    //定义以CommunityEntityList实体类为对象的数据集合
    private ArrayList<GroupBean> CommunityEntityList = new ArrayList<>();

    //商品recyclerveiw的适配器
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    //购物车recyclerveiw的适配器
    private StocktakingRecycleAdapter mStocktakingRecycleAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();


    Intent intent;

    RecyclerView RecyclerView_reason;
    private ReasonAdapter adapter_day;
    private List<ReasonBean> list_day = new ArrayList<ReasonBean>();


    public InventoryDao inventoryDao;
    private int page=1;
    private int limit=100;
    String submitJson;
    String goods_id;
    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //获取列表传入的实体
        ArrayList<CommunityBean>  ShoppingList= (ArrayList<CommunityBean>) getIntent().getSerializableExtra("mCommunityBeanList");

        if (ShoppingList!=null){
            ShoppingEntityList=ShoppingList;
        }

    }

    @Override
    public StocktakingViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(StocktakingViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_stocktaking;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){

                    case 3://清除购物车
                        ShoppingEntityList.clear();

                        initRecyclerViewTow();
                        //更新UI
                        updateView();
                        break;

                    case 4://提
                        String submitJson = new Gson().toJson(ShoppingEntityList);
                        KLog.d("需上传的解析数据："+submitJson);
                        showRemindDialog(submitJson,"确认提交盘点吗？");
                        break;
                    case 5://盘点成功
                        for (int i=0;i<ShoppingEntityList.size();i++){
                            int goodsId=ShoppingEntityList.get(i).getGoods_id();
                            int Number=Integer.parseInt(ShoppingEntityList.get(i).getChange_stock());
                            if (inventoryDao.upGoodsNumber(goodsId,Number)){
                                KLog.d(i+":修改成功");
                            }else {
                                KLog.d(i+":修改失败");
                            }
                        }
                        initGoodsList(goods_id);
                        ShoppingEntityList.clear();
                        //更新UI
                        updateView();
                        RemindDialog.dismiss();
                        break;

                }
            }
        });
        //分类回调
        viewModel.groupList.observe(this, new Observer<ArrayList<GroupBean>>() {
            @Override
            public void onChanged(ArrayList<GroupBean> groupBeans) {
                if (CommunityEntityList.size()>0){
                    CommunityEntityList.clear();
                }
                CommunityEntityList.addAll(groupBeans);

                initRecyclerView();
                if (CommunityEntityList.size()>0){
                    viewModel.getStockGoodsList(AppApplication.spUtils.getString("StoreId"),"0",page+"",limit+"","1");
                }
            }
        });
        //获取商品列表回调
        viewModel.goodList.observe(this, new Observer<ArrayList<CommunityBean>>() {
            @Override
            public void onChanged(ArrayList<CommunityBean> communityBeans) {
                if (CabinetEntityList.size()>0){
                    CabinetEntityList.clear();
                }
                inventoryDao.initTable(communityBeans);

                if (communityBeans.size() == limit) {
                    page++;
                    viewModel.getStockGoodsList(AppApplication.spUtils.getString("StoreId"),"0",page+"",limit+"","1");
                    return;
                }
                //关闭对话框
                dismissDialog();
                goods_id=CommunityEntityList.get(0).getId()+"";
                initGoodsList(goods_id);
            }
        });
        /**
         * 原因返回回调
         */
        viewModel.reasonList.observe(this, new Observer<ArrayList<ReasonBean>>() {
            @Override
            public void onChanged(ArrayList<ReasonBean> reasonBeans) {
                if (list_day.size()>0){
                    list_day.clear();
                }
                list_day=reasonBeans;


                initRecycle_day();
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        int displays= AppApplication.spUtils.getInt("displays");
        if (displays>1){
            initDisplay();
        }

        inventoryDao = new InventoryDao(this);
        boolean dataExists = inventoryDao.isDataExist();
        if (dataExists) {
            inventoryDao.deleteAllData();
        }

        viewModel.goods_number.set("0");

        /**
         * 获取盘点分类
         */
        initGoodsCategory();


        if (ShoppingEntityList!=null){
            //更新UI
            updateView();
        }
        EventBus.getDefault().register(this);
    }

    //    初始化副屏
    public void initDisplay() {

        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            return;
        }

        screenManager.init(this);
        // 获取真实存在的副屏   初始化副屏
        Display display = screenManager.getPresentationDisplays();
        if (display != null && null == showShopingDisplay) {
            showShopingDisplay = new ShowShopingDisplay(this, display);
            showShopingDisplay.show();
            showShopingDisplay.initBannnerView();
        } else {
            if (!showShopingDisplay.isShow) {
                showShopingDisplay.show();
                showShopingDisplay.initBannnerView();
            } else if (null != showShopingDisplay) {
                showShopingDisplay.initBannnerView();
            }
        }
    }

    /**
     * 获取原因
     */
    private void initGoodsCategory() {
        viewModel.getGoodsCategory();
    }


    //获取从ReasonAdapter传递的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReasonBeanEvbus(List<ReasonBean> message) {


        list_day=message;

        KLog.d("解析数据："+list_day.size());
    }


    private void initGoodsList(String goods_id) {
        //        刷新一下实际库存，因为如果客户没买，内存中的库存可能会有改变。但是并没有真正成交
        CabinetEntityList.clear();

        CabinetEntityList.addAll(inventoryDao.getGoodListByCategoryId(goods_id));
//        CabinetEntityList.addAll(inventoryDao.getAllDate());

        for (int i=0;i<CabinetEntityList.size();i++){
            for (int j=0;j<ShoppingEntityList.size();j++){
                if (ShoppingEntityList.get(j).getGoods_id()==CabinetEntityList.get(i).getGoods_id()){
                    CabinetEntityList.get(i).setGoods_number(ShoppingEntityList.get(j).getGoods_number());
                    CabinetEntityList.get(i).setCom_number_state(1);
                }
            }
        }

        initRecyclerViewTow();
    }

    /**
     * 分类
     */
    private void initRecyclerView() {
        //一级分类
        //创建adapter
        mCommunityRecycleAdapter = new CommunityRecycleAdapter(this, CommunityEntityList);
        //给RecyclerView设置adapter
        binding.recyclerviewCommunity.setAdapter(mCommunityRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerviewCommunity.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerviewCommunity.getItemDecorationCount()==0) {
            binding.recyclerviewCommunity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCommunityRecycleAdapter.setOnScanningListener(new CommunityRecycleAdapter.OnScanningListener() {
            @Override
            public void onScanning(GroupBean groupBean) {
                goods_id=groupBean.getId()+"";
                initGoodsList(goods_id);
            }
        });
    }

    /**
     * 商品列表
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mCabinetListRecycleAdapter = new CommunityListRecycleAdapter(this, CabinetEntityList);
        //给RecyclerView设置adapter
        binding.recyclerviewCommodity.setAdapter(mCabinetListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerviewCommodity.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerviewCommodity.getItemDecorationCount()==0) {
            binding.recyclerviewCommodity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mCabinetListRecycleAdapter.setOnItemClickListener(new CommunityListRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {
                data.setGoods_number(1);
                /**
                 * 商品详情弹窗
                 */
                showDialog(data,position);
            }
        });


    }
    /**
     * 更新商品UI
     */
    private void updateGoodsView(CommunityBean data, int position) {

        mCabinetListRecycleAdapter.notifyItemChanged(position,mCabinetListRecycleAdapter.getItemId(R.id.shopping_num));
    }


    /**
     * 购物车列表
     */
    private void initRecyclerViewThree() {

        binding.shoppingCartRecycler.setNestedScrollingEnabled(false);
        //创建adapter
        mStocktakingRecycleAdapter = new StocktakingRecycleAdapter(this, ShoppingEntityList);
        //给RecyclerView设置adapter
        binding.shoppingCartRecycler.setAdapter(mStocktakingRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.shoppingCartRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.shoppingCartRecycler.getItemDecorationCount()==0) {
            binding.shoppingCartRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        /**
         * 删除
         */
        mStocktakingRecycleAdapter.setOnDeleteListener(new StocktakingRecycleAdapter.OnDeleteListener() {
            @Override
            public void onDelete(CommunityBean data, int position) {
                DeleteShopping(data,position);
                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,data.getPosition());

            }
        });
    }

    /**
     * 添加到购物车
     * @param data
     */
    private void addUpShopping(CommunityBean data) {
        int hasInShopCar = -1;
        //循环判断有没有相同的商品
            for (int i=0;i<ShoppingEntityList.size();i++){
                if (data.getGoods_id()==ShoppingEntityList.get(i).getGoods_id()
                ){
                    //如果是修改直接中断循环
                    hasInShopCar = i;
                    break;
                }
            }

        if (hasInShopCar == -1) {
            ShoppingEntityList.add(data);
        }else {
            ShoppingEntityList.get(hasInShopCar).setGoods_number(data.getGoods_number());
            ShoppingEntityList.get(hasInShopCar).setMark(data.getMark());
        }
        //更新UI
        updateView();
    }

    /**
     * 更新UI
     */
    private void updateView() {
        if (ShoppingEntityList.size()==0){

            binding.nullView.setVisibility(View.VISIBLE);
            viewModel.goods_number.set("0");

        }else {
            binding.nullView.setVisibility(View.GONE);
                viewModel.goods_number.set(ShoppingEntityList.size()+"");
        }
        for (int i=0;i<CabinetEntityList.size();i++){
            CabinetEntityList.get(i).setCom_number_state(0);
            for (int j=0;j<ShoppingEntityList.size();j++){
                if (ShoppingEntityList.get(j).getGoods_id()==CabinetEntityList.get(i).getGoods_id()){
                    CabinetEntityList.get(i).setGoods_number(ShoppingEntityList.get(j).getGoods_number());
                    CabinetEntityList.get(i).setCom_number_state(1);
                }
            }
        }
        initRecyclerViewTow();
        initRecyclerViewThree();


    }



    /**
     * 删除操作
     * @param data
     */
    private void DeleteShopping(CommunityBean data, int position) {
        data.setCom_number_state(0);
        data.setGoods_number(1);
        /**
         * 更新商品UI
         */
        updateGoodsView(data,position);
        ShoppingEntityList.remove(position);
        //更新UI
        updateView();
    }



    /**
     * 商品详情弹窗
     */
    private void showDialog(final CommunityBean data, final int position) {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_stocktaking, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.show();

        //初始化控件

        final TextView GoodsName = (TextView) dialogView.findViewById(R.id.GoodsName);
        final TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final TextView confirm = (TextView) dialogView.findViewById(R.id.confirm);//确定
        final TextView stock = (TextView) dialogView.findViewById(R.id.stock);//原库存
        final EditText change_edit_text = (EditText) dialogView.findViewById(R.id.change_edit_text);//修改后的库存
        RecyclerView_reason=dialogView.findViewById(R.id.RecyclerView_reason);



        GoodsName.setText(data.getGoods_name());
        stock.setText(data.getStock()+"");
        viewModel.initReason();
        //如果原因数量==0走获取原因接口
//        if (list_day.size()==0){
//            viewModel.initReason();
//        }else {
//            initRecycle_day();
//        }
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(change_edit_text.getText().toString())){

                    RxToast.normal("请输入修改后的库存！");
                    return;
                }else {

                    data.setChange_stock(change_edit_text.getText().toString());
                }
                adapter_day.determine();

                 List<ReasonBean> list = new ArrayList<ReasonBean>();

                data.setMark(list);
                data.getMark().addAll(list_day);
                data.setCom_number_state(1);
                /**
                 * 添加到购物车
                 */
                addUpShopping(data);
                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,position);
                dialog.cancel();
            }
        });

    }


    /**
     * 原因
     */
    private void initRecycle_day() {
        // 纵向滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView_reason.setLayoutManager(linearLayoutManager);

        adapter_day = new ReasonAdapter(StocktakingActivity.this, list_day);
        RecyclerView_reason.setAdapter(adapter_day);
        //   添加动画
        RecyclerView_reason.setItemAnimator(new DefaultItemAnimator());

    }

    /**
     * 禁止Edittext弹出软件盘，光标依旧正常显示。
     */
    public void disableShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
        }

        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
        }
    }
     Dialog RemindDialog;
    /**
     * 提醒弹窗
     */
    private void showRemindDialog(final String data,String content) {

        RemindDialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_remind, null);
        //将界面填充到AlertDiaLog容器
        RemindDialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        RemindDialog.getWindow().setGravity(Gravity.CENTER);
        RemindDialog.setCancelable(true);//点击外部消失弹窗
        RemindDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        RemindDialog.show();

        //初始化控件

        final TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        final TextView content_text = (TextView) dialogView.findViewById(R.id.content_text);//内容
        final TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);//取消
        final TextView confirm = (TextView) dialogView.findViewById(R.id.confirm);//确定


        content_text.setText(content);
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemindDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemindDialog.dismiss();
            }
        });
        //确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("上传的数据");
                viewModel.sorting_inventory(data);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (showShopingDisplay != null && showShopingDisplay.isShow) {
            showShopingDisplay.dismiss();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (ScanUtils.getInstance().isInputFromScanner(StocktakingActivity.this, event)) {
            //暂时取消扫码
            ScanUtils.getInstance().setOnResultListener(scanListener);
            ScanUtils.getInstance().analysisKeyEvent(event);
            return false;
        }
        return false;
    }
    ScanUtils.OnResultListener scanListener = new ScanUtils.OnResultListener() {
        @Override
        public void onResult(final String resultStr) {
            Log.e("scanToWork", "onResult:" + resultStr);

            if (TextUtils.isEmpty(resultStr)) {
                return;
            }

            long last = AppApplication.spUtils.getLong("lastpay");
            if (System.currentTimeMillis() - last < 2000) {
                return;
            }
            AppApplication.spUtils.put("lastpay", System.currentTimeMillis());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scanToWork(resultStr);
                }
            });
        }
    };
    private void scanToWork(String resultStr) {
        if (resultStr.startsWith("xzks")) {
            String order_sn=resultStr.replace("xzks_","");
            viewModel.close_order(order_sn, StocktakingActivity.this);

        }
    }
}
