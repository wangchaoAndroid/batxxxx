package com.fenghuang.componentm_mall.camera;

import android.os.Bundle;
import android.view.View;

import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.R;

/**
 * Create by wangchao on 2018/7/18 19:45
 */
public class InputNunFragment extends LazyLoadFragment {

    @Override
    protected void init(View view,Bundle savedInstanceState) {
        addOnClickListeners(R.id.top_back);
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
        }
    }
}
