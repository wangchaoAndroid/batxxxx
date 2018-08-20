package com.fenghuang.component_user;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Create by wangchao on 2018/8/20 17:03
 */
public class RangeWrapper implements IPickerViewData{
    public String range;
    public int rangeMeter;

    public RangeWrapper(String range, int rangeMeter) {
        this.range = range;
        this.rangeMeter = rangeMeter;
    }

    @Override
    public String getPickerViewText() {
        return range;
    }
}
