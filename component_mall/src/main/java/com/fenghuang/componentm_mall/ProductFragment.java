package com.fenghuang.componentm_mall;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.tool.ImageLoader;
import com.fenghuang.componentm_mall.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/21 09:53
 */
public class ProductFragment extends LazyLoadFragment {
    private Product mProduct;
    private ImageView good_iv;
    private TextView doc_category,doc_buy_method,doc_pay_one,doc_pay_two;

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        doc_category = view.findViewById(R.id.doc_category);
        doc_buy_method = view.findViewById(R.id.doc_buy_method);
        doc_pay_one = view.findViewById(R.id.doc_pay_one);
        doc_pay_two = view.findViewById(R.id.doc_pay_two);
        good_iv = view.findViewById(R.id.good_iv);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_product;
    }

    @Override
    protected void lazyLoad() {
        Bundle arguments = getArguments();
        mProduct = (Product) arguments.getSerializable("product");
        if(mProduct != null){
            doc_category.setText(mProduct.describe1 + "");
            doc_buy_method.setText(mProduct.describe2 + "");
            doc_pay_one.setText(mProduct.describe3 + "");
            doc_pay_two.setText(mProduct.describe4 + "");
            ImageLoader.load(mProduct.img,good_iv);
        }
    }
}
