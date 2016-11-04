package com.hhly.mlottery.frame.numbersframe;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.numbersBean.NumberCurrentInfo;
import com.hhly.mlottery.bean.numbersBean.NumbersOpenBean;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.NumberDataUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 开奖列表内容详情
 *
 * @author Tenney
 * @ClassName: NumberContentFragment
 * @Description:
 * @date 2015-10-19 下午2:48:32
 */
public class CurrentNumberFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View mView;
    private Context mContext;
    private NumberCurrentInfo mNumberInfo;// 开奖彩票对象
    private String mNumberName;

    private boolean isOpenNumberStartHistory = false;// 获取彩种开奖状态
    private boolean isNextNumber = false;

    private List<NumberCurrentInfo> numberlist;// 各种彩票开奖对象
    private NumberDataUtils utils = new NumberDataUtils();

    private static final int STARTLOADING = 0;// 开始加载状态
    private static final int SUCCESSLOADING = 1;// 加载成功
    private static final int ERRORLOADING = 2;// 加载失败
    private static final int AUTOREFRESH = 3;// 自动和手动刷新数据
    private static final int OPENSTART = 4;// 开奖请求数据
    private static final int UPDATECOUNTDOWN = 5;// 更新倒计时
    private static final int OPENNUMBEROVER = 6;// 重新获取最新状态
    private static final int STARTANIMA = 7;// 播放动画

    private boolean isExit = true;// 退出时停止线程循环

    private FrameLayout fl_current_data;// 数据显示的容器
    private FrameLayout fl_current_notNet;// 网络访问失败提示
    private FrameLayout fl_current_reLoading;// 正在加载中
    private TextView tv_current_reLoading;// 刷新

    private String serverTime;// 服务器时间戳
    //private ImageView iv_loading_img;// 加载动画
    //private Animation animation;

    public static SwipeRefreshLayout mSwipeRefreshLayout;// 下拉刷新

    private LinearLayout ll_info_lastTime;// 显示时间容器
    private LinearLayout ll_info_startOpenNumber;// 显示提示销售状态容器
    private TextView tv_info_numberStart_desc;// 正在开奖中或截止销售

    private TextView tv_number_info_time_dd;// 天
    private TextView tv_number_info_dd_des;// 天 描述
    private TextView tv_number_info_time_hh;// 时
    private TextView tv_number_info_hh_des;// 时 描述
    private TextView tv_number_info_time_mm;// 分
    private TextView tv_number_info_time_ss;// 秒

    private LinearLayout ll_Currentnumber_numbers_copy;// 开奖球

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            mView = inflater.inflate(R.layout.numbers_current_page, container, false);// 国语
        } else {
            mView = inflater.inflate(R.layout.numbers_current_page_i18n, container, false);// 英语
        }

        mNumberName = null;
        mNumberName = getArguments().getString("mNumberName");

        initView();
        //initData();
        initEvent();
        return mView;
    }

    private void initData() {
        numbersDataShow(0);// 请求数据
    }

    /**
     * 倒计时开奖
     */
    private void getCountdownOpenNumber() {
        getStartCountDown();
    }

    private long mNumberTime;// 获取开奖时间
    private boolean isCountDown = false;// 启动倒计时

    /**
     * 开始倒计时
     */
    private void getStartCountDown() {
        try {
            mNumberTime = DateUtil.getCurrentTime(mNumberInfo.getNextTime()) - Long.parseLong(serverTime);// 获取下一期开奖时间
            if (!isCountDown && isExit) {
                new Thread() {
                    public void run() {
                        try {
                            isCountDown = true;
                            while (!isOpenNumberStartHistory && mNumberTime >= 0 && isExit) {

                                mHandler.sendEmptyMessage(UPDATECOUNTDOWN);// 更新倒计时

                                sleep(1000);
                                mNumberTime -= 1000;
                            }
                            if (isExit) {
                                allNumbers = null;
                                numbersDataShow(2);// 开奖时重新获取开奖状态
                            }
                            isCountDown = false;
                        } catch (InterruptedException e) {
                            L.d("倒计时子线程休眠异常！ " + e.getMessage());
                        }
                    }

                }.start();
            }

        } catch (Exception e) {
            L.d("时间日期转换异常！ " + e.getMessage());
        }
    }

    private boolean isNumberStart = false;

    /**
     * 开奖了，循环请求数据
     */
    private void getOpenNumberStart() {
        if (!isNumberStart && isExit) {
            new Thread() {
                public void run() {
                    try {
                        isNumberStart = true;
                        while (isOpenNumberStartHistory && isExit) {
                            sleep(1000);
                            numbersDataShow(6);// 重新获取开奖状态
                        }
                        if (isExit) {
                            numbersDataShow(1);// 重新获取开奖状态
                        }
                        isNumberStart = false;
                    } catch (Exception e) {
                    }
                }

            }.start();
        }
    }

    /**
     * 请求后台数据
     */
    public synchronized void numbersDataShow(final int num) {

        if (num == 0) {
            // 发送消息，开始加载数据
            mHandler.sendEmptyMessage(STARTLOADING);
        }
        /**测试数据用*/
        numberlist = new ArrayList<NumberCurrentInfo>();

        NumbersOpenBean json = JSON.parseObject(AppConstants.getTestData(),NumbersOpenBean.class);

        serverTime = json.getServerTime();
        numberlist = json.getNumLotteryResults();

        if (num == 1) {
            // 发送自动刷新和手动刷新加载数据成功消息
            mHandler.sendEmptyMessage(AUTOREFRESH);
        } else if (num == 2) {
            // 开奖时间到了，再次请求下后台数据
            mHandler.sendEmptyMessage(OPENSTART);

        } else if (num == 6) {
            // 开完奖了，重新请求下后台数据
            mHandler.sendEmptyMessage(OPENNUMBEROVER);

        } else {
            // 发送加载数据成功消息
            mHandler.sendEmptyMessage(SUCCESSLOADING);
        }
        /**测试数据用  end*/

//        VolleyContentFast.requestJsonByGet(AppConstants.numberHistoryURLs[0], new VolleyContentFast.ResponseSuccessListener<NumbersOpenBean>() {
//            @Override
//            public synchronized void onResponse(final NumbersOpenBean jsonObject) {
//                if (null != jsonObject) {// 判断数据是否为空
//
//                    numberlist = new ArrayList<NumberCurrentInfo>();
//
//                    serverTime = jsonObject.getServerTime();
//                    numberlist = jsonObject.getNumLotteryResults();
//
//                    if (num == 1) {
//                        // 发送自动刷新和手动刷新加载数据成功消息
//                        mHandler.sendEmptyMessage(AUTOREFRESH);
//                    } else if (num == 2) {
//                        // 开奖时间到了，再次请求下后台数据
//                        mHandler.sendEmptyMessage(OPENSTART);
//
//                    } else if (num == 6) {
//                        // 开完奖了，重新请求下后台数据
//                        mHandler.sendEmptyMessage(OPENNUMBEROVER);
//
//                    } else {
//                        // 发送加载数据成功消息
//                        mHandler.sendEmptyMessage(SUCCESSLOADING);
//                    }
//                } else {
//                    mHandler.sendEmptyMessage(ERRORLOADING);
//                }
//            }
//        }, new VolleyContentFast.ResponseErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
//                mHandler.sendEmptyMessage(ERRORLOADING);
//            }
//        }, NumbersOpenBean.class);
    }

    /**
     * 判断当前的彩种是否正在开奖中
     */
    private void initIsHKOpenNumberStart() {
        try {
            for (int i = 0, len = numberlist.size(); i < len; i++) {
                if (null != numberlist.get(i)) {
                    if ("1".equals(mNumberName) && "1".equals(numberlist.get(i).getName())) {
                        long numTime = DateUtil.getCurrentTime(numberlist.get(i).getNextTime()) - Long.parseLong(serverTime);

                        if (numTime <= 0 || !numberlist.get(i).getNumbers().contains("#")) {

                            isOpenNumberStartHistory = true;// 正在开奖中...

                            // 判断中是否已经在开奖了
                            if (numberlist.get(i).getNumbers().contains("#")) {
                                isNextNumber = true;// 未获取到了下一期的开奖号码
                            }

                        } else {
                            isOpenNumberStartHistory = false;
                            isNextNumber = false;
                        }
                    } else if (mNumberName.equals(numberlist.get(i).getName())) {
                        long numTime = DateUtil.getCurrentTime(numberlist.get(i).getNextTime()) - Long.parseLong(serverTime);
                        // 正在开奖中...
                        isOpenNumberStartHistory = numTime <= 0;
                    }
                }
            }
        } catch (Exception e) {
            L.d("时间日期转换异常！ " + e.getMessage());
        }
    }

    /**
     * 显示倒计时或正在开奖中
     */
    private void isOpenNumberOrCountDown() {
        if (isOpenNumberStartHistory) {// 正在开奖中
            ll_info_lastTime.setVisibility(View.GONE);
            ll_info_startOpenNumber.setVisibility(View.VISIBLE);
            ll_Currentnumber_numbers_copy.setVisibility(View.VISIBLE);
            tv_info_numberStart_desc.setText(mContext.getResources().getString(R.string.number_isOpenNumber));

            tv_number_info_time_dd.setText("-");
            tv_number_info_time_hh.setText("-");
            tv_number_info_time_mm.setText("-");
            tv_number_info_time_ss.setText("-");
        } else {
            // 判断是否截止销售状态
            try {
                mNumberTime = DateUtil.getCurrentTime(mNumberInfo.getNextTime()) - Long.parseLong(serverTime);// 获取下一期开奖时间
                long mm = (mNumberTime / 1000 / 60);// 获取相差分
                if (!"1".equals(mNumberName) && !"6".equals(mNumberName)) {// 低频彩种
                    if (mm > 10 || ("15".equals(mNumberName) && mm > 5)) {// 判断截止销售时间
                        // 显示截止销售字样
                        ll_info_lastTime.setVisibility(View.GONE);
                        ll_info_startOpenNumber.setVisibility(View.VISIBLE);
                        ll_Currentnumber_numbers_copy.setVisibility(View.GONE);
                        tv_info_numberStart_desc.setText(mContext.getResources().getString(R.string.number_info_stopSell));
                    } else {
                        // 显示开奖倒计时
                        ll_info_lastTime.setVisibility(View.VISIBLE);
                        ll_info_startOpenNumber.setVisibility(View.GONE);
                        ll_Currentnumber_numbers_copy.setVisibility(View.GONE);
                    }
                } else {
                    ll_info_lastTime.setVisibility(View.VISIBLE);
                    ll_info_startOpenNumber.setVisibility(View.GONE);
                    ll_Currentnumber_numbers_copy.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                L.d("时间转换异常: " + e.getMessage());
            }
        }
    }

    private void initEvent() {
        tv_current_reLoading.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initView() {
        fl_current_data = (FrameLayout) mView.findViewById(R.id.fl_current_data);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.number_current_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));

        fl_current_notNet = (FrameLayout) mView.findViewById(R.id.fl_current_notNet);
        tv_current_reLoading = (TextView) mView.findViewById(R.id.tv_current_reLoading);
        fl_current_reLoading = (FrameLayout) mView.findViewById(R.id.fl_current_reLoading);

		/*iv_loading_img = (ImageView) mView.findViewById(R.id.iv_loading_img);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.cirle);
		animation.setInterpolator(new LinearInterpolator());
		iv_loading_img.startAnimation(animation);*/

        fl_current_data.setVisibility(View.VISIBLE);
        fl_current_notNet.setVisibility(View.GONE);
        fl_current_reLoading.setVisibility(View.GONE);

        ll_Currentnumber_numbers_copy = (LinearLayout) mView.findViewById(R.id.ll_Currentnumber_numbers_copy);

        // 获取倒计时相关控件
        ll_info_lastTime = (LinearLayout) mView.findViewById(R.id.ll_info_lastTime);
        ll_info_startOpenNumber = (LinearLayout) mView.findViewById(R.id.ll_info_startOpenNumber);
        tv_info_numberStart_desc = (TextView) mView.findViewById(R.id.tv_info_numberStart_desc);
        tv_number_info_time_dd = (TextView) mView.findViewById(R.id.tv_number_info_time_dd);
        tv_number_info_dd_des = (TextView) mView.findViewById(R.id.tv_number_info_dd_des);
        tv_number_info_time_hh = (TextView) mView.findViewById(R.id.tv_number_info_time_hh);
        tv_number_info_hh_des = (TextView) mView.findViewById(R.id.tv_number_info_hh_des);
        tv_number_info_time_mm = (TextView) mView.findViewById(R.id.tv_number_info_time_mm);
        tv_number_info_time_ss = (TextView) mView.findViewById(R.id.tv_number_info_time_ss);

        if ("1".equals(mNumberName) || "6".equals(mNumberName)) {// 低频彩
            tv_number_info_time_dd.setVisibility(View.VISIBLE);
            tv_number_info_dd_des.setVisibility(View.VISIBLE);
            tv_number_info_time_hh.setVisibility(View.VISIBLE);
            tv_number_info_hh_des.setVisibility(View.VISIBLE);
        } else {// 高频彩
            tv_number_info_time_dd.setVisibility(View.GONE);
            tv_number_info_dd_des.setVisibility(View.GONE);
            tv_number_info_time_hh.setVisibility(View.GONE);
            tv_number_info_hh_des.setVisibility(View.GONE);
        }
    }
   private boolean isOneShow = true;// 首次加载
    @Override
    public void onResume() {
        isExit = true;
        if(isOneShow){
            initData();
            isOneShow = false;
        }else{
            numbersDataShow(1);
        }
        super.onResume();
        MobclickAgent.onPageStart("CurrentNumberFragment");
    }

    @Override
    public void onPause() {
        isExit = false;
        super.onPause();
        MobclickAgent.onPageEnd("CurrentNumberFragment");
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case STARTLOADING:// 开始加载
                    fl_current_reLoading.setVisibility(View.VISIBLE);
                    fl_current_data.setVisibility(View.GONE);
                    fl_current_notNet.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESSLOADING:// 加载成功
                    fl_current_data.setVisibility(View.VISIBLE);
                    fl_current_notNet.setVisibility(View.GONE);
                    fl_current_reLoading.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    isExit = true;
                    allNumbers = null;
                    obtainNewStart();
                    getCountdownOpenNumber();// 倒计时
                    break;
                case ERRORLOADING:// 加载失败
                    fl_current_data.setVisibility(View.GONE);
                    fl_current_notNet.setVisibility(View.VISIBLE);
                    fl_current_reLoading.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    isExit = false;
                    break;
                case AUTOREFRESH:// 自动和手动刷新数据
                    mSwipeRefreshLayout.setRefreshing(false);
                    obtainNewStart();
                    getCountdownOpenNumber();// 倒计时
                    break;
                case OPENSTART:// 开奖不断请求数据
                    obtainNewStart();
                    getOpenNumberStart();
                    getOpenAnimationStart();// 开奖动画
                    break;
                case OPENNUMBEROVER:// 开奖完成后重新获取最新状态
                    obtainNewStart();
                    break;
                case UPDATECOUNTDOWN:// 更新倒计时
                    showCountDownUpDate();
                    break;
                case STARTANIMA:// 播放开奖动画
                    startAnima();
                    break;
            }
        }
    };

    private int index = 0;// 快乐十分红球显示的下标位置

    /**
     * 开始播放开奖动画
     */
    private void startAnima() {
        if ("8".equals(mNumberName) || "11".equals(mNumberName) || "19".equals(mNumberName)) {
            try {
                ll_Currentnumber_numbers_copy.removeAllViews();
                int length = mNumberInfo.getNumbers().split(",").length;
                for (int j = 0; j < length; j++) {
                    LinearLayout.LayoutParams params;
                    params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                    ll_Currentnumber_numbers_copy.setLayoutParams(params);
                    LinearLayout ll = new LinearLayout(mContext);

                    params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);// ,
                    if (j == 0) {

                        params.setMargins(DisplayUtil.dip2px(mContext, 10), 0, 0, 0);
                    } else {
                        params.setMargins(DisplayUtil.dip2px(mContext, 8), 0, 0, 0);

                    }

                    ll_Currentnumber_numbers_copy.setPadding(0, DisplayUtil.dip2px(mContext, 10), 0, DisplayUtil.dip2px(mContext, 10));

                    ll.setLayoutParams(params);
                    ll.setOrientation(LinearLayout.VERTICAL);// 设置垂直布局

                    int dip = DisplayUtil.dip2px(mContext, 26);
                    params = new LinearLayout.LayoutParams(dip, dip);

                    ImageView iv = new ImageView(mContext);
                    iv.setLayoutParams(params);

                    if (j == index) {
//                        iv.setImageResource(R.mipmap.number_anim_klsf_red);// 红球
                        ImageLoader.loadFitCenter(mContext,R.mipmap.number_anim_klsf_red,R.mipmap.number_kj_icon_def).into(iv);// 红球
                    }

                    ll.addView(iv);
                    ll_Currentnumber_numbers_copy.addView(ll);
                }
                index++;
                if (index >= length) {
                    index = 0;
                }
            } catch (Exception e) {
                L.d("开奖动画异常： " + e.getMessage());
            }
        } else {
            utils.numberHistoryShow(mContext, mView, mNumberInfoCopy, 1, isOpenNumberStartHistory, isNextNumber, null);
        }
    }

    /**
     * 添加开奖动画效果
     */
    private void getOpenAnimationStart() {
        if (TextUtils.isEmpty(mNumberName) || !isOpenNumberStartHistory) {
            return;
        }
        try {
            int number = Integer.parseInt(mNumberName);
            switch (number) {
                case 15:// 北京赛车
                    isStartAnimation(10, 0);
                    break;
                case 10:// 快三
                case 16:
                case 18:
                    isStartAnimation(6, 1);
                    break;
                case 8:// 快乐十分，幸运农场
                case 11:
                case 19:
                    isStartAnimation(0, 0);
                    break;
            }
        } catch (Exception e) {
            L.d("彩种号码转换异常！ " + e.getMessage());
        }
    }

    private NumberCurrentInfo mNumberInfoCopy;// 产生的当前假数据对象
    private boolean isRnAnima = true;// 是否已经开启动画

    /**
     * 开启动画
     *
     * @param count
     * @desc 该方法每次开奖只会被调用一次
     */
    private void isStartAnimation(final int count, final int start) {
        if (isRnAnima) {
            new Thread() {
                public void run() {
                    isRnAnima = false;
                    try {
                        // 创建动画时需要显示的临时对象
                        mNumberInfoCopy = new NumberCurrentInfo();
                        mNumberInfoCopy.setName(mNumberInfo.getName());
                        mNumberInfoCopy.setIssue(mNumberInfo.getIssue());
                        mNumberInfoCopy.setNextIssue(mNumberInfo.getNextIssue());
                        mNumberInfoCopy.setTime(mNumberInfo.getTime());
                        mNumberInfoCopy.setNextTime(mNumberInfo.getNextTime());

                        while (isOpenNumberStartHistory && isExit) {
                            if ("8".equals(mNumberName) || "11".equals(mNumberName) || "19".equals(mNumberName)) {
                                SystemClock.sleep(100);
                                mHandler.sendEmptyMessage(STARTANIMA);
                            } else {
                                SystemClock.sleep(300);
                                mNumberInfoCopy.setNumbers(getRandom(count, start));// 获取动画的随机数
                                mHandler.sendEmptyMessage(STARTANIMA);
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {// 显示最新开奖号码
                                utils.numberHistoryShow(mContext, mView, mNumberInfo, 1, isOpenNumberStartHistory, isNextNumber, null);
                                if (!isOpenNumberStartHistory) {
                                    isOpenNumberOrCountDown();// 没有开奖了隐藏开奖显示
                                }
                            }
                        });
                    } catch (Exception e) {
                        L.e("开启动画异常-->" + e.getMessage());
                    }
                    isRnAnima = true;
                }

            }.start();
        }
    }

    /**
     * 生成指定长度的不重复号码
     *
     * @param count
     * @return
     */
    private String getRandom(int count, int start) {
        Random random = new Random();
        int len = 0;
        if (start == 1) {
            len = count / 2;
        } else {
            len = count;
        }
        List<Integer> arry = new ArrayList<Integer>(count);
        while (len > 0) {
            int rd = (random.nextInt(count)) + 1;
            boolean isRn = false;
            for (int i = 0, leng = arry.size(); i < leng; i++) {
                if (arry.get(i) == rd) {
                    isRn = true;
                }
            }
            if (!isRn) {
                arry.add(rd);
                len--;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, leng = arry.size(); i < leng; i++) {
            if (i == leng - 1) {
                sb.append(arry.get(i));
            } else {
                sb.append(arry.get(i) + ",");

            }
        }
        return sb.toString();
    }

    /**
     * 获取最新状态
     */
    private void obtainNewStart() {
        getCurrentBean();// 获取当前对象
        initIsHKOpenNumberStart();// 判断开奖状态
        animationPlay();
    }

    /**
     * 更新倒计时
     */
    private void showCountDownUpDate() {
        long dd = mNumberTime / (3600 * 1000) / 24;// 获取天
        long hh = mNumberTime / (3600 * 1000) % 24;// 获取相差小时
        long mm = (mNumberTime % (3600 * 1000)) / (60 * 1000);// 获取相差分
        long ss = ((mNumberTime % (3600 * 1000)) % (60 * 1000)) / 1000;// 获取相差秒

        tv_number_info_time_dd.setText(dd + "");

        String h = "";
        if (hh < 10) {
            h = "0" + hh;
        } else {
            h = "" + hh;
        }
        tv_number_info_time_hh.setText(h);

        String m = "";
        if (mm < 10) {
            m = "0" + mm;
        } else {
            m = "" + mm;
        }
        tv_number_info_time_mm.setText(m);

        String s = "";
        if (ss < 10) {
            s = "0" + ss;
        } else {
            s = "" + ss;
        }
        tv_number_info_time_ss.setText(s);
    }

    private String allNumbers = null;// 记录上一次的所有号码记录
    private Long cruuentIssue = (long) -1; // 当前期号

    /**
     * 智能刷新UI，如果数据有变动则刷新UI
     */
    private void animationPlay() {
        try {
            if (!mNumberInfo.getNumbers().contains("#")) {
                isNextNumber = false;
            }
            long mIssue = Long.parseLong(mNumberInfo.getIssue());
            if (cruuentIssue == -1 || ((cruuentIssue == mIssue) && isOpenNumberStartHistory) || cruuentIssue < mIssue || "1".equals(mNumberInfo.getName()) || "6".equals(mNumberInfo.getName())) {

                StringBuilder sb = new StringBuilder();
                sb.append(mNumberInfo.getNumbers());
                sb.append(mNumberInfo.getNextTime());
                sb.append(isOpenNumberStartHistory);

                if (allNumbers == null || !allNumbers.equals(sb.toString())) {
                    allNumbers = sb.toString();
                    isOpenNumberOrCountDown();// 显示倒计时或正在开奖中
                    utils.numberHistoryShow(mContext, mView, mNumberInfo, 1, isOpenNumberStartHistory, isNextNumber, null);
                }
                if (cruuentIssue < mIssue) {
                    cruuentIssue = mIssue;
                }
            }
        } catch (Exception e) {
            L.d("智能刷新UI异常： " + e.getMessage());
        }
    }

    /**
     * 获取当前对象
     */
    private void getCurrentBean() {
        // 获取当前要显示的对象
        for (int i = 0; i < numberlist.size(); i++) {
            if (null != numberlist.get(i)) {
                if (mNumberName.equals(numberlist.get(i).getName())) {
                    mNumberInfo = numberlist.get(i);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        numbersDataShow(1);// 请求数据
    }
}
