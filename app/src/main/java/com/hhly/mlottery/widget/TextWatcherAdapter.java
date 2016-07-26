package com.hhly.mlottery.widget;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 适配器
 * <p>
 * Created by loshine on 2016/7/19.
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
