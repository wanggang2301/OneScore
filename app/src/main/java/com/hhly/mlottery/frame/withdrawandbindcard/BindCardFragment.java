package com.hhly.mlottery.frame.withdrawandbindcard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvp.ViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.bean.WithdrawBean;
import de.greenrobot.event.EventBus;

/**
 * 绑定银行卡fragment
 */
public class BindCardFragment extends ViewFragment<BindCardContract.Presenter>implements BindCardContract.View {

    @BindView(R.id.bind_card_back)
    ImageView mBack;

    @BindView(R.id.bind_card_name)
    EditText mName;
    @BindView(R.id.bind_card_bank)
    EditText mBank;
    @BindView(R.id.bind_card_card_number)
    EditText mCardNumber;
    @BindView(R.id.bind_card_save)
    LinearLayout mSave;
    private String mCardName;
    private static final String ARG_PARAM1 = "param1";
    public BindCardFragment() {
        // Required empty public constructor
    }

    public static BindCardFragment newInstance(String param1){
        BindCardFragment fragment=new BindCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bind_card, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBank.getText().toString().isEmpty()){
                    Toast.makeText(mActivity, R.string.bind_card_bank_null, Toast.LENGTH_SHORT).show();
                }else if(checkBankCard(mCardNumber.getText().toString())){
                    Toast.makeText(mActivity, R.string.bind_card_card_error, Toast.LENGTH_SHORT).show();
                }else{
                    mPresenter.request(mName.getText().toString(),mBank.getText().toString(),mCardNumber.getText().toString());
                }

            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        mName.setText(mCardName);
        mName.setFocusable(false);
    }

    @Override
    public void onError() {

    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public  boolean checkBankCard(String cardId) {
        if(cardId.isEmpty()||cardId.length()<10){
            return true;
        }
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        return cardId.charAt(cardId.length() - 1) != bit; //不等于则是错误卡 、true就是错误
    }
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * 该校验的过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public  char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")||nonCheckCodeCardId.trim().length()<15
                ||nonCheckCodeCardId.trim().length()>18) {
            //如果传的数据不合法返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        // 执行luh算法
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {  //偶数位处理
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

    @Override
    public BindCardContract.Presenter initPresenter() {
        return new BindCardPresenter(this);
    }


    @Override
    public void bindSuccess(WithdrawBean.DataEntity bean) {
        Toast.makeText(mActivity, R.string.bind_success, Toast.LENGTH_SHORT).show();
//        EventBus.getDefault().post(bean);
        getActivity().finish();
    }

    @Override
    public void bindError() {
        Toast.makeText(mActivity, R.string.bind_error, Toast.LENGTH_SHORT).show();
    }
}
