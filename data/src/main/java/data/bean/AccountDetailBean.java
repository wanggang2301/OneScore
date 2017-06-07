package data.bean;

import java.util.List;

/**
 * 描    述：账户详情Bean
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailBean {

    /**
     * data : {"record":[{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-03 16:07:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 18:17:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 16:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 16:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 10:22:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 09:52:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 19:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 19:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 18:47:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 18:42:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null}],"balance":{"availableBalance":30100,"blockedBalance":0,"cashBalance":8000}}
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
         * record : [{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-03 16:07:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 18:17:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 16:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 16:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 10:22:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 09:52:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 19:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 19:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 18:47:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-01 18:42:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null}]
         * balance : {"availableBalance":30100,"blockedBalance":0,"cashBalance":8000}
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
             * availableBalance : 30100
             * blockedBalance : 0
             * cashBalance : 8000
             */

            private int availableBalance;
            private int blockedBalance;
            private int cashBalance;

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
