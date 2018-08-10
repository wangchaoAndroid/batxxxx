package com.fenghuang.componentm_mall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.componentm_mall.camera.CameraActivity;

/**
 * Create by wangchao on 2018/7/21 11:04
 */
public class BuyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImageView;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.mall_enter_camera_iv){
            startActivity(new Intent(this, CameraActivity.class));
        }else if(id == R.id.top_back){
            finish();
        }
    }

    @Override
    protected void initView() {
        mImageView = findViewById(R.id.mall_enter_camera_iv);
        addOnClickListeners(R.id.mall_enter_camera_iv,R.id.top_back);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_buy;
    }
}
