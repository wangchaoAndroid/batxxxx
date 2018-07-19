package com.fenghuang.battery;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.fenghuang.battery.helper.BottomNavigationViewHelper;
import com.fenghuang.battery.helper.FragmentFactory;
import com.fenghuang.component_base.adapter.CommonFragmentAdapter;
import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.base.BaseFragment;
import com.fenghuang.component_base.utils.ViewFinder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;

    @Override
    public void initData() {
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(FragmentFactory.createFragment(0));
        fragments.add(FragmentFactory.createFragment(1));
        fragments.add(FragmentFactory.createFragment(2));
        fragments.add(FragmentFactory.createFragment(3));
        mViewPager.setAdapter(new CommonFragmentAdapter(getSupportFragmentManager(),fragments));
    }

    @Override
    protected void setEvent() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_tab1:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.item_tab2:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.item_tab3:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.item_tab4:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return true;

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        ViewFinder viewFinder = new ViewFinder(this);
        mBottomNavigationView = viewFinder.find(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mViewPager = viewFinder.find(R.id.vp_view);
    }

    private void addOnClickListeners(@IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                findViewById(id).setOnClickListener(this);
            }
        }
    }



    CC componentBCC;
    @Override
    public void onClick(View v) {
        CCResult result = null;
        CC cc = null;
        switch (v.getId()) {
//            case R.id.test_lifecycle:
//                CC.obtainBuilder("demo.lifecycle")
//                        .build()
//                        .callAsyncCallbackOnMainThread(printResultCallback);
//                break;
//            case R.id.componentAOpenActivity:
//                cc = CC.obtainBuilder(COMPONENT_NAME_A)
//                        .setActionName("showActivityA")
//                        .build();
//                result = cc.call();
//                break;
//            case R.id.componentAAsyncOpenActivity:
//                CC.obtainBuilder(COMPONENT_NAME_A)
//                        .setActionName("showActivityA")
//                        .build().callAsyncCallbackOnMainThread(printResultCallback);
//                break;
//            case R.id.componentAGetData:
//                cc = CC.obtainBuilder(COMPONENT_NAME_A)
//                        .setActionName("getInfo")
//                        .build();
//                result = cc.call();
//                break;
//            case R.id.componentAAsyncGetData:
//                CC.obtainBuilder(COMPONENT_NAME_A)
//                        .setActionName("getInfo")
//                        .addInterceptor(new MissYouInterceptor())
//                        .build().callAsyncCallbackOnMainThread(printResultCallback);
//                break;
//            case R.id.componentB:
//                cc = CC.obtainBuilder("ComponentB")
//                        .setActionName("showActivity")
//                        .build();
//                result = cc.call();
//                break;
//            case R.id.componentBAsync:
//                if (componentBCC != null) {
//                    componentBCC.cancel();
//                    Toast.makeText(this, R.string.canceled, Toast.LENGTH_SHORT).show();
//                } else {
//                    componentBCC = CC.obtainBuilder("ComponentB")
//                            .setActionName("getNetworkData")
//                            .build();
//                    componentBCC.callAsyncCallbackOnMainThread(new IComponentCallback() {
//                        @Override
//                        public void onResult(CC cc, CCResult ccResult) {
//                            componentBCC = null;
//                            showResult(cc, ccResult);
//                        }
//                    });
//                    Toast.makeText(this, R.string.clickToCancel, Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.componentBGetData:
//                cc = CC.obtainBuilder("ComponentB")
//                        .setActionName("getData")
//                        .build();
//                result = cc.call();
//                break;
//            case R.id.componentBLogin:
//                CC.obtainBuilder("ComponentB")
//                        .setActionName("login")
//                        .build()
//                        .callAsyncCallbackOnMainThread(printResultCallback);
//                break;
//            case R.id.componentKt:
//                CC.obtainBuilder("demo.ktComponent")
//                        .setActionName("showActivity")
//                        .build()
//                        .callAsyncCallbackOnMainThread(printResultCallback);
//                break;
            default:
                break;
        }
//        if (cc != null && result != null) {
//            showResult(cc, result);
//        }
    }
    IComponentCallback printResultCallback = new IComponentCallback() {
        @Override
        public void onResult(CC cc, CCResult result) {
            showResult(cc, result);
        }
    };
    private void showResult(CC cc, CCResult result) {
        String text = "result:\n" + JsonFormat.format(result.toString());
        text += "\n\n---------------------\n\n";
        text += "cc:\n" + JsonFormat.format(cc.toString());

    }

    @Override
    protected void onDestroy() {
        if (componentBCC != null) {
            componentBCC.cancel();
        }
        super.onDestroy();
    }
}
