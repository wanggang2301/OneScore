package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketInfoGridAdapter;
import com.hhly.mlottery.adapter.basketball.ExpandableGridAdapter;
import com.hhly.mlottery.bean.basket.infomation.Be;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.bean.basket.infomation.Model;
import com.hhly.mlottery.bean.basket.infomation.NationalLeague;
import com.hhly.mlottery.callback.BasketInfomationCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/7/14 17:54
 * @des 篮球资料库列表项
 */
public class BasketInfomationFragment extends Fragment implements ExactSwipeRefrashLayout.OnRefreshListener {

    private static final String TAG = "BasketInfomationFragment";
    private static final String TYPE_PARM = "TYPE_PARM";
    private static final String HOT_MATCH = "hot";
    private static final int DATA_STATUS_LOADING = 1;
    private static final int DATA_STATUS_SUCCESS = 2;
    private static final int DATA_STATUS_ERROR = 3;
    private static final int NUM0 = 0;
    private static final int NUM1 = 1;
    private static final int NUM2 = 2;
    private static final int NUM3 = 3;


    private BasketInfomationCallBack basketInfomationCallBack;
    private ExactSwipeRefrashLayout mExactSwipeRefrashLayout;
    private BasketInfoGridAdapter mBasketInfoGridAdapterInter;
    private ExpandableGridAdapter mExpandableGridAdapter;
    private ExpandableListView expandableGridView;
    private RadioGroup radioGroup;
    private GridView gridviewInter;

    private int sign = -1;// 控制列表的展开
    private int childsign = -1;
    private View mView;
    private String mType;

    private List<String> hotRaceType; //热门
    private List<String> interRaceType; //洲际赛事
    private List<List<Be>> alldata;

    private List<List<NationalLeague>> interLeagues;
    private List<LeagueBean> hotLeagues;

    public static BasketInfomationFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_PARM, type);
        BasketInfomationFragment basketInfomationFragment = new BasketInfomationFragment();
        basketInfomationFragment.setArguments(bundle);
        return basketInfomationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE_PARM);
        }

/*

        if (mType == HOT) {
            L.d(TAG, "onCreate热门");

        } else if (mType == EUR) {
            L.d(TAG, "onCreate欧洲");

        } else if (mType == AMERICA) {
            L.d(TAG, "onCreateme美洲");

        } else if (mType == ASIA) {
            L.d(TAG, "onCreate亚洲");

        } else if (mType == AFRICA) {
            L.d(TAG, "onCreate非洲洲");

        } else if (mType == INTER) {
            L.d(TAG, "onCreate国际");

        }
*/


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_infomation, container, false);
       /* if (mType == HOT) {
            L.d(TAG, "onCreateView热门");

        } else if (mType == EUR) {
            L.d(TAG, "onCreateView欧洲");

        } else if (mType == AMERICA) {
            L.d(TAG, "onCreateView美洲");

        } else if (mType == ASIA) {
            L.d(TAG, "onCreateView亚洲");

        } else if (mType == AFRICA) {
            L.d(TAG, "onCreateView非洲洲");

        } else if (mType == INTER) {
            L.d(TAG, "onCreateView国际");

        }*/
        // L.d(TAG, mType + "--" + getUserVisibleHint() + "");


        initView();
        return mView;
    }

    private void initView() {
        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);
        gridviewInter = (GridView) mView.findViewById(R.id.gridview_inter);
        expandableGridView = (ExpandableListView) mView.findViewById(R.id.listview);
        mExactSwipeRefrashLayout = (ExactSwipeRefrashLayout) mView.findViewById(R.id.info_swiperefreshlayout);
        mExactSwipeRefrashLayout.setOnRefreshListener(this);
        mExactSwipeRefrashLayout.setColorSchemeResources(R.color.bg_header);
        mExactSwipeRefrashLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        //listview

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.info_inter_match:
                        gridviewInter.setVisibility(View.VISIBLE);
                        expandableGridView.setVisibility(View.GONE);
                        break;
                    case R.id.info_country_match:
                        gridviewInter.setVisibility(View.GONE);
                        expandableGridView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        if (HOT_MATCH.equals(mType)) {
            radioGroup.setVisibility(View.GONE);
        } else {
            radioGroup.setVisibility(View.VISIBLE);
        }


        new Handler().postDelayed(mLoadingDataThread, 0);

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case DATA_STATUS_LOADING:
                    break;

                case DATA_STATUS_SUCCESS:

                    break;

                case DATA_STATUS_ERROR:

                    break;

            }
        }
    };

    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            loadData();
        }
    };

    private void loadData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", mType);

        String url = BaseURLs.URL_BASKET_INFOMATION;

        /*VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<LeagueAllBean>() {

            @Override
            public void onResponse(LeagueAllBean json) {

                //if ()
                if (json.getSpecificLeague() != null) {
                    hotLeagues = json.getSpecificLeague();
                }

                if (json.getNationalLeague() != null) {
                    for (int t = 0; t < Math.ceil((double) json.getNationalLeague().size() / 4); t++) {
                        List<NationalLeague> list = new ArrayList<>();
                        for (int j = 0; j < 4; j++) {
                            if ((j + 4 * t) < list.size()) {
                                list.add(list.get(j + 4 * t));
                            }
                        }
                        interLeagues.add(list);
                    }
                }

                initViewData();


            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, LeagueAllBean.class);*/

        initViewData();

    }


    private void initViewData() {
        if (mType == HOT_MATCH) {//热门
            mBasketInfoGridAdapterInter = new BasketInfoGridAdapter(getActivity(), getDataHotRaceType());
            gridviewInter.setAdapter(mBasketInfoGridAdapterInter);

        } else { //欧洲、美洲、亚洲等

            //洲际赛事
            mBasketInfoGridAdapterInter = new BasketInfoGridAdapter(getActivity(), getDataInterRaceType());
            gridviewInter.setAdapter(mBasketInfoGridAdapterInter);
            alldata = Model.getdata2();

            basketInfomationCallBack = new BasketInfomationCallBack() {
                @Override
                public void onClick(View view, int groupPosition, int child) {
                    L.d("112", groupPosition + "--" + child + "--" + sign + "--" + childsign);


                    if (sign == -1 && childsign == -1) {
                        // 展开被选的group
                        expandableGridView.expandGroup(groupPosition);
                        // 设置被选中的group置于顶端
                        expandableGridView.setSelectedGroup(groupPosition);
                        sign = groupPosition;
                        childsign = child;
                        L.d("112", groupPosition + "--" + child + "--" + sign + "--" + childsign);

                        L.i("112", "第一次展开");


                    } else if (sign == groupPosition && child == childsign) {
                        expandableGridView.collapseGroup(sign);
                        sign = -1;
                        childsign = -1;

                        L.d("112", groupPosition + "--" + child + "--" + sign + "--" + childsign);

                        L.i("112", "关闭");
                        if (child == NUM0) {
                            ((ImageView) view.findViewById(R.id.iv0)).setVisibility(View.INVISIBLE);
                        } else if (child == NUM1) {
                            ((ImageView) view.findViewById(R.id.iv1)).setVisibility(View.INVISIBLE);

                        } else if (child == NUM2) {
                            ((ImageView) view.findViewById(R.id.iv2)).setVisibility(View.INVISIBLE);

                        } else if (child == NUM3) {
                            ((ImageView) view.findViewById(R.id.iv3)).setVisibility(View.INVISIBLE);

                        }

                        mExpandableGridAdapter.isInitChildAdapter = false;

                    } else if (sign != groupPosition) {
                        expandableGridView.collapseGroup(sign);
                        //view.


                        // 展开被选的group
                        expandableGridView.expandGroup(groupPosition);
                        // 设置被选中的group置于顶端
                        expandableGridView.setSelectedGroup(groupPosition);
                        sign = groupPosition;
                        childsign = child;
                        L.i("112", "关闭在展开");
                    }
                    childsign = child;
                }
            };
            mExpandableGridAdapter = new ExpandableGridAdapter(getActivity(), alldata);
            mExpandableGridAdapter.setBasketInfomationCallBack(basketInfomationCallBack);
            expandableGridView.setAdapter(mExpandableGridAdapter);
        }
    }

    @Override
    public void onRefresh() {
        L.d(TAG, "下拉刷新");
        //mExactSwipeRefrashLayout.setRefreshing(true);  //一直存在
        mExactSwipeRefrashLayout.setRefreshing(false);  //刷新消失

    }

    private List<String> getDataHotRaceType() {
        hotRaceType = new ArrayList<>();
        hotRaceType.add("CBA");
        hotRaceType.add("NBA");
        hotRaceType.add("DDD");
        hotRaceType.add("FFF");
        hotRaceType.add("TTT");
        hotRaceType.add("GGG");
        hotRaceType.add("HHH");
        hotRaceType.add("NNN");
        hotRaceType.add("TTT");
        hotRaceType.add("UUU");
        int size = hotRaceType.size() % 4 == 0 ? 0 : 4 - hotRaceType.size() % 4;
        for (int i = 0; i < size; i++) {
            hotRaceType.add("");
        }
        return hotRaceType;
    }

    private List<String> getDataInterRaceType() {
        interRaceType = new ArrayList<>();
        interRaceType.add("CBA");
        interRaceType.add("NBA");
        interRaceType.add("DDD");
        interRaceType.add("FFF");
        interRaceType.add("TTT");
        interRaceType.add("GGG");
        interRaceType.add("HHH");
        interRaceType.add("NNN");
        interRaceType.add("TTT");
        interRaceType.add("UUU");
        int size = interRaceType.size() % 4 == 0 ? 0 : 4 - interRaceType.size() % 4;

        for (int i = 0; i < size; i++) {
            interRaceType.add("");
        }
        return interRaceType;
    }


   /* @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
       // L.d(TAG, "setUserVisibleHint==" + isVisibleToUser);

    }*/
}
