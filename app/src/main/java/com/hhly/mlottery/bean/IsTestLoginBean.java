package com.hhly.mlottery.bean;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:  检查第三方登录的是提bean
 * @data: 2016/9/19 9:48
 */
public class IsTestLoginBean {

    /**
     * result : 200
     * data : {"qq":1,"weChat":1,"weibo":1}
     */

    public int result;
    /**
     * qq : 1
     * weChat : 1
     * weibo : 1
     */

    public DataBean data;

    public static class DataBean {
        public int qq;
        public int weChat;
        public int weibo;
    }
}
