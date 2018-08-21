package com.fenghuang.componentm_mall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.adapter.MallAdapter;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/18 14:00
 */
public class MallFragment extends LazyLoadFragment implements BaseQuickAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    List<String> categorys = new ArrayList<>();
    private MallAdapter mMallAdapter;

    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mall;
    }

    @Override
    protected void lazyLoad() {
        for (int i = 0; i < 8; i++) {
            categorys.add("categoty--" + i );
        }
        mMallAdapter = new MallAdapter(R.layout.item_category, categorys);
        mRecyclerView.setAdapter(mMallAdapter);
        mMallAdapter.setOnItemClickListener(this);
        mMallAdapter.setChecked(0);
        FragmentUtils.replaceFragment(getChildFragmentManager(),R.id.container2,new ProductFragment(),false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPayData();
            }
        }).start();

    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FragmentUtils.replaceFragment(getChildFragmentManager(),R.id.container2,new ProductFragment(),false);
        mMallAdapter.setChecked(position);
    }

    /**
     * @param context
     * @param data  charge/order 字符串
     *              PaymentHandler 支付结果回调类
     */
    public void pay(Activity context , String data ){
        //Pingpp.createPayment(context, data);

        PingppUI.createPay(context, data, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                int code = data.getExtras().getInt("code");
                String result = data.getExtras().getString("result");
                RxToast.info(""+ result);
                ILog.e(TAG,"code" + code + "----result" + result);
//                if(code == 1){
//                    details.remove(pos);
//                    orderDetailAdapter.notifyItemRemoved(pos);
//                }else {
//                    ToastUtils.showShortToast(OrderDetailActivity.this,""+ result);
//                }
                //Log.e(TAG, "handlePaymentResult: "+ code + "---" + result );
            }
        });
    }

    public void getPayData() {
        String url = "http://120.79.13.43:8080/fh_battery/payInterface?token=39139054-47fd-4cc2-98c0-c1729599c22f&payType=200&productNumber=dawd4545&batteryId=23";
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.connect();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(30000);
            String msg = "";
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200){
                // 从流中读取响应信息
                 BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                 String line = null;
                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    msg += line + "\n";
                }
                ILog.e(TAG,"MSG" + msg);
                reader.close(); // 关闭流
                pay(getActivity(),msg);
            }

        } catch (IOException e) {


        }
    }
}
