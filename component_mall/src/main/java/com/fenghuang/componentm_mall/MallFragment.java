package com.fenghuang.componentm_mall;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.adapter.MallAdapter;

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
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FragmentUtils.replaceFragment(getChildFragmentManager(),R.id.container2,new ProductFragment(),false);
        mMallAdapter.setChecked(position);
    }
}
