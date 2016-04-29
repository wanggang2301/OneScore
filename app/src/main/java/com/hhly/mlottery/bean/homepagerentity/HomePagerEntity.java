package com.hhly.mlottery.bean.homepagerentity;

import java.io.Serializable;
import java.util.List;

/**
 * 首页实体对象
 * Created by hhly107 on 2016/4/6.
 */
public class HomePagerEntity implements Serializable {

    private int result;
    private HomeMenusEntity menus;
    private HomeBannersEntity banners;
    private List<HomeOtherListsEntity> otherLists;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public HomeMenusEntity getMenus() {
        return menus;
    }

    public void setMenus(HomeMenusEntity menus) {
        this.menus = menus;
    }

    public HomeBannersEntity getBanners() {
        return banners;
    }

    public void setBanners(HomeBannersEntity banners) {
        this.banners = banners;
    }

    public List<HomeOtherListsEntity> getOtherLists() {
        return otherLists;
    }

    public void setOtherLists(List<HomeOtherListsEntity> otherLists) {
        this.otherLists = otherLists;
    }
}
