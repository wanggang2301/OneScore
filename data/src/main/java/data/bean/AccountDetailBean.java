package data.bean;

import java.util.List;

/**
 * 描    述：账户详情Bean
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailBean {


    /**
     * data : {"record":[{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-07 11:42:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-07 11:07:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-07 11:07:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-07 10:57:34","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":5,"status":0,"finishTime":"2017-06-07 10:56:37","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:43:43","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:27:23","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":3,"status":0,"finishTime":"2017-06-06 17:23:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:22:36","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:21:37","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null}],"balance":{"availableBalance":28650,"blockedBalance":0,"cashBalance":8250}}
     * code : 200
     */

    private DataEntity data;
    private String code;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataEntity {
        /**
         * record : [{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-07 11:42:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-07 11:07:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-07 11:07:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-07 10:57:34","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":5,"status":0,"finishTime":"2017-06-07 10:56:37","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:43:43","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:27:23","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":3,"status":0,"finishTime":"2017-06-06 17:23:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:22:36","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:21:37","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null}]
         * balance : {"availableBalance":28650,"blockedBalance":0,"cashBalance":8250}
         */

        private BalanceEntity balance;
        private List<RechargeBean> record;

        public BalanceEntity getBalance() {
            return balance;
        }

        public void setBalance(BalanceEntity balance) {
            this.balance = balance;
        }

        public List<RechargeBean> getRecord() {
            return record;
        }

        public void setRecord(List<RechargeBean> record) {
            this.record = record;
        }

        public static class BalanceEntity {
            /**
             * availableBalance : 28650
             * blockedBalance : 0
             * cashBalance : 8250
             */

            private int availableBalance;
            private int blockedBalance;
            private int cashBalance;
            private int totalAmount;

            public int getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(int totalAmount) {
                this.totalAmount = totalAmount;
            }

            public int getAvailableBalance() {
                return availableBalance;
            }

            public void setAvailableBalance(int availableBalance) {
                this.availableBalance = availableBalance;
            }

            public int getBlockedBalance() {
                return blockedBalance;
            }

            public void setBlockedBalance(int blockedBalance) {
                this.blockedBalance = blockedBalance;
            }

            public int getCashBalance() {
                return cashBalance;
            }

            public void setCashBalance(int cashBalance) {
                this.cashBalance = cashBalance;
            }
        }


    }
}
