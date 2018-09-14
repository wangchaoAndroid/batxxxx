package com.fenghuang.component_base.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fenghuang.component_base.R;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.view.ProgressAlertDialog;

import java.util.logging.Logger;

import javax.annotation.Nullable;

/**
 * Fragment预加载问题的解决方案：
 * 1.可以懒加载的Fragment
 * 2.切换到其他页面时停止加载数据（可选）
 * Created by yuandl on 2016-11-17.
 * blog ：http://blog.csdn.net/linglongxin24/article/details/53205878
 */

public abstract class LazyLoadFragment extends Fragment implements View.OnClickListener {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "LazyLoadFragment";
    public View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(setContentView(), container, false);
        isInit = true;
        /**初始化的时候去加载数据**/
        init(rootView,savedInstanceState);
        ILog.e(TAG,"onCreateView");
        isCanLoadData();
        return rootView;
    }
    protected abstract void init(View view,Bundle savedInstanceState);
    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        ILog.e(TAG,"setUserVisibleHint");
        isCanLoadData();
    }

    protected void addOnClickListeners(@IdRes int... ids) {
        if (ids != null) {
            for (@IdRes int id : ids) {
                findViewById(id).setOnClickListener(this);
            }
        }
    }





    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        ILog.e(TAG,"isCanLoadData" + isInit);
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            ILog.e(TAG,"getUserVisibleHint" + true);
            lazyLoad();
            isLoad = true;
        } else {
            ILog.e(TAG,"getUserVisibleHint" + false);
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;

    }

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract int setContentView();

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return rootView;
    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }




    @Override
    public void onClick(View view) {

    }
    public  ProgressAlertDialog alertDialog;
    public void showLoadingDialog() {
        if(alertDialog == null){
            alertDialog = new ProgressAlertDialog(getActivity());
        }
        alertDialog.show();
    }

    public void dimissLoadingDialog(){
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }
}

//        4.用法
//        LazyLoadFragment是一个抽象类，可以作为BaseFragment,继承它。
//
//        (1).用setContentView()方法去加载要显示的布局
//
//        (2).lazyLoad()方法去加载数据
//
//        (3).stopLoad()方法可选，当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
//
//        package cn.bluemobi.dylan.viewpagerfragmentlazyload;
//
//        import android.util.Log;



//public class Fragment1 extends LazyLoadFragment {
//    @Override
//    public int setContentView() {
//        return R.layout.fm_layout1;
//    }
//
//    @Override
//    protected void lazyLoad() {
//        String message = "Fragment1" + (isInit ? "已经初始并已经显示给用户可以加载数据" : "没有初始化不能加载数据")+">>>>>>>>>>>>>>>>>>>";
//        showToast(message);
//        Log.d(TAG, message);
//    }
//
//    @Override
//    protected void stopLoad() {
//        Log.d(TAG, "Fragment1" + "已经对用户不可见，可以停止加载数据");
//    }
//}
