package com.fenghuang.component_base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.fenghuang.component_base.R;
import com.fenghuang.component_base.data.CacheDataSource;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.utils.ActivityStackManager;
import com.fenghuang.component_base.utils.HandlerUtils;
import com.fenghuang.component_base.utils.StatusBarUtil;
import com.fenghuang.component_base.view.ProgressAlertDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;



/**
 * Create by wangchao on 2018/7/18 10:20
 */
public abstract class  BaseActivity extends RxAppCompatActivity implements HandlerUtils.OnReceiveMessageListener {
    private static final String TAG = "BaseActivity";

    public Handler mHandler;
    /**
     * context
     **/
    protected Context mContext;

    /**
     * 初始化界面
     **/
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 绑定事件
     */
    protected abstract void setEvent();

    protected abstract int getLayoutId();

    private ArrayList<LazyLoadFragment> fragments;// back fragment list.
    private LazyLoadFragment fragment;// current fragment.

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this,R.color.basic_white1);
        }
        if (savedInstanceState != null) {
            CacheDataSource.restoreData(savedInstanceState);
        }
        mHandler = new HandlerUtils.HandlerHolder(this);
        Log.i(TAG, "--->onCreate()");
        mContext = this;
        setContentView(getLayoutId());
        initView();
        initData(savedInstanceState);
        setEvent();

        ActivityStackManager.getInstance().addActivity(this);
    }

    public ArrayList<LazyLoadFragment> getFragments() {
        return fragments;
    }


    /**
     * set current fragment.
     */
    private void setFragment() {
        if (fragments != null && fragments.size() > 0) {
            fragment = fragments.get(fragments.size() - 1);
        } else {
            fragment = null;
        }
    }

    /**
     * 开启activity
     *
     * @param cls                  要打开的activity
     * @param closeCurrentActivity 是否需要关闭当前页面
     */
    protected void startActivity(Class<? extends Activity> cls, boolean closeCurrentActivity) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (closeCurrentActivity) {
            finish();
        }
    }

    protected void addOnClickListeners(@IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                findViewById(id).setOnClickListener((View.OnClickListener) this);
            }
        }
    }



    /**
     * get the current fragment.
     *
     * @return current fragment
     */
    public LazyLoadFragment getFirstFragment() {
        return fragment;
    }

    /**
     * get amount of fragment.
     *
     * @return amount of fragment
     */
    public int getFragmentNum() {
        return fragments != null ? fragments.size() : 0;
    }

    /**
     * clear fragment list
     */
    protected void clearFragments() {
        if (fragments != null) {
            fragments.clear();
        }
    }

    /**
     * remove previous fragment
     */
    private void removePrevious() {
        if (fragments != null && fragments.size() > 0) {
            fragments.remove(fragments.size() - 1);
        }
    }



    /**
     * remove all fragment from back stack.
     */
    protected void removeAllStackFragment() {
        clearFragments();
        setFragment();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * 跳转Activity
     * skip Another Activity
     *
     * @param activity
     * @param cls
     */
    public static void skipAnotherActivity(Activity activity,
                                           Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
    }


    /**
     * 退出应用
     * called while exit app.
     */
    public void exitLogic() {
        ActivityStackManager.getInstance().finishAllActivity();//remove all activity.
        removeAllStackFragment();
        System.exit(0);//system exit.
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--->onDestroy()");
        ActivityStackManager.getInstance().removeActivity(this);
    }

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handlerMessage(Message msg) {

    }
    public  ProgressAlertDialog alertDialog;
    public void showLoadingDialog() {
        if(alertDialog == null){
            alertDialog = new ProgressAlertDialog(this);
        }
        if(!isFinishing()){
            alertDialog.show();
        }
    }

    public void dimissLoadingDialog(){
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

    public boolean isDialogShowing(){
        if(alertDialog != null){
            if(alertDialog.isShowing()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }


}
