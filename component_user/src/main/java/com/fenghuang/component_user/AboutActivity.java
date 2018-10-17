package com.fenghuang.component_user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenghuang.component_base.base.BaseActivity;
import com.fenghuang.component_base.download.DownloadManager;
import com.fenghuang.component_base.download.IAppUtil;
import com.fenghuang.component_base.utils.CommonUtil;


public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private TextView mVersionTxt,upgrade_txt;
    private ImageView share_icon;
    public static void startActivity(Context context){
        Intent intent = new Intent(context,AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onClick(View v) {
        if (CommonUtil.isFastDoubleClick()){
            return;
        }
        int i = v.getId();
        if (i == R.id.back) {
            finish();

        }
    }

    @Override
    protected void initView() {
        findViewById(R.id.back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title);
        upgrade_txt = (TextView) findViewById(R.id.upgrade_txt);
        title.setText(R.string.about);

        mVersionTxt = (TextView)findViewById(R.id.version_txt);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String versionName = IAppUtil.getVersionName(this);
        String displayTxt = String.format(getString(R.string.local_version), versionName);
        mVersionTxt.setText(displayTxt);
        //share_icon.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setEvent() {
        upgrade_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.getInstance().upgrade(AboutActivity.this,false);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

}
