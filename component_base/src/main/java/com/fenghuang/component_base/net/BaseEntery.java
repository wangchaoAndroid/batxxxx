package com.fenghuang.component_base.net;

import java.io.Serializable;

/**
 * Create by wangchao on 2018/8/13 09:18
 */
public class BaseEntery<T> implements Serializable {
    public int code;
    public String msg;
    public T obj;

    @Override
    public String toString() {
        return "BaseEntery{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                '}';
    }
}
