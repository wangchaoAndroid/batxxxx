package com.fenghuang.component_user.camera;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.component_user.R;

/**
 * Create by wangchao on 2018/7/18 19:50
 */
public class CameraActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_camera);
        FragmentUtils.addFragment(getSupportFragmentManager(),new ActivityScanerCode(),R.id.root_view);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setEvent() {

    }
}
