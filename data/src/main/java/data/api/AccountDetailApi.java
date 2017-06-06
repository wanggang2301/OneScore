package data.api;

import data.bean.AccountDetailBean;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描    述：账户详情Api
 * 作    者：mady@13322.com
 * 时    间：2017/6/5
 */
public interface AccountDetailApi {

    @POST("pay/balancerecord")
    Observable<AccountDetailBean> getAccountData(@Query("userId") String userId,
                                                 @Query("loginToken") String loginToken,
                                                 @Query("sign") String sign,
                                                 @Query("pageNum") String number);
}
