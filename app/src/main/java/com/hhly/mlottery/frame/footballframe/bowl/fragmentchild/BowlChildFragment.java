package com.hhly.mlottery.frame.footballframe.bowl.fragmentchild;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.BottomOddsAdapter;
import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.L;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BowlChildFragment extends ViewFragment<IBowlChildContract.IBowlChildPresenter> implements IBowlChildContract.IBowlChildView {

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

    private String mThirdId;
    private int oddType;
    private BottomOddsDetails bowlBean;
    private BottomOddsAdapter mAdapter;
    private Context context;


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
        // mPresenter.requestData(mThirdId, oddType);


        View view = inflater.inflate(R.layout.fragment_bowl_child, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();

        mPresenter = new BowlChildPresenter(this);
        mPresenter.requestData("3458732", String.valueOf(oddType));


        return view;
    }

    private void initView() {


        switch (oddType) {
            case OUER_TYPE:

                setTextViewText(getResources().getStringArray(R.array.bowl_eur));
                break;

            case ALET_TYPE:
                setTextViewText(getResources().getStringArray(R.array.bowl_alet));

                break;

            case ASIZE_TYPE:
                setTextViewText(getResources().getStringArray(R.array.bowl_asize));

                break;

            case CORNER_TYPE:
                //setTextViewText();

                break;
        }
/*
        if (mType == ALET) {
            odd_title.setText(context.getResources().getString(R.string.alet_first));
            odds_left.setText(context.getResources().getString(R.string.foot_odds_alet_left));
            odds_middle.setText(context.getResources().getString(R.string.foot_odds_alet_middle));
            odds_right.setText(context.getResources().getString(R.string.foot_odds_alet_right));

            if (isNULLOrEmpty(mBottomOddsItem.getLeft()) || isNULLOrEmpty(mBottomOddsItem.getMiddle()) || isNULLOrEmpty(mBottomOddsItem.getRight())) {
                first_odd.setText("");
            } else {
                first_odd.setText(" " + mBottomOddsItem.getLeft() + " " + HandicapUtils.changeHandicap(mBottomOddsItem.getMiddle()) + " " + mBottomOddsItem.getRight());
            }

        } else if (mType == ASIZE) {
            odd_title.setText(context.getResources().getString(R.string.asize_first));
            odds_left.setText(context.getResources().getString(R.string.foot_odds_asize_left));
            odds_middle.setText(context.getResources().getString(R.string.foot_odds_asize_middle));
            odds_right.setText(context.getResources().getString(R.string.foot_odds_asize_right));

            if (isNULLOrEmpty(mBottomOddsItem.getLeft()) || isNULLOrEmpty(mBottomOddsItem.getMiddle()) || isNULLOrEmpty(mBottomOddsItem.getRight())) {
                first_odd.setText("");
            } else {
                first_odd.setText(" " + mBottomOddsItem.getLeft() + " " + HandicapUtils.changeHandicapByBigLittleBall(mBottomOddsItem.getMiddle()) + " " + mBottomOddsItem.getRight());
            }
        } else if (mType == EUR) {
            odd_title.setText(context.getResources().getString(R.string.eu_first));
            odds_left.setText(context.getResources().getString(R.string.foot_odds_eu_left));
            odds_middle.setText(context.getResources().getString(R.string.foot_odds_eu_middle));
            odds_right.setText(context.getResources().getString(R.string.foot_odds_eu_right));

            if (isNULLOrEmpty(mBottomOddsItem.getLeft()) || isNULLOrEmpty(mBottomOddsItem.getMiddle()) || isNULLOrEmpty(mBottomOddsItem.getRight())) {
                first_odd.setText("");
            } else {
                first_odd.setText(" " + mBottomOddsItem.getLeft() + " " + mBottomOddsItem.getMiddle() + " " + mBottomOddsItem.getRight());
            }
        }*/

    }


    private void setTextViewText(String[] names) {
        hostFirst.setText("111");
        handicapFirst.setText("111");
        guestFirst.setText("111");

        hostTxt.setText(names[0]);
        handicapTxt.setText(names[1]);
        guestTxt.setText(names[2]);

    }


    @Override
    public void loading() { //loading的view

    }


    @Override
    public void responseData() {

        bowlBean = mPresenter.getBowlBean();

        initView();


        mAdapter = new BottomOddsAdapter(context, bowlBean.getMatchoddlist(), oddType);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        L.d("bowl", bowlBean.getResult() + "");


    }

    @Override
    public void onError() {

    }

    private boolean isNULLOrEmpty(String s) {
        return s == null || "".equals(s);
    }
}
