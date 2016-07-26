package com.hhly.mlottery.frame.footframe;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.intelligence.BigDataForecast;
import com.hhly.mlottery.bean.intelligence.BigDataForecastData;
import com.hhly.mlottery.bean.intelligence.BigDataForecastFactor;
import com.hhly.mlottery.util.StringFormatUtils;
import com.hhly.mlottery.util.ToastTools;
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

    private int currentPosition = 0;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.dialog_intelligence_compute_method, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initViews(view);

        setData();

        return alertDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentPosition != 0) {
            if (currentPosition == 1) mRadioGroup.check(R.id.center);
            if (currentPosition == 2) mRadioGroup.check(R.id.right);
        }
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (mBigDataForecast != null) {

            BigDataForecastData battleHistory = mBigDataForecast.getBattleHistory();
            BigDataForecastData homeRecent = mBigDataForecast.getHomeRecent();
            BigDataForecastData guestRecent = mBigDataForecast.getGuestRecent();

            switch (getCurrentRadioPosition()) {
                case 0:
                    mHistoryEditText.setText(StringFormatUtils.toString
                            (mFactor.getHost().getHistoryTemp()));
                    mHostEditText.setText(StringFormatUtils.toString
                            (mFactor.getHost().getHomeTemp()));
                    mGuestEditText.setText(StringFormatUtils.toString
                            (mFactor.getHost().getGuestTemp()));

                    Float historyHomeWin = getHomeWinPercent(battleHistory);
                    Float homeRecentHomeWin = getHomeWinPercent(homeRecent);
                    Float guestRecentHomeWin = getHomeWinPercent(guestRecent);

                    mHistoryTextView.setText(StringFormatUtils.toString(historyHomeWin));
                    mHostTextView.setText(StringFormatUtils.toString(homeRecentHomeWin));
                    mGuestTextView.setText(StringFormatUtils.toString(guestRecentHomeWin));
                    break;
                case 1:
                    mHistoryEditText.setText(StringFormatUtils.toString
                            (mFactor.getSize().getHistoryTemp()));
                    mHostEditText.setText(StringFormatUtils.toString
                            (mFactor.getSize().getHomeTemp()));
                    mGuestEditText.setText(StringFormatUtils.toString
                            (mFactor.getSize().getGuestTemp()));

                    Float historySizeWin = getSizeWinPercent(battleHistory);
                    Float homeRecentSizeWin = getSizeWinPercent(homeRecent);
                    Float guestRecentSizeWin = getSizeWinPercent(guestRecent);

                    mHistoryTextView.setText(StringFormatUtils.toString(historySizeWin));
                    mHostTextView.setText(StringFormatUtils.toString(homeRecentSizeWin));
                    mGuestTextView.setText(StringFormatUtils.toString(guestRecentSizeWin));
                    break;
                case 2:
                    mHistoryEditText.setText(StringFormatUtils.toString
                            (mFactor.getAsia().getHistoryTemp()));
                    mHostEditText.setText(StringFormatUtils.toString
                            (mFactor.getAsia().getHomeTemp()));
                    mGuestEditText.setText(StringFormatUtils.toString
                            (mFactor.getAsia().getGuestTemp()));

                    Float historyAsiaWin = getAsiaWinPercent(battleHistory);
                    Float homeRecentAsiaWin = getAsiaWinPercent(homeRecent);
                    Float guestRecentAsiaWin = getAsiaWinPercent(guestRecent);

                    mHistoryTextView.setText(StringFormatUtils.toString(historyAsiaWin));
                    mHostTextView.setText(StringFormatUtils.toString(homeRecentAsiaWin));
                    mGuestTextView.setText(StringFormatUtils.toString(guestRecentAsiaWin));
                    break;
            }
        }
    }

    private Float checkNotNull(Float f) {
        return f == null ? 0f : f;
    }

    private Float getHomeWinPercent(BigDataForecastData data) {
        if (data == null) return 0f;
        return checkNotNull(data.getHomeWinPercent());
    }

    private Float getAsiaWinPercent(BigDataForecastData data) {
        if (data == null) return 0f;
        return checkNotNull(data.getAsiaWinPercent());
    }

    private Float getSizeWinPercent(BigDataForecastData data) {
        if (data == null) return 0f;
        return checkNotNull(data.getSizeWinPercent());
    }

    /**
     * 初始化 Views
     *
     * @param view view
     */
    private void initViews(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                currentPosition = getCurrentRadioPosition();
                setData();
            }
        });

        mMessage = (TextView) view.findViewById(R.id.message);

        mHistoryEditText = (EditText) view.findViewById(R.id.history_edt);
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
        mHostEditText = (EditText) view.findViewById(R.id.host_edt);
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
        mGuestEditText = (EditText) view.findViewById(R.id.guest_edt);
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

        mHistoryTextView = (TextView) view.findViewById(R.id.history_percent);
        mHostTextView = (TextView) view.findViewById(R.id.host_percent);
        mGuestTextView = (TextView) view.findViewById(R.id.guest_percent);

        mResultTextView = (TextView) view.findViewById(R.id.result);

        view.findViewById(R.id.close)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeInputMethod(getContext(), mGuestEditText);
                        getDialog().dismiss();
                        mFactor.refreshTemp();
                    }
                });

        view.findViewById(R.id.compute)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double history = StringFormatUtils.asDouble(mHistoryEditText.getText().toString());
                        double host = StringFormatUtils.asDouble(mHostEditText.getText().toString());
                        double guest = StringFormatUtils.asDouble(mGuestEditText.getText().toString());
                        if (!checkNumbers(history, host, guest)) {
                            ToastTools.showQuick(getContext(), R.string.big_data_check_alert);
                            return;
                        }
                        closeInputMethod(getContext(), mGuestEditText);
                        getDialog().dismiss();
                        mFactor.updateTemp();
                        ((IntelligenceFragment) getParentFragment()).refreshFactorUI(true);
                    }
                });
    }

    /**
     * 获取当前在 RadioGroup 的哪个位置
     *
     * @return position
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

    /**
     * 关闭输入法
     *
     * @param context  context
     * @param editText EditText
     */
    private void closeInputMethod(Context context, View editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 检查数字合法性
     *
     * @param number 数字
     * @return 是否合法
     */
    private boolean checkNumber(double number) {
        return number >= 0 && number <= 1;
    }

    /**
     * 检查所有数字合法性
     *
     * @param numbers 数字
     * @return 是否合法
     */
    private boolean checkNumbers(double... numbers) {
        boolean isOk = true;
        for (double d : numbers) {
            isOk &= checkNumber(d);
        }
        return isOk;
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
