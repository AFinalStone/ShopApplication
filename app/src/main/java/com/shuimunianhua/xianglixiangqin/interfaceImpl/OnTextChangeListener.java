package com.shuimunianhua.xianglixiangqin.interfaceImpl;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by SHI on 2016/9/12 19:52
 * EditText字段变动过程监听
 */
public abstract class OnTextChangeListener implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public abstract void afterTextChanged(Editable s);
}
