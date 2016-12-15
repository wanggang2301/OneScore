package com.hhly.mlottery.bean.chart;

/**
 * Created by yuely198 on 2016/12/13.
 */

public class ChartRoom {

    /**
     * chatRoomId : football401487
     * data : {"fromUser":{"userId":"hhly91754","userLogo":"http://public.13322.com/2e35cda6.jpg","userNick":"王总"},"message":"😀","msgCode":1,"msgId":"8AD236F9-1850-484F-B8A4-8E4913A4D3B3","msgType":null,"onlineNum":null,"sourceType":1,"time":"2016-12-13 10:57:18","toUser":null}
     * type : 200
     */

    private String chatRoomId;
    private DataBean data;
    private int type;

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class DataBean {
        /**
         * fromUser : {"userId":"hhly91754","userLogo":"http://public.13322.com/2e35cda6.jpg","userNick":"王总"}
         * message : 😀
         * msgCode : 1
         * msgId : 8AD236F9-1850-484F-B8A4-8E4913A4D3B3
         * msgType : null
         * onlineNum : null
         * sourceType : 1
         * time : 2016-12-13 10:57:18
         * toUser : null
         */


        private FromUserBean fromUser;
        private String message;
        private int msgCode;
        private String msgId;
        private String msgType;
        private String onlineNum;
        private int sourceType;
        private String time;
        private String toUser;
        private boolean isShowTime;
        private boolean isEmoji;

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getOnlineNum() {
            return onlineNum;
        }

        public void setOnlineNum(String onlineNum) {
            this.onlineNum = onlineNum;
        }

        public String getToUser() {
            return toUser;
        }

        public void setToUser(String toUser) {
            this.toUser = toUser;
        }

        public boolean isShowTime() {
            return isShowTime;
        }

        public void setShowTime(boolean showTime) {
            isShowTime = showTime;
        }

        public boolean isEmoji() {
            return isEmoji;
        }

        public void setEmoji(boolean emoji) {
            isEmoji = emoji;
        }

        public FromUserBean getFromUser() {
            return fromUser;
        }

        public void setFromUser(FromUserBean fromUser) {
            this.fromUser = fromUser;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getMsgCode() {
            return msgCode;
        }

        public void setMsgCode(int msgCode) {
            this.msgCode = msgCode;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }



        public static class FromUserBean {
            /**
             * userId : hhly91754
             * userLogo : http://public.13322.com/2e35cda6.jpg
             * userNick : 王总
             */

            private String userId;
            private String userLogo;
            private String userNick;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserLogo() {
                return userLogo;
            }

            public void setUserLogo(String userLogo) {
                this.userLogo = userLogo;
            }

            public String getUserNick() {
                return userNick;
            }

            public void setUserNick(String userNick) {
                this.userNick = userNick;
            }
        }
    }
}
