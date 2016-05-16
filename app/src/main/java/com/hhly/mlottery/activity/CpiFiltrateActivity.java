package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CpiFiltrateMatchAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.widget.GrapeGridview;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 103TJL on 2016/5/9.
 * 新版指数筛选
 */
public class CpiFiltrateActivity extends BaseActivity implements View.OnClickListener, CpiFiltrateMatchAdapter.OnItemCheckedChangedListener {

    private CpiFiltrateMatchAdapter mCpiAdapterHot;
    private CpiFiltrateMatchAdapter mCpiAdapterOther;
    private ImageView public_img_back;// 筛选界面返回
    private Context mContext;
    private GrapeGridview cpiGridviewHot;
    private GrapeGridview cpiGridviewOther;
    /**
     * 已选择的联赛id
     */
    public static LinkedList<String> mCheckedIds = new LinkedList<>();
    private LinkedList<String> mTempCheckidsReset = new LinkedList<>();
    //所有联赛
    private List<NewOddsInfo.FileterTagsBean> mFileterTagsList = new ArrayList<>();

    List<NewOddsInfo.FileterTagsBean> hotsTemp = new ArrayList<>();
    List<NewOddsInfo.FileterTagsBean> normalTemp = new ArrayList<>();

    private int allSize = 0;
    private int checkedSize = 0;

    //隐藏的场次，热门，全选，反选，重置，确定
    private TextView
            cpi_filtrate_match_hide_number,
            cpi_filtrate_match_hot_btn,
            cpi_filtrate_match_all_btn,
            cpi_filtrate_match_inverse_btn,
            cpi_filtrate_match_reset_btn;
    private Button cpi_filtrate_submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpi_filtrate);
        mContext = this;
        mFileterTagsList = (List<NewOddsInfo.FileterTagsBean>) getIntent().getSerializableExtra("fileterTags");

        ArrayList<String> checkedIdExtra = (ArrayList<String>) getIntent().getSerializableExtra("linkedListChecked");

        mTempCheckidsReset.addAll(checkedIdExtra);
//        mTempCheckidsReset.addAll(mTempCheckidsReset);
        mCheckedIds.clear();
        mCheckedIds.addAll(checkedIdExtra);

        initView();
        initData();


    }

    private void initView() {
        TextView public_txt_title = (TextView) findViewById(R.id.public_txt_left_title);
        public_txt_title.setVisibility(View.VISIBLE);
        public_txt_title.setText(R.string.basket_analyze_screen);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_txt_title).setVisibility(View.GONE);
        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        cpiGridviewHot = (GrapeGridview) findViewById(R.id.cpi_filtrate_match_gridview_hot);
        cpiGridviewOther = (GrapeGridview) findViewById(R.id.cpi_filtrate_match_gridview_other);
        //场次，热门，全选，反选，重置,确定
        cpi_filtrate_match_hide_number = (TextView) findViewById(R.id.cpi_filtrate_match_hide_number);
        cpi_filtrate_match_hot_btn = (TextView) findViewById(R.id.cpi_filtrate_match_hot_btn);
        cpi_filtrate_match_all_btn = (TextView) findViewById(R.id.cpi_filtrate_match_all_btn);
        cpi_filtrate_match_inverse_btn = (TextView) findViewById(R.id.cpi_filtrate_match_inverse_btn);
        cpi_filtrate_match_reset_btn = (TextView) findViewById(R.id.cpi_filtrate_match_reset_btn);
        cpi_filtrate_submit_btn = (Button) findViewById(R.id.cpi_filtrate_submit_btn);

        cpi_filtrate_match_hot_btn.setOnClickListener(this);
        cpi_filtrate_match_all_btn.setOnClickListener(this);
        cpi_filtrate_match_inverse_btn.setOnClickListener(this);
        cpi_filtrate_match_reset_btn.setOnClickListener(this);
        cpi_filtrate_submit_btn.setOnClickListener(this);

    }

    private void initData() {
        if (mFileterTagsList == null) return;
        if (mCheckedIds.size() == 0) {
            for (int i = 0; i < mFileterTagsList.size(); i++) {
                //所有场次
                allSize += mFileterTagsList.get(i).getMatchsInLeague();
                if (mFileterTagsList.get(i).isHot()) {
                    hotsTemp.add(mFileterTagsList.get(i));
                    mCheckedIds.add(mFileterTagsList.get(i).getLeagueId());
                    checkedSize += mFileterTagsList.get(i).getMatchsInLeague();
                } else {
                    normalTemp.add(mFileterTagsList.get(i));
                }
            }
        } else {

            for (int i = 0; i < mFileterTagsList.size(); i++) {
                //所有场次
                allSize += mFileterTagsList.get(i).getMatchsInLeague();
                if (mFileterTagsList.get(i).isHot()) {
                    hotsTemp.add(mFileterTagsList.get(i));
                } else {
                    normalTemp.add(mFileterTagsList.get(i));
                }

                if (mCheckedIds.contains(mFileterTagsList.get(i).getLeagueId())) {
                    checkedSize += mFileterTagsList.get(i).getMatchsInLeague();
                }
            }

        }


        setHideNumber();

        //热门赛事
        mCpiAdapterHot = new CpiFiltrateMatchAdapter(mContext, hotsTemp, mCheckedIds, R.layout.item_filtrate_match);
        mCpiAdapterHot.setOnItemCheckedChangedListener(this);
        cpiGridviewHot.setAdapter(mCpiAdapterHot);
        //其他赛事
        mCpiAdapterOther = new CpiFiltrateMatchAdapter(mContext, normalTemp, mCheckedIds, R.layout.item_filtrate_match);
        mCpiAdapterOther.setOnItemCheckedChangedListener(this);
        cpiGridviewOther.setAdapter(mCpiAdapterOther);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_img_back:// 关闭
                finish();
                break;
            case R.id.cpi_filtrate_match_hot_btn:// 热门
                mCheckedIds.clear();
                for (int i = 0; i < mFileterTagsList.size(); i++) {
                    if (mFileterTagsList.get(i).isHot()) {
                        mCheckedIds.add(mFileterTagsList.get(i).getLeagueId());
                    }
                }
                updateAdapter();
                break;
            case R.id.cpi_filtrate_match_all_btn:// 全选
                mCheckedIds.clear();
                for (int i = 0; i < mFileterTagsList.size(); i++) {
                    mCheckedIds.add(mFileterTagsList.get(i).getLeagueId());
                }
                updateAdapter();
                break;
            case R.id.cpi_filtrate_match_inverse_btn:// 反选
                List<String> tempCheckids = new ArrayList<>();
                tempCheckids.addAll(mCheckedIds);
                mCheckedIds.clear();
                for (int i = 0; i < mFileterTagsList.size(); i++) {
                    if (!tempCheckids.contains(mFileterTagsList.get(i).getLeagueId())) {
                        mCheckedIds.add(mFileterTagsList.get(i).getLeagueId());
                    }
                }
                updateAdapter();
                break;
            case R.id.cpi_filtrate_match_reset_btn:// 重置

                mTempCheckidsReset.addAll(mTempCheckidsReset);
                ;
                mCheckedIds.clear();
                for (int i = 0; i < mFileterTagsList.size(); i++) {
                    if (mTempCheckidsReset.contains(mFileterTagsList.get(i).getLeagueId())) {
                        mCheckedIds.add(mFileterTagsList.get(i).getLeagueId());
                    }
                }
                updateAdapter();
                break;
            case R.id.cpi_filtrate_submit_btn:// 确定
                Intent intent = new Intent();
                intent.putExtra("key", mCheckedIds);
                setResult(0, intent);
                finish();

                break;
            default:
                break;
        }

    }


    public void updateAdapter() {
        mCpiAdapterHot.setCheckedIds(mCheckedIds);
        mCpiAdapterHot.notifyDataSetChanged();
        mCpiAdapterOther.setCheckedIds(mCheckedIds);
        mCpiAdapterOther.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, NewOddsInfo.FileterTagsBean fileterTagsBean) {
        if (isChecked) {
            mCheckedIds.add(fileterTagsBean.getLeagueId());
        } else {
            mCheckedIds.remove(fileterTagsBean.getLeagueId());
        }
        setHideNumber();
    }

    private void setHideNumber() {
        checkedSize = 0;

        for (NewOddsInfo.FileterTagsBean tagsBean : mFileterTagsList) {

            if (mCheckedIds.contains(tagsBean.getLeagueId())) {
                checkedSize += tagsBean.getMatchsInLeague();
            }
        }
        cpi_filtrate_match_hide_number.setText("" + (allSize - checkedSize));

    }
}
