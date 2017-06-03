package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.FiltrateAdapter;
import com.hhly.mlottery.bean.basket.BasketMatchFilter;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.frame.footballframe.FiltrateMatchFragment;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketScoreImmedEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketScoreResultEventBusEntity;
import com.hhly.mlottery.frame.footballframe.eventbus.BasketScoreScheduleEventBusEntity;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MultipleBasketFilterListEvent;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.view.GrapeGridview;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author yixq
 * @Description: 篮球筛选页面Activity
 */
public class BasketFiltrateActivity extends BaseActivity implements View.OnClickListener, FiltrateMatchFragment.CheckedCupsCallback {

    private final static String TAG = "BasketFiltrateActivity";

    public final static String CHECKED_CUPS_IDS = "checkedCupIds";


    private List<BasketMatchFilter> mAllFilterDatas = new ArrayList<BasketMatchFilter>(); //所有的数据
    private List<BasketMatchFilter> mChickedFilterDatas = new ArrayList<BasketMatchFilter>(); //选中的数据
    private List<String> mCupAll;//所有联赛id;
    private List<String> mCupHot; //热门的id
    private List<String> mCupOther; //其它的id
    private List<String> mCupChicked; //选中的id


    private ImageView public_img_back;// 筛选界面返回

    private TextView filtrate_hot_btn; //热门
    private TextView filtrate_inverse_btn; //反选
    private TextView filtrate_all_btn; //全选
    private TextView filtrate_reset_btn; //重置


    private Button mSubmit; //确定 button ;

    private int mFragmentMatchId = 0;

    private View mNetworkExceptionLayout;  //当前网络不给力...
    private View mSubmitLayout; //确定按键

    private GrapeGridview gview_hot;
    private GrapeGridview givew_other;

    private List<Map<String, Object>> data_list;

    private FiltrateAdapter mAdapter1;
    private FiltrateAdapter mAdapter2;
    private int currentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_score);

        initView();
        initData();

    }

    public List<Map<String, Object>> getData(int[] icon, String[] name) {
        //icon和name的长度是相同的，这里任选其一都可以
        data_list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", name[i]);
            data_list.add(map);
        }
        return data_list;
    }


    private void initView() {
        TextView public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.basket_filtrate_tittle);

        gview_hot = (GrapeGridview) findViewById(R.id.filtrate_hot_gridview);
        givew_other = (GrapeGridview) findViewById(R.id.filtrate_other_gridview);

        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        public_img_back.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.number_back_icon));

        mNetworkExceptionLayout = findViewById(R.id.network_exception_layout);
        mSubmitLayout = findViewById(R.id.filtrate_submit_layout);

        filtrate_hot_btn = (TextView) findViewById(R.id.filtrate_hot_btn); //热门
        filtrate_inverse_btn = (TextView) findViewById(R.id.filtrate_inverse_btn); //反选
        filtrate_all_btn = (TextView) findViewById(R.id.filtrate_all_btn); //全选
        filtrate_reset_btn = (TextView) findViewById(R.id.filtrate_reset_btn); //重置

        filtrate_hot_btn.setOnClickListener(this);
        filtrate_inverse_btn.setOnClickListener(this);
        filtrate_all_btn.setOnClickListener(this);
        filtrate_reset_btn.setOnClickListener(this);

        findViewById(R.id.public_btn_set).setVisibility(View.GONE); //隐藏设置按键
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE); //隐藏筛选按键

        mNetworkExceptionLayout.setVisibility(View.GONE);
        mSubmitLayout.setVisibility(View.VISIBLE);

        mSubmit = (Button) findViewById(R.id.filtrate_submit_btn);
        mSubmit.setOnClickListener(this);
    }

    private void initData() {


        Serializable mSerializableAll = getIntent().getSerializableExtra("MatchAllFilterDatas"); //取值 Serializable
        Serializable mSerialIzableChicked = getIntent().getSerializableExtra("MatchChickedFilterDatas"); // 选中的
        mAllFilterDatas = (List<BasketMatchFilter>) mSerializableAll;//赋值==>> 得到List<BasketMatchFilter> 对象 （所有的数据）
        mChickedFilterDatas = (List<BasketMatchFilter>) mSerialIzableChicked; // 选中的
        currentId = getIntent().getIntExtra("currentfragment", 0);

        if (mAllFilterDatas == null) {
            return;
        }

        /**
         * 遍历所有，区分热门 其他
         */
        List<BasketMatchFilter> hotdatas = new ArrayList<>(); // 已有热门
        List<BasketMatchFilter> otherdatas = new ArrayList<>();//已有其他

        for (BasketMatchFilter filter : mAllFilterDatas) {
            String leagueId = filter.getLeagueId();
            if (leagueId.equals("1") || leagueId.equals("5") || leagueId.equals("15") ||
                    leagueId.equals("21") || leagueId.equals("22") || leagueId.equals("57") || leagueId.equals("377")) {
                hotdatas.add(filter);
            } else {
                otherdatas.add(filter);
            }
        }

        /**
         * 热门赛事为空时
         */
        if (hotdatas.size() == 0) {
            List<BasketMatchFilter> nohotdatas = new ArrayList<>();
            BasketMatchFilter matchlis = new BasketMatchFilter();
            matchlis.setLeagueName(this.getString(R.string.basket_unfiltrate));
            matchlis.setLeagueId("-1");
            matchlis.setLeagueLogoUrl("");
            nohotdatas.add(matchlis);
            mAdapter1 = new FiltrateAdapter(this, nohotdatas, R.layout.basket_item_gv_sx);
            gview_hot.setAdapter(mAdapter1);
//            givew_other.setAdapter(mAdapter1);
        }
        /**
         * 其他赛事为空时
         */
        if (otherdatas.size() == 0) {
            List<BasketMatchFilter> nootherdatas = new ArrayList<>();
            BasketMatchFilter matchlis = new BasketMatchFilter();
            matchlis.setLeagueName(this.getString(R.string.basket_unfiltrate));
            matchlis.setLeagueId("-1");
            matchlis.setLeagueLogoUrl("");
            nootherdatas.add(matchlis);

            mAdapter2 = new FiltrateAdapter(this, nootherdatas, R.layout.basket_item_gv_sx);
            givew_other.setAdapter(mAdapter2);
        }

        List<BasketMatchFilter> mHotFilterDatas = new ArrayList<>();//热门
        List<BasketMatchFilter> mOtherFilterDatas = new ArrayList<>();//其它

        mCupAll = new ArrayList<>();
        mCupOther = new ArrayList<>();
        mCupHot = new ArrayList<>();
        mCupChicked = new ArrayList<>();

        //遍历选中数据 得到选中id
        for (BasketMatchFilter chickedFilter : mChickedFilterDatas) {
            mCupChicked.add(chickedFilter.getLeagueId());//选中的id

            if (!chickedFilter.isChecked()) {
                chickedFilter.setIsChecked(true);
            }
        }

        //遍历所有数据  区分热门;
        for (BasketMatchFilter filter : mAllFilterDatas) {

            mCupAll.add(filter.getLeagueId());//所有联赛的  id
            String leagueId = filter.getLeagueId();
            /***|| leagueId.equals("8") NCAA (产品要求) 从热门中去除*/


            if (leagueId.equals("1") || leagueId.equals("5") || leagueId.equals("15") || leagueId.equals("21") || leagueId.equals("22") || leagueId.equals("57") || leagueId.equals("377")) {
                mHotFilterDatas.add(filter); // 热门的数据
                mCupHot.add(filter.getLeagueId());//热门id
            } else {
                mOtherFilterDatas.add(filter); // 其它的数据
                mCupOther.add(filter.getLeagueId());//其它id
            }


            mCupChicked.clear();
            //遍历选中数据 得到选中id

            List<String> localSaveIds = getLocalSaveLeagueIds();

            if (localSaveIds.size() > 0) {


                for (String chickedFilter : localSaveIds) {
                    //选中的id
                    mCupChicked.add(chickedFilter);
                    //标记选中
                    if (chickedFilter.equals(filter.getLeagueId())) {
                        filter.setIsChecked(true);
                    }

                }

            } else {
                for (BasketMatchFilter chickedFilter : mChickedFilterDatas) {
                    //选中的id
                    mCupChicked.add(chickedFilter.getLeagueId());
                    //标记选中
                    if (chickedFilter.getLeagueId().equals(filter.getLeagueId())) {
                        filter.setIsChecked(true);
                    }

                }
            }


        }

        FiltrateAdapter.OnCheckListener onCheckListener = new FiltrateAdapter.OnCheckListener() {
            @Override
            public void onCheck(BasketMatchFilter mFilter) {
                MobclickAgent.onEvent(mContext, "Basketball_Filter_Givew");
                if (!mFilter.isChecked()) {//不选中->选中
                    mCupChicked.add(mFilter.getLeagueId());//添加id
                    mFilter.setIsChecked(true);
                } else {//选中->不选中
                    mCupChicked.remove(mFilter.getLeagueId());//移除id
                    mFilter.setIsChecked(false);
                }
            }
        };

        /**
         * 防止 热门有其他无 或者 热门无其他有情况
         */
        if (mAdapter1 == null) {
            mAdapter1 = new FiltrateAdapter(this, mHotFilterDatas, R.layout.basket_item_gv_sx); //热门
            mAdapter1.setOnCheckListener(onCheckListener);
            gview_hot.setAdapter(mAdapter1);
        }
        if (mAdapter2 == null) {
            mAdapter2 = new FiltrateAdapter(this, mOtherFilterDatas, R.layout.basket_item_gv_sx);//其它
            mAdapter2.setOnCheckListener(onCheckListener);
            givew_other.setAdapter(mAdapter2);
        }

    }


    private final int IMMEDIA_FRAGMENT = 0;
    private final int RESULT_FRAGMENT = 1;
    private final int SCHEDULE_FRAGMENT = 2;


    private List<String> getLocalSaveLeagueIds() {

        List<String> list = new ArrayList<>();
        switch (currentId) {
            case IMMEDIA_FRAGMENT:
                list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.BASKET_IMMEDIA);

                break;

            case RESULT_FRAGMENT:
                list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.BASKET_RESULT);

                break;
            case SCHEDULE_FRAGMENT:
                list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.BASKET_SCHEDULE);

                break;
            default:
                break;


        }

        return list;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:// 关闭(返回)
                MobclickAgent.onEvent(mContext, "Basketball_Filter_Exit");
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.filtrate_submit_btn:
                MobclickAgent.onEvent(mContext, "Basketball_Filter_Save");

                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                String[] checkedCups = mCupChicked.toArray(new String[]{});
//                System.out.println(">>>>>>>>>>>>>>--checkedCups.length-->>>>>>>>>>>>>>" + checkedCups.length + ">>>>>>>>>>>>>>");

                bundle.putCharSequenceArray(CHECKED_CUPS_IDS, checkedCups);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                if (mCupChicked.size() <= 0) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.at_least_one_race), Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("checkedCupIds", mCupChicked);

                    if (currentId == 0) {
                        EventBus.getDefault().post(new BasketScoreImmedEventBusEntity(map));
                    } else if (currentId == 1) {
                        EventBus.getDefault().post(new BasketScoreResultEventBusEntity(map));
                    } else if (currentId == 2) {
                        EventBus.getDefault().post(new BasketScoreScheduleEventBusEntity(map));
                    } else if (currentId == 3) {
                        EventBus.getDefault().post(new MultipleBasketFilterListEvent(map));
                    }

                    L.d("currentId >>>>>>>>>>>", "currentId == >" + currentId);
                    finish();
                    overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                }
                break;

            case R.id.filtrate_hot_btn:
                MobclickAgent.onEvent(mContext, "Basketball_Filter_HotBtn");
                mCupChicked.clear();

                /**
                 * 热门按键（不带反选功能）
                 */
                for (BasketMatchFilter chickedCup : mAllFilterDatas) {
                    if (chickedCup.isHot()) { //热门选项
                        chickedCup.setIsChecked(true);
                        mCupChicked.add(chickedCup.getLeagueId());
                    } else {
                        chickedCup.setIsChecked(false);
                    }
                }
                mAdapter1.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();


                break;
            case R.id.filtrate_inverse_btn:
                MobclickAgent.onEvent(mContext, "Basketball_Filter_Inverse");
//                Toast.makeText(this,"反选",Toast.LENGTH_SHORT).show();

                mCupChicked.clear();
                for (BasketMatchFilter chickedCup : mAllFilterDatas) {
                    if (chickedCup.isChecked()) {
                        chickedCup.setIsChecked(false);
                    } else {
                        chickedCup.setIsChecked(true);
                        mCupChicked.add(chickedCup.getLeagueId());
                    }
                }
                mAdapter1.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();

                break;
            case R.id.filtrate_all_btn:
                MobclickAgent.onEvent(mContext, "Basketball_Filter_All");
//                Toast.makeText(this,"全选",Toast.LENGTH_SHORT).show();
                mCupChicked.clear();

                for (BasketMatchFilter chickedCup : mAllFilterDatas) {
                    chickedCup.setIsChecked(true);
                    mCupChicked.add(chickedCup.getLeagueId());
                }

                mAdapter1.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();
                break;
            case R.id.filtrate_reset_btn:
                MobclickAgent.onEvent(mContext, "Basketball_Filter_Reset");
//                Toast.makeText(this,"重置",Toast.LENGTH_SHORT).show();
                mCupChicked.clear();

                for (BasketMatchFilter chickedCup : mAllFilterDatas) {
                    boolean iscup = false;
                    for (BasketMatchFilter cupid : mChickedFilterDatas) {
                        if (chickedCup.getLeagueId().equals(cupid.getLeagueId())) {
                            iscup = true;
                        }
                    }
                    if (iscup) {
                        chickedCup.setIsChecked(true);
                        mCupChicked.add(chickedCup.getLeagueId());
                    } else {
                        chickedCup.setIsChecked(false);
                    }
                }

                mAdapter1.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onChange(LinkedList<String> checkedCups) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}