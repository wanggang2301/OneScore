package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketOddsDetailsAdapter;
import com.hhly.mlottery.bean.basket.basketdetails.BasketDetailOddsBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketDetailOddsDetailsBean;
import com.hhly.mlottery.bean.basket.basketdetails.OddsDataEntity;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * /**
 * @Description: 篮球赔率详情的Activity
 * @author yixq
 * Created by A on 2016/4/1.
 */
public class BasketOddsDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private ListView mListView1;
    private ListView mListView2;

    Handler mHandler = new Handler();
    private TextView mTveuro;

    private List<String> mCompany = new ArrayList<>(); //公司名list
    private String mOddsId ; //公司id
    //    private String mOddsType; //赔率id
    private ImageView mBackImg;
    private TextView mTittle;

    private BasketOddsDetailsAdapter mAdapter;
    private CompanyAdapter mAdapter2;

    private int mPositionId; // 点击的公司顺序
    private String mOddsType; //欧亚大小(id)
    private List<BasketDetailOddsBean.CompanyOddsEntity> mOddsCompanyList = new ArrayList<>();
    private TextView mNoData;
    private ExactSwipeRefreshLayout mRefresh;
    private LinearLayout mEerrorll;
    private TextView mErrorBtn;
    private TextView mOddsLeft;
    private TextView mOddsRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basketball_odds_details_to);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mPositionId = bundle.getInt("position");
        mOddsType = bundle.get("type").toString();
        mOddsCompanyList = bundle.getParcelableArrayList("list");
        /**
         * 判断是否是欧赔（euro 去平均）
         */
        if (mOddsType.equals(BasketDetailsActivityTest.ODDS_EURO)) {
            mOddsCompanyList.remove(0);
        }

        if (mOddsType.equals(BasketDetailsActivityTest.ODDS_EURO)) {
            mOddsId = mOddsCompanyList.get(mPositionId-1).getOddsId();
        }else{
            mOddsId = mOddsCompanyList.get(mPositionId).getOddsId();
        }

        initView();
        mHandler.postDelayed(mRun , 500); // 加载数据

        if (mAdapter2 == null) {
            mAdapter2 = new CompanyAdapter(BasketOddsDetailsActivity.this , mOddsCompanyList , R.layout.basket_odds_company_item);
            mListView1.setAdapter(mAdapter2);
            if (mOddsType.equals(BasketDetailsActivityTest.ODDS_EURO)) {
                mAdapter2.setChickedId(mPositionId-1);
                mListView1.setSelection(mPositionId-1);
            }else{
                mAdapter2.setChickedId(mPositionId);
                mListView1.setSelection(mPositionId);
            }

        }
    }

    private void initView(){
        mListView1 = (ListView) findViewById(R.id.basket_odds_listview1);
        mListView2 = (ListView) findViewById(R.id.basket_odds_listview2);
        mTveuro = (TextView)findViewById(R.id.basket_odds_details_iseuro);
        mOddsLeft = (TextView)findViewById(R.id.basket_odds_details_left);
        mOddsRight = (TextView)findViewById(R.id.basket_odds_details_right);
        if (mOddsType.equals(BasketDetailsActivityTest.ODDS_EURO)) {
            mTveuro.setVisibility(View.GONE);
        }else{
            mTveuro.setVisibility(View.VISIBLE);
        }
        mBackImg = (ImageView) findViewById(R.id.basket_odds_details_back);
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MyApp.getContext(),"BasketOddsDetailsActivity_Exit");
                finish();
            }
        });
        mTittle = (TextView) findViewById(R.id.basket_odds_details_title);
        if (mOddsType.equals("euro")) {
            mTittle.setText(getResources().getText(R.string.basket_odds_euro_details));
            mOddsLeft.setText(getResources().getText(R.string.basket_analyze_guest_win));
            mOddsRight.setText(getResources().getText(R.string.basket_analyze_home_win));
        }else if(mOddsType.equals("asiaLet")){
            mTittle.setText(getResources().getText(R.string.basket_odds_asiaLet_details));
            mOddsLeft.setText(getResources().getText(R.string.basket_analyze_guest_win));
            mOddsRight.setText(getResources().getText(R.string.basket_analyze_home_win));
        }else if(mOddsType.equals("asiaSize")){
            mTittle.setText(getResources().getText(R.string.basket_odds_asiaSize_details));
            mOddsLeft.setText(getResources().getText(R.string.odd_home_big_txt));
            mOddsRight.setText(getResources().getText(R.string.odd_guest_big_txt));
        }
        mListView1.setOnItemClickListener(new ItemClick());

        mNoData = (TextView) findViewById(R.id.basket_odds_no_data);
        mEerrorll = (LinearLayout) findViewById(R.id.basketball_odds_details_error);

        mErrorBtn = (TextView) findViewById(R.id.basketball_odds_details_error_btn);
        mErrorBtn.setOnClickListener(this);

        mRefresh = (ExactSwipeRefreshLayout)findViewById(R.id.basket_odds_details_refreshlayout);
        mRefresh.setColorSchemeResources(R.color.tabhost);
        mRefresh.setOnRefreshListener(BasketOddsDetailsActivity.this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(this, StaticValues.REFRASH_OFFSET_END));
        mRefresh.setRefreshing(true);

        solveConflict(mListView1);
        solveConflict(mListView2);

    }

    /**
     * 下拉刷新滑动冲突处理
     * @param mView
     */
    private void solveConflict(final ListView mView){
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //listview不处于滑动到顶部时，关闭下拉刷新
                        if (mView.getFirstVisiblePosition() != 0) {
                            mRefresh.setEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mRefresh.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }


    private void initData(String oddsId){

//        String url = "http://192.168.10.242:8181/mlottery/core/basketballDetail.findOddsDetail.do"; //?oddsId=2652491&lang=zh&oddsType=euro
//        String url = "http://192.168.31.48:8888/mlottery/core/basketballDetail.findOddsDetail.do"; //?oddsId=2652491&lang=zh&oddsType=euro
        String url = BaseURLs.URL_BASKET_ODDS_DETAILS;
        Map<String , String> map = new HashMap<>();
//        map.put("oddsId", "2738173");
        map.put("oddsId", oddsId);
        map.put("oddsType" , mOddsType);

        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<BasketDetailOddsDetailsBean>() {
            @Override
            public void onResponse(BasketDetailOddsDetailsBean json) {
                List<OddsDataEntity> mOddsData = json.getOddsData();

                if (mOddsData.size() != 0) {
//                    if (mAdapter == null) {
                    if (mOddsType.equals(BasketDetailsActivityTest.ODDS_EURO)) {
                        mAdapter = new BasketOddsDetailsAdapter(BasketOddsDetailsActivity.this, mOddsData, R.layout.basket_odds_details_item, true);
                    } else {
                        mAdapter = new BasketOddsDetailsAdapter(BasketOddsDetailsActivity.this, mOddsData, R.layout.basket_odds_details_item, false);
                    }
                    mListView2.setAdapter(mAdapter);
//                    }else{
//                        upDataAdapter(mOddsData);
//                    }
                    mListView2.setVisibility(View.VISIBLE);
                    mNoData.setVisibility(View.GONE);
                    mEerrorll.setVisibility(View.GONE);
                } else {
                    mListView2.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);
                    mEerrorll.setVisibility(View.GONE);
//                    mNoData.setText("暂无数据");
//                    mNoData.setText(getResources().getText(R.string.basket_nodata));
                }
                mRefresh.setRefreshing(false);
//                mListView2.setVisibility(View.VISIBLE);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
//                L.d("oddsID >>>>>>>>>>>>>>>>" , "Error");
                mListView2.setVisibility(View.GONE);
                mNoData.setVisibility(View.GONE);
                mEerrorll.setVisibility(View.VISIBLE);
//                mNoData.setText("网络异常");
                mRefresh.setRefreshing(false);
            }
        }, BasketDetailOddsDetailsBean.class);
    }
    /**
     * 子线程 处理数据加载
     */
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData(mOddsId);
        }
    };

    private void upDataAdapter(List<OddsDataEntity> datas){
        if (mAdapter == null) {
            return;
        }
        mAdapter.upDataList(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);
                initData(mOddsId);
            }
        },500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.basketball_odds_details_error_btn:

                mListView2.setVisibility(View.GONE);
                mNoData.setVisibility(View.GONE);
                mEerrorll.setVisibility(View.GONE);
                mRefresh.setRefreshing(true);
                mHandler.postDelayed(mRun , 500); // 加载数据
                break;
            default:
                break;
        }
    }

    private class ItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            /**
             * 更改选中状态
             */
            if (mAdapter2 != null) {
                mAdapter2.setChickedId(position);
            }

//            mOddsId , mOddsType
            mOddsId = mOddsCompanyList.get(position).getOddsId();
//            initData(mOddsId);
//            initData(mOddsId , mOddsType);

            mListView2.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
            mEerrorll.setVisibility(View.GONE);
            mRefresh.setRefreshing(true);

            mHandler.postDelayed(mRun , 500); // 加载数据

        }
    }
    class CompanyAdapter extends CommonAdapter<BasketDetailOddsBean.CompanyOddsEntity> {
        private int mPosition;
        public CompanyAdapter(Context context, List<BasketDetailOddsBean.CompanyOddsEntity> datas, int layoutId) {
            super(context, datas, layoutId);
            this.mContext = context;
        }

        /**
         * 选中传值
         * @param position
         */
        public void setChickedId(int position){
            if (position != mPosition) {
                mPosition = position;
                notifyDataSetChanged();
            }
        }
        @Override
        public void convert(ViewHolder holder, BasketDetailOddsBean.CompanyOddsEntity data) {
            if (data != null) {
                holder.setText(R.id.basket_odds_company_text , data.getCompany());
            }
            if (mPosition == holder.getPosition()) {
                holder.setBackgroundColor(R.id.basket_odds_company_text , getResources().getColor(R.color.whitesmoke));
            }else {
                holder.setBackgroundColor(R.id.basket_odds_company_text, getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
