package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.hhly.mlottery.bean.basket.infomation.Model;
import com.hhly.mlottery.callback.BasketInfomationCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/7/14 17:54
 * @des 篮球资料库列表项
 */
public class BasketInfomationFragment extends Fragment {

    public static final int DATA_STATUS_LOADING = 1;
    public static final int DATA_STATUS_SUCCESS = 2;
    public static final int DATA_STATUS_ERROR = 3;

    private static final int NUM0 = 0;
    private static final int NUM1 = 1;
    private static final int NUM2 = 2;
    private static final int NUM3 = 3;


    private static final int HOT = 0;
    private static final int EUR = 1;
    private static final int AMERICA = 2;
    private static final int ASIA = 3;
    private static final int AFRICA = 4;
    private static final int INTER = 5;


    private BasketInfomationCallBack basketInfomationCallBack;

    private final static String TYPE_PARM = "TYPE_PARM";

    private final static int HOT_MATCH = 0;


    private RadioGroup radioGroup;
    private GridView gridviewInter;
    private ExpandableListView expandableGridView;

    private View mView;
    private int mType = 0;


    private BasketInfoGridAdapter mBasketInfoGridAdapterInter;
    private ExpandableGridAdapter adapter;

    private List<Map<String, Object>> list;
    private String[][] child_text_array;
    private int sign = -1;// 控制列表的展开

    private int childsign = -1;


    private List<String> hotRaceType; //热门

    private List<String> interRaceType; //洲际赛事

    private List<List<Be>> alldata;


    public static BasketInfomationFragment newInstance(int type) {

        Bundle bundle = new Bundle();

        bundle.putInt(TYPE_PARM, type);

        BasketInfomationFragment basketInfomationFragment = new BasketInfomationFragment();
        basketInfomationFragment.setArguments(bundle);
        return basketInfomationFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE_PARM);
        }


        if (mType==HOT){
            Log.d("123456", "onCreate热门");

        }else if (mType==EUR){
            Log.d("123456", "onCreate欧洲");

        }else if (mType==AMERICA){
            Log.d("123456", "onCreateme美洲");

        }else if (mType==ASIA){
            Log.d("123456", "onCreate亚洲");

        }else if (mType==AFRICA){
            Log.d("123456", "onCreate非洲洲");

        }else if (mType==INTER){
            Log.d("123456", "onCreate国际");

        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_infomation, container, false);
        if (mType==HOT){
            Log.d("123456", "onCreateView热门");

        }else if (mType==EUR){
            Log.d("123456", "onCreateView欧洲");

        }else if (mType==AMERICA){
            Log.d("123456", "onCreateView美洲");

        }else if (mType==ASIA){
            Log.d("123456", "onCreateView亚洲");

        }else if (mType==AFRICA){
            Log.d("123456", "onCreateView非洲洲");

        }else if (mType==INTER){
            Log.d("123456", "onCreateView国际");

        }


        initView();
        if (mType == HOT_MATCH) {
            radioGroup.setVisibility(View.GONE);
        } else {
            radioGroup.setVisibility(View.VISIBLE);
        }

        return mView;
    }

    private void initView() {
        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);
        gridviewInter = (GridView) mView.findViewById(R.id.gridview_inter);
        expandableGridView = (ExpandableListView) mView.findViewById(R.id.listview);

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

        new Handler().postDelayed(mLoadingDataThread, 0);


    }

    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            loadData();
        }
    };

    private void loadData() {


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
                    Log.d("112", groupPosition + "--" + child + "--" + sign + "--" + childsign);


                    if (sign == -1 && childsign == -1) {
                        // 展开被选的group
                        expandableGridView.expandGroup(groupPosition);
                        // 设置被选中的group置于顶端
                        expandableGridView.setSelectedGroup(groupPosition);
                        sign = groupPosition;
                        childsign = child;
                        Log.d("112", groupPosition + "--" + child + "--" + sign + "--" + childsign);

                        Log.i("112", "第一次展开");


                    } else if (sign == groupPosition && child == childsign) {
                        expandableGridView.collapseGroup(sign);
                        sign = -1;
                        childsign = -1;

                        Log.d("112", groupPosition + "--" + child + "--" + sign + "--" + childsign);

                        Log.i("112", "关闭");
                        if (child == NUM0) {
                            ((ImageView) view.findViewById(R.id.iv0)).setVisibility(View.INVISIBLE);
                        } else if (child == NUM1) {
                            ((ImageView) view.findViewById(R.id.iv1)).setVisibility(View.INVISIBLE);

                        } else if (child == NUM2) {
                            ((ImageView) view.findViewById(R.id.iv2)).setVisibility(View.INVISIBLE);

                        } else if (child == NUM3) {
                            ((ImageView) view.findViewById(R.id.iv3)).setVisibility(View.INVISIBLE);

                        }

                       /* view.findViewById(R.id.iv1).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.iv2).setVisibility(View.INVISIBLE);
                        view.findViewById(R.id.iv3).setVisibility(View.INVISIBLE);*/

                        adapter.isInitChildAdapter = false;

                    } else if (sign != groupPosition) {
                        expandableGridView.collapseGroup(sign);
                        //view.


                        // 展开被选的group
                        expandableGridView.expandGroup(groupPosition);
                        // 设置被选中的group置于顶端
                        expandableGridView.setSelectedGroup(groupPosition);
                        sign = groupPosition;
                        childsign = child;
                        Log.i("112", "关闭在展开");
                    }
                    childsign = child;
                }
            };
            adapter = new ExpandableGridAdapter(getActivity(), alldata);
            adapter.setBasketInfomationCallBack(basketInfomationCallBack);
            expandableGridView.setAdapter(adapter);
        }
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

}
