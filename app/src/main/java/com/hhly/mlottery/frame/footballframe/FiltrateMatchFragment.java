package com.hhly.mlottery.frame.footballframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.FiltrateMatchAdapter;
import com.hhly.mlottery.adapter.FiltrateMatchAdapter.CheckedChangeListener;
import com.hhly.mlottery.adapter.FiltrateMatchAdapter.ClickChangeListener;
import com.hhly.mlottery.bean.HotFocusLeagueCup;
import com.hhly.mlottery.bean.LeagueCup;
import com.hhly.mlottery.callback.RequestHostFocusCallBack;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.HotFocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.GrapeGridView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chenml
 * @ClassName: FiltrateMatchFragment
 * @Description: 赛事选择fragment   screen
 * @date 2015-10-20 下午2:07:09
 */
public class FiltrateMatchFragment extends Fragment implements OnClickListener {

    private final static String TAG = "FiltrateMatchFragment";

    //private GridView mGridView;
    private final static String FG_ID = "fgid";

    private final static String ALL_CUPS = "allcupss";
    private final static String CHECKED_CUPS = "checkedcups";
    private final static String CHECKED_DEFUALT = "checked_defualt";

    private final int ROLLBALL_FRAGMENT = 0;
    private final int IMMEDIA_FRAGMENT = 1;
    private final int RESULT_FRAGMENT = 2;
    private final int SCHEDULE_FRAGMENT = 3;
    private final int FOCUS_FRAGMENT = 4;

    private FiltrateMatchAdapter mAdapterHot;
    private FiltrateMatchAdapter mAdapterOther;


    /**
     * 全选按钮
     */
    private TextView mSelectAllBtn;
    /**
     * 反选按钮
     */
    private TextView mSelectInverseBtn;
    /**
     * 热门按钮
     */
    private TextView mSelectHotBtn;
    private TextView mSelectFocusBtn;

    /**
     * 全部联赛
     */
    private List<LeagueCup> mAllCups;
    /**
     * 只是用来计算被选中的比赛场次
     */
    private List<LeagueCup> mSelectedCups;
    /**
     * 已选择的联赛id
     */
    private LinkedList<String> mCheckedIds;

    /**
     * “隐藏”个数
     */
    private TextView mHideNumber;

    /**
     * 全部场次个数
     */
    private int mAllSize;
    /**
     *
     */
    private int mSelectedSize;//

    private boolean isCheckedDefualt;

    private GrapeGridView mGrapeGridViewHot;
    private GrapeGridView mGrapeGridViewOther;

    private int mCurrentFgId = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FiltrateMatchFragment newInstance(int fgId, Parcelable[] cups, Parcelable[] checkedCups, boolean isDefualt) {
        FiltrateMatchFragment fragment = new FiltrateMatchFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(FG_ID, fgId);
        bundle.putParcelableArray(ALL_CUPS, cups);
        bundle.putParcelableArray(CHECKED_CUPS, checkedCups);
        bundle.putBoolean(CHECKED_DEFUALT, isDefualt);
        fragment.setArguments(bundle);

        return fragment;
    }

    private int columns = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frage_filtrate_match, container, false);

        //mGridView = (GridView) view.findViewById(R.id.filtrate_match_gridview);
        mGrapeGridViewHot = (GrapeGridView) view.findViewById(R.id.filtrate_match_gridview_hot);
        mGrapeGridViewOther = (GrapeGridView) view.findViewById(R.id.filtrate_match_gridview_other);
        // 国际版3列
        if (AppConstants.isGOKeyboard) {
            columns = 3;
        } else {
            columns = 4;
        }
        //mGridView.setNumColumns(columns);
        mGrapeGridViewHot.setNumColumns(columns);
        mGrapeGridViewOther.setNumColumns(columns);

        mHideNumber = (TextView) view.findViewById(R.id.filtrate_match_hide_number);

        Bundle bundle = getArguments();
        Parcelable[] cups = bundle.getParcelableArray(ALL_CUPS);
        mAllCups = new ArrayList<>();
        if (cups != null && cups.length > 0) {
            for (Parcelable cup : cups) {
                mAllCups.add((LeagueCup) cup);
            }
        }

        mCurrentFgId = bundle.getInt(FG_ID);

        Parcelable[] checkedCups = bundle.getParcelableArray(CHECKED_CUPS);
        mCheckedIds = new LinkedList<>();
        mSelectedCups = new ArrayList<>();

        isCheckedDefualt = bundle.getBoolean(CHECKED_DEFUALT);
        if (!isCheckedDefualt && checkedCups != null && checkedCups.length > 0) {
            for (Parcelable cup : checkedCups) {
                // mCheckedIds.add(((LeagueCup) cup).getRaceId());
                mSelectedCups.add((LeagueCup) cup);
            }
        }


        //设置本地保存的checkId
        setFilterCheckId();


        mAllSize = ComputeRaceTotal(mAllCups);
        mSelectedSize = ComputeSelectRaceTotal(mSelectedCups);
        mHideNumber.setText(String.valueOf(mAllSize - mSelectedSize));

        mSelectAllBtn = (TextView) view.findViewById(R.id.filtrate_match_all_btn);
        mSelectInverseBtn = (TextView) view.findViewById(R.id.filtrate_match_inverse_btn);
        mSelectHotBtn = (TextView) view.findViewById(R.id.filtrate_match_hot_btn);
        mSelectFocusBtn = (TextView) view.findViewById(R.id.filtrate_match_focus_btn);

        mSelectAllBtn.setOnClickListener(this);
        mSelectInverseBtn.setOnClickListener(this);
        mSelectHotBtn.setOnClickListener(this);
        mSelectFocusBtn.setOnClickListener(this);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                newAdapter();
            }
        });

        return view;
    }

    private void setFilterCheckId() {
        List<String> list = new ArrayList<>();
        switch (mCurrentFgId) {
            case ROLLBALL_FRAGMENT:
                list.addAll((ArrayList) PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_ROLL));
                break;
            case IMMEDIA_FRAGMENT:
                list.addAll((ArrayList) PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_IMMEDIA));
                break;
            case RESULT_FRAGMENT:
                list.addAll((ArrayList) PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_RESULT));
                break;
            case SCHEDULE_FRAGMENT:
                list.addAll((ArrayList) PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_SCHEDULE));
                break;
            default:
                break;
        }

        L.d("ddddeee", "shuai==" + list.size() + "");

        for (String id : list) {
            mCheckedIds.add(id);
        }
    }

    private void newAdapter() {
        HotFocusUtils hotFocusUtils = new HotFocusUtils();
        hotFocusUtils.loadHotFocusData(getActivity(), new RequestHostFocusCallBack() {
            @Override
            public void callBack(HotFocusLeagueCup hotFocusLeagueCup) {

                List<String> hotList = null;

                if (hotFocusLeagueCup == null) {
                    hotList = new ArrayList<>();
                } else {
                    hotList = hotFocusLeagueCup.getHotLeagueIds();
                }

                List<LeagueCup> hotsTemp = new ArrayList<>();
                List<LeagueCup> normalTemp = new ArrayList<>();

                for (LeagueCup leagueCup : mAllCups) {
                    if (hotList.contains(leagueCup.getRaceId())) {
                        hotsTemp.add(leagueCup);
                    } else {
                        normalTemp.add(leagueCup);
                    }
                }

//				mAllCups.clear();
//
//				LeagueCup cupTitle = new LeagueCup();//标题和灰色的地方
//				cupTitle.setType(LeagueCup.TYPE_TITLE);
//				cupTitle.setRacename(getResources().getString(R.string.hot_score_txt));
//				mAllCups.add(cupTitle);
//
//				for(int n=0;n<columns-1;n++){
//					LeagueCup cupBlank = new LeagueCup();
//					cupBlank.setType(LeagueCup.TYPE_BLANK);
//					mAllCups.add(cupBlank);
//				}
//				mAllCups.addAll(hotsTemp);//筛选选项
//
//				int remainder = hotsTemp.size() % columns;
//
//				L.d(TAG, "remainder = " + remainder);
//
//				if (remainder != 0) {//选项后面多出来的空白处
//					for (int n = 0; n < (columns - remainder); n++) {
//						LeagueCup cupBlank = new LeagueCup();
//						cupBlank.setType(LeagueCup.TYPE_BLANK_AFTER_CUP);
//						mAllCups.add(cupBlank);
//					}
//				}
//
//				LeagueCup cupTitle2 = new LeagueCup();//标题和灰色的地方
//				cupTitle2.setType(LeagueCup.TYPE_TITLE);
//				cupTitle2.setRacename(getResources().getString(R.string.other_score_txt));
//				mAllCups.add(cupTitle2);
//
//				for(int n=0;n<columns-1;n++){
//					LeagueCup cupBlank = new LeagueCup();
//					cupBlank.setType(LeagueCup.TYPE_BLANK);
//					mAllCups.add(cupBlank);
//				}
//
//				mAllCups.addAll(normalTemp);//筛选选项

                mAdapterHot = new FiltrateMatchAdapter(getActivity(), hotsTemp, mCheckedIds, R.layout.item_filtrate_match);
                mAdapterHot.setCheckedChangeListener(new CheckedChangeListener() {
                    @Override
                    public void onChanged(CompoundButton buttonView, boolean isChecked) {

                    }
                });

                ClickChangeListener clickChangeListener = new ClickChangeListener() {

                    @Override
                    public void onClick(CompoundButton buttonView) {
                        String targetId = (String) buttonView.getTag();
                        LeagueCup targetCup = null;
                        for (LeagueCup cup : mAllCups) {
                            if (targetId.equals(cup.getRaceId())) {
                                targetCup = cup;
                                break;
                            }
                        }

                        if (targetCup != null) {
                            L.e(TAG, "checked");
                            if (buttonView.isChecked()) {

                                if (isCheckedDefualt) {
                                    mSelectedSize = targetCup.getCount();
                                } else {
                                    mSelectedSize += targetCup.getCount();
                                }
                                mHideNumber.setText(String.valueOf(mAllSize - mSelectedSize));
                            } else {
                                mSelectedSize -= targetCup.getCount();
                                mHideNumber.setText(String.valueOf(mAllSize - mSelectedSize));
                            }

                        }

                        isCheckedDefualt = false;
                    }
                };

                mAdapterHot.setClickChangeListener(clickChangeListener);
                mGrapeGridViewHot.setAdapter(mAdapterHot);

                mAdapterOther = new FiltrateMatchAdapter(getActivity(), normalTemp, mCheckedIds, R.layout.item_filtrate_match);
                mAdapterOther.setClickChangeListener(clickChangeListener);
                mGrapeGridViewOther.setAdapter(mAdapterOther);
            }
        });

    }

    public interface CheckedCupsCallback {
        void onChange(LinkedList<String> checkedCups);
    }

    public LinkedList<String> getCheckedCups() {

        return mCheckedIds;
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.filtrate_match_all_btn:
                MobclickAgent.onEvent(getContext(), "Football_Filtrate_All");
                mCheckedIds.clear();
                // mCheckedIds = new LinkedList<String>();
                for (LeagueCup cup : mAllCups) {
                    mCheckedIds.add(cup.getRaceId());
                }
                updateAdapter();
                mSelectedSize = mAllSize;
                mHideNumber.setText("0");
                break;

            case R.id.filtrate_match_inverse_btn:
                MobclickAgent.onEvent(getContext(), "Football_Filtrate_Inverse");
                int count = 0;
                LinkedList<String> temp = new LinkedList<>();
                for (LeagueCup cup : mAllCups) {
                    if (!mCheckedIds.contains(cup.getRaceId())) {
                        temp.add(cup.getRaceId());
                        count += cup.getCount();
                    }
                }

                mCheckedIds = temp;
                mSelectedSize = count;
                updateAdapter();

                mHideNumber.setText(String.valueOf(mAllSize - mSelectedSize));
                break;
            case R.id.filtrate_match_hot_btn:
            case R.id.filtrate_match_focus_btn:

                HotFocusUtils hotFocusUtils = new HotFocusUtils();
                hotFocusUtils.loadHotFocusData(getActivity(), new RequestHostFocusCallBack() {
                    @Override
                    public void callBack(HotFocusLeagueCup hotFocusLeagueCup) {

                        LinkedList<String> temp = new LinkedList<String>();
                        int count = 0;

                        if (v.getId() == R.id.filtrate_match_hot_btn) {// 热点
                            MobclickAgent.onEvent(getContext(), "Football_Filtrate_HotBtn");
                            List<String> hots = hotFocusLeagueCup.getHotLeagueIds();
                            for (LeagueCup cup : mAllCups) {
                                if (hots.contains(cup.getRaceId())) {
                                    temp.add(cup.getRaceId());
                                    count += cup.getCount();
                                }
                            }
                        } else {
                            MobclickAgent.onEvent(getContext(), "Football_Filtrate_Focus");
                            List<String> focuss = hotFocusLeagueCup.getFocusLeagueIds();
                            for (LeagueCup cup : mAllCups) {
                                if (focuss.contains(cup.getRaceId())) {
                                    temp.add(cup.getRaceId());
                                    count += cup.getCount();
                                }
                            }
                        }

                        mCheckedIds = temp;
                        mSelectedSize = count;
                        updateAdapter();
                        mHideNumber.setText(String.valueOf(mAllSize - mSelectedSize));
                    }
                });

                break;
            default:
                break;
        }
    }

    private void updateAdapter() {
        if (mAdapterHot != null && mAdapterOther != null) {
            mAdapterHot.setCheckedIds(mCheckedIds);
            mAdapterHot.notifyDataSetChanged();
            mAdapterOther.setCheckedIds(mCheckedIds);
            mAdapterOther.notifyDataSetChanged();
        }
    }

    private int ComputeRaceTotal(List<LeagueCup> list) {
        int total = 0;
        for (LeagueCup leagueCup : list) {
            total += leagueCup.getCount();
        }
        return total;
    }


    private int ComputeSelectRaceTotal(List<LeagueCup> list) {
        int total = 0;
        for (LeagueCup leagueCup : list) {
            if (CollectionUtils.notEmpty(mCheckedIds)) {
                if (mCheckedIds.contains(leagueCup.getRaceId())) {
                    total += leagueCup.getCount();
                }
            } else {
                total += leagueCup.getCount();
            }
        }
        return total;
    }

}
