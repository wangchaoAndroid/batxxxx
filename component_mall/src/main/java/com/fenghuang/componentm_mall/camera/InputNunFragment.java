package com.fenghuang.componentm_mall.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.R;

/**
 * Create by wangchao on 2018/7/18 19:45
 */
public class InputNunFragment extends LazyLoadFragment {
    private TextView btn_login;
    private TextInputEditText tiet_password;
    @Override
    protected void init(View view,Bundle savedInstanceState) {
        addOnClickListeners(R.id.top_back,R.id.btn_login);
        btn_login = view.findViewById(R.id.btn_login);
        tiet_password = view.findViewById(R.id.tiet_password);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_input_num;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if(id == R.id.top_back){
//            getActivity().finish();
            FragmentUtils.removeFragment(this);
        }else if(id == R.id.btn_login){
            String trim = tiet_password.getText().toString().trim();
            Intent intent = new Intent();
            if(TextUtils.isEmpty(trim)){
                RxToast.error(getResources().getString(R.string.please_input_num));
                return;
            }
            intent.putExtra("productNumber",trim);
            getActivity().setResult(Activity.RESULT_OK,intent);
            getActivity().finish();
        }
    }
}
