package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.BasketballMatchSearchAdapter;
import com.hhly.mlottery.bean.BasketballItemSearchBean;
import com.hhly.mlottery.bean.BasketballSearchBean;
import com.hhly.mlottery.bean.FoucsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketScheduleNewScoreFragment;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yuely198 on 2017/3/27.
 * 篮球比分搜索
 */

public class BasketBallMatchSearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ImmedBasketballFragment";
    public final static String BASKET_THIRD_ID = "thirdId";
    public final static String BASKET_MATCH_STATUS = "MatchStatus";
    public final static String BASKET_MATCH_LEAGUEID = "leagueId";
    public final static String BASKET_MATCH_MATCHTYPE = "matchType";

    //内容数据
    private List<BasketballItemSearchBean> mMatchdata = new ArrayList<>();//match的 内容(json)
    private BasketScheduleNewScoreFragment.BasketFocusClickListener mFocusClickListener; //关注点击监听
    public static int isLoad = -1;
    private BasketFocusClickListener basketFocusClickListener;

    public static int getIsLoad() {
        return isLoad;
    }

    private static final String LEAGUEID = "league";
    private BasketballMatchSearchAdapter basketballInforSerachAdapter;
    private EditText et_keyword;
    private RecyclerView explistview;
    private TextView mNo_serach_tv;
    private static final String SEARCHKEYWORD = "searchKeyword";
    private ImageView mSearch_iv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basketsearch);
        initView();
        initEvent();
    }


    private void initEvent() {

        et_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!et_keyword.getText().toString().trim().isEmpty()) {

                    explistview.setVisibility(View.VISIBLE);
                    initData(et_keyword.getText().toString().trim());
                    mSearch_iv_delete.setVisibility(View.VISIBLE);
                } else {
                    mSearch_iv_delete.setVisibility(View.GONE);
                    explistview.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // 定义关注监听
    public interface BasketFocusClickListener {
        void FocusOnClick(View view, BasketballItemSearchBean root);
    }

    /**
     * 点击关注事件
     */
    public void fucusChicked() {
        // 检查之前是否被选中
        basketFocusClickListener = new BasketFocusClickListener() {
            @Override
            public void FocusOnClick(View view, BasketballItemSearchBean root) {
                boolean isCheck = (Boolean) view.getTag();// 检查之前是否被选中

                if (!isCheck) {//未关注->关注
                    FocusUtils.addBasketFocusId(root.getThirdId());
                    view.setTag(true);
                    ((ImageView) view).setImageResource(R.mipmap.football_focus);
                } else {//关注->未关注
                    FocusUtils.deleteBasketFocusId(root.getThirdId());
                    view.setTag(false);
                    ((ImageView) view).setImageResource(R.mipmap.football_nomal);
                    //((BasketballScoresActivity)mContext).basketFocusCallback();
                    EventBus.getDefault().post(new FoucsBean("foucs"));
                }
            }
        };


    }


    //请求加载数据
    private void initData(String keyWord) {
        mMatchdata.clear();
        Map<String, String> params = new HashMap<>();
        params.put("searchKeyword", keyWord);//接口添加 version=xx 字段
        VolleyContentFast.requestJsonByPost(BaseURLs.IOSBASKETBALLMATCH, params, new VolleyContentFast.ResponseSuccessListener<BasketballSearchBean>() {
            @Override
            public void onResponse(BasketballSearchBean json) {


                if (!json.getData().isEmpty()) {
                    explistview.setVisibility(View.VISIBLE);
                    mNo_serach_tv.setVisibility(View.GONE);
                    mMatchdata = json.getData();

                    basketballInforSerachAdapter = new BasketballMatchSearchAdapter(BasketBallMatchSearchActivity.this, mMatchdata);
                    explistview.setAdapter(basketballInforSerachAdapter);
                    basketballInforSerachAdapter.setmFocus(basketFocusClickListener);//设置关注
                    basketballInforSerachAdapter.setmOnItemClickListener(new BasketballMatchSearchAdapter.OnRecycleItemClickListener() {
                        @Override
                        public void onItemClick(View view, BasketballItemSearchBean currData) {

                            Intent intent = new Intent(BasketBallMatchSearchActivity.this, BasketDetailsActivityTest.class);
                            intent.putExtra(BASKET_THIRD_ID, currData.getThirdId());//跳转到详情
                            intent.putExtra(BASKET_MATCH_STATUS, currData.getMatchStatus());//跳转到详情
                            intent.putExtra("currentfragment", 0);
                            intent.putExtra(BASKET_MATCH_LEAGUEID, currData.getLeagueId());
                            intent.putExtra(BASKET_MATCH_MATCHTYPE, currData.getMatchType());
                            startActivity(intent);
                        }
                    });

                } else {
                    explistview.setVisibility(View.GONE);
                    mNo_serach_tv.setVisibility(View.VISIBLE);
                    mNo_serach_tv.setText(R.string.not_find_search);

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            /**
             * 加载失败
             *
             * @param exception
             */
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.e(TAG, "exception.getErrorCode() = " + exception.getErrorCode());
                explistview.setVisibility(View.GONE);
                mNo_serach_tv.setVisibility(View.VISIBLE);
                mNo_serach_tv.setText(R.string.network_suck);

            }
        }, BasketballSearchBean.class);
        fucusChicked();//点击关注监听
    }

    private void initView() {


        //搜索框
        et_keyword = (EditText) findViewById(R.id.et_keyword);
        et_keyword.setHint(R.string.please_hint_team);
        //数据返回显示
        explistview = (RecyclerView) findViewById(R.id.explistview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BasketBallMatchSearchActivity.this);
        explistview.setLayoutManager(layoutManager);
        //无数据显示
        mNo_serach_tv = (TextView) findViewById(R.id.no_serach_tv);
        //删除键
        mSearch_iv_delete = (ImageView) findViewById(R.id.search_iv_delete);
        mSearch_iv_delete.setOnClickListener(this);


        //返回
        findViewById(R.id.search_btn_back).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_btn_back:
                finish();
                break;


            case R.id.search_iv_delete:

                et_keyword.setText("");

                break;

            default:
                break;

        }
    }

}
