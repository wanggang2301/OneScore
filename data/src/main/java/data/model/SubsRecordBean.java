package data.model;

import java.util.List;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 訂閱記錄
 * @created on:2017/6/3  17:27.
 */

public class SubsRecordBean {

    /**
     * code : 200
     * purchaseRecords : {"pageNum":1,"pageSize":10,"size":1,"orderBy":"r.createtime desc","startRow":1,"endRow":1,"total":1,"pages":1,"list":[{"id":"643","title":"yyy","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853606","matchDate":"06-03","matchTime":"08:15","leagueId":"2","type":0,"status":2,"createtime":1496370021827,"leagueName":"阿甲","homeName":"戈多伊克鲁斯","guestName":"拉斐拉竞技","screening":"周五005","releaseTime":null,"orderStatus":0,"count":null,"income":null,"buyTime":1496392790822,"levels":null,"isEcpert":"1","nickName":"帕尔梅拉斯作客1:1战平土库曼竞技","headImg":"http://tp.1332255.com/img/2017-05-16/af2d1032-b9e2-49b6-8791-bd828336419a.jpg","winpointThreeDays":1,"errpointThreeDays":5}],"firstPage":1,"prePage":0,"nextPage":0,"lastPage":1,"isFirstPage":true,"isLastPage":true,"hasPreviousPage":false,"hasNextPage":false,"navigatePages":8,"navigatepageNums":[1]}
     */

    private String code;
    private PurchaseRecordsBean purchaseRecords;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PurchaseRecordsBean getPurchaseRecords() {
        return purchaseRecords;
    }

    public void setPurchaseRecords(PurchaseRecordsBean purchaseRecords) {
        this.purchaseRecords = purchaseRecords;
    }

    public static class PurchaseRecordsBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * size : 1
         * orderBy : r.createtime desc
         * startRow : 1
         * endRow : 1
         * total : 1
         * pages : 1
         * list : [{"id":"643","title":"yyy","context":null,"choose":null,"price":1,"userId":"HHLY00000136","matchId":"848853606","matchDate":"06-03","matchTime":"08:15","leagueId":"2","type":0,"status":2,"createtime":1496370021827,"leagueName":"阿甲","homeName":"戈多伊克鲁斯","guestName":"拉斐拉竞技","screening":"周五005","releaseTime":null,"orderStatus":0,"count":null,"income":null,"buyTime":1496392790822,"levels":null,"isEcpert":"1","nickName":"帕尔梅拉斯作客1:1战平土库曼竞技","headImg":"http://tp.1332255.com/img/2017-05-16/af2d1032-b9e2-49b6-8791-bd828336419a.jpg","winpointThreeDays":1,"errpointThreeDays":5}]
         * firstPage : 1
         * prePage : 0
         * nextPage : 0
         * lastPage : 1
         * isFirstPage : true
         * isLastPage : true
         * hasPreviousPage : false
         * hasNextPage : false
         * navigatePages : 8
         * navigatepageNums : [1]
         */

        private int pageNum;
        private int pageSize;
        private int size;
        private String orderBy;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private int firstPage;
        private int prePage;
        private int nextPage;
        private int lastPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

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

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
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

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public static class ListBean {
            /**
             * id : 643
             * title : yyy
             * context : null
             * choose : null
             * price : 1
             * userId : HHLY00000136
             * matchId : 848853606
             * matchDate : 06-03
             * matchTime : 08:15
             * leagueId : 2
             * type : 0
             * status : 2
             * createtime : 1496370021827
             * leagueName : 阿甲
             * homeName : 戈多伊克鲁斯
             * guestName : 拉斐拉竞技
             * screening : 周五005
             * releaseTime : null
             * orderStatus : 0
             * count : null
             * income : null
             * buyTime : 1496392790822
             * levels : null
             * isEcpert : 1
             * nickName : 帕尔梅拉斯作客1:1战平土库曼竞技
             * headImg : http://tp.1332255.com/img/2017-05-16/af2d1032-b9e2-49b6-8791-bd828336419a.jpg
             * winpointThreeDays : 1
             * errpointThreeDays : 5
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
            private int orderStatus;
            private String count;
            private String income;
            private long buyTime;
            private String levels;
            private String isEcpert;
            private String nickName;
            private String headImg;
            private int winpointThreeDays;
            private int errpointThreeDays;

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

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
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

            public long getBuyTime() {
                return buyTime;
            }

            public void setBuyTime(long buyTime) {
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
