package com.hhly.mlottery.bean.bettingbean;

/**
 * Created by：XQyi on 2017/6/8 10:06
 * Use:  余额查询实体类
 */
public class BalanceDataBean {

//        code = 200;
//        data

    private Integer code;
    private DataBean data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{

//        balance
//        isApplePay = 1;
        private String isApplePay;
        private BalanceBean balance;

        public String getIsApplePay() {
            return isApplePay;
        }

        public void setIsApplePay(String isApplePay) {
            this.isApplePay = isApplePay;
        }

        public BalanceBean getBalance() {
            return balance;
        }

        public void setBalance(BalanceBean balance) {
            this.balance = balance;
        }

        public class BalanceBean{
            //        availableBalance = 4998103;
            //        blockedBalance = 0;
            //        cashBalance = 0;
            //        totalAmount = 4998103;
            private String availableBalance; //可用余额
            private String blockedBalance;
            private String cashBalance; //可提现余额
            private String totalAmount;

            public String getAvailableBalance() {
                return availableBalance;
            }

            public void setAvailableBalance(String availableBalance) {
                this.availableBalance = availableBalance;
            }

            public String getBlockedBalance() {
                return blockedBalance;
            }

            public void setBlockedBalance(String blockedBalance) {
                this.blockedBalance = blockedBalance;
            }

            public String getCashBalance() {
                return cashBalance;
            }

            public void setCashBalance(String cashBalance) {
                this.cashBalance = cashBalance;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }
        }

    }

}
