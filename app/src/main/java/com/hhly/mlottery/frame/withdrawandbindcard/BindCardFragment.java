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

    public BindCardFragment() {
        // Required empty public constructor
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mName.getText().toString().isEmpty()){
                    Toast.makeText(mActivity, "开户名不能为空", Toast.LENGTH_SHORT).show();
                }else if(mBank.getText().toString().isEmpty()){
                    Toast.makeText(mActivity, "开户银行不能为空", Toast.LENGTH_SHORT).show();
                }else if(checkBankCard(mCardNumber.getText().toString())){
                    Toast.makeText(mActivity, "银行卡号不正确", Toast.LENGTH_SHORT).show();
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
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        return cardId.charAt(cardId.length() - 1) == bit;
    }
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public  char getBankCardCheckCode(String nonCheckCodeCardId) {
        int cardLenth = nonCheckCodeCardId.trim().length();
        if (nonCheckCodeCardId == null || cardLenth == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("不是银行卡的卡号!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

        @Override
    public BindCardContract.Presenter initPresenter() {
        return new BindCardPresenter(this);
    }


    @Override
    public void bindSuccess(WithdrawBean.DataEntity bean) {
        Toast.makeText(mActivity, "绑定成功", Toast.LENGTH_SHORT).show();
//        EventBus.getDefault().post(bean);
        getActivity().finish();
    }

    @Override
    public void bindError() {
        Toast.makeText(mActivity, "绑定失败", Toast.LENGTH_SHORT).show();
    }
}
