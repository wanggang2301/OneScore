package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.mvppresenter.MvpBettingPayDetailsPresenter;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseBean;

/**
 * Created by：XQyi on 2017/4/18 11:12
 * Use:竞彩单关页面[MVP_view  页面展示]
 */

public class MvpBettingPayDetailsActivity extends BaseActivity implements MView<BasketDatabaseBean>, View.OnClickListener {

    private ImageView mBack;
    private LinearLayout mToPay;
    private MvpBettingPayDetailsPresenter mvpBettingPayDetailsPresenter;
    private TextView mSecialistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.betting_recommend_details);

        mContext = this;
        mvpBettingPayDetailsPresenter = new MvpBettingPayDetailsPresenter(this);

        initView();
        initData();
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.public_txt_title);
        title.setText(mContext.getResources().getText(R.string.betting_title_details));
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);
        mToPay = (LinearLayout)findViewById(R.id.betting_topay_ll);
        mToPay.setOnClickListener(this);

        mSecialistName = (TextView) findViewById(R.id.betting_recommend_specialist_name);
    }

    private void initData(){
        //http://m.13322.com/mlottery/core/basketballData.findLeagueHeader.do?lang=zh&timeZone=8&leagueId=1

        mvpBettingPayDetailsPresenter.loadData("http://m.13322.com/mlottery/core/basketballData.findLeagueHeader.do");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.betting_topay_ll:
                Intent mIntent = new Intent(mContext , MvpBettingOnlinePaymentActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
                break;
        }
    }

    @Override
    public void loadSuccessView(BasketDatabaseBean basebean) {
        String name = basebean.getLeagueName();
        mSecialistName.setText(name);
    }

    @Override
    public void loadFailView() {
        Toast.makeText(mContext, "网络请求失败~！！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNoData() {
        Toast.makeText(mContext, "暂无数据~！！", Toast.LENGTH_SHORT).show();
    }
}
