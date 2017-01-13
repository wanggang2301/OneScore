package com.hhly.mlottery.bean.focusAndPush;

import java.util.List;

/**
 * 描    述：足球或者篮球的请求回来的数据实体类
 * 作    者：mady@13322.com
 * 时    间：2016/10/17
 */
public class BasketballConcernListBean {
    String result;
    List<String> concerns;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getConcerns() {
        return concerns;
    }

    public void setConcerns(List<String> concerns) {
        this.concerns = concerns;
    }
}
