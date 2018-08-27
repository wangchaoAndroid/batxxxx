package com.fenghuang.componentm_mall.camera;

import android.content.Intent;
import android.os.Bundle;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.R;
import com.fenghuang.componentm_mall.module.scaner.OnRxScanerListener;
import com.google.zxing.Result;

/**
 * Create by wangchao on 2018/7/18 19:50
 */
public class CameraActivity extends BaseActivity implements OnRxScanerListener {
    private ActivityScanerCode activityScanerCode;
    @Override
    protected void initView() {
        activityScanerCode = new ActivityScanerCode();
        activityScanerCode.setScanerListener(this);
        FragmentUtils.addFragment(getSupportFragmentManager(),activityScanerCode, R.id.root_view);
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

    @Override
    public void onSuccess(String type, Result result) {
        ILog.e("1111",result.getText() + "---" + result.toString());
        Intent intent = new Intent();
        intent.putExtra("productNumber",result.getText());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onFail(String type, String message) {

    }
}
