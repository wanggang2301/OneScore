package data.api;

import data.bean.CodeBean;
import data.bean.WithdrawBean;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public interface WithDrawApi {

    /**
     * 提现页面
     * @param userId
     * @param token
     * @param sign
     * @return
     */
    @POST("user/card/info")
    Observable<WithdrawBean> getCardInfo(@Query("userId") String userId,
                                         @Query("loginToken") String token,
                                         @Query("sign") String sign);

    /**
     * 绑定银行卡。返回值只有一个code
     * @param userId
     * @param token
     * @param cardNum
     * @param bankName
     * @param sign
     * @return
     */
    @POST("user/card/bind")
    Observable<WithdrawBean> bindCard(@Query("userId") String userId,
                                      @Query("loginToken") String token,
                                      @Query("cardNum") String cardNum,
                                      @Query("bankName") String bankName,
                                      @Query("sign") String sign);

    /**
     * 提现请求
     * @param userId
     * @param amount 元
     * @return
     */
    @POST("user/pay/enchashment")
    Observable<CodeBean> withdrawCommit(@Query("userId") String userId,
                                        @Query("loginToken") String token,
                                        @Query("amount") String amount,
                                        @Query("sign") String sign);
}
