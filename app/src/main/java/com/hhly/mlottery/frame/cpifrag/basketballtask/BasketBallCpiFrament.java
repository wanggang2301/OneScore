package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvp.ViewFragment;

/**
 * @author wangg
 * @des:篮球指数
 * @date:2017/3/16
 */
public class BasketBallCpiFrament extends ViewFragment<BasketBallContract.CpiPresenter> implements BasketBallContract.CpiView {

    private String[] mItems;
    private Activity mActivity;
    private LinearLayout d_header;
    private TextView tv_match_name;
    private ImageView iv_match;
    private LinearLayout ll_match_select;
    private View mView;


    public BasketBallCpiFrament() {

    }

    public static BasketBallCpiFrament newInstace() {
        BasketBallCpiFrament basketBallCpiFrament = new BasketBallCpiFrament();
        return basketBallCpiFrament;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_basket_ball_cpi, container, false);
      /*  d_header = (LinearLayout) mView.findViewById(R.id.d_heasder);
        tv_match_name = (TextView) mView.findViewById(R.id.tv_match_name);
        iv_match = (ImageView) mView.findViewById(R.id.iv_match);
        ll_match_select = (LinearLayout) mView.findViewById(R.id.ll_match_select);

        tv_match_name.setText(getResources().getString(R.string.basketball_txt));
*/
        // return mView;


        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItems = getResources().getStringArray(R.array.zhishu_select);
        mPresenter = new BasketBallCpiPresenter(this);
        mPresenter.switchFg();
    }


    @Override
    public void switchFgView() {
        Toast.makeText(mActivity, "ddd", Toast.LENGTH_SHORT).show();

        //  initEvent();
    }


    /*private void initEvent() {
        *//*ll_match_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_match.setImageResource(R.mipmap.nav_icon_up);
                backgroundAlpha(getActivity(), 0.5f);
                popWindow(v);
            }
        });*//*
    }

    private void popWindow(final View v) {
        final View mView = View.inflate(mActivity, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mActivity, mItems, BallType.BASKETBALL);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(d_header);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new ScoreSwitchFg(position));
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_match.setImageResource(R.mipmap.nav_icon_cbb);
                backgroundAlpha(getActivity(), 1f);
            }
        });
    }
*/

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    @Override
    public void onError() {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;

    }
}
