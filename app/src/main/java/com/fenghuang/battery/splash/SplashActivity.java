package com.fenghuang.battery.splash;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.battery.MainActivity;
import com.fenghuang.battery.R;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.data.SPDataSource;

import org.w3c.dom.Text;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    @Override
    public void handlerMessage(Message msg) {
        super.handlerMessage(msg);
        boolean hasComponentUser = CC.hasComponent("component_user");
        if (hasComponentUser) {
            CCResult ccResult = CC.obtainBuilder("component_user")
                    .setContext(this)
                    .setActionName("forceGetLoginUser")
                    .build()
                    .call();
            String data = ccResult.getDataItem(SPDataSource.USER_TOKEN);
            if(!TextUtils.isEmpty(data)){
                startActivity(MainActivity.class, true);
            }
        } else {
            // 如果没有用户组件直接进入主界面
            startActivity(MainActivity.class, true);
        }
    }

    @Override
    protected void initView() {}

    @Override
    protected void initData(Bundle savedInstanceState) {
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }


    @Override
    protected void setEvent() {}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }
}
