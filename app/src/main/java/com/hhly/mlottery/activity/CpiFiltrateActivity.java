package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CpiFiltrateMatchAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.GrapeGridView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 103TJL on 2016/5/9.
 * 新版指数筛选
 */
public class CpiFiltrateActivity extends BaseActivity implements View.OnClickListener, CpiFiltrateMatchAdapter.OnItemClickListenerListener {

    private CpiFiltrateMatchAdapter mCpiAdapterHot;
    private CpiFiltrateMatchAdapter mCpiAdapterOther;
    private ImageView public_img_back;// 筛选界面返回
    private Context mContext;
    private GrapeGridView cpiGridViewHot;
    private GrapeGridView cpiGridViewOther;

    public static boolean isDefaultHot = true;

    /**
     * 已选择的联赛id
     */
    public static LinkedList<String> mCheckedIds = new LinkedList<>();
    //选中的备份
    private LinkedList<String> mTempCheckIdsReset = new LinkedList<>();
    //所有联赛
    private List<NewOddsInfo.FileterTagsBean> mFilterTagsList = new ArrayList<>();

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

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpi_filtrate);
        mContext = this;
        //全部的
        mFilterTagsList =
                (List<NewOddsInfo.FileterTagsBean>) getIntent().getSerializableExtra("fileterTags");
        //选中的
        ArrayList<String> checkedIdExtra =
                (ArrayList<String>) getIntent().getSerializableExtra("linkedListChecked");
        //选中的备份

        mCheckedIds.clear();
        if (checkedIdExtra != null)
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
        cpiGridViewHot = (GrapeGridView) findViewById(R.id.cpi_filtrate_match_gridview_hot);
        cpiGridViewOther = (GrapeGridView) findViewById(R.id.cpi_filtrate_match_gridview_other);
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
        if (mFilterTagsList == null) return;
        if (isDefaultHot) {
            //如果没选中的
            for (int i = 0; i < mFilterTagsList.size(); i++) {
                //所有场次
                allSize += mFilterTagsList.get(i).getMatchsInLeague();
                if (mFilterTagsList.get(i).isHot()) {
                    hotsTemp.add(mFilterTagsList.get(i));
                    mCheckedIds.add(mFilterTagsList.get(i).getLeagueId());
                    checkedSize += mFilterTagsList.get(i).getMatchsInLeague();
                } else {
                    normalTemp.add(mFilterTagsList.get(i));
                }
            }

            //如果本地存在取本地的值
            if (PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_INDEX).size() > 0) {
                List<String> list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_INDEX);
                mCheckedIds.clear();
                mCheckedIds.addAll(list);
            }

            mTempCheckIdsReset.addAll(mCheckedIds);
//            tempCheckids1.addAll(mCheckedIds);
        } else {
            //如果有选中的
            for (int i = 0; i < mFilterTagsList.size(); i++) {
                //所有场次
                allSize += mFilterTagsList.get(i).getMatchsInLeague();
                if (mFilterTagsList.get(i).isHot()) {
                    hotsTemp.add(mFilterTagsList.get(i));
                } else {
                    normalTemp.add(mFilterTagsList.get(i));
                }

                if (mCheckedIds.contains(mFilterTagsList.get(i).getLeagueId())) {
                    checkedSize += mFilterTagsList.get(i).getMatchsInLeague();
                }
            }
            mTempCheckIdsReset.addAll(mCheckedIds);
//            tempCheckids1.addAll(mCheckedIds);
        }

        setHideNumber();

        //热门赛事
        mCpiAdapterHot = new CpiFiltrateMatchAdapter(mContext, hotsTemp, mCheckedIds, R.layout.item_cpi_filtrate);
        mCpiAdapterHot.setOnItemCheckedChangedListener(this);
        cpiGridViewHot.setAdapter(mCpiAdapterHot);
        //其他赛事
        mCpiAdapterOther = new CpiFiltrateMatchAdapter(mContext, normalTemp, mCheckedIds, R.layout.item_cpi_filtrate);
        mCpiAdapterOther.setOnItemCheckedChangedListener(this);
        cpiGridViewOther.setAdapter(mCpiAdapterOther);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_img_back:// 关闭
                finish();
                break;
            case R.id.cpi_filtrate_match_hot_btn:// 热门
                mCheckedIds.clear();
                for (int i = 0; i < mFilterTagsList.size(); i++) {
                    if (mFilterTagsList.get(i).isHot()) {
                        mCheckedIds.add(mFilterTagsList.get(i).getLeagueId());
                    }
                }
                updateAdapter();
                setHideNumber();
                break;
            case R.id.cpi_filtrate_match_all_btn:// 全选
                mCheckedIds.clear();
                for (int i = 0; i < mFilterTagsList.size(); i++) {
                    mCheckedIds.add(mFilterTagsList.get(i).getLeagueId());
                }
                updateAdapter();
                setHideNumber();
                break;
            case R.id.cpi_filtrate_match_inverse_btn:// 反选
                List<String> tempCheckIds = new ArrayList<>();
                tempCheckIds.addAll(mCheckedIds);
                mCheckedIds.clear();
                for (int i = 0; i < mFilterTagsList.size(); i++) {
                    if (!tempCheckIds.contains(mFilterTagsList.get(i).getLeagueId())) {
                        mCheckedIds.add(mFilterTagsList.get(i).getLeagueId());
                        //选中的大小
                        checkedSize += mFilterTagsList.get(i).getMatchsInLeague();
                    }
                }
                updateAdapter();
                setHideNumber();
                break;
            case R.id.cpi_filtrate_match_reset_btn:// 重置
                mCheckedIds.clear();
                for (int i = 0; i < mFilterTagsList.size(); i++) {
                    if (mTempCheckIdsReset.contains(mFilterTagsList.get(i).getLeagueId())) {
                        mCheckedIds.add(mFilterTagsList.get(i).getLeagueId());
                    }
                }
                updateAdapter();
                setHideNumber();
                break;
            case R.id.cpi_filtrate_submit_btn:// 确定

                if (mCheckedIds.size() <= 0) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.at_least_one_race), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("key", mCheckedIds);
                    setResult(0, intent);
                    finish();
                    isDefaultHot = false;
                }
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


    private void setHideNumber() {
        checkedSize = 0;

        for (NewOddsInfo.FileterTagsBean tagsBean : mFilterTagsList) {

            if (mCheckedIds.contains(tagsBean.getLeagueId())) {
                checkedSize += tagsBean.getMatchsInLeague();
            }
        }
        cpi_filtrate_match_hide_number.setText("" + (allSize - checkedSize));


    }

    @Override
    public void onClick(View buttonView, boolean isChecked, NewOddsInfo.FileterTagsBean fileterTagsBean) {
        if (isChecked) {
            mCheckedIds.add(fileterTagsBean.getLeagueId());
        } else {
            mCheckedIds.remove(fileterTagsBean.getLeagueId());
        }
        setHideNumber();
    }
}
