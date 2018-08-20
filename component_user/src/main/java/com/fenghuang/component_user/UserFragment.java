package com.fenghuang.component_user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Range;
import android.view.View;
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
    TextView mTextView;
    private TextView scan_tv;
    private TextView logoutTv;
    private Object mNearbyShop;
    NetServices netServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    @Override
    protected void init(View view,Bundle savedInstanceState) {
        mTextView = (TextView) view.findViewById(R.id.tv_name);
        scan_tv = (TextView) view.findViewById(R.id.scan_tv);
        logoutTv = view.findViewById(R.id.btn_logout);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected void lazyLoad() {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        addOnClickListeners(R.id.scan_tv,R.id.btn_logout,R.id.tv_nearbyShop);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.scan_tv) {
            CC.obtainBuilder("component_mall")
                    .setContext(getActivity())
                    .setActionName("showActivityA")
                    .build()
                    .call();
        } else if (id == R.id.btn_logout) {
            logout();
        } else if (id == R.id.tv_nearbyShop) {
            getNearbyShop();
        }
    }

    /**
     * 退出登录
     */
    public void logout(){
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        netServices.logout(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {
                        ILog.e( "logout" ,value + "");
                        //清除内存保存用户数据
                        UserManager.clearUserInfo();
                        //清除sp token
                        SPDataSource.put(getActivity(),SPDataSource.USER_TOKEN,"");
                        //注销信鸽
                        XGPushManager.unregisterPush(getActivity());
                        //退到登录界面
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                        ActivityStackManager.getInstance().finishAllActivity();
                    }

                    @Override
                    public void onFailture(String e) {
                        ILog.e( "logout" ,e + "");
                        RxToast.error(e);
                    }
                });
    }


    public void getNearbyShop() {
        showPickerView();
    }
    private List<RangeWrapper> ranges = new ArrayList<>();
    /**
     * 弹出选择器
     */
    private void showPickerView() {
        ranges.add(new RangeWrapper("500米内",500));
        ranges.add(new RangeWrapper("1000米内",1000));
        ranges.add(new RangeWrapper("2000米内",2000));
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if(ranges != null && !ranges.isEmpty()){
                    ILog.e(TAG,options1 + "---" + options2 + "---" + options3);
                    RangeWrapper rangeWrapper = ranges.get(options1);
                    int rangeMeter = rangeWrapper.rangeMeter;
                    Intent intent = new Intent(getActivity(), NeiboorhoorActivity.class);
                    intent.putExtra("range",rangeMeter);
                    startActivity(intent);
                }
            }
        })
                .setTitleText("范围")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(14)
                .build();

        pvOptions.setPicker(ranges);//一级选择器
        //pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
}
