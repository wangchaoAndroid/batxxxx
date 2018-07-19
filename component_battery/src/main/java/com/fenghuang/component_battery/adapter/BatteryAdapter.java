package com.fenghuang.component_battery.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fenghuang.component_battery.R;
import com.fenghuang.component_base.helper.GlideRoundTransform;

import java.util.List;

/**
 * Create by wangchao on 2018/7/19 14:37
 */
public class BatteryAdapter extends BaseQuickAdapter<Integer,BaseViewHolder> {
    public BatteryAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setImageResource(R.id.image,item);
        RequestOptions myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(mContext,20));

        Glide.with(mContext)
                .load(item)
                .apply(myOptions)
                .into((ImageView) helper.getView(R.id.image));
    }
}
