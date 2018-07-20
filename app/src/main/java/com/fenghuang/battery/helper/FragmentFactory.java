package com.fenghuang.battery.helper;

import android.util.Log;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.component_base.base.LazyLoadFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by wangchao on 2018/7/18 10:16
 */
public class FragmentFactory {
    private static Map<Integer,LazyLoadFragment> fragMap = new HashMap<>();

    public static LazyLoadFragment createFragment(int pos) {
        LazyLoadFragment fragment = fragMap.get(pos);
        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = CC.obtainBuilder("component_battery")
                            .setActionName("getLifecycleFragment")
                            .build().call().getDataItem("fragment");
                    break;
                case 1:
                    fragment = CC.obtainBuilder("component_map")
                            .setActionName("getLifecycleFragment")
                            .build().call().getDataItem("fragment");

                    break;
                case  2:
                    fragment = CC.obtainBuilder("component_mall")
                            .setActionName("getLifecycleFragment")
                            .build().call().getDataItem("fragment");
                    break;
                case  3:
                    fragment = CC.obtainBuilder("component_user")
                            .setActionName("getLifecycleFragment")
                            .build().call().getDataItem("fragment");
                    break;
                default:
                    break;
            }
            fragMap.put(pos,fragment);
        }
        return fragment;
    }
}

