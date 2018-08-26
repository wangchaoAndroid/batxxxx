package com.fenghuang.component_user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.tool.RxToast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener{
    EditText qaq_describe;
    NetServices mNetServices = RetrofitManager.getInstance().initRetrofit().create(NetServices.class);
    @Override
    protected void initView() {
        qaq_describe = findViewById(R.id.qaq_describe);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setEvent() {
        addOnClickListeners(R.id.btn_commit);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_commit){
            commit();
        }
    }

    public void commit(){
        String token = UserManager.getToken();
        if(TextUtils.isEmpty(token)){
            return;
        }
        String content =  qaq_describe.getText().toString().trim();
        mNetServices.commitDescribe(token,content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseCallback<BaseEntery>() {
                    @Override
                    public void onSuccess(BaseEntery value) {
                        RxToast.showToast("提交成功");
                        FeedBackActivity.this.finish();
                    }

                    @Override
                    public void onFailture(String e) {
                        RxToast.error(e + "");
                    }
                });
    }
}
