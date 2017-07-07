package com.hhly.mlottery.mvptask.bowl;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.BottomOddsAdapter;
import com.hhly.mlottery.bean.footballDetails.WebSocketRollballOdd;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.mvptask.IContract;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.HandicapUtils;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import data.bean.BottomOddsDetails;
import data.bean.BottomOddsDetailsItem;
import data.bean.BottomOddsItem;
import data.bean.FirstOdd;

/**
 * A simple {@link Fragment} subclass.
 */
public class BowlChildFragment extends ViewFragment<IContract.IBowlChildPresenter> implements IContract.IChildView {

    private static final int OUER_TYPE = 2;
    private static final int ALET_TYPE = 1;
    private static final int ASIZE_TYPE = 3;
    private static final int CORNER_TYPE = 4; //暂定为四

    private final static String ODD_TYPE = "odd_type";
    private final static String THIRDID = "thirdId";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    @BindView(R.id.host_txt)
    TextView hostTxt;
    @BindView(R.id.handicap_txt)
    TextView handicapTxt;
    @BindView(R.id.guest_txt)
    TextView guestTxt;
    @BindView(R.id.host_first)
    TextView hostFirst;
    @BindView(R.id.handicap_first)
    TextView handicapFirst;
    @BindView(R.id.guest_first)
    TextView guestFirst;
    @BindView(R.id.fl_loading)
    FrameLayout flLoading;
    @BindView(R.id.reLoading)
    TextView reLoading;
    @BindView(R.id.fl_networkError)
    FrameLayout flNetworkError;
    @BindView(R.id.fl_nodata)
    RelativeLayout flNodata;
    @BindView(R.id.ll_context)
    LinearLayout llContext;
    @BindView(R.id.handle_exception)
    LinearLayout handleException;


    private String mThirdId;
    private int oddType;
    private BottomOddsDetails bowlBean;
    private BottomOddsAdapter mAdapter;
    private Context context;
    private Activity mActivity;

    private List<BottomOddsDetailsItem> mBottomOddsDetailsItemList;


    public static BowlChildFragment newInstance(String thirdId, int oddType) {
        BowlChildFragment bowlChildFragment = new BowlChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString(THIRDID, thirdId);
        bundle.putInt(ODD_TYPE, oddType);
        bowlChildFragment.setArguments(bundle);
        return bowlChildFragment;
    }


    public BowlChildFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString(THIRDID);
            oddType = getArguments().getInt(ODD_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bowl_child, container, false);
        ButterKnife.bind(this, view);
        context = mActivity;

        //  mPresenter = new BowlChildPresenter(this);
        mPresenter.requestData(mThirdId, String.valueOf(oddType));
        return view;
    }


    @Override
    public IContract.IBowlChildPresenter initPresenter() {
        return new BowlChildPresenter(this);
    }

    private void setOddTypeText() {
        switch (oddType) {
            case OUER_TYPE:
                setTitleTxt(getResources().getStringArray(R.array.bowl_eur));
                break;
            case ALET_TYPE:
                setTitleTxt(getResources().getStringArray(R.array.bowl_alet));
                break;
            case ASIZE_TYPE:
                setTitleTxt(getResources().getStringArray(R.array.bowl_asize));
                break;
            case CORNER_TYPE:
                setTitleTxt(getResources().getStringArray(R.array.bowl_corner));
                break;
        }
    }

    public void onRefresh() {
        mPresenter.requestData(mThirdId, String.valueOf(oddType));
    }


    private void setTitleTxt(String[] names) {
        hostTxt.setText(names[0]);
        handicapTxt.setText(names[1]);
        guestTxt.setText(names[2]);
    }


    @Override
    public void loading() { //loading的view
        llContext.setVisibility(View.GONE);
        handleException.setVisibility(View.VISIBLE);
        flLoading.setVisibility(View.VISIBLE);
        flNetworkError.setVisibility(View.GONE);
        flNodata.setVisibility(View.GONE);
    }


    @Override
    public void responseData() {
        llContext.setVisibility(View.VISIBLE);
        handleException.setVisibility(View.GONE);
        flLoading.setVisibility(View.GONE);
        flNetworkError.setVisibility(View.GONE);
        flNodata.setVisibility(View.GONE);
        bowlBean = mPresenter.getBowlBean();

        mBottomOddsDetailsItemList = new ArrayList<>();

        mBottomOddsDetailsItemList.addAll(bowlBean.getMatchoddlist());

        setFirstOdd(bowlBean.getFirst());
        setOddTypeText();

        mAdapter = new BottomOddsAdapter(context, mBottomOddsDetailsItemList, oddType);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        L.d("bowl", bowlBean.getResult() + "");
    }

    public void updateFragmentOddList(WebSocketRollballOdd webSocketRollballOdd) {
        if (CollectionUtils.notEmpty(mBottomOddsDetailsItemList)) {
            L.d("bowlpush", webSocketRollballOdd.toString());
            mBottomOddsDetailsItemList.add(0, setLiveOdds(mBottomOddsDetailsItemList.get(0), webSocketRollballOdd));
            mAdapter.notifyDataSetChanged();
        }
    }


    private synchronized BottomOddsDetailsItem setLiveOdds(BottomOddsDetailsItem b, WebSocketRollballOdd webodds) {
        BottomOddsDetailsItem destination = new BottomOddsDetailsItem();
        BottomOddsItem bottomOddsItem = new BottomOddsItem();

        if (isNULLOrEmpty(webodds.getLeft()) || isNULLOrEmpty(webodds.getMiddle()) || isNULLOrEmpty(webodds.getRight()) || isNULLOrEmpty(b.getOdd().getLeft()) || isNULLOrEmpty(b.getOdd().getMiddle()) || isNULLOrEmpty(b.getOdd().getRight())) {

            if (isNULLOrEmpty(webodds.getLeft()) || isNULLOrEmpty(webodds.getMiddle()) || isNULLOrEmpty(webodds.getRight())) {
                bottomOddsItem.setLeft("0");
                bottomOddsItem.setMiddle("0");
                bottomOddsItem.setRight("0");
                bottomOddsItem.setLeftUp("0");
                bottomOddsItem.setMiddleUp("0");
                bottomOddsItem.setRightUp("0");
            } else {
                bottomOddsItem.setLeft(webodds.getLeft());
                bottomOddsItem.setMiddle(webodds.getMiddle());
                bottomOddsItem.setRight(webodds.getRight());
                bottomOddsItem.setLeftUp("0");
                bottomOddsItem.setMiddleUp("0");
                bottomOddsItem.setRightUp("0");
            }


        } else {

            if (Float.parseFloat(b.getOdd().getLeft()) > Float.parseFloat(webodds.getLeft())) {
                bottomOddsItem.setLeftUp("-1");
            } else if (Float.parseFloat(b.getOdd().getLeft()) < Float.parseFloat(webodds.getLeft())) {
                bottomOddsItem.setLeftUp("1");
            } else {
                bottomOddsItem.setLeftUp("0");
            }

            bottomOddsItem.setLeft(webodds.getLeft());

            if (Float.parseFloat(b.getOdd().getMiddle()) > Float.parseFloat(webodds.getMiddle())) {
                bottomOddsItem.setMiddleUp("-1");  //降
            } else if (Float.parseFloat(b.getOdd().getMiddle()) < Float.parseFloat(webodds.getMiddle())) {
                bottomOddsItem.setMiddleUp("1");
            } else {
                bottomOddsItem.setMiddleUp("0");
            }

            bottomOddsItem.setMiddle(webodds.getMiddle());

            if (Float.parseFloat(b.getOdd().getRight()) > Float.parseFloat(webodds.getRight())) {
                bottomOddsItem.setRightUp("-1");  //降
            } else if (Float.parseFloat(b.getOdd().getRight()) < Float.parseFloat(webodds.getRight())) {
                bottomOddsItem.setRightUp("1");
            } else {
                bottomOddsItem.setRightUp("0");
            }

            bottomOddsItem.setRight(webodds.getRight());
        }


        destination.setTime(webodds.getMatchInTime());
        destination.setScore(webodds.getScore());
        destination.setOdd(bottomOddsItem);

        return destination;
    }

    private void setFirstOdd(FirstOdd firstOdd) {
        //初盘
        if (isNULLOrEmpty(firstOdd.getLeft()) || isNULLOrEmpty(firstOdd.getMiddle()) || isNULLOrEmpty(firstOdd.getRight())) {

            hostFirst.setText("-");
            handicapFirst.setText("-");
            guestFirst.setText("-");
        } else {
            hostFirst.setText(firstOdd.getLeft());
            switch (oddType) {
                case OUER_TYPE:
                    handicapFirst.setText(firstOdd.getMiddle());
                    break;
                case ALET_TYPE:
                    handicapFirst.setText(HandicapUtils.changeHandicap(firstOdd.getMiddle()));
                    break;
                case ASIZE_TYPE:
                    handicapFirst.setText(HandicapUtils.changeHandicapByBigLittleBall(firstOdd.getMiddle()));
                    break;
                case CORNER_TYPE:
                    handicapFirst.setText(firstOdd.getMiddle());
                    break;
            }

            guestFirst.setText(firstOdd.getRight());
        }
    }

    @Override
    public void onError() {
        llContext.setVisibility(View.GONE);
        handleException.setVisibility(View.VISIBLE);
        flLoading.setVisibility(View.GONE);
        flNetworkError.setVisibility(View.VISIBLE);
        flNodata.setVisibility(View.GONE);
    }

    @Override
    public void noData() {
        llContext.setVisibility(View.GONE);
        handleException.setVisibility(View.VISIBLE);
        flLoading.setVisibility(View.GONE);
        flNetworkError.setVisibility(View.GONE);
        flNodata.setVisibility(View.VISIBLE);
    }

    private boolean isNULLOrEmpty(String s) {
        return s == null || "".equals(s) || "-".equals(s);
    }

    @OnClick(R.id.reLoading)
    public void onClick() {
        mPresenter.requestData(mThirdId, String.valueOf(oddType));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
