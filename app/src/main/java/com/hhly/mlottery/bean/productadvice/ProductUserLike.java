package com.hhly.mlottery.bean.productadvice;

/**
 * 描    述：产品建议点赞接口
 * 作    者：mady@13322.com
 * 时    间：2016/12/19
 */
public class ProductUserLike {
    /**
     * 500表示已经点赞过
     * 200表示点赞成功
     * 2000表示报错
     */
    int  result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
