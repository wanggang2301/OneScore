package com.hhly.mlottery.frame.footframe;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.intelligence.BigDataForecast;
import com.hhly.mlottery.bean.intelligence.BigDataForecastFactor;
import com.hhly.mlottery.util.StringFormatUtils;
import com.hhly.mlottery.widget.TextWatcherAdapter;

import java.util.Locale;

/**
 * 大数据预测 DIY 算法对话框
 * <p/>
 * Created by Loshine on 2016/7/19.
 */
public class IntelligenceComputeMethodDialogFragment extends DialogFragment {

    private static final String KEY_BIG_DATA_FORECAST = "bigDataForecast";
    private static final String KEY_BIG_DATA_FACTOR = "bigDataFactor";

    TextView mMessage;

    RadioGroup mRadioGroup;

    EditText mHistoryEditText;
    EditText mHostEditText;
    EditText mGuestEditText;

    TextView mHistoryTextView;
    TextView mHostTextView;
    TextView mGuestTextView;

    TextView mResultTextView;

    private BigDataForecast mBigDataForecast;
    private BigDataForecastFactor mFactor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mBigDataForecast = args.getParcelable(KEY_BIG_DATA_FORECAST);
            mFactor = args.getParcelable(KEY_BIG_DATA_FACTOR);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_intelligence_compute_method);

        initViews(dialog);

        setData();

        return dialog;
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (mBigDataForecast != null) {
            switch (getCurrentRadioPosition()) {
                case 0:
                    mHistoryEditText.setText(StringFormatUtils.toString
                            (mFactor.getHost().getHistoryTemp()));
                    mHostEditText.setText(StringFormatUtils.toString
                            (mFactor.getHost().getHomeTemp()));
                    mGuestEditText.setText(StringFormatUtils.toString
                            (mFactor.getHost().getGuestTemp()));

                    mHistoryTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getBattleHistory().getHomeWinPercent()));
                    mHostTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getHomeRecent().getHomeWinPercent()));
                    mGuestTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getGuestRecent().getHomeWinPercent()));
                    break;
                case 1:
                    mHistoryEditText.setText(StringFormatUtils.toString
                            (mFactor.getSize().getHistoryTemp()));
                    mHostEditText.setText(StringFormatUtils.toString
                            (mFactor.getSize().getHomeTemp()));
                    mGuestEditText.setText(StringFormatUtils.toString
                            (mFactor.getSize().getGuestTemp()));

                    mHistoryTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getBattleHistory().getSizeWinPercent()));
                    mHostTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getHomeRecent().getSizeWinPercent()));
                    mGuestTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getGuestRecent().getSizeWinPercent()));
                    break;
                case 2:
                    mHistoryEditText.setText(StringFormatUtils.toString
                            (mFactor.getAsia().getHistoryTemp()));
                    mHostEditText.setText(StringFormatUtils.toString
                            (mFactor.getAsia().getHomeTemp()));
                    mGuestEditText.setText(StringFormatUtils.toString
                            (mFactor.getAsia().getGuestTemp()));

                    mHistoryTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getBattleHistory().getAsiaWinPercent()));
                    mHostTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getHomeRecent().getAsiaWinPercent()));
                    mGuestTextView.setText(StringFormatUtils.toString(
                            mBigDataForecast.getGuestRecent().getAsiaWinPercent()));
                    break;
            }
        }
    }

    /**
     * 初始化 Views
     *
     * @param dialog
     */
    private void initViews(Dialog dialog) {
        mRadioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setData();
            }
        });

        mMessage = (TextView) dialog.findViewById(R.id.message);

        mHistoryEditText = (EditText) dialog.findViewById(R.id.history_edt);
        mHistoryEditText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                double aDouble = StringFormatUtils.asDouble(s.toString());
                switch (getCurrentRadioPosition()) {
                    case 0:
                        mFactor.getHost().setHistoryTemp(aDouble);
                        break;
                    case 1:
                        mFactor.getSize().setHistoryTemp(aDouble);
                        break;
                    case 2:
                        mFactor.getAsia().setHistoryTemp(aDouble);
                        break;
                }
                updateMessage();
            }
        });
        mHostEditText = (EditText) dialog.findViewById(R.id.host_edt);
        mHostEditText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                double aDouble = StringFormatUtils.asDouble(s.toString());
                switch (getCurrentRadioPosition()) {
                    case 0:
                        mFactor.getHost().setHomeTemp(aDouble);
                        break;
                    case 1:
                        mFactor.getSize().setHomeTemp(aDouble);
                        break;
                    case 2:
                        mFactor.getAsia().setHomeTemp(aDouble);
                        break;
                }
                updateMessage();
            }
        });
        mGuestEditText = (EditText) dialog.findViewById(R.id.guest_edt);
        mGuestEditText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                double aDouble = StringFormatUtils.asDouble(s.toString());
                switch (getCurrentRadioPosition()) {
                    case 0:
                        mFactor.getHost().setGuestTemp(aDouble);
                        break;
                    case 1:
                        mFactor.getSize().setGuestTemp(aDouble);
                        break;
                    case 2:
                        mFactor.getAsia().setGuestTemp(aDouble);
                        break;
                }
                updateMessage();
            }
        });

        mHistoryTextView = (TextView) dialog.findViewById(R.id.history_percent);
        mHostTextView = (TextView) dialog.findViewById(R.id.host_percent);
        mGuestTextView = (TextView) dialog.findViewById(R.id.guest_percent);

        mResultTextView = (TextView) dialog.findViewById(R.id.result);

        dialog.findViewById(R.id.close)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeInputMethod(getContext(), mGuestEditText);
                        getDialog().dismiss();
                        mFactor.refreshTemp();
                    }
                });

        dialog.findViewById(R.id.compute)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeInputMethod(getContext(), mGuestEditText);
                        getDialog().dismiss();
                        mFactor.updateTemp();
                        ((IntelligenceFragment) getParentFragment()).refreshFactorUI();
                    }
                });
    }

    /**
     * 获取当前在 RadioGroupd 的哪个位置
     *
     * @return
     */
    public int getCurrentRadioPosition() {
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.left:
                return 0;
            case R.id.center:
                return 1;
            case R.id.right:
                return 2;
            default:
                return 0;
        }
    }

    /**
     * 更新提示信息
     */
    public void updateMessage() {
        String message = getString(R.string.diy_compute_method_message);
        String history = "";
        String home = "";
        String guest = "";
        switch (getCurrentRadioPosition()) {
            case 0:
                history = StringFormatUtils.toString(mFactor.getHost().getHistoryTemp());
                home = StringFormatUtils.toString(mFactor.getHost().getHomeTemp());
                guest = StringFormatUtils.toString(mFactor.getHost().getGuestTemp());
                break;
            case 1:
                history = StringFormatUtils.toString(mFactor.getSize().getHistoryTemp());
                home = StringFormatUtils.toString(mFactor.getSize().getHomeTemp());
                guest = StringFormatUtils.toString(mFactor.getSize().getGuestTemp());
                break;
            case 2:
                history = StringFormatUtils.toString(mFactor.getAsia().getHistoryTemp());
                home = StringFormatUtils.toString(mFactor.getAsia().getHomeTemp());
                guest = StringFormatUtils.toString(mFactor.getAsia().getGuestTemp());
                break;
        }
        mMessage.setText(String.format(Locale.getDefault(), message, history, home, guest));
        setResult();
    }

    /**
     * 设置结果
     */
    private void setResult() {
        switch (getCurrentRadioPosition()) {
            case 0:
                mResultTextView.setText(StringFormatUtils.toString(
                        mFactor.computeHostWinRate(mBigDataForecast, true)));
                break;
            case 1:
                mResultTextView.setText(StringFormatUtils.toString(
                        mFactor.computeSizeWinRate(mBigDataForecast, true)));
                break;
            case 2:
                mResultTextView.setText(StringFormatUtils.toString(
                        mFactor.computeAsiaWinRate(mBigDataForecast, true)));
                break;
        }
    }

    private void closeInputMethod(Context context, View editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static IntelligenceComputeMethodDialogFragment newInstance(BigDataForecast bigDataForecast,
                                                                      BigDataForecastFactor factor) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_BIG_DATA_FORECAST, bigDataForecast);
        args.putParcelable(KEY_BIG_DATA_FACTOR, factor);
        IntelligenceComputeMethodDialogFragment fragment = new IntelligenceComputeMethodDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
