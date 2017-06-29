package com.hhly.mlottery.bean.bettingbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by：XQyi on 2017/6/3 16:45
 * Use: 推介列表实体bean
 */
public class BettingListDataBean {
//    promotionList: {},
//    code: "200",
//    leagueNames: []

    private PromotionData promotionList;
    private List<LeagueNameData> leagueNames;
    private int code;
    private Integer hasPlay;


    public static class PromotionData{

//        pageNum: 1,
//        pageSize: 2,
//        size: 2,
//        orderBy: null,
//        startRow: 1,

//        endRow: 2,
//        total: 4,
//        pages: 2,
//        list: [],
//        firstPage: 1,

//        prePage: 0,
//        nextPage: 2,
//        lastPage: 2,
//        isFirstPage: true,
//        isLastPage: false,

//        hasPreviousPage: false,
//        hasNextPage: true,
//        navigatePages: 8,
//        navigatepageNums: []

        private String pageNum;
        private String pageSize;
        private String size;
        private String orderBy;
        private String startRow;

        private String endRow;
        private String total;
        private String pages;
        private List<BettingListData> list;
        private String firstPage;

        private String prePage;
        private String nextPage;
        private String lastPage;
        private String isFirstPage;
        private String isLastPage;

        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private String navigatePages;
        private List<Integer> navigatepageNums;

        public static class BettingListData implements Serializable{
//            id: "645",
//            enable: null,
//            remark: null,
//            createBy: null,
//            createTime: null,
//            updateBy: null,
//            updateTime: null,
//            title: "横滨FC&nbsp;VS&nbsp;雷法山口",
//            choose: null,
//            context: "",
//            choose1: null,
//            price: 1,
//            userid: "HHLY00000136",
//            matchid: 848851980,
//            leagueid: null,
//            type: 0,
//            status: 0,
//            createtime: 1496475797877,
//            updatetime: null,
//            oddsid: null,
//            nickname: "帕尔梅拉斯作客1:1战平土库曼竞技",
//            orderStatus: null,
//            orderUserId: null,
//            leagueName: "日职乙",
//            homeName: "横滨FC",
//            guestName: "雷法山口",
//            winPoint: 1,
//            errPoint: 5,
//            serNum: "周六007",
//            matchId: "848851980",
//            levels: null,
//            isExpert: 1,
//            photoUrl: "http://tp.1332255.com/img/2017-05-16/af2d1032-b9e2-49b6-8791-bd828336419a.jpg",
//            releaseDate: "2017-06-03 15:17",
//            matchDateTime: 1496480400000,
//            expert: "白银专家",
//            expertImg: "",
//            typeStr: "竞彩单关",
//            lookStatus: 2,
//            detailUrl: "http://tj.1332255.com/tjDetail/totjDetail?id=645"

//            earningsRate: "0"

            private String id;
            private String enable;
            private String remark;
            private String createBy;
            private String createTime;
            private String updateBy;
            private String updateTime;
            private String title;
            private String choose;
            private String context;

            private String choose1;
            private String price;
            private String userid;
            private String matchid;
            private String leagueid;
            private String type;
            private String status;
            private String createtime;
            private String updatetime;
            private String oddsid;

            private String nickname;
            private String orderStatus;
            private String orderUserId;
            private String leagueName;
            private String homeName;
            private String guestName;
            private String winPoint;
            private String errPoint;
            private String serNum;
            private String matchId;

            private String levels;
            private String isExpert;
            private String photoUrl;
            private String releaseDate;
            private String matchDateTime;
            private String expert;
            private String expertImg;
            private String typeStr;
            private String lookStatus;
            private String detailUrl;
            private String countOrder;
            private String earningsRate;

            public String getEarningsRate() {
                return earningsRate;
            }

            public void setEarningsRate(String earningsRate) {
                this.earningsRate = earningsRate;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEnable() {
                return enable;
            }

            public void setEnable(String enable) {
                this.enable = enable;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(String updateBy) {
                this.updateBy = updateBy;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getChoose() {
                return choose;
            }

            public void setChoose(String choose) {
                this.choose = choose;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getChoose1() {
                return choose1;
            }

            public void setChoose1(String choose1) {
                this.choose1 = choose1;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getMatchid() {
                return matchid;
            }

            public void setMatchid(String matchid) {
                this.matchid = matchid;
            }

            public String getLeagueid() {
                return leagueid;
            }

            public void setLeagueid(String leagueid) {
                this.leagueid = leagueid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public String getOddsid() {
                return oddsid;
            }

            public void setOddsid(String oddsid) {
                this.oddsid = oddsid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public String getOrderUserId() {
                return orderUserId;
            }

            public void setOrderUserId(String orderUserId) {
                this.orderUserId = orderUserId;
            }

            public String getLeagueName() {
                return leagueName;
            }

            public void setLeagueName(String leagueName) {
                this.leagueName = leagueName;
            }

            public String getHomeName() {
                return homeName;
            }

            public void setHomeName(String homeName) {
                this.homeName = homeName;
            }

            public String getGuestName() {
                return guestName;
            }

            public void setGuestName(String guestName) {
                this.guestName = guestName;
            }

            public String getWinPoint() {
                return winPoint;
            }

            public void setWinPoint(String winPoint) {
                this.winPoint = winPoint;
            }

            public String getErrPoint() {
                return errPoint;
            }

            public void setErrPoint(String errPoint) {
                this.errPoint = errPoint;
            }

            public String getSerNum() {
                return serNum;
            }

            public void setSerNum(String serNum) {
                this.serNum = serNum;
            }

            public String getMatchId() {
                return matchId;
            }

            public void setMatchId(String matchId) {
                this.matchId = matchId;
            }

            public String getLevels() {
                return levels;
            }

            public void setLevels(String levels) {
                this.levels = levels;
            }

            public String getIsExpert() {
                return isExpert;
            }

            public void setIsExpert(String isExpert) {
                this.isExpert = isExpert;
            }

            public String getPhotoUrl() {
                return photoUrl;
            }

            public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
            }

            public String getReleaseDate() {
                return releaseDate;
            }

            public void setReleaseDate(String releaseDate) {
                this.releaseDate = releaseDate;
            }

            public String getMatchDateTime() {
                return matchDateTime;
            }

            public void setMatchDateTime(String matchDateTime) {
                this.matchDateTime = matchDateTime;
            }

            public String getExpert() {
                return expert;
            }

            public void setExpert(String expert) {
                this.expert = expert;
            }

            public String getExpertImg() {
                return expertImg;
            }

            public void setExpertImg(String expertImg) {
                this.expertImg = expertImg;
            }

            public String getTypeStr() {
                return typeStr;
            }

            public void setTypeStr(String typeStr) {
                this.typeStr = typeStr;
            }

            public String getLookStatus() {
                return lookStatus;
            }

            public void setLookStatus(String lookStatus) {
                this.lookStatus = lookStatus;
            }

            public String getDetailUrl() {
                return detailUrl;
            }

            public void setDetailUrl(String detailUrl) {
                this.detailUrl = detailUrl;
            }

            public String getCountOrder() {
                return countOrder;
            }

            public void setCountOrder(String countOrder) {
                this.countOrder = countOrder;
            }
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public String getStartRow() {
            return startRow;
        }

        public void setStartRow(String startRow) {
            this.startRow = startRow;
        }

        public String getEndRow() {
            return endRow;
        }

        public void setEndRow(String endRow) {
            this.endRow = endRow;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPages() {
            return pages;
        }

        public void setPages(String pages) {
            this.pages = pages;
        }

        public List<BettingListData> getList() {
            return list;
        }

        public void setList(List<BettingListData> list) {
            this.list = list;
        }

        public String getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(String firstPage) {
            this.firstPage = firstPage;
        }

        public String getPrePage() {
            return prePage;
        }

        public void setPrePage(String prePage) {
            this.prePage = prePage;
        }

        public String getNextPage() {
            return nextPage;
        }

        public void setNextPage(String nextPage) {
            this.nextPage = nextPage;
        }

        public String getLastPage() {
            return lastPage;
        }

        public void setLastPage(String lastPage) {
            this.lastPage = lastPage;
        }

        public String getIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(String isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public String getIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(String isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public String getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(String navigatePages) {
            this.navigatePages = navigatePages;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }
    }
    public static class LeagueNameData implements Serializable {
//        key: "225889",
//        content: "阿甲",
//        leagueId: "2"
        private String key;
        private String leagueName;
        private String leagueId;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(String leagueId) {
            this.leagueId = leagueId;
        }
    }

    public PromotionData getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(PromotionData promotionList) {
        this.promotionList = promotionList;
    }

    public List<LeagueNameData> getLeagueNames() {
        return leagueNames;
    }

    public void setLeagueNames(List<LeagueNameData> leagueNames) {
        this.leagueNames = leagueNames;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Integer getHasPlay() {
        return hasPlay;
    }

    public void setHasPlay(Integer hasPlay) {
        this.hasPlay = hasPlay;
    }
}
