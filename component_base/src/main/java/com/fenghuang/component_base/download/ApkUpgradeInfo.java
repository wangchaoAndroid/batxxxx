package com.fenghuang.component_base.download;


import com.fenghuang.component_base.net.BaseEntery;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/1/15 18:13
 */
public class ApkUpgradeInfo  implements Serializable {

    public String updateUrl;
    public String appVersion;
    public String MD5;
    public long apkSize;


    @Override
    public String toString() {
        return "ApkUpgradeInfo{" +
                "updateUrl='" + updateUrl + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", MD5='" + MD5 + '\'' +
                ", apkSize=" + apkSize +
                '}';
    }
}
