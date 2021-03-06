package com.fenghuang.component_user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fenghuang.component_user.R;
import com.fenghuang.component_user.bean.Neiboor;

import java.util.List;

/**
 * Create by wangchao on 2018/7/19 17:18
 */
public class NeiborAdapter extends BaseQuickAdapter<Neiboor,BaseViewHolder>{

    public NeiborAdapter(int layoutResId, @Nullable List<Neiboor> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Neiboor item) {
        helper.setText(R.id.warn_date,item.address);
        helper.setText(R.id.warn_info,item.address);
    }


}
