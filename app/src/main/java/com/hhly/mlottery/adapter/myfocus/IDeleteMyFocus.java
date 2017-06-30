package com.hhly.mlottery.adapter.myfocus;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/26  16:09.
 */

public interface IDeleteMyFocus {

    void deleteMyFocusGroup(int groupPosition);

    void deleteMyFocusChild(int groupPosition, int childPosition);
}
