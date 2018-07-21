package com.fenghuang.componentm_mall;

import android.os.Bundle;
import android.view.View;

import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.componentm_mall.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/21 09:53
 */
public class ProductFragment extends LazyLoadFragment {
    private Product mProduct;

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_product;
    }

    @Override
    protected void lazyLoad() {

    }
}
