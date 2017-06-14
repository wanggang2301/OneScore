package data.bean;

/**
 * 描    述：账户详细某一条bean
 * 作    者：mady@13322.com
 * 时    间：2017/6/6
 */
public class RechargeBean {
    /**
     * tradeType : 3
     * tradeName : 支出
     * payType : null
     * tradeAmount : 1
     * status : 0
     * finishTime : 2017-06-06 17:43:43
     * rechargeType : 1
     * rechargeName : 竞彩推介
     * transactionId : null
     * payResult : null
     */

    private int tradeType;
    private String tradeName;
    private String payType;
    private int tradeAmount;
    private int status;
    private String finishTime;
    private int rechargeType;
    private String rechargeName;
    private String transactionId;
    private String payResult;
    private String statusName;

    public int getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(int tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statueName) {
        this.statusName = statueName;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(int rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getRechargeName() {
        return rechargeName;
    }

    public void setRechargeName(String rechargeName) {
        this.rechargeName = rechargeName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }
}
