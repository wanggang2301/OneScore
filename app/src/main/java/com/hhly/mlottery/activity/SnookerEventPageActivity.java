package com.hhly.mlottery.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.InforPopuWindowdapter;
import com.hhly.mlottery.adapter.football.TabsAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerRaceHeadBean;
import com.hhly.mlottery.bean.snookerbean.SnookerRefrshBean;
import com.hhly.mlottery.frame.scorefrag.SnookerDataQualificationHeatFragement;
import com.hhly.mlottery.frame.scorefrag.SnookerDatabaseFragment;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.greenrobot.event.EventBus;


/**
 * Created by yuely198 on 2017/2/13.
 * 斯诺克赛事内页
 */

public class SnookerEventPageActivity extends BaseActivity implements View.OnClickListener {


    private TextView public_txt_title;
    private FragmentManager fragmentManager;
    private TabsAdapter mTabsAdapter;
    private List<String> seasonList = new ArrayList<>();

    private static final int QUALIFICATIONS = 0;  //资格赛
    private static final int RACE = 1;         //正赛
    private static final int SUCCESSIVE = 2;    //历届冠军
    private static final int PROFILE = 3;          //赛事简介
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private PopupWindow popupWindow;//弹出框
    private LinearLayout mTxt_title1;//获取弹出框所在位置

    private String mIsCurenDatas;//默认赛季时间
    private InforPopuWindowdapter mAdapter;
    private TextView tv_right;
    private ImageView ib_operate_more;
    private LinearLayout text_times_title1;

    // private String LEAGUEID = "";

    private String leagueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   //注册EventBus
        EventBus.getDefault().register(this);
*/

        if (getIntent().getExtras() != null) {
            leagueId = getIntent().getExtras().getString("leagueId");
        }

        initView();
        initData();
    }


    /*获取内页头部数据*/
    private void initData() {

        String url = "http://m.1332255.com:81/mlottery/core/snookerData.findLeagueHeaderList.do";

        final Map<String, String> map = new HashMap();
        map.put("leagueId", leagueId);
        map.put("season", "");//默认不填是当前数据


        VolleyContentFast.requestJsonByPost(url, map, new VolleyContentFast.ResponseSuccessListener<SnookerRaceHeadBean>() {
            @Override
            public void onResponse(SnookerRaceHeadBean json) {
                if (json == null) {
                    return;
                }
                if (json.getResult() == 200) {

                    json.getData().getLeagueName();

                    public_txt_title.setText(json.getData().getLeagueName());
                    mIsCurenDatas = json.getData().getCurrentSeason();
                    tv_right.setText(mIsCurenDatas);
                    //获取赛季时
                    seasonList = json.getData().getSeasonList();
                    //获取赛事简介
                    if (json.getData().getLeagueProfile()!=null){
                        EventBus.getDefault().post(json.getData().getLeagueProfile());
                    }else{
                        EventBus.getDefault().post("nodata");
                    }



                    tv_right.setVisibility(View.VISIBLE);
                    ib_operate_more.setVisibility(View.VISIBLE);


                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, SnookerRaceHeadBean.class);


    }

    //显示下拉框
    private void showPopuWindows(View v) {

        /// 找到布局文件
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.task_detail_popupwindow, null);
        // 实例化listView
        final ListView listView = (ListView) layout.findViewById(R.id.lv_popup_list);
        // 设置listView的适配器

        if (mAdapter == null) {

            mAdapter = new InforPopuWindowdapter(this, seasonList);
        }

        listView.setAdapter(mAdapter);

        // 实例化一个PopuWindow对象
        popupWindow = new PopupWindow(v);
        // 设置弹框的宽度为布局文件的宽
        popupWindow.setWidth(text_times_title1.getWidth());
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
        popupWindow.showAsDropDown(text_times_title1);

        // listView的item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int postion, long arg3) {
                // TODO Auto-generated method stub
                view.findViewById(R.id.tv_list_item).setBackgroundColor(getResources().getColor(R.color.bg));
                //显示选择的赛季数据
                tv_right.setText(seasonList.get(postion));// 设置所选的item作为下拉框的标题
                EventBus.getDefault().post(new SnookerRefrshBean(seasonList.get(postion).toString()));
                // 弹框消失
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

    }


    private void initView() {
        setContentView(R.layout.activity_snooker);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);

        findViewById(R.id.public_img_back).setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tab_Fragment);
        viewPager = (ViewPager) findViewById(R.id.vp_Fragment_pager);

        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);

        text_times_title1 = (LinearLayout) findViewById(R.id.text_times_title);
        ib_operate_more = (ImageView) findViewById(R.id.ib_operate_more);

        String[] titles = getApplicationContext().getResources().getStringArray(R.array.snooker_info_tabs);
        fragmentManager = getSupportFragmentManager();

        mTabsAdapter = new TabsAdapter(fragmentManager);
        mTabsAdapter.setTitles(titles);
        mTabsAdapter.addFragments(SnookerDatabaseFragment.newInstance(QUALIFICATIONS, leagueId),
                SnookerDataQualificationHeatFragement.newInstance(RACE, leagueId),
                SnookerDatabaseFragment.newInstance(SUCCESSIVE, leagueId),
                SnookerDatabaseFragment.newInstance(PROFILE, leagueId)
        );

        viewPager.setOffscreenPageLimit(3);//设置预加载页面的个数。
        viewPager.setAdapter(mTabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        // tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.public_img_back:
                finish();
                break;
            case R.id.tv_right:
                showPopuWindows(v);
                break;
            default:
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }
}
