package com.fenghuang.component_battery;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.helper.SpacesItemDecoration;
import com.fenghuang.component_base.utils.DateTimeUtil;
import com.fenghuang.component_battery.adapter.WarnAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/18 19:45
 */
public class WarnFragment extends BaseActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;

    private ImageView back;



    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        back = findViewById(R.id.back);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        back.setOnClickListener(this);
        List<WarnItem> warnItemList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            WarnItem warnItem = new WarnItem();
            warnItem.warnDate = DateTimeUtil.formatDateTime(System.currentTimeMillis());
            warnItem.warnInfo = "这是第" + i +"个警报";
            warnItemList.add(warnItem);
        }
        WarnAdapter warnAdapter = new WarnAdapter(R.layout.item_warn,warnItemList);
        mRecyclerView.setAdapter(warnAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

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
