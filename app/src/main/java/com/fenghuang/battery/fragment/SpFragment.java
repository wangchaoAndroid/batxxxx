package com.fenghuang.battery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.battery.MainActivity;
import com.fenghuang.battery.R;
import com.fenghuang.component_base.data.SPDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/8/10 13:49
 */
public class SpFragment extends Fragment implements View.OnClickListener {

    private static final String ARG = "res_id";
    private static final String ARG_STR = "text";
    private static final String ARG_STR_SUB = "textSub_id";
    private static final String ARG_TIP = "tip_id";
    private int mInt;
    private String mText;
    private int mTextSub;
    private int tipRes;
    private ImageView mIv;
    private TextView mtv;
    private TextView mSubTv;
    private TextView skip_buy;
    private ImageView iv_tip;

    public static SpFragment newInstance(int arg,String text,int arg_sub,int tipRes) {
        SpFragment fragment = new SpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG, arg);
        bundle.putInt(ARG_STR_SUB, arg_sub);
        bundle.putInt(ARG_TIP, tipRes);
        bundle.putString(ARG_STR, text);
        fragment.setArguments(bundle);
        return fragment;
    }

        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_item,null,false);
        mIv = rootView.findViewById(R.id.iv_sp);
        mtv = rootView.findViewById(R.id.tv_sp);
        mSubTv = rootView.findViewById(R.id.tv_sp_sub);
        skip_buy = rootView.findViewById(R.id.skip_buy);
        iv_tip = rootView.findViewById(R.id.iv_tip);
            skip_buy.setOnClickListener(this);
        initData();
        return rootView;
    }

    private void initData() {
        mInt = getArguments().getInt(ARG);
        tipRes = getArguments().getInt(ARG_TIP);
        mText = getArguments().getString(ARG_STR);
        mTextSub = getArguments().getInt(ARG_STR_SUB);
        mIv.setImageResource(mInt);
        mtv.setText(mText);
        mSubTv.setText(mTextSub);
        iv_tip.setImageResource(tipRes);
        if("绑定电池".equals(mText)){
            skip_buy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.skip_buy){
            boolean hasComponentUser = CC.hasComponent("component_user");
            if (hasComponentUser) {
                CCResult ccResult = CC.obtainBuilder("component_user")
                        .setContext(getActivity())
                        .setActionName("toLoginActivity")
                        .build()
                        .call();
//                String data = ccResult.getDataItem(SPDataSource.USER_TOKEN);
//                if(!TextUtils.isEmpty(data)){
//                    startActivity(MainActivity.class, true);
//                }
            } else {
                // 如果没有用户组件直接进入主界面
                startActivity(MainActivity.class, true);
            }
        }
    }

    /**
     * 开启activity
     *
     * @param cls                  要打开的activity
     * @param closeCurrentActivity 是否需要关闭当前页面
     */
    protected void startActivity(Class<? extends Activity> cls, boolean closeCurrentActivity) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        if (closeCurrentActivity) {
            getActivity().finish();
        }
    }
}
