package com.hhly.mlottery.bean.homepagerentity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by hhly107 on 2016/4/6.
 */
public class HomeBodysEntity implements Serializable {

    private int jumpType;// 跳转类型	 0无 1页面 2跳内页
    private String jumpAddr;// 跳转位置
    private String thirdId;// 足球赛事id
    private String racename;// 比赛名称
    private String raceId;// 比赛ID
    private String raceColor;// 赛名标题颜色
    private String date;// 比赛日期
    private String time;// 比赛时间
    private String homeId;// 足球主队ID
    private String hometeam;// 主队队名
    private String guestId;// 足球客队ID
    private String guestteam;// 客队队名
    private String statusOrigin;// 足球状态
    private String homeScore;// 主队比分
    private String guestScore;// 客队比分
    private String homeHalfScore;// 主队半场比分
    private String guestHalfScore;// 客队半场比分
    private String homeLogoUrl;// 主队图标URL
    private String guestLogoUrl;// 客队图标URL

    private String leagueId;// 篮球赛事id
    private String leagueName;// 篮球比赛名称
    private String leagueColor;// 篮球名称标题颜色
    private int matchStatus;// 篮球状态
    private String homeTeamId;// 篮球主队ID
    private String guestTeamId;// 篮球客队ID

    private String name;// 彩票彩种
    private String issue;// 彩票期号
    private String numbers;// 彩票号码
    private String zodiac;// 彩票生肖
    private String picUrl;// 图标URL

    private String title;// 资讯标题
    private String relateMatch;// 是否有关联赛事
    private int type;// 关联赛事类型 1篮球，2足球
    private String infoTypeName;// 资讯类型
    private String summary;// 资讯摘要

    //产品建议的字段
    private String osName;
    private String osVersion;
    private String deviceBrand;
    private String deviceModel;
    private String sendTime;
    private String content;
    private String deviceToken;
    private String appVersion;
    private String userId;
    private String replyContent;
    private String id;

    // 专家专栏
    private int infoId;
    private String subTitle;// 副标题
    private String infoUrl;// 资讯详情跳转地址
    private String infoSource;// 来源信息
    private String lastModifyDate;// 最后修改日期
    private String lastModifyTime;// 最后修改时间
    private String infoType;
    private String isvideoNews;// 是否是资讯视频
    private String videoUrl;// 视频资讯地址
    private int matchType;// 比赛类型 0是足球,1是篮球
    private String expertId;// 专家id

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(String infoSource) {
        this.infoSource = infoSource;
    }

    public String getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(String lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getIsvideoNews() {
        return isvideoNews;
    }

    public void setIsvideoNews(String isvideoNews) {
        this.isvideoNews = isvideoNews;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getMatchType() {
        return matchType;
    }

    public void setMatchType(int matchType) {
        this.matchType = matchType;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    private String replyTime;
    private String nickName;
    private String userImg;
    private int likes;

    private List<HomeBodysLottery> lottery;// 新彩票条目

    public List<HomeBodysLottery> getLottery() {
        return lottery;
    }

    public void setLottery(List<HomeBodysLottery> lottery) {
        this.lottery = lottery;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getInfoTypeName() {
        return infoTypeName;
    }

    public void setInfoTypeName(String infoTypeName) {
        this.infoTypeName = infoTypeName;
    }

    public String getRelateMatch() {
        return relateMatch;
    }

    public void setRelateMatch(String relateMatch) {
        this.relateMatch = relateMatch;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public String getJumpAddr() {
        return jumpAddr;
    }

    public void setJumpAddr(String jumpAddr) {
        this.jumpAddr = jumpAddr;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getRacename() {
        return racename;
    }

    public void setRacename(String racename) {
        this.racename = racename;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceColor() {
        return raceColor;
    }

    public void setRaceColor(String raceColor) {
        this.raceColor = raceColor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getHometeam() {
        return hometeam;
    }

    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getGuestteam() {
        return guestteam;
    }

    public void setGuestteam(String guestteam) {
        this.guestteam = guestteam;
    }

    public String getStatusOrigin() {
        return statusOrigin;
    }

    public void setStatusOrigin(String statusOrigin) {
        this.statusOrigin = statusOrigin;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getGuestScore() {
        return guestScore;
    }

    public void setGuestScore(String guestScore) {
        this.guestScore = guestScore;
    }

    public String getHomeHalfScore() {
        return homeHalfScore;
    }

    public void setHomeHalfScore(String homeHalfScore) {
        this.homeHalfScore = homeHalfScore;
    }

    public String getGuestHalfScore() {
        return guestHalfScore;
    }

    public void setGuestHalfScore(String guestHalfScore) {
        this.guestHalfScore = guestHalfScore;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public String getGuestLogoUrl() {
        return guestLogoUrl;
    }

    public void setGuestLogoUrl(String guestLogoUrl) {
        this.guestLogoUrl = guestLogoUrl;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueColor() {
        return leagueColor;
    }

    public void setLeagueColor(String leagueColor) {
        this.leagueColor = leagueColor;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getGuestTeamId() {
        return guestTeamId;
    }

    public void setGuestTeamId(String guestTeamId) {
        this.guestTeamId = guestTeamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
