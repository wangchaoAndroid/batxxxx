package com.fenghuang.componentm_mall.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.ProductFragment;
import com.fenghuang.componentm_mall.R;

import java.util.List;

/**
 * Create by wangchao on 2018/7/21 09:47
 */
public class MallAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public MallAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }
    /**
     * item 是否被选中
     */
    private int checkedPos;



    public void setChecked(int position){
        checkedPos = position;
        notifyDataSetChanged();
    }
    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        helper.setText(R.id.text1,item);
        helper.setChecked(R.id.text1,checkedPos == helper.getAdapterPosition());
    }
}
