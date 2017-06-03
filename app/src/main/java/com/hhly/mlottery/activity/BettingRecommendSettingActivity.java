package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.bettingadapter.BettingRecommendSettingAdapter;
import com.hhly.mlottery.bean.bettingbean.BettingSettingItenDataBean;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.GrapeGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：Administrator on 2017/4/24 10:36
 * Use: 竞彩推荐设置页
 */
public class BettingRecommendSettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;
    private Button mSave;
    private GrapeGridView mPlayingGrid;
    private GrapeGridView mLeagueGrid;
    private int numColumns = 4;//grid列数 默认每行显示4个(国际版需要调整)
    private BettingRecommendSettingAdapter playAdapter;
    private BettingRecommendSettingAdapter leagueAdapter;

    private List<String> mPlayChecked = new ArrayList<>();//选中玩法的id
    private List<String> mLeagueChecked = new ArrayList<>();//选中联赛的id
    private BettingRecommendSettingAdapter.BettingClickChangeListener clickChangeListener;

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
    List<BettingSettingItenDataBean> playList = new ArrayList<>();
    List<BettingSettingItenDataBean> leagueList = new ArrayList<>();
    private void initData(){

        for (int i = 0; i < 10; i++) {
            BettingSettingItenDataBean dataBean = new BettingSettingItenDataBean();
            dataBean.setName("玩法[" + i +"]");
            dataBean.setId(i+"");
            playList.add(dataBean);
        }

        for (int i = 0; i < 10; i++) {
            BettingSettingItenDataBean dataBean = new BettingSettingItenDataBean();
            dataBean.setName("联赛[" + i +"]");
            dataBean.setId(i+"");
            leagueList.add(dataBean);
        }

        mPlayChecked.add("0");
        mPlayChecked.add("6");
        mPlayChecked.add("2");

        mLeagueChecked.add("1");
        mLeagueChecked.add("3");
        mLeagueChecked.add("7");

        clickChangeListener = new BettingRecommendSettingAdapter.BettingClickChangeListener(){

            @Override
            public void onClick(CompoundButton buttonView) {
                upDataAdapter();
            }
        };
    }

    private void setData(){

        playAdapter = new BettingRecommendSettingAdapter(this ,mPlayChecked, playList , R.layout.betting_set_grid_item);
        playAdapter.setClickChangeListener(clickChangeListener);
        mPlayingGrid.setAdapter(playAdapter);
        leagueAdapter = new BettingRecommendSettingAdapter(this ,mLeagueChecked, leagueList , R.layout.betting_set_grid_item);
        leagueAdapter.setClickChangeListener(clickChangeListener);
        mLeagueGrid.setAdapter(leagueAdapter);

    }

    private void upDataAdapter(){
        playAdapter.notifyDataSetChanged();
        leagueAdapter.notifyDataSetChanged();
        L.d("qwer==play==>> " , mPlayChecked.size()+"");
        L.d("qwer==league==>> " , mLeagueChecked.size()+"");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_btn_save: // 保存
                break;
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
        }
    }
}
