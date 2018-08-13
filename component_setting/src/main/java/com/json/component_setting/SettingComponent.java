package com.json.component_setting;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;

/**
 * Create by wangchao on 2018/8/13 10:47
 */
public class SettingComponent implements IComponent {
    @Override
    public String getName() {
        return "component_setting";
    }

    @Override
    public boolean onCall(CC cc) {

        return false;
    }
}
