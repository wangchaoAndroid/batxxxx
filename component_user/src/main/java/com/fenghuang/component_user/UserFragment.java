package com.fenghuang.component_user;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Range;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.ActivityStackManager;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.component_user.login.LoginActivity;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author billy.qi
 * @since 17/12/8 15:30
 */
public class UserFragment extends LazyLoadFragment {

    private TextView tv_name,setting_tv;
    private ImageView avatar;
    NetServices mNetServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    @Override
    protected void init(View view,Bundle savedInstanceState) {

        tv_name = view.findViewById(R.id.tv_name);
        setting_tv = view.findViewById(R.id.setting_tv);
        avatar = view.findViewById(R.id.avatar);

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected void lazyLoad() {
        getNetUserInfo();

        addOnClickListeners(R.id.setting_tv,R.id.warn_info,R.id.feed_back,R.id.avatar);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        int id = view.getId();
        if(id == R.id.setting_tv){
            startActivity(new Intent(getActivity(),SettingActivity.class));
        }else if(id == R.id.warn_info){
            if(toLoginForToken()){
                CC.obtainBuilder("component_battery")
                        .setActionName("getWarnInfo")
                        .build()
                        .call();
            }

        }else if(id == R.id.feed_back){
            if(toLoginForToken()){
                startActivity(new Intent(getActivity(),FeedBackActivity.class));
            }
        }else if(id == R.id.avatar){
            String token1 = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
            if(TextUtils.isEmpty(token1)){
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }

        }
    }

    public boolean toLoginForToken(){
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            CCResult ccResult = CC.obtainBuilder("component_user")
                    .setContext(getActivity())
                    .setActionName("toLoginActivityForToken")
                    .build()
                    .call();
            String data = ccResult.getDataItem(SPDataSource.USER_TOKEN);
            if(!TextUtils.isEmpty(data)){
                getNetUserInfo();
                return true;
            }
            return false;

        }
        return true;
    }


    public void getNetUserInfo(){
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        mNetServices.getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery<LoginModel>>() {
                    @Override
                    public void onSuccess(BaseEntery<LoginModel> value) {
                        LoginModel loginModel = value.obj;
                        if(loginModel != null){
                            tv_name.setText(loginModel.nickName + "");
                        }

                    }

                    @Override
                    public void onFailture(String e) {

                    }
                });

    }



}
