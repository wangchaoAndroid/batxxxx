package com.fenghuang.component_base.download;

/**
 * Create by wangchao on 2017/12/13 09:22
 */
public  interface ProgressListener  {
    void onProgress(long progress, long total, boolean done);
}
