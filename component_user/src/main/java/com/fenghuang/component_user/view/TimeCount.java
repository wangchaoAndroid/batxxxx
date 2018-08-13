package com.fenghuang.component_user.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.fenghuang.component_user.R;

/**
 * Create by wangchao on 2018/8/13 10:24
 */
public class TimeCount extends CountDownTimer {
    private TextView tvCode;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);
        this.tvCode = tv;
    }

    @Override
    public void onTick(long millisUntilFinished) {
       //tvCode.setBackgroundResource(R.drawable.regist_suc);
        tvCode.setTextSize(13);
        tvCode.setText(millisUntilFinished / 1000 + "秒");
        tvCode.setClickable(false);
    }

    @Override
    public void onFinish() {
      //  tvCode.setBackgroundResource(R.drawable.regist_suc);
        tvCode.setTextSize(13);
        tvCode.setText("h");
        tvCode.setClickable(true);
    }
}

