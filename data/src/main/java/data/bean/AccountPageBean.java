package data.bean;

import java.util.List;

/**
 * 描    述：分页bean
 * 作    者：mady@13322.com
 * 时    间：2017/6/6
 */
public class AccountPageBean {

    /**
     * data : [{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:43:43","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:27:23","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":3,"status":0,"finishTime":"2017-06-06 17:23:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:22:36","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-06 17:21:37","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":1,"status":0,"finishTime":"2017-06-05 16:44:09","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":3,"tradeName":"支出","payType":null,"tradeAmount":3,"status":0,"finishTime":"2017-06-05 15:20:06","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-03 16:07:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 18:17:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null},{"tradeType":4,"tradeName":"收入","payType":null,"tradeAmount":0.5,"status":0,"finishTime":"2017-06-02 16:02:32","rechargeType":1,"rechargeName":"竞彩推介","transactionId":null,"payResult":null}]
     * code : 200
     */

    private String code;
    private List<RechargeBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<RechargeBean> getData() {
        return data;
    }

    public void setData(List<RechargeBean> data) {
        this.data = data;
    }



}
