package com.hhly.mlottery.bean.homepagerentity;

/**
 * desc:专家专栏——专家名单实体类_单个人名
 * Created by 107_tangrr on 2017/1/6 0006.
 */

public class HomeHeadContent {

    private int expertId;// 专家id
    private String title;// 标题
    private String isExpert;
    private String icon;// 图标

    public int getExpertId() {
        return expertId;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsExpert() {
        return isExpert;
    }

    public void setIsExpert(String isExpert) {
        this.isExpert = isExpert;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
