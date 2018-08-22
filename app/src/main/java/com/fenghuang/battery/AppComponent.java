package com.fenghuang.battery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.fenghuang.component_base.net.ILog;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/20 16:18
 */
public class AppComponent implements IComponent {
    @Override
    public String getName() {
        return "component_app";
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        switch (actionName){
            case "enterMain":
                startMainActivity(cc);
                break;
        }

        return false;
    }

    private void startMainActivity(CC cc) {
        Context context = cc.getContext();
        if(context instanceof Activity){
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
            CC.sendCCResult(cc.getCallId(), CCResult.success());
        }
    }
}
