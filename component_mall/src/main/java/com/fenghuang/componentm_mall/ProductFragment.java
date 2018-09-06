package com.fenghuang.componentm_mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.bumptech.glide.Glide;
import com.fenghuang.component_base.base.LazyLoadFragment;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.helper.GlideRoundTransform;
import com.fenghuang.component_base.utils.FragmentUtils;
import com.fenghuang.componentm_mall.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by wangchao on 2018/7/21 09:53
 */
public class ProductFragment extends LazyLoadFragment implements View.OnClickListener {
    private Product mProduct;
    private ImageView good_iv;
    private TextView doc_category,doc_buy_method,doc_pay_one,doc_pay_two,btn_buy;

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        doc_category = view.findViewById(R.id.doc_category);
        doc_buy_method = view.findViewById(R.id.doc_buy_method);
        doc_pay_one = view.findViewById(R.id.doc_pay_one);
        doc_pay_two = view.findViewById(R.id.doc_pay_two);
        good_iv = view.findViewById(R.id.good_iv);
        btn_buy = view.findViewById(R.id.btn_buy);
        addOnClickListeners(R.id.btn_buy);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_product;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        String token = (String) SPDataSource.get(getActivity(),SPDataSource.USER_TOKEN,"");
        if(TextUtils.isEmpty(token)){
            CCResult ccResult = CC.obtainBuilder("component_user")
                    .setContext(getActivity())
                    .setActionName("toLoginActivityForToken")
                    .build()
                    .call();
            String data = ccResult.getDataItem(SPDataSource.USER_TOKEN);
            if(!TextUtils.isEmpty(data)){
//                getHomeInfo();
            }
            return;
        }
        int id = view.getId();
        if(id == R.id.btn_buy){
            startActivity(new Intent(getActivity(),BuyActivity.class));
        }
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
            Glide.with(getActivity())
                    .load(mProduct.img)
                    .transform(new GlideRoundTransform(getActivity())).into(good_iv);
        }
    }
}
