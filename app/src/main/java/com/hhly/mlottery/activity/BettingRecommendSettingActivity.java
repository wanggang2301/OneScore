package com.hhly.mlottery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.bettingadapter.BettingRecommendSettingAdapter;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.bean.bettingbean.BettingSettingItenDataBean;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingSettingResultEventBusEntity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.GrapeGridView;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by：Administrator on 2017/4/24 10:36
 * Use: 竞彩推荐设置页
 */
public class BettingRecommendSettingActivity extends Activity implements View.OnClickListener {

    private ImageView mBack;
    private Button mSave;
    private GrapeGridView mPlayingGrid;
    private GrapeGridView mLeagueGrid;
    private int numColumns = 4;//grid列数 默认每行显示4个(国际版需要调整)
    private BettingRecommendSettingAdapter playAdapter;
    private BettingRecommendSettingAdapter leagueAdapter;

    List<BettingListDataBean.LeagueNameData> playList = new ArrayList<>();//玩法所有
    private List<String> mPlayChecked = new ArrayList<>();//选中玩法的id

    private List<BettingListDataBean.LeagueNameData> mAllLeague = new ArrayList<>();//所有的联赛
    private List<BettingListDataBean.LeagueNameData> mCurrLeague = new ArrayList<>();//选中的联赛
    private List<String> mKeyChecked = new ArrayList<>();//选中联赛的 Key（选中的key从选中的联赛中提取）

    private BettingRecommendSettingAdapter.BettingClickChangeListener clickChangeListener;
    private String palyType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.betting_setting_activity);
        initView();
        initData();
        setData();
    }

    private void initView(){
        //隐藏多余控件
        findViewById(R.id.public_txt_title).setVisibility(View.INVISIBLE);
        findViewById(R.id.public_btn_filter).setVisibility(View.INVISIBLE);
        findViewById(R.id.public_btn_set).setVisibility(View.INVISIBLE);

        TextView title = (TextView) findViewById(R.id.public_txt_title);
        title.setVisibility(View.VISIBLE);
        title.setText(getApplicationContext().getString(R.string.basket_analyze_screen));

        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
        mSave = (Button) findViewById(R.id.public_btn_save);
        mSave.setVisibility(View.VISIBLE);
        mSave.setTextColor(getResources().getColor(R.color.betting_recommend_zhuanjia_grand_color));
        mSave.setOnClickListener(this);

        mPlayingGrid = (GrapeGridView)findViewById(R.id.betting_set_playing_gridview);//玩法
        mLeagueGrid = (GrapeGridView)findViewById(R.id.betting_set_league_gridview);//联赛
        // 国际版3列
        if (AppConstants.isGOKeyboard) {
            numColumns = 3;
        } else {
            numColumns = 4;
        }
        mPlayingGrid.setNumColumns(numColumns);
        mLeagueGrid.setNumColumns(numColumns);


    }

    private void initData(){

        Serializable allLeague = getIntent().getSerializableExtra(ConstantPool.ALL_LEAGUE);
        Serializable currLeague = getIntent().getSerializableExtra(ConstantPool.CURR_LEAGUE);
        mAllLeague = (List<BettingListDataBean.LeagueNameData>)allLeague;
        mCurrLeague = (List<BettingListDataBean.LeagueNameData>)currLeague;
        palyType = getIntent().getStringExtra(ConstantPool.CURR_PALY_TYPE);


        BettingListDataBean.LeagueNameData dataBean1 = new BettingListDataBean.LeagueNameData();
        dataBean1.setLeagueName(getApplicationContext().getString(R.string.jingcaidanguan_txt));
        dataBean1.setKey("0");
        playList.add(dataBean1);
        BettingListDataBean.LeagueNameData dataBean2 = new BettingListDataBean.LeagueNameData();
        dataBean2.setLeagueName(getApplicationContext().getString(R.string.betting_plays_asialet));
        dataBean2.setKey("1");
        playList.add(dataBean2);
        BettingListDataBean.LeagueNameData dataBean3 = new BettingListDataBean.LeagueNameData();
        dataBean3.setLeagueName(getApplicationContext().getString(R.string.betting_plays_asize));
        dataBean3.setKey("2");
        playList.add(dataBean3);

        BettingListDataBean.LeagueNameData dataBean4 = new BettingListDataBean.LeagueNameData();
        dataBean4.setLeagueName(getApplicationContext().getString(R.string.betting_plays_allcheck));
        dataBean4.setKey("-1");
        playList.add(dataBean4);

        mPlayChecked.add(palyType);//默认选中全部



        for (BettingListDataBean.LeagueNameData currId : mCurrLeague) {
//            mKeyChecked.add(currId.getLeagueId());
            mKeyChecked.add(currId.getKey());
        }

        clickChangeListener = new BettingRecommendSettingAdapter.BettingClickChangeListener(){

            @Override
            public void onClick(CompoundButton buttonView) {
                upDataAdapter();
            }
        };
    }

    private void setData(){

        playAdapter = new BettingRecommendSettingAdapter(this ,mPlayChecked, playList , R.layout.betting_set_grid_item , true);
        playAdapter.setClickChangeListener(clickChangeListener);
        mPlayingGrid.setAdapter(playAdapter);
        leagueAdapter = new BettingRecommendSettingAdapter(this ,mKeyChecked, mAllLeague , R.layout.betting_set_grid_item , false);
        leagueAdapter.setClickChangeListener(clickChangeListener);
        mLeagueGrid.setAdapter(leagueAdapter);

    }

    private void upDataAdapter(){
        playAdapter.notifyDataSetChanged();
        leagueAdapter.notifyDataSetChanged();
        L.d("qwer==play==>> " , mPlayChecked.size()+"");
        L.d("qwer==league==>> " , mKeyChecked.size()+"");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_btn_save: // 保存
                EventBus.getDefault().post(new BettingSettingResultEventBusEntity(mPlayChecked , mKeyChecked));
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
