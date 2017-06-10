package data.bean;

import java.util.List;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 文章推荐
 * @created on:2017/6/5  11:00.
 */

public class RecommendArticlesBean {

    /**
     * code : 200
     * publishPromotions : {"pageNum":1,"pageSize":10,"size":10,"orderBy":"d.createtime desc","startRow":1,"endRow":10,"total":54,"pages":6,"list":[{"id":"647","title":"1","context":"1","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848851360","matchDate":"06-03","matchTime":"21:30","leagueId":"22","type":0,"status":0,"createtime":1496486253738,"leagueName":"挪超","homeName":"维京","guestName":"瓦勒伦加","screening":"周六010","releaseTime":null,"orderStatus":null,"count":null,"income":"0","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"646","title":"1","context":"洛根利泰柠&nbsp;VS&nbsp;米切尔顿","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853523","matchDate":"06-03","matchTime":"16:00","leagueId":"774","type":1,"status":2,"createtime":1496475877067,"leagueName":"澳布超","homeName":"洛根利泰柠","guestName":"米切尔顿","screening":null,"releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"645","title":"横滨FC&nbsp;VS&nbsp;雷法山口","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848851980","matchDate":"06-03","matchTime":"17:00","leagueId":"284","type":0,"status":0,"createtime":1496475797877,"leagueName":"日职乙","homeName":"横滨FC","guestName":"雷法山口","screening":"周六007","releaseTime":null,"orderStatus":null,"count":null,"income":"0","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"643","title":"yyy","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853606","matchDate":"06-03","matchTime":"08:15","leagueId":"2","type":0,"status":2,"createtime":1496370021827,"leagueName":"阿甲","homeName":"戈多伊克鲁斯","guestName":"拉斐拉竞技","screening":"周五005","releaseTime":null,"orderStatus":0,"count":null,"income":"1.5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"642","title":"test","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848854172","matchDate":"06-03","matchTime":"06:00","leagueId":"2","type":0,"status":1,"createtime":1496368264276,"leagueName":"阿甲","homeName":"萨尔米安杜","guestName":"防卫者","screening":"周五004","releaseTime":null,"orderStatus":0,"count":null,"income":"1","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"641","title":"tt","context":"1","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853561","matchDate":"06-02","matchTime":"01:00","leagueId":"26","type":0,"status":2,"createtime":1496299632417,"leagueName":"瑞典超","homeName":"诺科平","guestName":"索尔纳","screening":"周四001","releaseTime":null,"orderStatus":0,"count":null,"income":"1","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"640","title":"维也纳快速&nbsp;VS&nbsp;萨尔茨堡","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848850963","matchDate":"06-02","matchTime":"02:30","leagueId":"96","type":2,"status":2,"createtime":1496283745657,"leagueName":"奥地利杯","homeName":"维也纳快速","guestName":"萨尔茨堡","screening":null,"releaseTime":null,"orderStatus":0,"count":null,"income":"1","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"639","title":"1","context":"诺科平&nbsp;VS&nbsp;索尔纳","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853561","matchDate":"06-02","matchTime":"01:00","leagueId":"26","type":2,"status":2,"createtime":1496283734107,"leagueName":"瑞典超","homeName":"诺科平","guestName":"索尔纳","screening":"周四001","releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"638","title":"1","context":"维也纳快速&nbsp;VS&nbsp;萨尔茨堡","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848850963","matchDate":"06-02","matchTime":"02:30","leagueId":"96","type":1,"status":2,"createtime":1496283193670,"leagueName":"奥地利杯","homeName":"维也纳快速","guestName":"萨尔茨堡","screening":null,"releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"637","title":"1","context":"库恩卡&nbsp;VS&nbsp;华恩斯体育生库恩卡&nbsp;VS&nbsp;华恩斯体育生","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848851731","matchDate":"06-02","matchTime":"08:45","leagueId":"263","type":0,"status":2,"createtime":1496283171576,"leagueName":"南球杯","homeName":"库恩卡","guestName":"奥利恩特","screening":"周四008","releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0}],"firstPage":1,"prePage":0,"nextPage":2,"lastPage":6,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6]}
     */

    private String code;
    private PublishPromotionsBean publishPromotions;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PublishPromotionsBean getPublishPromotions() {
        return publishPromotions;
    }

    public void setPublishPromotions(PublishPromotionsBean publishPromotions) {
        this.publishPromotions = publishPromotions;
    }

    public static class PublishPromotionsBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * orderBy : d.createtime desc
         * startRow : 1
         * endRow : 10
         * total : 54
         * pages : 6
         * list : [{"id":"647","title":"1","context":"1","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848851360","matchDate":"06-03","matchTime":"21:30","leagueId":"22","type":0,"status":0,"createtime":1496486253738,"leagueName":"挪超","homeName":"维京","guestName":"瓦勒伦加","screening":"周六010","releaseTime":null,"orderStatus":null,"count":null,"income":"0","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"646","title":"1","context":"洛根利泰柠&nbsp;VS&nbsp;米切尔顿","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853523","matchDate":"06-03","matchTime":"16:00","leagueId":"774","type":1,"status":2,"createtime":1496475877067,"leagueName":"澳布超","homeName":"洛根利泰柠","guestName":"米切尔顿","screening":null,"releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"645","title":"横滨FC&nbsp;VS&nbsp;雷法山口","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848851980","matchDate":"06-03","matchTime":"17:00","leagueId":"284","type":0,"status":0,"createtime":1496475797877,"leagueName":"日职乙","homeName":"横滨FC","guestName":"雷法山口","screening":"周六007","releaseTime":null,"orderStatus":null,"count":null,"income":"0","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"643","title":"yyy","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853606","matchDate":"06-03","matchTime":"08:15","leagueId":"2","type":0,"status":2,"createtime":1496370021827,"leagueName":"阿甲","homeName":"戈多伊克鲁斯","guestName":"拉斐拉竞技","screening":"周五005","releaseTime":null,"orderStatus":0,"count":null,"income":"1.5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"642","title":"test","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848854172","matchDate":"06-03","matchTime":"06:00","leagueId":"2","type":0,"status":1,"createtime":1496368264276,"leagueName":"阿甲","homeName":"萨尔米安杜","guestName":"防卫者","screening":"周五004","releaseTime":null,"orderStatus":0,"count":null,"income":"1","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"641","title":"tt","context":"1","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853561","matchDate":"06-02","matchTime":"01:00","leagueId":"26","type":0,"status":2,"createtime":1496299632417,"leagueName":"瑞典超","homeName":"诺科平","guestName":"索尔纳","screening":"周四001","releaseTime":null,"orderStatus":0,"count":null,"income":"1","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"640","title":"维也纳快速&nbsp;VS&nbsp;萨尔茨堡","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848850963","matchDate":"06-02","matchTime":"02:30","leagueId":"96","type":2,"status":2,"createtime":1496283745657,"leagueName":"奥地利杯","homeName":"维也纳快速","guestName":"萨尔茨堡","screening":null,"releaseTime":null,"orderStatus":0,"count":null,"income":"1","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"639","title":"1","context":"诺科平&nbsp;VS&nbsp;索尔纳","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853561","matchDate":"06-02","matchTime":"01:00","leagueId":"26","type":2,"status":2,"createtime":1496283734107,"leagueName":"瑞典超","homeName":"诺科平","guestName":"索尔纳","screening":"周四001","releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"638","title":"1","context":"维也纳快速&nbsp;VS&nbsp;萨尔茨堡","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848850963","matchDate":"06-02","matchTime":"02:30","leagueId":"96","type":1,"status":2,"createtime":1496283193670,"leagueName":"奥地利杯","homeName":"维也纳快速","guestName":"萨尔茨堡","screening":null,"releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0},{"id":"637","title":"1","context":"库恩卡&nbsp;VS&nbsp;华恩斯体育生库恩卡&nbsp;VS&nbsp;华恩斯体育生","choose":null,"price":1,"userId":"HHLY00000136","matchId":"848851731","matchDate":"06-02","matchTime":"08:45","leagueId":"263","type":0,"status":2,"createtime":1496283171576,"leagueName":"南球杯","homeName":"库恩卡","guestName":"奥利恩特","screening":"周四008","releaseTime":null,"orderStatus":0,"count":null,"income":".5","buyTime":null,"levels":null,"isEcpert":null,"nickName":null,"headImg":null,"winpointThreeDays":0,"errpointThreeDays":0}]
         * firstPage : 1
         * prePage : 0
         * nextPage : 2
         * lastPage : 6
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2,3,4,5,6]
         */

        private int pageNum;
        private int pageSize;

        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private List<ListBean> list;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
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


        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }


        public static class ListBean {
            /**
             * id : 647
             * title : 1
             * context : 1
             * choose : null
             * price : 1
             * userId : HHLY00000136
             * matchId : 848851360
             * matchDate : 06-03
             * matchTime : 21:30
             * leagueId : 22
             * type : 0
             * status : 0
             * createtime : 1496486253738
             * leagueName : 挪超
             * homeName : 维京
             * guestName : 瓦勒伦加
             * screening : 周六010
             * releaseTime : null
             * orderStatus : null
             * count : null
             * income : 0
             * buyTime : null
             * levels : null
             * isEcpert : null
             * nickName : null
             * headImg : null
             * winpointThreeDays : 0
             * errpointThreeDays : 0
             */

            private String id;
            private String title;
            private String context;
            private String choose;
            private int price;
            private String userId;
            private String matchId;
            private String matchDate;
            private String matchTime;
            private String leagueId;
            private int type;
            private int status;
            private long createtime;
            private String leagueName;
            private String homeName;
            private String guestName;
            private String screening;
            private String releaseTime;
            private String orderStatus;
            private String count;
            private String income;
            private String buyTime;
            private String levels;
            private String isEcpert;
            private String nickName;
            private String headImg;
            private int winpointThreeDays;
            private int errpointThreeDays;
            private String typeStr;

            public String getTypeStr() {
                return typeStr;
            }

            public void setTypeStr(String typeStr) {
                this.typeStr = typeStr;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getChoose() {
                return choose;
            }

            public void setChoose(String choose) {
                this.choose = choose;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getMatchId() {
                return matchId;
            }

            public void setMatchId(String matchId) {
                this.matchId = matchId;
            }

            public String getMatchDate() {
                return matchDate;
            }

            public void setMatchDate(String matchDate) {
                this.matchDate = matchDate;
            }

            public String getMatchTime() {
                return matchTime;
            }

            public void setMatchTime(String matchTime) {
                this.matchTime = matchTime;
            }

            public String getLeagueId() {
                return leagueId;
            }

            public void setLeagueId(String leagueId) {
                this.leagueId = leagueId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
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

            public String getScreening() {
                return screening;
            }

            public void setScreening(String screening) {
                this.screening = screening;
            }

            public String getReleaseTime() {
                return releaseTime;
            }

            public void setReleaseTime(String releaseTime) {
                this.releaseTime = releaseTime;
            }

            public String getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getIncome() {
                return income;
            }

            public void setIncome(String income) {
                this.income = income;
            }

            public String getBuyTime() {
                return buyTime;
            }

            public void setBuyTime(String buyTime) {
                this.buyTime = buyTime;
            }

            public String getLevels() {
                return levels;
            }

            public void setLevels(String levels) {
                this.levels = levels;
            }

            public String getIsEcpert() {
                return isEcpert;
            }

            public void setIsEcpert(String isEcpert) {
                this.isEcpert = isEcpert;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            public int getWinpointThreeDays() {
                return winpointThreeDays;
            }

            public void setWinpointThreeDays(int winpointThreeDays) {
                this.winpointThreeDays = winpointThreeDays;
            }

            public int getErrpointThreeDays() {
                return errpointThreeDays;
            }

            public void setErrpointThreeDays(int errpointThreeDays) {
                this.errpointThreeDays = errpointThreeDays;
            }
        }
    }
}
