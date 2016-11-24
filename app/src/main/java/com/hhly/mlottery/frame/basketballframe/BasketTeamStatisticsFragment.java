package com.hhly.mlottery.frame.basketballframe;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.bean.basket.basketstatistics.BasketTeamStatisticsBean;
import com.hhly.mlottery.util.net.VolleyContentFast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**created by mdy155
 * 球队统计Fragment
 */
public class BasketTeamStatisticsFragment extends Fragment {

    View mView;

    private String mThirdId;

    //投篮
    @BindView(R.id.guest_team_shoot)
    TextView guestShoot;
    @BindView(R.id.home_team_shoot)
    TextView homeShoot;
    @BindView(R.id.pro_basket_shoot)
    ProgressBar progressShoot;
    @BindView(R.id.guest_team_three)
    //三分
    TextView guestThree;
    @BindView(R.id.home_team_three)
    TextView homeThree;
    @BindView(R.id.pro_basket_three)
    ProgressBar progressThree;
    //罚球
    @BindView(R.id.guest_team_free)
    TextView guestFree;
    @BindView(R.id.home_team_free)
    TextView homeFree;
    @BindView(R.id.pro_basket_free)
    ProgressBar progressFree;
    //篮板
    @BindView(R.id.guest_team_rebound)
    TextView guestRebound;
    @BindView(R.id.home_team_rebound)
    TextView homeRebound;
    @BindView(R.id.pro_basket_rebound)
    ProgressBar progressRebound;

    //助攻
    @BindView(R.id.guest_team_assist)
    TextView guestAssist;
    @BindView(R.id.home_team_assist)
    TextView homeAssist;
    @BindView(R.id.pro_basket_assist)
    ProgressBar progressAssist;
    //抢断
    @BindView(R.id.guest_team_steal)
    TextView guestSteal;
    @BindView(R.id.home_team_steal)
    TextView homeSteal;
    @BindView(R.id.pro_basket_steal)
    ProgressBar progressSteal;
    //盖帽
    @BindView(R.id.guest_team_blockshoot)
    TextView guestBlockShoot;
    @BindView(R.id.home_team_blockshoot)
    TextView homeBlockShoot;
    @BindView(R.id.pro_basket_blockshoot)
    ProgressBar progressBlockShoot;
    //犯规
    @BindView(R.id.guest_team_foul)
    TextView guestFoul;
    @BindView(R.id.home_team_foul)
    TextView homeFoul;
    @BindView(R.id.pro_basket_foul)
    ProgressBar progressFoul;
    //失误
    @BindView(R.id.guest_team_turnover)
    TextView guestTurnover;
    @BindView(R.id.home_team_turnover)
    TextView homeTurnover;
    @BindView(R.id.pro_basket_turnover)
    ProgressBar progressTurnover;

    private BasketTeamStatisticsBean mData;


    Timer timer=new Timer();
    TimerTask timerTask;


    public static BasketTeamStatisticsFragment newInstance() {
        BasketTeamStatisticsFragment basketTeamStatisticsFragment = new BasketTeamStatisticsFragment();
        return basketTeamStatisticsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_basket_team_statistics,container,false);
        ButterKnife.bind(this,mView);
        initView();
        initData();
        return mView;
    }

    /**
     * 一分钟刷新一次
     */
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initData();
        }
    };




    private void initView() {
        if(timer!=null){
            if(timerTask!=null){
                timerTask.cancel();
            }

        }
        //定时器
        timerTask=new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask,1000*60,1000*60);

    }

    private void initData() {
        String url="http://m.13322.com/mlottery/core/IOSBasketballDetail.findTeamStats.do";
        Map<String ,String > params=new HashMap<>();

        params.put("thirdId", BasketDetailsActivityTest.mThirdId);
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<BasketTeamStatisticsBean>() {
            @Override
            public void onResponse(BasketTeamStatisticsBean jsonObject) {
                if(jsonObject.getResult()==200){
                    mData=jsonObject;
                    loadData();
                    Log.e("timerTask","定时器开启了哦");
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        },BasketTeamStatisticsBean.class);

    }

    private void loadData(){
        BasketTeamStatisticsBean.DataEntity.GuestTeamStatsEntity guestEntity=mData.getData().getGuestTeamStats();
        BasketTeamStatisticsBean.DataEntity.HomeTeamStatsEntity homeEntity=mData.getData().getHomeTeamStats();

        if(guestEntity!=null){
            guestShoot.setText(guestEntity.getShootHitRate()+"");
            guestThree.setText(guestEntity.getThreePointShotHitRate()+"");
            guestFree.setText(guestEntity.getFreeThrowHitRate()+"");
            guestRebound.setText(guestEntity.getRebound()+"");
            guestAssist.setText(guestEntity.getAssist()+"");
            guestFoul.setText(guestEntity.getFoul()+"");
            guestSteal.setText(guestEntity.getSteal()+"");
            guestTurnover.setText(guestEntity.getTurnover()+"");
            guestBlockShoot.setText(guestEntity.getBlockShot()+"");
        }
       if(homeEntity!=null){
           homeThree.setText(homeEntity.getThreePointShotHitRate()+"");
           homeShoot.setText(homeEntity.getShootHitRate()+"");
           homeFree.setText(homeEntity.getFreeThrowHitRate()+"");
           homeRebound.setText(homeEntity.getRebound()+"");
           homeAssist.setText(homeEntity.getAssist()+"");
           homeFoul.setText(homeEntity.getFoul()+"");
           homeSteal.setText(homeEntity.getSteal()+"");
           homeTurnover.setText(homeEntity.getTurnover()+"");
           homeBlockShoot.setText(homeEntity.getBlockShot()+"");
       }


        //设置进度
        if(homeEntity!=null&&guestEntity!=null){
            progressShoot.setProgress( getProgress(guestEntity.getShootHitRate(),homeEntity.getShootHitRate()));
            progressThree.setProgress(getProgress(guestEntity.getThreePointShotHitRate(),homeEntity.getThreePointShotHitRate()));
            progressFree.setProgress(getProgress(guestEntity.getFreeThrowHitRate(),homeEntity.getFreeThrowHitRate()));
            progressRebound.setProgress(getProgress(guestEntity.getRebound(),homeEntity.getRebound()));
            progressAssist.setProgress(getProgress(guestEntity.getAssist(),homeEntity.getAssist()));
            progressFoul.setProgress(getProgress(guestEntity.getFoul(),homeEntity.getFoul()));
            progressSteal.setProgress(getProgress(guestEntity.getSteal(),homeEntity.getSteal()));
            progressTurnover.setProgress(getProgress(guestEntity.getTurnover(),homeEntity.getTurnover()));
            progressBlockShoot.setProgress(getProgress(guestEntity.getBlockShot(),homeEntity.getBlockShot()));

        }

    }

    /**
     * 获取进度条比例
     */
    private int getProgress(int guest,int home){
        int progress=50;
        if(guest==0&&home==0){
            progress=50;
        }else if(guest==0){
            progress=0;
        }else if(home==0){
            progress=100;
        }else {
            progress=guest*100/(guest+home);
        }
        return progress;

    }

}
