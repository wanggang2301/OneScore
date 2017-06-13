package com.hhly.mlottery.frame.withdrawandbindcard;


import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BindCardActivity;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.UnitsUtil;
import com.hhly.mlottery.widget.TextWatcherAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.bean.WithdrawBean;
import de.greenrobot.event.EventBus;

/**
 * 提现fragment
 * created by mdy
 */
public class WithDrawFragment extends ViewFragment<WithdrawContract.Presenter> implements WithdrawContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /**传入的余额*/
    private String mTextBalance;

    private View mView;

    @BindView(R.id.withdraw_name)
    TextView mCardName;
    @BindView(R.id.withdraw_card_number)
    TextView mCardNumber;
    @BindView(R.id.withdraw_balance)
    TextView mBalance;
    @BindView(R.id.withdraw_edit)
    EditText mWithDrawAmount;
    @BindView(R.id.withdraw_button)
    LinearLayout mCommitButton;

    @BindView(R.id.withdraw_layout)
    LinearLayout mWithdrawLayout;

    @BindView(R.id.check_layout)
    LinearLayout mCheckLayout;
    @BindView(R.id.withdraw_back)
    ImageView mWithdrawback;

    @BindView(R.id.check_back)
    ImageView mCheckback;

    private String mEditText;
    private int MIX_NUM=100;
    String mChangeEdit; //监听变化时的值

    public WithDrawFragment() {
    }


    public static WithDrawFragment newInstance(String param1, String param2) {
        WithDrawFragment fragment = new WithDrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTextBalance = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public WithdrawContract.Presenter initPresenter() {
        return new WithdrawPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_with_draw, container, false);

        ButterKnife.bind(this,mView);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCheckLayout.setVisibility(View.GONE);
        mWithdrawLayout.setVisibility(View.VISIBLE);
        mBalance.setText(" ¥ "+UnitsUtil.fenToYuan(mTextBalance));

        mPresenter.requestData();

        setListener();

    }

    /**
     * 设置监听
     */
    private void setListener() {

        mWithDrawAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mChangeEdit=mWithDrawAmount.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mChangeEdit=mWithDrawAmount.getText().toString();
                if(!mChangeEdit.equals("")){
                    if(Double.parseDouble(mChangeEdit)>Double.parseDouble(mTextBalance)/100){ //分转元
                        mWithDrawAmount.setText(((int)Double.parseDouble(mTextBalance)/100)+"");
                        mWithDrawAmount.setSelection(mWithDrawAmount.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText=mWithDrawAmount.getText().toString();
                if(mEditText==null||mEditText.equals("")){
                    Toast.makeText(getActivity(), R.string.withdraw_input_money, Toast.LENGTH_LONG).show();
                }
                else if(Double.parseDouble(mEditText)<MIX_NUM){
                    Toast.makeText(getActivity(), R.string.withdraw_unless_100, Toast.LENGTH_LONG).show();
                }
                else if(mPresenter.getCardInfo().getCardNum()==null||mPresenter.getCardInfo().getCardNum().equals("")){ //未绑定银行卡
                    //跳绑定银行卡页面
                    L.e("sign","没绑卡呢");
                    Intent intent=new Intent(getActivity(), BindCardActivity.class);
                    intent .putExtra("cardName",mPresenter.getCardInfo().getAccountName());
                    startActivity(intent);
                }else if(Double.parseDouble(mEditText)>Double.parseDouble(mTextBalance)/100){
                    Toast.makeText(getActivity(), R.string.withdraw_morethan_total, Toast.LENGTH_LONG).show();
                }
                else { //提现
                    mPresenter.commitWithdraw(mEditText);
                    L.e("sign",mEditText);
                }
            }
        });
        mCheckback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckLayout.setVisibility(View.GONE);
                mWithdrawLayout.setVisibility(View.VISIBLE);
                mBalance.setText(" ¥"+UnitsUtil.fenToYuan((int)(Double.parseDouble(mTextBalance)-Double.parseDouble(mWithDrawAmount.getText().toString())*100)+"")); //乘100转分
                mTextBalance=Double.parseDouble(mTextBalance)-Double.parseDouble(mWithDrawAmount.getText().toString())*100+"";
            }
        });
        mWithdrawback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.requestData();
//        EventBus.getDefault().register(this);
    }

//    public void onEventMainThread(WithdrawBean.DataEntity bean){
//        mCardName.setText(bean.getAccountName()+"");
//        mCardNumber.setText(bean.getCardNum());
//    }

    @Override
    public void onError() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showCardInfo() {
        mCardName.setText(mPresenter.getCardInfo().getAccountName());
        String head=""; String last="";
        if(mPresenter.getCardInfo().getCardNum()!=null&&!mPresenter.getCardInfo().getCardNum().equals("")){
            head=mPresenter.getCardInfo().getCardNum().substring(0,4)+" **** **** ";
            last=mPresenter.getCardInfo().getCardNum().substring(mPresenter.getCardInfo().getCardNum().length()-4,mPresenter.getCardInfo().getCardNum().length());
        }
        mCardNumber.setText(head+last);
    }

    @Override
    public void onWithdrawing() {
        mCheckLayout.setVisibility(View.VISIBLE);
        mWithdrawLayout.setVisibility(View.GONE);
    }

    @Override
    public void withdrawError() {
        Toast.makeText(getActivity(), R.string.withdraw_error, Toast.LENGTH_LONG).show();

    }
}
