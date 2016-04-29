package com.hhly.mlottery.frame.basketballframe;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketAnalyzeMoreRecordActivity;
import com.hhly.mlottery.activity.BasketDetailsActivity;
//import com.hhly.mlottery.activity.BasketOddsDetailsActivity;
import com.hhly.mlottery.adapter.basketball.BasketOddsDetailsAdapter;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketDetailOddsDetailsBean;
import com.hhly.mlottery.bean.basket.BasketDetails.OddsDataEntity;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.ObservableScrollView;
import com.hhly.mlottery.view.ScrollUtils;
import com.hhly.mlottery.view.Scrollable;
import com.hhly.mlottery.widget.NestedListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2016/4/15.
 */
public class BasketOddsDetailsFragment extends BasketDetailsBaseFragment<ObservableScrollView> {

    private View mView;
    private ObservableScrollView scrollView;
    private NestedListView mHistoryListView;
    private NestedListView mRecentListView1;
    private LinearLayout mLinear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.basket_odds_details, container, false);

        scrollView = (ObservableScrollView) mView.findViewById(R.id.scroll);
        scrollView.setTouchInterceptionViewGroup((ViewGroup) mView.findViewById(R.id.basket_odds_details_fragment_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, scrollY);
                }
            });
            updateFlexibleSpace(scrollY, mView);
        } else {
            updateFlexibleSpace(0, mView);
        }
        scrollView.setScrollViewCallbacks(this);

        initView();
        initData();

//        mHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext() , "position = " + position , Toast.LENGTH_SHORT).show();
////                mHistoryListView.smoothScrollToPositionFromTop(position, 0);
////                mRecentListView1.setAdapter(mAdapter);
//
//                Intent intent = new Intent(getActivity(), BasketOddsDetailsActivity.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
//
//            }
//        });
        return mView;
    }

    private void initView(){

        mHistoryListView = (NestedListView)mView.findViewById(R.id.basket_odds_company);
        mRecentListView1 = (NestedListView)mView.findViewById(R.id.basket_odds_company_details);
        mLinear = (LinearLayout) mView.findViewById(R.id.basket_list_ll);

    }

    private BasketOddsDetailsAdapter mAdapter;
    public void initData(){

        String url = "http://192.168.10.242:8181/mlottery/core/basketballDetail.findOddsDetail.do"; //?oddsId=2652491&lang=zh&oddsType=euro
        Map<String , String> map = new HashMap<>();
        map.put("oddsId", "2652491");
        map.put("oddsType" , "euro");

        VolleyContentFast.requestJsonByGet(url ,map, new VolleyContentFast.ResponseSuccessListener<BasketDetailOddsDetailsBean>() {
            @Override
            public void onResponse(BasketDetailOddsDetailsBean json) {

                String oddsID = json.getOddsId();
                List<OddsDataEntity> mOddsData = json.getOddsData();
                List<OddsDataEntity> mData = new ArrayList<>();

//                mData.add(mOddsData.get(0));

                for (OddsDataEntity data : mOddsData) {
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                    mData.add(data);
                }

                List<String>  mCompanyList = new ArrayList<>();
                mCompanyList.add("皇冠");
                mCompanyList.add("澳门");
                mCompanyList.add("易胜博");
                mCompanyList.add("bet365");
                mCompanyList.add("韦德");
                mCompanyList.add("皇冠");
                mCompanyList.add("澳门");
                mCompanyList.add("易胜博");
                mCompanyList.add("bet365");
                mCompanyList.add("韦德");
                mCompanyList.add("皇冠");
                mCompanyList.add("澳门");
                mCompanyList.add("易胜博");
                mCompanyList.add("bet365");
                mCompanyList.add("韦德");


//                mLinear.setPadding(0,0,0,R.dimen.y10);

//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
//                lp.setMargins(0,0,0,R.dimen.y10);
//                mLinear.setLayoutParams(lp);
                mAdapter = new BasketOddsDetailsAdapter(getContext() , mData , R.layout.basket_odds_details_item , true);
                mRecentListView1.setAdapter(mAdapter);

                CompanyAdapter mAdapter2 = new CompanyAdapter(getContext() , mCompanyList , R.layout.basket_odds_company_item);
                mHistoryListView.setAdapter(mAdapter2);

                L.d("oddsID >>>>>>>>>>>>>>>>" ,"success" + oddsID);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("oddsID >>>>>>>>>>>>>>>>" , "Error");
            }
        },BasketDetailOddsDetailsBean.class);
    }



    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {
        ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        // Also pass this event to parent Activity
        BasketDetailsActivity parentActivity = (BasketDetailsActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.onScrollChanged(scrollY, scrollView);
        }
    }
    @Override
    public void updateFlexibleSpace(int scrollY) {
        // Sometimes scrollable.getCurrentScrollY() and the real scrollY has different values.
        // As a workaround, we should call scrollVerticallyTo() to make sure that they match.
        Scrollable s = getScrollable();
        s.scrollVerticallyTo(scrollY);

        // If scrollable.getCurrentScrollY() and the real scrollY has the same values,
        // calling scrollVerticallyTo() won't invoke scroll (or onScrollChanged()), so we call it here.
        // Calling this twice is not a problem as long as updateFlexibleSpace(int, View) has idempotence.
        updateFlexibleSpace(scrollY, getView());
    }

    class CompanyAdapter extends CommonAdapter<String>{

        public CompanyAdapter(Context context, List<String> datas, int layoutId) {
            super(context, datas, layoutId);

            this.mContext = context;
        }

        @Override
        public void convert(ViewHolder holder, String data) {

            holder.setText(R.id.basket_odds_company_text , data);

        }
    }
}
