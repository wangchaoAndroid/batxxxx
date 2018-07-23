package com.fenghuang.componentm_mall.camera;

import android.os.Bundle;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.R;

/**
 * Create by wangchao on 2018/7/18 19:50
 */
public class CameraActivity extends BaseActivity {
    @Override
    protected void initView() {
        FragmentUtils.addFragment(getSupportFragmentManager(),new ActivityScanerCode(), R.id.root_view);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    protected void setEvent() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }
}
