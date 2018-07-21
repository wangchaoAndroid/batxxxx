package com.fenghuang.componentm_mall.camera;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.helper.SpacesItemDecoration;
import com.fenghuang.component_base.utils.DateTimeUtil;
import com.fenghuang.componentm_mall.R;
import com.fenghuang.componentm_mall.adapter.WarnAdapter;
import com.fenghuang.componentm_mall.bean.WarnItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/18 19:45
 */
public class WarnFragment extends LazyLoadFragment{

    private RecyclerView mRecyclerView;


    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));
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
    protected int setContentView() {
        return R.layout.fragment_warn;
    }

    @Override
    protected void lazyLoad() {

    }

}
