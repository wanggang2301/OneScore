package data.repository;

import data.api.BasketTeamDataApi;
import data.bean.BasketTeamDataBean;
import data.bean.BasketTeamResultBean;
import rx.Observable;

/**
 * 描    述：篮球球队数据赛程赛果
 * 作    者：mady@13322.com
 * 时    间：2017/6/22
 */
public class BasketTeamDataRepository {

    BasketTeamDataApi mTeamDataApi;

    public BasketTeamDataRepository(BasketTeamDataApi mTeamDataApi) {
        this.mTeamDataApi = mTeamDataApi;
    }

    /**
     * 赛程赛果
     * @param season
     * @param leagueId
     * @param teamId
     * @param pageNum
     * @return
     */
    public Observable<BasketTeamResultBean> getMatchResult(String season, String leagueId, String teamId, String pageNum ){
        return mTeamDataApi.getMatchResult(season, leagueId, teamId, pageNum);
    }
    //球员数据
    public Observable<BasketTeamDataBean> getTeamData(String season, String leagueId, String teamId){
        return mTeamDataApi.getTeamData(season, leagueId, teamId);
    }
}
