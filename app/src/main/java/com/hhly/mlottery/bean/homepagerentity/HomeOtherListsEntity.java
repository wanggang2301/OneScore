package com.hhly.mlottery.bean.homepagerentity;

import java.io.Serializable;

/**
 * 首页其它条目对象
 * Created by hhly107 on 2016/4/6.
 */
public class HomeOtherListsEntity implements Serializable {

    private HomeContentEntity content;
    private int result;

    public HomeContentEntity getContent() {
        return content;
    }

    public void setContent(HomeContentEntity content) {
        this.content = content;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
