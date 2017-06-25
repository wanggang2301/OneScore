package data;

import android.content.Context;

import javax.inject.Inject;

import data.repository.AccountDetailRepository;
import data.repository.BasketTeamDataRepository;
import data.repository.UserCenterRepository;
import data.repository.WithdrawRepository;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/6/5  17:24.
 */

public class DataManager {

    @Inject
    public UserCenterRepository userCenterRepository;   //在DataModule中已经被注入，dataManager里面可以直接使用

    @Inject
    public AccountDetailRepository mAccountRepository;

    @Inject
    public WithdrawRepository mWithdrawRepository;

    @Inject
    public BasketTeamDataRepository mTeamDataRepository;

    public DataManager(Context context, String apiHostUrl, String timeZone, String lang) {

        DaggerDataComponent.builder()
                .dataModule(new DataModule(context, apiHostUrl, timeZone, lang))
                .build()
                .inject(this);


    }
}
