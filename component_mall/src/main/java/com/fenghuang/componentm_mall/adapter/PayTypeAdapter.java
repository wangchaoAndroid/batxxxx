package com.fenghuang.componentm_mall.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fenghuang.componentm_mall.R;
import com.fenghuang.componentm_mall.bean.PayType;

import java.util.List;

/**
 * Created by Administrator on 2018/9/11 0011.
 */

public class PayTypeAdapter extends BaseQuickAdapter<PayType,BaseViewHolder> {


    public PayTypeAdapter(int layoutResId, @Nullable List<PayType> data) {
        super(layoutResId, data);
    }

    public void switchChecked(){
        for(PayType payType : mData){
            payType.checked = false;
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, final  PayType item) {
        helper.setText(R.id.des_pay,item.staging + "");
        final ImageView radioButton = helper.getView(R.id.rbcheck);
        radioButton.setSelected(item.checked);
        radioButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onCbClick(item);
                    switchChecked();
                    item.checked = true;
                    notifyDataSetChanged();
                }
            }
        });
    }

    public interface OnCustomCheckedListener{
        void onCbClick( PayType item);
    }

    private OnCustomCheckedListener listener;

    public void setOnCustomCheckedListener(OnCustomCheckedListener onCustomCheckedListener){
        listener = onCustomCheckedListener;
    }


}
