package com.hhly.mlottery.frame.basketballframe;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivity;
import com.hhly.mlottery.adapter.basketball.BasketOddsAdapter;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketDetailOddsBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.ObservableListView;
import com.hhly.mlottery.view.ScrollUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andy on 2016/4/13 11:24.
 * <p/>
 * 描述：篮球 欧赔亚盘 大小球
 */
public class BasketOddsAsiaLetFragment extends BasketDetailsBaseFragment<ObservableListView> {

    /**
     * 主listviewAdapter
     */
    private BasketOddsAdapter mOddsAdapter;
    /**
     * 主list数据
     */
    private List<BasketDetailOddsBean.CompanyOddsEntity> mOddsCompanyList = new ArrayList<>();


    private View mView;

    private ObservableListView listView;

    /**
     * 客胜
     */
    private TextView mTitleGuestWin;
    /**
     * 盘口
     */
    private TextView mTitleHandicap;

    /**
     * 主胜
     */
    private TextView mTitleHomeWin;

    //  private ListView mOddsListView;

    public BasketOddsAsiaLetFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_odds, container, false);
        listView = (ObservableListView) mView.findViewById(R.id.scroll);


        View paddingView = new View(getActivity());
        final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                flexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);
        //View view=mView.findViewById(R.id.basket_odds_title_layout);
        View view = View.inflate(getActivity(),R.layout.basket_odds_title,null);

        listView.addHeaderView(paddingView);
        listView.addHeaderView(view);
        View paddintviewButoom=new View(getActivity());
        AbsListView.LayoutParams lp1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
               5*flexibleSpaceImageHeight/3);
        paddintviewButoom.setLayoutParams(lp1);
        listView.addFooterView(paddintviewButoom);
        listView.setTouchInterceptionViewGroup((ViewGroup) mView.findViewById(R.id.basket_odds_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();

      //  mType=args.getString("type");

        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(listView, new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    int offset = scrollY % flexibleSpaceImageHeight;
                    listView.setSelectionFromTop(0, -offset);
                }
            });
            updateFlexibleSpace(scrollY, mView);
        } else {
            updateFlexibleSpace(0, mView);
        }
        listView.setScrollViewCallbacks(this);
        //  initView();
            initData();
//        for (int i = 0; i < 5; i++) {
//            BasketDetailOddsBean.CompanyOddsEntity companyOddsEntity = new BasketDetailOddsBean.CompanyOddsEntity("mdy" + i);
//            mOddsCompanyList.add(companyOddsEntity);
//        }
//        mOddsAdapter = new BasketOddsAdapter(getActivity(), mOddsCompanyList);
//        listView.setAdapter(mOddsAdapter);
        updateFlexibleSpace(0, mView);
        return mView;
    }


    private void initView() {
//        mTitleGuestWin= (TextView) mView.findViewById(R.id.basket_odds_guest_win);
//        mTitleHandicap= (TextView) mView.findViewById(R.id.basket_odds_handicap);
//        mTitleHomeWin= (TextView) mView.findViewById(R.id.basket_odds_home_win);
//      //  mOddsListView= (ListView) mView.findViewById(R.id.basket_odds_listview);

    }

    public void initData() {
        Map<String, String> params = new HashMap<>();
//        params.put("thirdId",  ((BasketDetailsActivity) getActivity()).getmThirdId());
        params.put("thirdId", "966397");
        params.put("oddsType", "asiaLet");
        VolleyContentFast.requestJsonByGet("http://192.168.10.242:8181/mlottery/core/basketballDetail.findOdds.do", params, new VolleyContentFast.ResponseSuccessListener<BasketDetailOddsBean>() {
            @Override
            public void onResponse(BasketDetailOddsBean oddsBean) {

                L.e("TAGGGGG", oddsBean.getThirdId());
                loadData(oddsBean);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, BasketDetailOddsBean.class);
    }

    /**
     * 请求到网络数据后加载数据
     */
    private void loadData(BasketDetailOddsBean oddsBean) {

        mOddsCompanyList = oddsBean.getCompanyOdds();
        mOddsAdapter = new BasketOddsAdapter(getActivity(), mOddsCompanyList,"");
        //  mOddsListView.setAdapter(mOddsAdapter);
        listView.setAdapter(mOddsAdapter);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setScrollY(int scrollY, int threshold) {
        View view = getView();
        if (view == null) {
            return;
        }
        ObservableListView listView = (ObservableListView) view.findViewById(R.id.scroll);
        if (listView == null) {
            return;
        }
        View firstVisibleChild = listView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            if (threshold < scrollY) {
                int baseHeight = firstVisibleChild.getHeight();
                position = scrollY / baseHeight;
                offset = scrollY % baseHeight;
            }
            listView.setSelectionFromTop(position, -offset);
        }
    }

    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {
//        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
//
//        View listBackgroundView = view.findViewById(R.id.list_background);
//
//        // Translate list background
//        ViewHelper.setTranslationY(listBackgroundView, Math.max(0, -scrollY + flexibleSpaceImageHeight));

        // Also pass this event to parent Activity
        BasketDetailsActivity parentActivity =
                (BasketDetailsActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.onScrollChanged(scrollY, (ObservableListView) view.findViewById(R.id.scroll));
        }
    }
}
