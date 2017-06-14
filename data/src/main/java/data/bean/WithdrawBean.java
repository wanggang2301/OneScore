package data.bean;

/**
 * 描    述：提现界面返回的绑定银行卡信息
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public class WithdrawBean {

    /**
     * data : {"accountName":"BBSBS","bankName":"中国银行222","cardNum":"13332223333"}
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
         * accountName : BBSBS
         * bankName : 中国银行222
         * cardNum : 13332223333
         */

        private String accountName;
        private String bankName;
        private String cardNum;

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getCardNum() {
            return cardNum;
        }

        public void setCardNum(String cardNum) {
            this.cardNum = cardNum;
        }
    }
}
