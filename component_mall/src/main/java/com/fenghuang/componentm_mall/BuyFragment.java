package com.fenghuang.componentm_mall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.componentm_mall.camera.CameraActivity;

/**
 * Create by wangchao on 2018/7/21 11:04
 */
public class BuyFragment extends LazyLoadFragment implements View.OnClickListener {
    private ImageView mImageView;
    @Override
    protected void init(View view, Bundle savedInstanceState) {
        mImageView = view.findViewById(R.id.mall_enter_camera_iv);
        addOnClickListeners(R.id.mall_enter_camera_iv);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_buy;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.mall_enter_camera_iv){
            startActivity(new Intent(getActivity(), CameraActivity.class));
        }
    }
}
