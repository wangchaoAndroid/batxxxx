package com.fenghuang.component_battery;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.helper.SpacesItemDecoration;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.DateTimeUtil;
import com.fenghuang.component_battery.adapter.WarnAdapter;
import com.fenghuang.component_battery.bean.WarnModel;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/7/18 19:45
 */
public class WarnActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "WarnActivity";
    private RecyclerView mRecyclerView;

    private ImageView back;
    List<WarnModel> warnItemList = new ArrayList<>();
    private WarnAdapter mWarnAdapter;
    BatteryNetServices batteryNetServices = RetrofitManager.getInstance().initRetrofit().create(BatteryNetServices.class);

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        back = findViewById(R.id.back);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        back.setOnClickListener(this);
        mWarnAdapter = new WarnAdapter(R.layout.item_warn,warnItemList);
        mRecyclerView.setAdapter(mWarnAdapter);
        mWarnAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //标记为已读
                if(!warnItemList.isEmpty()){
                    WarnModel warnModel = warnItemList.get(position);
                    markWarkInfo(warnModel.id,position);
                }

            }
        });
    }

    /**
     * 标记为已读
     */
    public void markWarkInfo(long id,final int position){
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        ILog.e(TAG,token);
        batteryNetServices.getAlarmtabById(token,id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<WarnModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<WarnModel> value) {
                        ILog.e(TAG,value + "");
                        WarnModel warnModel = value.obj;
                        RxToast.warning(warnModel.alarmcontent);
                        WarnModel oldWarnModel = warnItemList.get(position);
                        oldWarnModel.alarmcontent = warnModel.alarmcontent;
                        oldWarnModel.status = warnModel.status;
                        mWarnAdapter.notifyItemChanged(position,position);
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e);
                    }
                });
    }


    /**
     * 获取告警信息
     */
    public void getWarnInfos(){
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        ILog.e(TAG,token);
        batteryNetServices.getAlarmtabAll(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<List<WarnModel>>>() {
                    @Override
                    public void onSuccess(BaseEntery<List<WarnModel>> value) {
                        ILog.e(TAG,value + "");
                        if(value != null && !value.obj.isEmpty()){
                            warnItemList.clear();
                            warnItemList.addAll(value.obj);
                            mWarnAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e);
                    }
                });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getWarnInfos();
    }



    @Override
    protected void setEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_warn;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.back){
            Log.e("22222","22222222222222222222");
            finish();
        }
    }
}
