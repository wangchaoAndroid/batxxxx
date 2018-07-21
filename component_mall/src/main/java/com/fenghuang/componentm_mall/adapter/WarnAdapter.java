package com.fenghuang.componentm_mall.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fenghuang.componentm_mall.R;
import com.fenghuang.componentm_mall.bean.WarnItem;

import java.util.List;

/**
 * Create by wangchao on 2018/7/19 17:18
 */
public class WarnAdapter extends BaseQuickAdapter<WarnItem,BaseViewHolder>{

    public WarnAdapter(int layoutResId, @Nullable List<WarnItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WarnItem item) {
        helper.setText(R.id.warn_date,item.warnDate);
        helper.setText(R.id.warn_info,item.warnInfo);
    }
}
