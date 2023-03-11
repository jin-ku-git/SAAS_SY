package com.youwu.shouyinsaas.ui.main.popup;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.Gson;
import com.lxj.xpopup.core.CenterPopupView;
import com.youwu.shouyinsaas.R;
import com.youwu.shouyinsaas.app.AppApplication;
import com.youwu.shouyinsaas.db.InventoryDao;
import com.youwu.shouyinsaas.service.Bean;
import com.youwu.shouyinsaas.service.HttpCallback;
import com.youwu.shouyinsaas.service.HttpHelper;
import com.youwu.shouyinsaas.service.MyHashMap;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityListRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.CommunityRecycleAdapter;
import com.youwu.shouyinsaas.ui.main.adapter.MealShoppingAdapter;
import com.youwu.shouyinsaas.ui.main.bean.CommunityBean;
import com.youwu.shouyinsaas.ui.main.bean.GroupBean;
import com.youwu.shouyinsaas.utils_view.DividerItemDecorations;
import com.youwu.shouyinsaas.utils_view.RxToast;

import java.util.ArrayList;

import me.goldze.mvvmhabit.utils.KLog;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 选择套餐
 */

public class ChoiceMealDialog extends CenterPopupView {



    Context mContext;
    public InventoryDao inventoryDao;

    ArrayList<GroupBean> groupList=new ArrayList<>();
    //分类recyclerveiw的适配器
    private CommunityRecycleAdapter mCommunityRecycleAdapter;

    //商品recyclerveiw的适配器
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();
    //购物车recyclerveiw的适配器
    private MealShoppingAdapter mMealShoppingAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();


    RecyclerView mRecyclerView;
    RecyclerView recyclerview_commodity;
    RecyclerView shopping_cart_recycler;
    TextView shopping_num;
    TextView determine;
    RelativeLayout clear_layout;
    LinearLayout null_view;
    public ChoiceMealDialog(@NonNull Context context, ArrayList<CommunityBean> cabinetEntityList) {
        super(context);
        this.mContext=context;
        ShoppingEntityList.addAll(cabinetEntityList);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_choice_meal;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        inventoryDao = new InventoryDao(mContext);

        //初始化控件
        TextView close_text = (TextView) findViewById(R.id.close_text);
        TextView cancel = (TextView) findViewById(R.id.cancel);
        shopping_num = (TextView) findViewById(R.id.shopping_num);
        clear_layout = (RelativeLayout) findViewById(R.id.clear_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_community);
        recyclerview_commodity = (RecyclerView) findViewById(R.id.recyclerview_commodity);
        shopping_cart_recycler = (RecyclerView) findViewById(R.id.shopping_cart_recycler);
        null_view=findViewById(R.id.null_view);
        determine=findViewById(R.id.determine);

        //如果
        if (ShoppingEntityList.size()!=0){
            updateView();
        }
        getGoodsGroup();
        //返回
        close_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        clear_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShoppingEntityList.size()!=0){
                    ShoppingEntityList.clear();

                    shopping_num.setVisibility(GONE);

                    for (int i=0;i<CabinetEntityList.size();i++){
                        CabinetEntityList.get(i).setGoods_number(0);
                        CabinetEntityList.get(i).setCom_number_state(0);
                    }
                    mCabinetListRecycleAdapter.notifyDataSetChanged();
                    mMealShoppingAdapter.notifyDataSetChanged();
                }else {
                    RxToast.normal("已经没有商品了");
                }

            }
        });
        determine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShoppingEntityList.size()==0||ShoppingEntityList.size()==1){
                    RxToast.normal("至少选择两件商品");
                }else {
                    Double total_price=0.0;
                    for (int i=0;i<ShoppingEntityList.size();i++){
                        total_price+=(Double.parseDouble(ShoppingEntityList.get(i).getGoods_price()))*ShoppingEntityList.get(i).getGoods_number();
                    }
                    if (mHeatListener!=null){
                        mHeatListener.onHeat(ShoppingEntityList,total_price);
                    }
                    dialog.dismiss();
                }
            }
        });






    }

    /**
     * 获取群组
     */
    private void getGoodsGroup() {

        String store_id=AppApplication.spUtils.getString("StoreId");
        //访问网络
        String url = "https://saas_test_api.youwuu.com/app/goods/goods_category";
        Log.i("获取分类", url);

        MyHashMap<String> mParams = new MyHashMap<String>();
//        mParams.put("store_id", store_id);

        //访问网络
        HttpHelper.obtain().post(url,
                mParams, new HttpCallback<Bean>() {
                    @Override
                    public void onSuccess(Bean expressBean) {

                        if (expressBean.code == 200) {
                            String JsonData = new Gson().toJson(expressBean.data);

                            ArrayList<GroupBean> list=  AppApplication.getObjectList(JsonData,GroupBean.class);

                            groupList.addAll(list);
                            initRecyclerView();
                            if (groupList.size()>0){
                                initGoodsList(groupList.get(0).getId()+"");
                            }
                        }else {
                            RxToast.normal(expressBean.message);
                        }
                    }
                    @Override
                    public void onFailed(String string) {

                        RxToast.normal("网络请求失败，请检查网络");

                    }
                });
    }

    /**
     * 分类
     */
    private void initRecyclerView() {
        //一级分类
        //创建adapter
        mCommunityRecycleAdapter = new CommunityRecycleAdapter(mContext, groupList);
        //给RecyclerView设置adapter
        mRecyclerView.setAdapter(mCommunityRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (mRecyclerView.getItemDecorationCount()==0) {
            mRecyclerView.addItemDecoration(new DividerItemDecorations(mContext, DividerItemDecorations.VERTICAL));
        }
        mCommunityRecycleAdapter.setOnScanningListener(new CommunityRecycleAdapter.OnScanningListener() {
            @Override
            public void onScanning(GroupBean groupBean) {
                initGoodsList(groupBean.getId()+"");
//                viewModel.getGoodsGroup(AppApplication.spUtils.getString("StoreId"),groupBean.getId()+"");
            }
        });
    }
    //    刷新商品列表
    private void initGoodsList(String goods_id) {
        //        刷新一下实际库存，因为如果客户没买，内存中的库存可能会有改变。但是并没有真正成交
        CabinetEntityList.clear();

        CabinetEntityList.addAll(inventoryDao.getGoodListByCategoryId(goods_id));
        for (int i=0;i<CabinetEntityList.size();i++){
            for (int j=0;j<ShoppingEntityList.size();j++){
                if (ShoppingEntityList.get(j).getGoods_id()==CabinetEntityList.get(i).getGoods_id()){
                    CabinetEntityList.get(i).setGoods_number(ShoppingEntityList.get(j).getGoods_number());
                    CabinetEntityList.get(i).setCom_number_state(1);
                }
            }
        }
        initRecyclerViewTow();


        String JsonData = new Gson().toJson(CabinetEntityList);
        KLog.d("解析数据："+JsonData);

    }

    /**
     * 商品列表
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mCabinetListRecycleAdapter = new CommunityListRecycleAdapter(mContext, CabinetEntityList);
        //给RecyclerView设置adapter
        recyclerview_commodity.setAdapter(mCabinetListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        recyclerview_commodity.setLayoutManager(new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (recyclerview_commodity.getItemDecorationCount()==0) {
            recyclerview_commodity.addItemDecoration(new DividerItemDecorations(mContext, DividerItemDecorations.VERTICAL));
        }
        mCabinetListRecycleAdapter.setOnItemClickListener(new CommunityListRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {
                data.setCom_number_state(1);

                /**
                 * 添加
                 */
                addShopping(data,position);

                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,position);

            }
        });
        mCabinetListRecycleAdapter.setOnPopupListener(new CommunityListRecycleAdapter.OnPopupListener() {
            @Override
            public void onPopup(CommunityBean data, int position) {
                data.setGoods_number(1);
                /**
                 * 商品详情弹窗
                 */
//                showDialog(data,position);
            }
        });

    }

    /**
     * 添加到购物车  +1
     * @param data
     * @param position
     */
    private void addShopping(CommunityBean data, int position) {
        int hasInShopCar = -1;
        for (int i=0;i<ShoppingEntityList.size();i++){
            if (data.getGoods_id()==ShoppingEntityList.get(i).getGoods_id()){
                //如果是修改直接中断循环
                hasInShopCar = i;
                break;
            }
        }

        if (hasInShopCar == -1) {
            data.setGoods_number(1);
            ShoppingEntityList.add(data);
            CabinetEntityList.get(position).setGoods_number(data.getGoods_number());
        }else {
            ShoppingEntityList.get(hasInShopCar).setGoods_number(ShoppingEntityList.get(hasInShopCar).getGoods_number()+1);
            CabinetEntityList.get(position).setGoods_number(data.getGoods_number());
        }

        //更新UI
        updateView();
    }
    /**
     * 更新商品UI
     */
    private void updateGoodsView(CommunityBean data, int position) {

        mCabinetListRecycleAdapter.notifyItemChanged(position,mCabinetListRecycleAdapter.getItemId(R.id.shopping_num));
    }

    /**
     * 更新UI
     */
    private void updateView() {
        if (ShoppingEntityList.size()==0){
            shopping_num.setVisibility(View.GONE);
            null_view.setVisibility(View.VISIBLE);

            for (int i=0;i<CabinetEntityList.size();i++){
                CabinetEntityList.get(i).setCom_number_state(0);
            }
        }else {
            null_view.setVisibility(View.GONE);
            shopping_num.setVisibility(View.VISIBLE);

            int num=0;
            for (int i=0;i<ShoppingEntityList.size();i++){
                num+=ShoppingEntityList.get(i).getGoods_number();
            }

            shopping_num.setText(num+"");

            for (int i=0;i<CabinetEntityList.size();i++){
                CabinetEntityList.get(i).setCom_number_state(0);
                for (int j=0;j<ShoppingEntityList.size();j++){
                    if (ShoppingEntityList.get(j).getGoods_id()==CabinetEntityList.get(i).getGoods_id()){
                        CabinetEntityList.get(i).setGoods_number(ShoppingEntityList.get(j).getGoods_number());
                        CabinetEntityList.get(i).setCom_number_state(1);
                    }
                }
            }

        }
        initRecyclerViewTow();
        initRecyclerViewThree();
    }
    /**
     * 购物车列表
     */
    private void initRecyclerViewThree() {

        shopping_cart_recycler.setNestedScrollingEnabled(false);
        //创建adapter
        mMealShoppingAdapter = new MealShoppingAdapter(mContext, ShoppingEntityList);
        //给RecyclerView设置adapter
        shopping_cart_recycler.setAdapter(mMealShoppingAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        shopping_cart_recycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (shopping_cart_recycler.getItemDecorationCount()==0) {
            shopping_cart_recycler.addItemDecoration(new DividerItemDecorations(mContext, DividerItemDecorations.VERTICAL));
        }
        /**
         * 加减
         */
        mMealShoppingAdapter.setOnChangeListener(new MealShoppingAdapter.OnChangeListener() {
            @Override
            public void onChange(CommunityBean data, int position) {
                updateShopping(data,position);

                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,data.getPosition());
            }
        });
        /**
         * 删除
         */
        mMealShoppingAdapter.setOnDeleteListener(new MealShoppingAdapter.OnDeleteListener() {
            @Override
            public void onDelete(CommunityBean data, int position) {
                DeleteShopping(data,position);
                /**
                 * 更新商品U
                 */
                updateGoodsView(data,data.getPosition());

            }
        });
    }
    /**
     * 加减操作
     * @param data
     */
    private void updateShopping(CommunityBean data, int position) {
        ShoppingEntityList.set(position,data);

        //更新UI
        updateView();
    }
    /**
     * 删除操作
     * @param data
     */
    private void DeleteShopping(CommunityBean data, int position) {
        CabinetEntityList.get(position).setGoods_number(1);
        CabinetEntityList.get(position).setCom_number_state(0);

        /**
         * 更新商品UI
         */
        updateGoodsView(data,position);
        ShoppingEntityList.remove(position);
        //更新UI
        updateView();
    }

    //确定的回调
    public interface OnHeatListener {
        void onHeat(ArrayList<CommunityBean> bean,double total_price);
    }

    public void setOnBeanListener(OnHeatListener listener) {
        mHeatListener = listener;
    }

    private OnHeatListener mHeatListener;




    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
