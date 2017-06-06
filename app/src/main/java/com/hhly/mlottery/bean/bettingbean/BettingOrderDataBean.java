package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by：XQyi on 2017/6/6 16:53
 * Use: 推荐创建订单的实体
 */
public class BettingOrderDataBean {
//    code	3000
//    data	Object

    private Integer code;
    private orderDataBean data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public orderDataBean getData() {
        return data;
    }

    public void setData(orderDataBean data) {
        this.data = data;
    }

    public class orderDataBean{
//        amount	99900
//        orderNum	RCMhhly905311496733941449
//        payPrice	-
//        promotionId	658
//        userId	hhly90531

        private String amount;
        private String orderNum;
        private String payPrice;
        private String promotionId;
        private String userId;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(String payPrice) {
            this.payPrice = payPrice;
        }

        public String getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(String promotionId) {
            this.promotionId = promotionId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
