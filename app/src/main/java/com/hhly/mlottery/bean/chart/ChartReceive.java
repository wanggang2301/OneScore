package com.hhly.mlottery.bean.chart;

import java.util.List;

/**
 * Created by yuely198 on 2016/12/12.
 */

public class ChartReceive {

    /**
     * result : 200
     * data : {"chatHistory":[{"msgId":"6152DDFF-7CEC-4393-BDF6-2A330C812EFC","msgCode":1,"fromUser":{"userId":"hhly299272","userLogo":null,"userNick":"hhly299272"},"toUser":null,"message":"哎","sourceType":1,"time":"2016-12-09 17:40:14","msgType":0},{"msgId":"E072F939-939C-4D1E-9F9A-8CF9D70000E5","msgCode":1,"fromUser":{"userId":"hhly297764","userLogo":null,"userNick":"稳胆裙6970027"},"toUser":null,"message":"稳胆推荐实力见证+V15858194533","sourceType":1,"time":"2016-12-09 17:40:37","msgType":0},{"msgId":"944CE083-3476-4595-9C5B-25CCA2CF835B","msgCode":1,"fromUser":{"userId":"hhly299185","userLogo":"http://public.13322.com/4dcb2d51.jpg","userNick":"这场不红天理难容"},"toUser":null,"message":"角球啊，卧槽！","sourceType":1,"time":"2016-12-09 17:40:42","msgType":0},{"msgId":"399B144E-381F-426A-BFB4-A8619D4D4596","msgCode":1,"fromUser":{"userId":"hhly297764","userLogo":null,"userNick":"稳胆裙6970027"},"toUser":null,"message":"稳胆推荐实力见证+V15858194533","sourceType":1,"time":"2016-12-09 17:41:16","msgType":0},{"msgId":"29961FC6-C644-45E4-96BE-32C1EB33CE54","msgCode":1,"fromUser":{"userId":"hhly299185","userLogo":"http://public.13322.com/4dcb2d51.jpg","userNick":"这场不红天理难容"},"toUser":null,"message":"好脚","sourceType":1,"time":"2016-12-09 17:41:42","msgType":0},{"msgId":"49490BF3-642E-4F90-B81C-C43DB7FCE343","msgCode":1,"fromUser":{"userId":"hhly296999","userLogo":null,"userNick":"hhly296999"},"toUser":null,"message":"我操 大1.5角球高水收米","sourceType":1,"time":"2016-12-09 17:41:52","msgType":0},{"msgId":"E75F253A-CD7C-406F-8C79-F947C4188F13","msgCode":1,"fromUser":{"userId":"hhly299185","userLogo":"http://public.13322.com/4dcb2d51.jpg","userNick":"这场不红天理难容"},"toUser":null,"message":"越南坑了，擦","sourceType":1,"time":"2016-12-09 17:42:19","msgType":0},{"msgId":"0DC2D72E-E051-4911-B96C-428E7C13E266","msgCode":1,"fromUser":{"userId":"hhly299141","userLogo":null,"userNick":"hhly299141"},"toUser":null,"message":"进你妹，你个扑街。老子小2.5","sourceType":1,"time":"2016-12-09 17:43:38","msgType":0},{"msgId":"68D4C8AC-1D4C-4A19-A2E1-490FC60C9F72","msgCode":1,"fromUser":{"userId":"hhly299185","userLogo":"http://public.13322.com/4dcb2d51.jpg","userNick":"这场不红天理难容"},"toUser":null,"message":"印尼有上3.5的么","sourceType":1,"time":"2016-12-09 17:44:36","msgType":0},{"msgId":"CD11A157-5A1D-4840-AF66-93C056C7547F","msgCode":1,"fromUser":{"userId":"13266752386","userLogo":null,"userNick":"kimono"},"toUser":null,"message":"😃😃","sourceType":1,"time":"2016-12-09 17:47:38","msgType":0}]}
     */


    private String result;
    private DataBean data;
/*
    public ChartReceive(String result, DataBean data) {
        this.result = result;
        this.data = data;

    }*/


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ChatHistoryBean> chatHistory;

        public List<ChatHistoryBean> getChatHistory() {
            return chatHistory;
        }

        public void setChatHistory(List<ChatHistoryBean> chatHistory) {
            this.chatHistory = chatHistory;
        }

        public static class ChatHistoryBean {
            /**
             * msgId : 6152DDFF-7CEC-4393-BDF6-2A330C812EFC
             * msgCode : 1
             * fromUser : {"userId":"hhly299272","userLogo":null,"userNick":"hhly299272"}
             * toUser : null
             * message : 哎
             * sourceType : 1
             * time : 2016-12-09 17:40:14
             * msgType : 0
             */

            private String msgId;
            private int msgCode;
            private FromUserBean fromUser;
            private ToUser toUser;
            private String message;
            private int sourceType;
            private String time;
            private int msgType;
            private String onlineNum;
            private boolean isShowTime;

            public ChatHistoryBean(int msgcode, String s, FromUserBean fromUserBean, ToUser toUserBean) {
                this.msgCode = msgcode;
                this.fromUser = fromUserBean;
                this.message = s;
                this.toUser = toUserBean;
            }

            public ChatHistoryBean() {

            }


            public boolean isShowTime() {
                return isShowTime;
            }

            public void setShowTime(boolean showTime) {
                isShowTime = showTime;
            }

            public String getMsgId() {
                return msgId;
            }

            public void setMsgId(String msgId) {
                this.msgId = msgId;
            }

            public int getMsgCode() {
                return msgCode;
            }

            public void setMsgCode(int msgCode) {
                this.msgCode = msgCode;
            }

            public FromUserBean getFromUser() {
                return fromUser;
            }

            public void setFromUser(FromUserBean fromUser) {
                this.fromUser = fromUser;
            }

            public ToUser getToUser() {
                return toUser;
            }

            public void setToUser(ToUser toUser) {
                this.toUser = toUser;
            }

            public String getOnlineNum() {
                return onlineNum;
            }

            public void setOnlineNum(String onlineNum) {
                this.onlineNum = onlineNum;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
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

            public int getMsgType() {
                return msgType;
            }

            public void setMsgType(int msgType) {
                this.msgType = msgType;
            }

            public static class ToUser {
                private String userId;
                private String userLogo;
                private String userNick;

                public ToUser() {
                }

                public ToUser(String userid, String userlogo, String usernick) {
                    this.userId = userid;
                    this.userLogo = userlogo;
                    this.userNick = usernick;
                }

                public String getUserNick() {
                    return userNick;
                }

                public void setUserNick(String userNick) {
                    this.userNick = userNick;
                }

                public String getUserLogo() {
                    return userLogo;
                }

                public void setUserLogo(String userLogo) {
                    this.userLogo = userLogo;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }
            }

            public static class FromUserBean {
                // *
                // * userId : hhly299272
                // userLogo : null
                // userNick : hhly299272

                private String userId;
                private String userLogo;
                private String userNick;

                public FromUserBean(String userId, String userLogo, String userNick) {
                    this.userId = userId;
                    this.userLogo = userLogo;
                    this.userNick = userNick;
                }


                public FromUserBean() {

                }

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
}
