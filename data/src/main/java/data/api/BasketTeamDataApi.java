package data.api;

import data.bean.BasketTeamDataBean;
import data.bean.BasketTeamResultBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描    述：篮球球队队员数据与赛程赛果
 * 作    者：mady@13322.com
 * 时    间：2017/6/22
 */
public interface BasketTeamDataApi {

    @GET("mlottery/core/basketballData.teamMatchData.do")
    Observable<BasketTeamResultBean> getMatchResult(@Query("season") String season,
                                                   @Query("leagueId") String leagueId,
                                                   @Query("teamId") String teamId,
                                                   @Query("pageNum") String pageNum);

    @GET("mlottery/core/basketballData.teamData.do")
    Observable<BasketTeamDataBean> getTeamData(@Query("season") String season,
                                               @Query("leagueId") String leagueId,
                                               @Query("teamId") String teamId);
}
