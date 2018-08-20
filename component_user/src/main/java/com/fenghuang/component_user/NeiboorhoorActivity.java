package com.fenghuang.component_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.helper.SpacesItemDecoration;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_user.adapter.NeiborAdapter;
import com.fenghuang.component_user.bean.Neiboor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by wangchao on 2018/8/20 17:19
 */
public class NeiboorhoorActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "WarnActivity";
    private RecyclerView mRecyclerView;

    private ImageView back;
    List<Neiboor> mNeiboors = new ArrayList<>();
    private NeiborAdapter mNeiborAdapter;
    NetServices batteryNetServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        back = findViewById(R.id.back);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        back.setOnClickListener(this);
        mNeiborAdapter = new NeiborAdapter(R.layout.item_neibor,mNeiboors);
        mRecyclerView.setAdapter(mNeiborAdapter);
        mNeiborAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //标记为已读
                if(!mNeiboors.isEmpty()){
                    Neiboor neiboor = mNeiboors.get(position);
                }

            }
        });
    }

    /**
     * 获取告警信息
     */
    public void getNeiborInfo(int neiborMeters){
//        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
//        ILog.e(TAG,token);
//        batteryNetServices.getNearbyShop(token,neiborMeters,).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ResponseCallback<BaseEntery<List<Neiboor>>>() {
//                    @Override
//                    public void onSuccess(BaseEntery<List<Neiboor>> value) {
//                        ILog.e(TAG,value + "");
//                        if(value != null && !value.obj.isEmpty()){
//                            mNeiboors.clear();
//                            mNeiboors.addAll(value.obj);
//                            mNeiborAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onFailture(String e) {
//                        RxToast.error(e);
//                    }
//                });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int range = intent.getIntExtra("range", 0);
        getNeiborInfo(range);
    }



    @Override
    protected void setEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_neibor;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.back){
            ILog.e("22222","22222222222222222222");
            finish();
        }
    }
}
