package data.repository;

import data.api.WithDrawApi;
import data.bean.CodeBean;
import data.bean.WithdrawBean;
import rx.Observable;

/**
 * 描    述：提现、绑定银行卡
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public class WithdrawRepository {

    WithDrawApi mWithdrawApi;

    public WithdrawRepository(WithDrawApi mWithdrawApi) {
        this.mWithdrawApi = mWithdrawApi;
    }

    public Observable<WithdrawBean> getCardInfo(String userId,String loginToken,String sign){
        return mWithdrawApi.getCardInfo(userId, loginToken, sign);
    }

    public Observable<WithdrawBean> bindCard(String userId,String loginToken,String cardNum,String bankName ,String sign){
        return mWithdrawApi.bindCard(userId, loginToken, cardNum, bankName, sign);
    }

    public Observable<CodeBean> withdrawCommit(String userId, String loginToken, String amount, String sign){
        return mWithdrawApi.withdrawCommit(userId, loginToken, amount, sign);
    }
}
