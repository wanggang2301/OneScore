package com.hhly.mlottery.bean.homepagerentity;

import java.io.Serializable;
import java.util.List;

/**
 * 首页轮播图对象
 * Created by hhly107 on 2016/4/6.
 */
public class HomeBannersEntity implements Serializable {
    private int result;
    private List<HomeContentEntity> content;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<HomeContentEntity> getContent() {
        return content;
    }

    public void setContent(List<HomeContentEntity> content) {
        this.content = content;
    }
}
