package com.fenghuang.component_battery;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_battery.adapter.BatteryAdapter;
import com.fenghuang.component_base.helper.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/18 10:26
 */
public class BatteryFragment  extends LazyLoadFragment{

    private RecyclerView mRecyclerView;


    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) (Resources.getSystem().getDisplayMetrics().density * 20)));
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.default1);
        list.add(R.drawable.default1);
        BatteryAdapter batteryAdapter = new BatteryAdapter(R.layout.item_imageview,list);
        mRecyclerView.setAdapter(batteryAdapter);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_battery;
    }

    @Override
    protected void lazyLoad() {

    }

}
