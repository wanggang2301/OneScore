package data.repository;

import data.api.AccountDetailApi;
import data.bean.AccountDetailBean;
import rx.Observable;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public class AccountDetailRepository {
    AccountDetailApi mAccountApi;

    public AccountDetailRepository(AccountDetailApi mAccountApi) {
        this.mAccountApi = mAccountApi;
    }

    public Observable<AccountDetailBean> getAccountData( String userId,String loginToken,String sign ,String pageNumber){
        return mAccountApi.getAccountData(userId, loginToken, sign ,pageNumber);
    }
}
