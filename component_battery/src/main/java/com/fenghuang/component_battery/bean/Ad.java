package com.fenghuang.component_battery.bean;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/8/20 14:59
 */
public class Ad implements Serializable{
    public long id;
    public String advertisingjpg;
    public String advertisingurl;
    public int advertisingstatus;

    @Override
    public String toString() {
        return "Ad{" +
                "id=" + id +
                ", advertisingjpg='" + advertisingjpg + '\'' +
                ", advertisingurl='" + advertisingurl + '\'' +
                ", advertisingstatus=" + advertisingstatus +
                '}';
    }
}
