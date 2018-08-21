package com.fenghuang.battery.splash;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.battery.MainActivity;
import com.fenghuang.battery.R;
import com.fenghuang.battery.adapter.SpalashAdapter;
import com.fenghuang.battery.adapter.ViewPagerIndicator;
import com.fenghuang.battery.fragment.SpFragment;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.utils.ViewFinder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private ViewPager mPager;
    private List<Integer> res = new ArrayList<>();
    private List<String> text = new ArrayList<>();
    private List<Integer> textSub = new ArrayList<>();
    private List<Integer> tipRes= new ArrayList<>();
    private LinearLayout ll;
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
    protected void initView() {

        mPager = (ViewPager) findViewById(R.id.wel_page);
        ll = (LinearLayout) findViewById(R.id.ll);
        mPager.setOnPageChangeListener(new ViewPagerIndicator(this, mPager, ll, 3));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String token = (String) SPDataSource.get(this,SPDataSource.USER_TOKEN,"");
        if(!TextUtils.isEmpty(token)){
            // 已经登录过的，自动进入
            startActivity(MainActivity.class, true);
            return;
        }
        res.add(R.mipmap.bg1);
        res.add(R.mipmap.sp2);
        res.add(R.mipmap.sp3);
        text.add("产品目录");
        text.add("购买电池");
        text.add("绑定电池");
        textSub.add(R.string.sp1_text_sub);
        textSub.add(R.string.sp2_text_sub);
        textSub.add(R.string.sp3_text_sub);
        tipRes.add(R.mipmap.sp1_4);
        tipRes.add(R.mipmap.sp2_7);
        tipRes.add(R.mipmap.sp3_9);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SpFragment spFragment = SpFragment.newInstance(res.get(i),text.get(i),textSub.get(i),tipRes.get(i));
            fragments.add(spFragment);
        }
        mPager.setAdapter(new SpalashAdapter(getSupportFragmentManager(),fragments));
    }


    @Override
    protected void setEvent() {
        //mHandler.sendEmptyMessage(0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }
}
