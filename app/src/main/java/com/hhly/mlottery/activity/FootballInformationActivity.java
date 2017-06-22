package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.InforFragmentAdapter;
import com.hhly.mlottery.adapter.InforPopuWindowdapter;
import com.hhly.mlottery.bean.footballDetails.IntegralBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footballframe.AgendalFragment;
import com.hhly.mlottery.frame.footballframe.IntegralFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description: 足球数据内页显示
 * @data: 2016/3/29 16:32
 */
public class FootballInformationActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "FootballInformationActivity";

    private static final int ISNET_SUCCESS = 11;//数据请求成功
    private static final int ISNET_RERROR = 22;//网络请求失败

    private List<List<IntegralBean.LangueScoreBean.ListBean>> childDataList = new ArrayList<>();//小组数据
    private List<IntegralBean.LangueScoreBean> mAllLangueScore;  //获取小组数据
    private List<IntegralBean.LeagueTimesBean.LeagueDateBean> leagueData;//获取联赛时间
    private ArrayList<String> mListDatas = new ArrayList<>();//赛季存储集合

    private TabLayout mFragment_Tablayout;
    private ViewPager mFragment_pager;
    private ImageView mBackImag;  //返回
    public String mStmLeaguesId;  //联赛
    private RelativeLayout ll_head_bar;  ///获取下拉框
    private TextView integral_datas;//获取下拉显示的数据
    private PopupWindow popupWindow;//弹出框
    private String mLeagueType;  //联赛类型
    private List<String>groupDataList = new ArrayList<>();//小组列表

    private int mSize;
    private String mTeam_abb;//联赛简称标头
    private TextView mTxt_title;
    private InforPopuWindowdapter mAdapter;
    private String mIsCurenDatas;//默认赛季时间
    private LinearLayout mTxt_title1;//获取弹出框所在位置
    private List<Fragment> listFragment;//fragment集合
    private List<String> titles;//fagment标题
    private IntegralFragment integralFragment;//积分榜
    private AgendalFragment agendalFragment;//赛程
    private InforFragmentAdapter mInforFragmentAdapter;
    private  boolean isLoadData=false;//判断是否加载成功数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.football_infor_library);
        /**不统计当前Activity，统计下面的Fragment*/
        MobclickAgent.openActivityDurationTrack(false);
        //获取联赛ID
        Intent i = getIntent();
        mStmLeaguesId = i.getStringExtra("lid");
        mLeagueType = i.getStringExtra("leagueType");

        initView();//初始化视图
        initViewPager();
        initEvent();
        intiData(true);

}


    //初始化viewPager
    private void initViewPager() {
        mFragment_Tablayout = (TabLayout) findViewById(R.id.tab_Fragment_title);
        mFragment_pager = (ViewPager) findViewById(R.id.vp_Fragment_pager);
        titles = new ArrayList<>();
        //添加标头
        titles.add(getString(R.string.information_integral_text));
        titles.add(getString(R.string.information_agendal_text));
        listFragment = new ArrayList<>();
        //积分榜
        integralFragment = IntegralFragment.newInstance();
        listFragment.add(integralFragment);
        //赛程
        agendalFragment = AgendalFragment.newInstance();
        listFragment.add(agendalFragment);
        mInforFragmentAdapter = new InforFragmentAdapter(getSupportFragmentManager(), listFragment, titles);
        mFragment_pager.setAdapter(mInforFragmentAdapter);
        mFragment_pager.setOffscreenPageLimit(2);
        //设置Labyout
        mFragment_Tablayout.setTabMode(TabLayout.MODE_FIXED);
        //TabLayout加载viewpager
        mFragment_Tablayout.setupWithViewPager(mFragment_pager);

    }

    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ISNET_SUCCESS:
                    if(groupDataList!=null&&childDataList!=null&mLeagueType!=null) {
                        integralFragment.initData(groupDataList, childDataList, mLeagueType);
                    }
                    if(mStmLeaguesId!=null&&mLeagueType!=null&&mIsCurenDatas!=null) {
                        agendalFragment.getLeagueRoundDataFromNet(mStmLeaguesId, mLeagueType, mIsCurenDatas);
                    }

                    // agendalFragment.getLeagueRoundDataFromNet(mStmLeaguesId, mLeagueType, mIsCurenDatas);
                    break;
                case ISNET_RERROR:
                     integralFragment.requestFail();
                     agendalFragment.requestFail();
                    break;
            }
        }
    };

    //网络请求数据
    public void intiData(final boolean isFrist) {


        String url = BaseURLs.URL_FOOT_QLEAGUEDATE;
        Map<String, String> myPostParams = new HashMap<>();
        //第二次请求需要日期
        if (!isFrist && mIsCurenDatas != null) {
            myPostParams.put("leagueDate", mIsCurenDatas);
        }
        myPostParams.put("type", mLeagueType);
        myPostParams.put("leagueId", mStmLeaguesId);
        //请求数据
        VolleyContentFast.requestJsonByGet(url, myPostParams, new VolleyContentFast.ResponseSuccessListener<IntegralBean>() {

            @Override
            public synchronized void onResponse(final IntegralBean json) {
                if (json != null) {//如果json不为空，清除数据（刷新的时候）

                    groupDataList.clear();
                    childDataList.clear();
                    mListDatas.clear();

                   // isLoadData=true;//加载数据成功

                    //获取默认积分榜数据
                    mAllLangueScore = json.getLangueScore();
                    if( mAllLangueScore !=null) {
                        mSize = mAllLangueScore.size();
                        //获取子类父类数据
                        for (int i = 0; i < mSize; i++) {
                            //循环添加父view数据  可扩展view
                            groupDataList.add(mAllLangueScore.get(i).getGroup());
                            childDataList.add(mAllLangueScore.get(i).getList());
                        }

                        //设置联赛标头
                        mTeam_abb = json.getLeagueTimes().getAbb();
                        mTxt_title.setText(mTeam_abb);

                        //获取联赛赛季时间
                        leagueData = json.getLeagueTimes().getLeagueDate();
                        for (int i = 0; i < leagueData.size(); i++) {
                            //判断是否为当前赛季
                            mListDatas.add(leagueData.get(i).getDate());
                            if (leagueData.get(i).isCurrentSeason() && (isFrist || mIsCurenDatas == null)) {
                                //遍历获取数据进行赋值控件
                                //文本框显示数据
                                mIsCurenDatas = leagueData.get(i).getDate();
                                integral_datas.setText(mIsCurenDatas);

                            }
                        }

                        mViewHandler.sendEmptyMessage(ISNET_SUCCESS);
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //网络请求失败
                mViewHandler.sendEmptyMessage(ISNET_RERROR);

            }
        }, IntegralBean.class);

    }

/*
    private void updatePage(int selectedPage) {
        // L.d(TAG, "____updatePage______");

        switch (selectedPage) {
            case INTEGRAL_FRAGMENT:
                //integralFragment.initData(groupDataList, childDataList, mLeagueType,mIsCurenDatas);
                //((IntegralFragment) listFragment.get(selectedPage)).initData(groupDataList, childDataList, mLeagueType);
                // integralFragment.initData(groupDataList, childDataList, mLeagueType);

                break;
            case AGENDAL_FRAGMENT:

                     if(!((AgendalFragment) listFragment.get(selectedPage)).getisLoadDataed()) {
                         ((AgendalFragment) listFragment.get(selectedPage)).getLeagueRoundDataFromNet(mStmLeaguesId, mLeagueType, mIsCurenDatas);
                     }

                break;
        }

    }
*/

    //事件
    private void initEvent() {

  /*      mFragment_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updatePage(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


        //获取联赛列表时间集
        //  listDatas=mIntegralFragment.setDatas();

        if (mListDatas != null) {

            ll_head_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopuWindows(v);//显示窗口
                }

            });

        } else {
            //如果数据为空
            integral_datas.setText("---------");
            integral_datas.setOnClickListener(this);
        }
    }

    //显示下拉框
    private void showPopuWindows(View v) {

        /// 找到布局文件
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.task_detail_popupwindow, null);
        // 实例化listView
        final ListView listView = (ListView) layout.findViewById(R.id.lv_popup_list);
        // 设置listView的适配器

            if (mAdapter == null) {

                mAdapter = new InforPopuWindowdapter(this, mListDatas);
        }

        listView.setAdapter(mAdapter);

        // 实例化一个PopuWindow对象
        popupWindow = new PopupWindow(v);
        // 设置弹框的宽度为布局文件的宽
        popupWindow.setWidth(mTxt_title1.getWidth());
        // 高度随着内容变化
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        popupWindow.setOutsideTouchable(true);
        // 设置焦点
        popupWindow.setFocusable(true);
        // 设置所在布局
        popupWindow.setContentView(layout);
        // 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
        popupWindow.showAsDropDown(mTxt_title1);

        // listView的item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int postion, long arg3) {
                MobclickAgent.onEvent(mContext,"Football_InformationFragment_Date");
                //获取选择下标数据
                mIsCurenDatas = mListDatas.get(postion);

                //显示选择的赛季数据
                integral_datas.setText(mIsCurenDatas);// 设置所选的item作为下拉框的标题

                // 弹框消失
                popupWindow.dismiss();
                popupWindow = null;
                //  mAdapter.addData(mListDatas);
                // mAdapter.notifyDataSetChanged();

                //当弹框消失后  请求数据
               // isLoadData=false;
                intiData(false);
               // agendalFragment.getLeagueRoundDataFromNet(mStmLeaguesId, mLeagueType, mListDatas.get(postion));
                // agendalFragment.onRefresh();

            }
        });

    }

    private void initView() {
        //获取联赛头标
        mTxt_title = (TextView) findViewById(R.id.txt_title);
        //获取赛季头标
        ll_head_bar = (RelativeLayout) findViewById(R.id.ll_head_bar);
        mTxt_title1 = (LinearLayout) findViewById(R.id.text_times_title);
        //赛季时间
        integral_datas = (TextView) findViewById(R.id.intggral_datas);
        //返回
        mBackImag = (ImageView) findViewById(R.id.img_back);
        mBackImag.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_back:
                setResult(Activity.RESULT_OK);
                finish();
                MobclickAgent.onEvent(mContext,"Football_InformationFragment_Exit");
                break;
            case R.id.intggral_datas:
                //Toast.makeText(mContext, "亲,请检查你的网络....", Toast.LENGTH_LONG).show();
                MobclickAgent.onEvent(mContext,"Football_InformationFragment_NotNet");
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewHandler.removeCallbacksAndMessages(null);
    }
}
