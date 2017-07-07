package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.NumberDataUtils;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 彩票开奖列表
 * Created by hhly107 on 2016/4/6.
 */
public class NumbersActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ImageView public_img_back;// 返回到菜单
    private Context mContext;
    private TextView public_txt_title;// 开奖列表标题展示
    private ImageView public_btn_set;// 定制彩种
    private ListView mListView;// 数据加载成功
    private SwipeRefreshLayout mSwipeRefreshLayout;// 下拉刷新
    private LinearLayout ll_numbers_startLoading;// 开始加载
    private LinearLayout ll_numbers_errorLoading;// 加载失败
    private List<NumberCurrentInfo> numberlist = new ArrayList<>();// 各种彩票开奖对象
    private TextView reLoading;// 重新加载数据

    // private int[] sorts = new int[] { 1, 6, 15, 8, 11, 19, 2, 4, 5, 23, 3, 7,
    // 20, 22, 10, 16, 18 };// 默认排序彩种Name

    private List<Integer> sortDef1 = new ArrayList<Integer>(26);
    private List<NumberCurrentInfo> numberSortList = new ArrayList<NumberCurrentInfo>();// 系统默认排序的彩种对象集合

    private static final int STARTLOADING = 0;// 开始加载状态
    private static final int SUCCESSLOADING = 1;// 加载成功
    private static final int ERRORLOADING = 2;// 加载失败
    private static final int COUNTDOWN = 3;// 倒计时
    private static final int AUTOREFRESH = 4;// 自动和手动刷新数据
    private static final int RENOTIFY = 5;// 重新刷新界面数据

    public static String serverTime;// 服务器时间
    private long HKnumberTime;// 香港彩开奖时间
    private long QXCnumberTime;// 七星彩开奖时间
    private long SSQnumberTime;// 双色球开奖时间
    private long QLCnumberTime;// 七乐彩开奖时间
    private long DLTnumberTime;// 大乐透开奖时间

    private List<String> numbers = new ArrayList<String>();// 存放开奖号码
    private List<String> zodiacs = new ArrayList<String>();// 存放六合彩生肖

    private numbersAdapter mAdapter;
    private boolean isHKCnumberTime = false;// 香港彩是否在倒计时false
    private boolean isQXCCnumberTime = false;// 七星彩是否在倒计时false
    private boolean isSSQCnumberTime = false;// 双色球是否在倒计时false
    private boolean isQLCCnumberTime = false;// 七乐彩是否在倒计时false
    private boolean isDLTCnumberTime = false;// 大乐透是否在倒计时false

    private boolean isHKOpenNumberStartNF = false; // 香港彩是否正在开奖中false
    private boolean isQXCOpenNumberStartNF = false; // 七星彩是否正在开奖中false
    private boolean isSSQOpenNumberStartNF = false; // 双色球是否正在开奖中false
    private boolean isQLCOpenNumberStartNF = false; // 七乐彩是否正在开奖中false
    private boolean isDLTOpenNumberStartNF = false; // 大乐透是否正在开奖中false
    private boolean isOtherOpenNumberStartNF = false; // 其它彩种是否正在开奖中false

    private boolean isHKHttps = true;// 香港彩正在开奖中的状态true
    private boolean isQXCHttps = true;// 七星彩正在开奖中的状态true
    private boolean isSSQHttps = true;// 双色球正在开奖中的状态true
    private boolean isQLCHttps = true;// 七乐彩正在开奖中的状态true
    private boolean isDLTHttps = true;// 大乐透正在开奖中的状态true

    private boolean isHKReqDataState = false;// 香港彩请求数据状态
    private boolean isQXCReqDataState = false;// 七星彩请求数据状态
    private boolean isSSQReqDataState = false;// 双色球请求数据状态
    private boolean isQLCReqDataState = false;// 七乐彩请求数据状态
    private boolean isDLTReqDataState = false;// 大乐透请求数据状态

    private boolean isNextNumber = false;// 正在获取下一期开奖号码false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frage_numbers);
        mContext = NumbersActivity.this;

        initView();
        initEnver();
    }

    boolean isOnResume = false;

    @Override
    public void onResume() {
        isExit = true;
        allNumbers = null;
        if (isOnResume) {
            numbersDataShow(5);
        } else {
            initData();
            isOnResume = true;
        }
        super.onResume();
    }

    private boolean isExit = true;

    @Override
    public void onPause() {
        isExit = false;
        super.onPause();
    }

    private void initData() {

        if (null != sortDef1) {
            sortDef1.clear();
        }
        // 1, 6, 24,29,28,27,25,26, 15, 8, 11, 19, 2, 4, 5, 23, 3, 7, 20, 22, 10, 16, 18, 30, 31, 32  按需求排列彩种
        sortDef1.add(1);
        sortDef1.add(6);

        sortDef1.add(24);
        sortDef1.add(29);
//        sortDef1.add(28);
//        sortDef1.add(27);
        sortDef1.add(25);
        sortDef1.add(26);

        sortDef1.add(15);
        sortDef1.add(8);
        sortDef1.add(11);
        sortDef1.add(19);
        sortDef1.add(2);
        sortDef1.add(4);
        sortDef1.add(5);
        sortDef1.add(23);
//        sortDef1.add(3);
        sortDef1.add(7);
        sortDef1.add(20);
        sortDef1.add(22);
        sortDef1.add(10);
        sortDef1.add(16);
        sortDef1.add(18);

        sortDef1.add(30);
        sortDef1.add(31);
        sortDef1.add(32);

        numbersDataShow(0);// 开奖列表显示

    }

    private boolean isThreadRequestData = true;// 高频彩正在开奖中，创建线程获取数据
    private boolean isThreadRequestData2 = true;// 高频彩正在开奖中，创建线程获取数据

    /**
     * 自动更新并获取数据
     */
    private void autoRefreshData() {
        long updateTime = -1;// 最小更新时间差
        try {
            for (int i = 0, len = numberSortList.size(); i < len; i++) {
                String nextTime = numberSortList.get(i).getNextTime();// 获取下一期开奖时间
                if (nextTime != null) {
                    long num = DateUtil.getCurrentTime(nextTime) - Long.parseLong(serverTime);// 获取每个彩种下一期开奖时间
                    if (num > 0) {
                        if (i == 0) {
                            updateTime = num;
                        } else {
                            if (updateTime > num) {
                                updateTime = num;// 获取最小开奖时间
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            L.e("时间转换异常-->" + e.getMessage());
        }

        final long dateTime = updateTime;
        // 如果七星彩或香港彩正在开奖中，则不请求数据
        if (!isHKOpenNumberStartNF && !isQXCOpenNumberStartNF && !isSSQOpenNumberStartNF && !isQLCOpenNumberStartNF && !isDLTOpenNumberStartNF) {
            if (isOtherOpenNumberStartNF) {
                // 如果有其它高频彩种正在开奖中，则第隔一秒请求数据
                if (isThreadRequestData && isExit) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                isThreadRequestData = false;
                                while (isOtherOpenNumberStartNF && isExit && !isHKOpenNumberStartNF && !isQXCOpenNumberStartNF && !isSSQOpenNumberStartNF && !isQLCOpenNumberStartNF && !isDLTOpenNumberStartNF) {
                                    sleep(1000);
                                    numbersDataShow(1);
                                    L.d("xxx", "高频彩请求数据。。。");
                                }
                                isThreadRequestData = true;
                            } catch (Exception e) {
                                L.e("自动请求休眠异常-->" + e.getMessage());
                            }
                        }
                    }.start();
                }
            } else {
                // 若没有正在开奖中的，则休眠到开奖时间 了再请求数据
                if (isThreadRequestData2 && isExit && dateTime >= 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                isThreadRequestData2 = false;
                                sleep(dateTime);
                                numbersDataShow(1);
                                isThreadRequestData2 = true;
                            } catch (Exception e) {
                                L.e("自动请求休眠异常-->" + e.getMessage());
                            }
                        }
                    }.start();
                }
            }
        }
    }

    /**
     * 倒计时开奖
     */
    private void getCountdownOpenNumber() {

        try {
            for (int i = 0, len = numberSortList.size(); i < len; i++) {
                if ("1".equals(numberSortList.get(i).getName())) {

                    // 获取下一期开奖时间
                    String nextTime = numberSortList.get(i).getNextTime();

                    final long num = DateUtil.getCurrentTime(nextTime) - Long.parseLong(serverTime);// 获取每个彩种下一期开奖时间

                    HKnumberTime = num;// 香港彩开奖时间

                    if (!isHKCnumberTime && !isHKOpenNumberStartNF) {
                        isHKReqDataState = false;
                        new Thread() {
                            @Override
                            public void run() {
                                isHKCnumberTime = true;
                                try {
                                    while (HKnumberTime > 0 && isExit && !isHKOpenNumberStartNF) {

                                        SystemClock.sleep(1000);
                                        HKnumberTime -= 1000;

                                        long hh = HKnumberTime / (3600 * 1000);// 获取相差小时

                                        if (hh <= 8) {// 已显示倒计时，通知数据发生改变，更新界面
                                            mHandler.sendEmptyMessage(COUNTDOWN);
                                        }
                                    }
                                } catch (Exception e) {
                                    L.e("自动请求休眠异常-->" + e.getMessage());
                                }
                                isHKCnumberTime = false;

                                if (isExit) {
                                    numbersDataShow(1);
                                }
                            }
                        }.start();
                    } else if (isHKOpenNumberStartNF) {// null !=
                        // currentHKOpenNumberIssue &&
                        // 如果倒计时结束后下一期还未开奖时，不断请求数据
                        // 走到这里应该让Item显示正在 开奖中.....
                        // -->请求时间太快，次数太频繁，容易挂掉。需要加上休眠
                        if (isHKHttps) {// 判断有没有开启线程请求数据，如果有，就不开启了
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        isHKHttps = false;
                                        // isNextNumber = true;// 正在获取下一期开奖号码

                                        while ((isHKOpenNumberStartNF || NumberDataUtils.isDynamicNumber) && isExit) {

                                            sleep(1000);
                                            if (!isQXCReqDataState && !isSSQReqDataState && !isQLCReqDataState && !isDLTReqDataState) {
                                                isHKReqDataState = true;
                                                numbersDataShow(1);
                                                L.d("xxx", "香港彩请求数据。。。");
                                            }
                                        }
                                        isHKHttps = true;
                                        isHKReqDataState = false;
                                    } catch (Exception e) {
                                        L.e("香港彩请求最新开奖号码时,休眠异常-->" + e.getMessage());
                                    }
                                }
                            }.start();
                        }

                        // 判断中是否已经在开奖了
                        if (!numberSortList.get(i).getNumbers().contains("#")) {

                            isNextNumber = false;// 已经获取到了下一期的开奖号码

                        } else {
                            if (HKnumberTime > 0 && !NumberDataUtils.isDynamicNumber) {// 开奖完成了

                                isHKOpenNumberStartNF = false;// 隐藏正在开奖中
                                isHKHttps = true;

                                numbersDataShow(1);
                            }
                        }
                    }

                } else if ("6".equals(numberSortList.get(i).getName())) {

                    // 获取下一期开奖时间
                    String nextTime = numberSortList.get(i).getNextTime();
                    long num = DateUtil.getCurrentTime(nextTime) - Long.parseLong(serverTime);// 获取每个彩种下一期开奖时间

                    // 七星彩开奖时间
                    QXCnumberTime = num;

                    if (!isQXCCnumberTime && !isQXCOpenNumberStartNF) {
                        isQXCReqDataState = false;
                        new Thread() {
                            @Override
                            public void run() {
                                isQXCCnumberTime = true;
                                try {
                                    while (QXCnumberTime > 0 && isExit && !isQXCOpenNumberStartNF) {
                                        SystemClock.sleep(1000);
                                        QXCnumberTime -= 1000;

                                        long hh = QXCnumberTime / (3600 * 1000);// 获取相差小时

                                        if (hh <= 8) {// 如何香港彩没有在倒计时，同时倒计时已显示，则通知ListView更新
                                            mHandler.sendEmptyMessage(COUNTDOWN);
                                        }

                                    }
                                } catch (Exception e) {
                                    L.e("自动请求休眠异常-->" + e.getMessage());
                                }
                                isQXCCnumberTime = false;

                                if (isExit) {
                                    numbersDataShow(1);
                                }
                            }
                        }.start();
                    } else if (isQXCOpenNumberStartNF) {
                        // 如果倒计时结束后下一期还未开奖时，不断请求数据
                        // 走到这里应该让Item显示正在 开奖中.....
                        // -->请求时间太快，次数太频繁，容易挂掉。需要加上休眠
                        if (isQXCHttps) {// 判断有没有开启线程请求数据，如果有，就不开启了

                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        isQXCHttps = false;

                                        while (isQXCOpenNumberStartNF && isExit) {// 需要加个循环请求

                                            sleep(1000);
                                            // 如果其它调频彩正在开奖中，这里就不重复请求数据了
                                            if (!isHKReqDataState && !isSSQReqDataState && !isQLCReqDataState && !isDLTReqDataState) {
                                                isQXCReqDataState = true;
                                                numbersDataShow(1);
                                                L.d("xxx", "七星彩请求数据。。。");
                                            }
                                        }
                                        isQXCHttps = true;
                                        isQXCReqDataState = false;
                                    } catch (Exception e) {
                                        L.e("七星彩请求最新开奖号码时,休眠异常-->" + e.getMessage());
                                    }
                                }
                            }.start();
                        }

                        if (num > 0) {
                            isQXCOpenNumberStartNF = false;// 隐藏正在开奖中
                            isQXCHttps = true;

                            numbersDataShow(1);
                        }
                    }
                } else if ("24".equals(numberSortList.get(i).getName())) {

                    // 获取下一期开奖时间
                    String nextTime = numberSortList.get(i).getNextTime();
                    long num = DateUtil.getCurrentTime(nextTime) - Long.parseLong(serverTime);// 获取每个彩种下一期开奖时间

                    // 双色球开奖时间
                    SSQnumberTime = num;

                    if (!isSSQCnumberTime && !isSSQOpenNumberStartNF) {
                        isSSQReqDataState = false;
                        new Thread() {
                            @Override
                            public void run() {
                                isSSQCnumberTime = true;
                                try {
                                    while (SSQnumberTime > 0 && isExit && !isSSQOpenNumberStartNF) {
                                        SystemClock.sleep(1000);
                                        SSQnumberTime -= 1000;

                                        long hh = SSQnumberTime / (3600 * 1000);// 获取相差小时

                                        if (hh <= 8) {// 如何香港彩没有在倒计时，同时倒计时已显示，则通知ListView更新
                                            mHandler.sendEmptyMessage(COUNTDOWN);
                                        }

                                    }
                                } catch (Exception e) {
                                    L.e("自动请求休眠异常-->" + e.getMessage());
                                }
                                isSSQCnumberTime = false;

                                if (isExit) {
                                    numbersDataShow(1);
                                }
                            }
                        }.start();
                    } else if (isSSQOpenNumberStartNF) {
                        // 如果倒计时结束后下一期还未开奖时，不断请求数据
                        // 走到这里应该让Item显示正在 开奖中.....
                        // -->请求时间太快，次数太频繁，容易挂掉。需要加上休眠
                        if (isSSQHttps) {// 判断有没有开启线程请求数据，如果有，就不开启了

                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        isSSQHttps = false;

                                        while (isSSQOpenNumberStartNF && isExit) {// 需要加个循环请求

                                            sleep(1000);

                                            // 如果其它高频彩正在开奖中，这里就不重复请求数据了
                                            if (!isHKReqDataState && !isQXCReqDataState && !isQLCReqDataState && !isDLTReqDataState) {
                                                isSSQReqDataState = true;
                                                numbersDataShow(1);
                                                L.d("xxx", "双色球请求数据。。。");
                                            }
                                        }
                                        isSSQHttps = true;
                                        isSSQReqDataState = false;
                                    } catch (Exception e) {
                                        L.e("双色球请求最新开奖号码时,休眠异常-->" + e.getMessage());
                                    }
                                }
                            }.start();
                        }

                        if (num > 0) {
                            isSSQOpenNumberStartNF = false;// 隐藏正在开奖中
                            isSSQHttps = true;

                            numbersDataShow(1);
                        }
                    }
                } else if ("28".equals(numberSortList.get(i).getName())) {

                    // 获取下一期开奖时间
                    String nextTime = numberSortList.get(i).getNextTime();
                    long num = DateUtil.getCurrentTime(nextTime) - Long.parseLong(serverTime);// 获取每个彩种下一期开奖时间

                    // 七乐彩开奖时间
                    QLCnumberTime = num;

                    if (!isQLCCnumberTime && !isQLCOpenNumberStartNF) {
                        isQLCReqDataState = false;
                        new Thread() {
                            @Override
                            public void run() {
                                isQLCCnumberTime = true;
                                try {
                                    while (QLCnumberTime > 0 && isExit && !isQLCOpenNumberStartNF) {
                                        SystemClock.sleep(1000);
                                        QLCnumberTime -= 1000;

                                        long hh = QLCnumberTime / (3600 * 1000);// 获取相差小时

                                        if (hh <= 8) {// 如何香港彩没有在倒计时，同时倒计时已显示，则通知ListView更新
                                            mHandler.sendEmptyMessage(COUNTDOWN);
                                        }

                                    }
                                } catch (Exception e) {
                                    L.e("自动请求休眠异常-->" + e.getMessage());
                                }
                                isQLCCnumberTime = false;

                                if (isExit) {
                                    numbersDataShow(1);
                                }
                            }
                        }.start();
                    } else if (isQLCOpenNumberStartNF) {
                        // 如果倒计时结束后下一期还未开奖时，不断请求数据
                        // 走到这里应该让Item显示正在 开奖中.....
                        // -->请求时间太快，次数太频繁，容易挂掉。需要加上休眠
                        if (isQLCHttps) {// 判断有没有开启线程请求数据，如果有，就不开启了

                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        isQLCHttps = false;

                                        while (isQLCOpenNumberStartNF && isExit) {// 需要加个循环请求

                                            sleep(1000);

                                            // 如果其它高频彩正在开奖中，这里就不重复请求数据了
                                            if (!isHKReqDataState && !isQXCReqDataState && !isSSQReqDataState && !isDLTReqDataState) {
                                                isQLCReqDataState = true;
                                                numbersDataShow(1);
                                                L.d("xxx", "七乐彩请求数据。。。");
                                            }
                                        }
                                        isQLCHttps = true;
                                        isQLCReqDataState = false;
                                    } catch (Exception e) {
                                        L.e("七乐彩请求最新开奖号码时,休眠异常-->" + e.getMessage());
                                    }
                                }
                            }.start();
                        }

                        if (num > 0) {
                            isQLCOpenNumberStartNF = false;// 隐藏正在开奖中
                            isQLCHttps = true;

                            numbersDataShow(1);
                        }
                    }
                } else if ("29".equals(numberSortList.get(i).getName())) {

                    // 获取下一期开奖时间
                    String nextTime = numberSortList.get(i).getNextTime();
                    long num = DateUtil.getCurrentTime(nextTime) - Long.parseLong(serverTime);// 获取每个彩种下一期开奖时间

                    // 大乐透开奖时间
                    DLTnumberTime = num;

                    if (!isDLTCnumberTime && !isDLTOpenNumberStartNF) {
                        isDLTReqDataState = false;
                        new Thread() {
                            @Override
                            public void run() {
                                isDLTCnumberTime = true;
                                try {
                                    while (DLTnumberTime > 0 && isExit && !isDLTOpenNumberStartNF) {
                                        SystemClock.sleep(1000);
                                        DLTnumberTime -= 1000;

                                        long hh = DLTnumberTime / (3600 * 1000);// 获取相差小时

                                        if (hh <= 8) {// 如何香港彩没有在倒计时，同时倒计时已显示，则通知ListView更新
                                            mHandler.sendEmptyMessage(COUNTDOWN);
                                        }

                                    }
                                } catch (Exception e) {
                                    L.e("自动请求休眠异常-->" + e.getMessage());
                                }
                                isDLTCnumberTime = false;

                                if (isExit) {
                                    numbersDataShow(1);
                                }
                            }
                        }.start();
                    } else if (isDLTOpenNumberStartNF) {
                        // 如果倒计时结束后下一期还未开奖时，不断请求数据
                        // 走到这里应该让Item显示正在 开奖中.....
                        // -->请求时间太快，次数太频繁，容易挂掉。需要加上休眠
                        if (isDLTHttps) {// 判断有没有开启线程请求数据，如果有，就不开启了

                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        isDLTHttps = false;

                                        while (isDLTOpenNumberStartNF && isExit) {// 需要加个循环请求

                                            sleep(1000);

                                            // 如果其它高频彩正在开奖中，这里就不重复请求数据了
                                            if (!isHKReqDataState && !isQXCReqDataState && !isSSQReqDataState && !isQLCReqDataState) {
                                                isDLTReqDataState = true;
                                                numbersDataShow(1);
                                                L.d("xxx", "大乐透请求数据。。。");
                                            }
                                        }
                                        isDLTHttps = true;
                                        isDLTReqDataState = false;
                                    } catch (Exception e) {
                                        L.e("大乐透请求最新开奖号码时,休眠异常-->" + e.getMessage());
                                    }
                                }
                            }.start();
                        }

                        if (num > 0) {
                            isDLTOpenNumberStartNF = false;// 隐藏正在开奖中
                            isDLTHttps = true;

                            numbersDataShow(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 总异常捕获
            L.d("倒计时功能模块异常-->>" + e.getMessage());
        }
    }

    /**
     * 选择进入详细页面
     */
    private void initEnver() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isExit) {
                    MobclickAgent.onEvent(mContext, "Lottery_Info");
                    // 跳转到详情页面
                    Intent intent = new Intent(mContext, NumbersInfoBaseActivity.class);
                    intent.putExtra("numberName", numberSortList.get(position).getName());
                    startActivity(intent);
                    isExit = false;
                }
            }
        });
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 开始加载
                    ll_numbers_startLoading.setVisibility(View.VISIBLE);
                    ll_numbers_errorLoading.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESSLOADING:// 加载成功
                    ll_numbers_startLoading.setVisibility(View.GONE);
                    ll_numbers_errorLoading.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    isExit = true;
                    allNumbers = null;
                    numberSort();// 彩种排序
                    listDataCopy();// 备份list数据
                    initIsHKOpenNumberStart();// 判断进入的时候香港彩是否正在开奖中
                    mAdapter = new numbersAdapter();
                    mListView.setAdapter(mAdapter);
                    autoRefreshData();// 根据开奖时间自动刷新数据
                    getCountdownOpenNumber();// 倒计时开奖
                    break;
                case ERRORLOADING:// 加载失败
                    ll_numbers_startLoading.setVisibility(View.GONE);
                    ll_numbers_errorLoading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    isExit = false;
                    break;
                case COUNTDOWN:// 倒计时
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case AUTOREFRESH:// 自动和手动刷新数据
                    mSwipeRefreshLayout.setRefreshing(false);
                    initIsHKOpenNumberStart();// 判断进入的时候香港彩是否正在开奖中
                    animationPlay();// 智能刷新UI
                    autoRefreshData();// 根据开奖时间自动刷新数据
                    break;
                case RENOTIFY:// 重新刷新界面数据
                    numberSort();// 彩种排序
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    listDataCopy();// 备份list数据
                    initIsHKOpenNumberStart();// 判断进入的时候香港彩是否正在开奖中
                    autoRefreshData();// 根据开奖时间自动刷新数据
                    getCountdownOpenNumber();// 倒计时开奖
                    break;
            }
        }

    };

    private String allNumbers = null;// 记录上一次的所有号码记录
    private List<NumberCurrentInfo> numberSortListCopy = new ArrayList<NumberCurrentInfo>();
    private List<NumberCurrentInfo> numberlistCopy = new ArrayList<NumberCurrentInfo>();// 没排序前的备份

    /**
     * 智能刷新UI，如果数据有变动则刷新UI
     */
    private void animationPlay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = numberlist.size(); i < len; i++) {
            if (null != numberlist.get(i)) {
                sb.append(numberlist.get(i).getNumbers());
                sb.append(numberlist.get(i).getNextTime());
                sb.append(isHKOpenNumberStartNF);
                sb.append(isQXCOpenNumberStartNF);
                sb.append(isSSQOpenNumberStartNF);
                sb.append(isQLCOpenNumberStartNF);
                sb.append(isDLTOpenNumberStartNF);
            }
        }

        if (allNumbers == null || !allNumbers.equals(sb.toString())) {
            if (allNumbers == null) {// 下拉刷新的时候会进来执行
                numberSort();// 彩种排序
                upAssignDataChanged();// 获取更新的对象，改变背景属性值
            } else {
                // 判断彩种是否有更新了,若没有变为新的期号则不更新界面
                for (int i = 0, len = numberlist.size(); i < len; i++) {
                    if (null != numberlist.get(i) && null != numberlistCopy.get(i)) {
                        if (numberlist.get(i).getIssue() != null && numberlistCopy.get(i).getIssue() != null) {
                            if (("1".equals(numberlist.get(i).getName())) || ("6".equals(numberlist.get(i).getName()))) {
                                numberSort();// 彩种排序
                                mAdapter.notifyDataSetChanged();// 刷新界面
                            } else if (Long.parseLong(numberlist.get(i).getIssue()) > Long.parseLong(numberlistCopy.get(i).getIssue())) {
                                numberSort();// 彩种排序
                                upAssignDataChanged();// 获取更新的对象，改变背景属性值
                                allNumbers = sb.toString();
                                getCountdownOpenNumber();// 倒计时开奖
                                return;
                            }
                        }
                    }
                }
            }
            allNumbers = sb.toString();
            getCountdownOpenNumber();// 倒计时开奖
        }
    }

    int num = -1;
    int num2 = -1;
    List<Integer> openNumberIndex = new ArrayList<Integer>();// 记录当前开奖下标

    /**
     * 获取更新的对象，改变背景属性值
     */
    private void upAssignDataChanged() {
        openNumberIndex.clear();
        Long cruuentIssue = (long) -1; // 当前期号

        if (numberSortList.size() != numberSortListCopy.size()) {
            listDataCopy();// 备份list数据
            return;
        }
        // 判断哪个对象发生了改变
        try {
            for (int i = 0, len = numberSortList.size(); i < len; i++) {
                if (!numberSortList.get(i).getIssue().equals(numberSortListCopy.get(i).getIssue()) || !numberSortList.get(i).getNumbers().equals(numberSortListCopy.get(i).getNumbers())) {
                    // 判断数据是否发生改变了

                    if (num == -1 || num != i) {// 第一次进来或彩种变化了
                        num = i;

                        cruuentIssue = Long.parseLong(numberSortList.get(num).getIssue());
                        openNumberIndex.add(num);// 添加变化的彩种
                    } else {
                        // 还是相同彩种，获取最新的期号
                        if (cruuentIssue < Long.parseLong(numberSortList.get(num).getIssue())) {

                            cruuentIssue = Long.parseLong(numberSortList.get(num).getIssue());
                        }
                    }
                }
            }

            listDataCopy();// 备份list数据

            // 修改变更的对象的属性值
            for (int i = 0, len = openNumberIndex.size(); i < len; i++) {
                if (!"1".equals(numberSortList.get(openNumberIndex.get(i)).getName()) && !"6".equals(numberSortList.get(openNumberIndex.get(i)).getName())) {
                    if (num2 == -1 || num2 != openNumberIndex.get(i)) {// 当彩种发生变化
                        numberSortList.get(openNumberIndex.get(i)).setOpen(true);
                        num2 = openNumberIndex.get(i);

                        mAdapter.notifyDataSetChanged();// 刷新界面
                        final int j = i;
                        new Thread() {
                            public void run() {
                                try {
                                    SystemClock.sleep(2000);
                                    if (openNumberIndex.size() > j) {
                                        if (numberSortList.size() > openNumberIndex.get(j)) {// 判断集合有没有被清除，若清除了默认就为false了，若没有，则手动改为false
                                            numberSortList.get(openNumberIndex.get(j)).setOpen(false);
                                        }
                                    }
                                } catch (Exception e) {
                                    L.d("下标越界： " + e.getMessage());
                                }
                                mHandler.sendEmptyMessage(COUNTDOWN);// 刷新界面
                            }

                        }.start();
                    } else {
                        if (cruuentIssue < Long.parseLong(numberSortList.get(i).getIssue())) {// Long.parseLong(numberSortList.get(i).getIssue())
                            numberSortList.get(openNumberIndex.get(i)).setOpen(true);

                            mAdapter.notifyDataSetChanged();// 刷新界面
                            final int j = i;
                            new Thread() {
                                public void run() {
                                    try {
                                        SystemClock.sleep(2000);
                                        if (openNumberIndex.size() > j) {
                                            if (numberSortList.size() > openNumberIndex.get(j)) {
                                                numberSortList.get(openNumberIndex.get(j)).setOpen(false);
                                            }
                                        }
                                    } catch (Exception e) {
                                        L.d("下标越界： " + e.getMessage());
                                    }
                                    mHandler.sendEmptyMessage(COUNTDOWN);// 刷新界面
                                }

                            }.start();
                        }
                    }
                }
            }
        } catch (Exception e) {
            L.d("数组脚标越界: " + e.getMessage());
        }
    }

    /**
     * 备份list数据
     */
    private synchronized void listDataCopy() {
        numberSortListCopy.clear();
        for (NumberCurrentInfo info1 : numberSortList) {
            numberSortListCopy.add(info1);
        }

        numberlistCopy.clear();
        for (NumberCurrentInfo info2 : numberlist) {
            numberlistCopy.add(info2);
        }
    }

    /**
     * 初始化进入界面时判断是否正在开奖中
     */
    private void initIsHKOpenNumberStart() {
        try {
            isOtherOpenNumberStartNF = false;// 默认没有开奖中
            for (int i = 0; i < numberSortList.size(); i++) {
                if ("1".equals(numberSortList.get(i).getName())) {
                    long numTime = DateUtil.getCurrentTime(numberSortList.get(i).getNextTime()) - Long.parseLong(serverTime);

                    if (numTime <= 0 || !numberSortList.get(i).getNumbers().contains("#")) {

                        isHKOpenNumberStartNF = true;// 正在开奖中...

                        // 判断中是否已经在开奖了
                        // 未获取到了下一期的开奖号码
                        isNextNumber = numberSortList.get(i).getNumbers().contains("#");

                    } else {
                        isHKOpenNumberStartNF = false;
                        isNextNumber = false;
                        NumberDataUtils.isDynamicNumber = false;
                    }

                } else if ("6".equals(numberSortList.get(i).getName())) {
                    long numTime = DateUtil.getCurrentTime(numberSortList.get(i).getNextTime()) - Long.parseLong(serverTime);
                    // 七星彩正在开奖中...
                    isQXCOpenNumberStartNF = numTime <= 0;

                } else if ("24".equals(numberSortList.get(i).getName())) {
                    long numTime = DateUtil.getCurrentTime(numberSortList.get(i).getNextTime()) - Long.parseLong(serverTime);
                    // 双色球正在开奖中...
                    isSSQOpenNumberStartNF = numTime <= 0;

                } else if ("28".equals(numberSortList.get(i).getName())) {
                    long numTime = DateUtil.getCurrentTime(numberSortList.get(i).getNextTime()) - Long.parseLong(serverTime);
                    // 七乐彩正在开奖中...
                    isQLCOpenNumberStartNF = numTime <= 0;

                } else if ("29".equals(numberSortList.get(i).getName())) {
                    long numTime = DateUtil.getCurrentTime(numberSortList.get(i).getNextTime()) - Long.parseLong(serverTime);
                    // 大乐透正在开奖中...
                    isDLTOpenNumberStartNF = numTime <= 0;

                } else if ("30".equals(numberSortList.get(i).getName()) || "31".equals(numberSortList.get(i).getName()) || "32".equals(numberSortList.get(i).getName())) {
                    // 此三个彩种没有开奖状态
                    isOtherOpenNumberStartNF = false;
                } else {// 其它彩种是否正在开奖中
                    long numTime = DateUtil.getCurrentTime(numberSortList.get(i).getNextTime()) - Long.parseLong(serverTime);
                    if (numTime <= 0) {
                        isOtherOpenNumberStartNF = true;// 有高频彩正在开奖中...
                    }
                }
            }
        } catch (Exception e) {
            L.d("时间日期转换异常！" + e.getMessage());
        }
    }

    //private Animation animation;
    //private ImageView iv_loading_img;

    /**
     * 给彩种排序，按指定顺序显示彩种
     */
    private void numberSort() {
        numberSortList.clear();// 清空数据

        String oks = PreferenceUtil.getString(MyConstants.NUMBEROKS, null);// 获取用户已定制彩种

        try {
            if (!TextUtils.isEmpty(oks)) {
                String[] split = oks.split(",");

                for (int j = 0, len1 = split.length; j < len1; j++) {
                    for (int i = 0, len2 = numberlist.size(); i < len2; i++) {
                        if (null != numberlist.get(i)) {
                            if ((split[j] + "").equals(numberlist.get(i).getName())) {
                                numberSortList.add(numberlist.get(i));
                            }
                        }
                    }
                }
            } else {
                // 按照默认方式排序
                for (int j = 0, len3 = sortDef1.size(); j < len3; j++) {
                    for (int i = 0, len4 = numberlist.size(); i < len4; i++) {
                        if (null != numberlist.get(i)) {
                            if ((sortDef1.get(j) + "").equals(numberlist.get(i).getName())) {
                                numberSortList.add(numberlist.get(i));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            L.d("脚标越界：" + e.getMessage());
        }
    }

    public void initView() {

        // 加载动画
        /*iv_loading_img = (ImageView) view.findViewById(R.id.iv_loading_img);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.cirle);
        animation.setInterpolator(new LinearInterpolator());
        iv_loading_img.startAnimation(animation);*/
        // 标题
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.number_tltle_txt);
        // 侧滑图标
        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setImageDrawable(getResources().getDrawable(R.mipmap.back));
        public_img_back.setOnClickListener(this);

        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        // 定制彩种
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setImageResource(R.mipmap.cpi_filtrate);
        public_btn_set.setVisibility(View.VISIBLE);
        public_btn_set.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.ll_numbers_content);// listView加载成功
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.number_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(NumbersActivity.this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));

        ll_numbers_startLoading = (LinearLayout) findViewById(R.id.ll_numbers_startLoading);
        ll_numbers_errorLoading = (LinearLayout) findViewById(R.id.ll_numbers_errorLoading);

        reLoading = (TextView) findViewById(R.id.reLoading);
        reLoading.setOnClickListener(this);

    }

    /**
     * 开奖列表详细页面数据显示
     */
    public synchronized void numbersDataShow(final int num) {

        if (num == 0) {
            // 发送消息，开始加载数据
            mHandler.sendEmptyMessage(STARTLOADING);
        }

        VolleyContentFast.requestJsonByGet(AppConstants.numberHistoryURLs[0], new VolleyContentFast.ResponseSuccessListener<NumbersOpenBean>() {
            @Override
            public synchronized void onResponse(final NumbersOpenBean jsonObject) {
                if (null != jsonObject) {// 判断数据是否为空

                    numberlist.clear();
                    numberlist.addAll(jsonObject.getNumLotteryResults());

                    serverTime = null;
                    serverTime = jsonObject.getServerTime();

                    L.d("xxx", "请求后台数据。。。");
                    if (num == 1) {
                        // 发送自动刷新和手动刷新加载数据成功消息
                        mHandler.sendEmptyMessage(AUTOREFRESH);
                    } else if (num == 5) {
                        // 界面重新显示时,刷新界面数据
                        mHandler.sendEmptyMessage(RENOTIFY);
                    } else {
                        // 发送加载数据成功消息
                        mHandler.sendEmptyMessage(SUCCESSLOADING);
                    }
                } else {
                    mHandler.sendEmptyMessage(ERRORLOADING);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERRORLOADING);
            }
        }, NumbersOpenBean.class);
    }

    /**
     * 数据
     *
     * @ClassName: numbersAdapter
     * @date 2015-10-25 下午3:06:50
     */
    private class numbersAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (null != numberSortList) {
                return numberSortList.size();
            }
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
                    convertView = View.inflate(mContext, R.layout.numbers_current_contentinfo, null);// 国语
                } else {
                    convertView = View.inflate(mContext, R.layout.numbers_current_contentinfo_i18n, null);// 英语
                    holder.tv_number_time_name2 = (TextView) convertView.findViewById(R.id.tv_number_time_name2);
                }

                holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
                holder.tv_numbers_name = (TextView) convertView.findViewById(R.id.tv_numbers_name);
                holder.tv_numbers_issue = (TextView) convertView.findViewById(R.id.tv_numbers_issue);
                holder.tv_numbers_time = (TextView) convertView.findViewById(R.id.tv_numbers_time);
                holder.tv_number_desc = (TextView) convertView.findViewById(R.id.tv_number_desc);
                holder.ll_numbers_container = (LinearLayout) convertView.findViewById(R.id.ll_numbers_container);
                // 倒计时部分Item
                holder.ll_numbers_time = (LinearLayout) convertView.findViewById(R.id.ll_number_time_item);
                holder.tv_number_time_name = (TextView) convertView.findViewById(R.id.tv_number_time_name);

                holder.tv_number_time_hh = (TextView) convertView.findViewById(R.id.tv_number_time_hh);
                holder.tv_number_time_mm = (TextView) convertView.findViewById(R.id.tv_number_time_mm);
                holder.tv_number_time_ss = (TextView) convertView.findViewById(R.id.tv_number_time_ss);

                holder.ll_lastTime = (LinearLayout) convertView.findViewById(R.id.ll_lastTime);
                holder.ll_startOpenNumber = (LinearLayout) convertView.findViewById(R.id.ll_startOpenNumber);

                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            NumberCurrentInfo mNumberInfo = numberSortList.get(position);// 获取当前对象

            // 开奖了，更改背景色提示
            if (mNumberInfo.isOpen()) {
                holder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.number_open_bg));
            } else {
                holder.ll_item.setBackgroundColor(Color.WHITE);
            }

            // 显示开奖描述
            if ("1".equals(mNumberInfo.getName())) {
                holder.tv_number_desc.setVisibility(View.GONE);
            } else {
                holder.tv_number_desc.setVisibility(View.VISIBLE);
            }

//            holder.tv_numbers_name.setText(AppConstants.numberNames[Integer.parseInt(mNumberInfo.getName()) - 1]);
            NumberDataUtils.setTextTitle(mContext, holder.tv_numbers_name, mNumberInfo.getName());// 设置彩票名称
            holder.tv_numbers_issue.setText(mContext.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue() + mContext.getResources().getString(R.string.number_code_qi));

            numbers.clear();// 清除上次数据
            zodiacs.clear();// 清除上次数据

            holder.ll_numbers_container.removeAllViews();// 清除所有子控件

            /** ----------------------------拆分开奖号码-------------------------- */
            NumberDataUtils.disposeSubNumbers(mNumberInfo, numbers, zodiacs);

            /** ----------------------------设置显示开奖号码内容------------------------- */
            NumberDataUtils.numberAddInfo(mContext, mNumberInfo, holder.ll_numbers_container, numbers, zodiacs, isHKOpenNumberStartNF, isQXCOpenNumberStartNF, isSSQOpenNumberStartNF, isQLCOpenNumberStartNF, isDLTOpenNumberStartNF, isNextNumber, 0, null);

            /** ----------------------------设置显示开奖描述信息-------------------------- */
            NumberDataUtils.numberAddDesc(mContext, holder.tv_number_desc, mNumberInfo.getName());

            /** ----------------------------显示隐藏倒时计Item------------------------- */
            isCountdown(holder, mNumberInfo);

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    private class ViewHolder {
        TextView tv_numbers_name;
        TextView tv_numbers_issue;
        TextView tv_numbers_time;
        TextView tv_number_desc;// 开奖描述信息
        LinearLayout ll_numbers_container;

        LinearLayout ll_item;// item容器
        LinearLayout ll_numbers_time;// 倒计时
        TextView tv_number_time_name;// 倒计时彩种name
        TextView tv_number_time_name2;// 倒计时彩种name2
        TextView tv_number_time_hh;// 倒计时——时
        TextView tv_number_time_mm;// 倒计时——分
        TextView tv_number_time_ss;// 倒计时——秒

        LinearLayout ll_lastTime;// 显示倒计时的容器
        LinearLayout ll_startOpenNumber;// 显示正在开奖中的容器
    }

    /**
     * 倒计时显示与隐藏
     *
     * @param holder
     * @param mNumberInfo
     */
    private void isCountdown(ViewHolder holder, NumberCurrentInfo mNumberInfo) {
        holder.tv_number_time_name.setText("");

        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
        } else {
            holder.tv_number_time_name2.setText("");
        }

        holder.tv_number_time_hh.setText("");
        holder.tv_number_time_ss.setText("");

        String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));// 根据日期获取星期
        String[] Dates = {null, null};
        if (mNumberInfo.getTime() != null) {
            Dates = mNumberInfo.getTime().split(" ");
        }

        // 显示隐藏倒计时控件
        if ("1".equals(mNumberInfo.getName())) {
            // 香港彩
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            } else {
                holder.tv_number_time_name2.setText(mContext.getResources().getString(R.string.number_hk_hint2));
            }
            // 显示倒计时和日期
            settingTime(holder, Dates, weekDate, HKnumberTime, R.string.number_hk_hint);

            if (isHKOpenNumberStartNF) {// 显示正在开奖中
                holder.ll_numbers_time.setVisibility(View.VISIBLE);
                holder.ll_lastTime.setVisibility(View.GONE);
                holder.ll_startOpenNumber.setVisibility(View.VISIBLE);

                if (!"88".equals(numbers.get(7))) { // 如果下一期还未开出来，则显示当前和下一期期号

                    // 显示下一期号和时间
                    holder.tv_numbers_issue.setText(mContext.getResources().getString(R.string.number_code_di) + mNumberInfo.getNextIssue()
                            + mContext.getResources().getString(R.string.number_code_qi));

                    String weekDate2 = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getNextTime()));
                    String[] Dates2 = {null, null};
                    if (mNumberInfo.getNextTime() != null) {
                        Dates2 = mNumberInfo.getNextTime().split(" ");
                    }

                    holder.tv_numbers_time.setText(DateUtil.convertDateToNation(Dates2[0]) + " " + weekDate2);
                } else {

                    // 显示当前期号和时间
                    holder.tv_numbers_issue.setText(mContext.getResources().getString(R.string.number_code_di) + mNumberInfo.getIssue()
                            + mContext.getResources().getString(R.string.number_code_qi));

                    String weekDate2 = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getTime()));
                    String[] Dates2 = {null, null};
                    if (mNumberInfo.getNextTime() != null) {
                        Dates2 = mNumberInfo.getNextTime().split(" ");
                    }

                    holder.tv_numbers_time.setText(DateUtil.convertDateToNation(Dates2[0]) + " " + weekDate2);
                }
            }

        } else if ("6".equals(mNumberInfo.getName())) {
            // 七星彩
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            } else {
                holder.tv_number_time_name2.setText(mContext.getResources().getString(R.string.number_qxc_hint2));
            }
            // 显示倒计时和日期
            settingTime(holder, Dates, weekDate, QXCnumberTime, R.string.number_qxc_hint);

            if (isQXCOpenNumberStartNF) {
                // 显示正在开奖中
                showTheLottery(holder, mNumberInfo);
            }
        } else if ("24".equals(mNumberInfo.getName())) {
            // 双色球
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            } else {
                holder.tv_number_time_name2.setText(mContext.getResources().getString(R.string.number_ssq_hint2));
            }
            // 显示倒计时和日期
            settingTime(holder, Dates, weekDate, SSQnumberTime, R.string.number_ssq_hint);

            if (isSSQOpenNumberStartNF) {
                // 显示正在开奖中
                showTheLottery(holder, mNumberInfo);
            }
        } else if ("28".equals(mNumberInfo.getName())) {
            // 七乐彩
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            } else {
                holder.tv_number_time_name2.setText(mContext.getResources().getString(R.string.number_qlc_hint2));
            }
            // 显示倒计时和日期
            settingTime(holder, Dates, weekDate, QLCnumberTime, R.string.number_qlc_hint);

            if (isQLCOpenNumberStartNF) {
                // 显示正在开奖中
                showTheLottery(holder, mNumberInfo);
            }
        } else if ("29".equals(mNumberInfo.getName())) {
            // 大乐透
            if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
            } else {
                holder.tv_number_time_name2.setText(mContext.getResources().getString(R.string.number_dlt_hint2));
            }
            // 显示倒计时和日期
            settingTime(holder, Dates, weekDate, DLTnumberTime, R.string.number_dlt_hint);

            if (isDLTOpenNumberStartNF) {
                // 显示正在开奖中
                showTheLottery(holder, mNumberInfo);
            }
        } else if ("25".equals(mNumberInfo.getName()) || "26".equals(mNumberInfo.getName())) {
            // 排3和排5显示日期
            holder.tv_numbers_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);// 设置日期
            holder.ll_numbers_time.setVisibility(View.GONE);
        } else {
            String mTime = Dates[1] == null ? "" : Dates[1].substring(0, Dates[1].lastIndexOf(":"));

            holder.tv_numbers_time.setText(mTime);// 设置日期
            holder.ll_numbers_time.setVisibility(View.GONE);
        }
    }

    /**
     * 设置倒计时和开奖日期
     *
     * @param holder        显示的控件
     * @param Dates         日期
     * @param weekDate      周期
     * @param CnumberTime   离开奖总时间
     * @param resourcesName 彩种名称
     */
    private void settingTime(ViewHolder holder, String[] Dates, String weekDate, long CnumberTime, int resourcesName) {

        holder.tv_numbers_time.setText(DateUtil.convertDateToNation(Dates[0]) + " " + weekDate);// 设置日期
        holder.tv_number_time_name.setText(mContext.getResources().getString(resourcesName));

        long mTime = (CnumberTime / 1000 / 60 / 60);// 获取总共小时数
        if (mTime >= 0 && mTime < 8) {

            long hh = CnumberTime / (3600 * 1000);// 获取相差小时
            long mm = (CnumberTime % (3600 * 1000)) / (60 * 1000);// 获取相差分
            long ss = ((CnumberTime % (3600 * 1000)) % (60 * 1000)) / 1000;// 获取相差秒

            holder.ll_numbers_time.setVisibility(View.VISIBLE);
            holder.ll_lastTime.setVisibility(View.VISIBLE);
            holder.ll_startOpenNumber.setVisibility(View.GONE);

            String h;
            if (hh < 10) {
                h = "0" + hh;
            } else {
                h = "" + hh;
            }
            holder.tv_number_time_hh.setText(h);

            String m;
            if (mm < 10) {
                m = "0" + mm;
            } else {
                m = "" + mm;
            }
            holder.tv_number_time_mm.setText(m);

            String s;
            if (ss < 10) {
                s = "0" + ss;
            } else {
                s = "" + ss;
            }
            holder.tv_number_time_ss.setText(s);
        } else {
            holder.ll_numbers_time.setVisibility(View.GONE);
        }
    }

    /**
     * 显示开奖中
     *
     * @param holder      显示控件
     * @param mNumberInfo 开奖对象
     */
    private void showTheLottery(ViewHolder holder, NumberCurrentInfo mNumberInfo) {
        holder.ll_numbers_time.setVisibility(View.VISIBLE);
        holder.ll_lastTime.setVisibility(View.GONE);
        holder.ll_startOpenNumber.setVisibility(View.VISIBLE);
        // 显示下一期号和时间
        holder.tv_numbers_issue.setText(mContext.getResources().getString(R.string.number_code_di) + mNumberInfo.getNextIssue()
                + mContext.getResources().getString(R.string.number_code_qi));
        String weekDate2 = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mNumberInfo.getNextTime()));
        String[] Dates2 = {null, null};
        if (mNumberInfo.getNextTime() != null) {
            Dates2 = mNumberInfo.getNextTime().split(" ");
        }
        holder.tv_numbers_time.setText(DateUtil.convertDateToNation(Dates2[0]) + " " + weekDate2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:// 点击返回菜单栏
                MobclickAgent.onEvent(mContext, "Lottery_List_Exit");
                finish();
                break;
            case R.id.public_btn_set:// 点击进入定制彩种
                MobclickAgent.onEvent(mContext, "Lottery_Customize");
                // 跳 转到定制彩种页面
                Intent intent = new Intent(mContext, NumberCustomizeActivity.class);
                intent.putExtra("DefSortList", (Serializable) sortDef1);
                startActivity(intent);

                break;

            case R.id.reLoading:// 重新加载数据
                MobclickAgent.onEvent(mContext, "Lottery_Refresh");
                numbersDataShow(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        allNumbers = null;
        isThreadRequestData2 = true;// 自动刷新状态,重新获取休眠时间
        numbersDataShow(1);// 刷新加载
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
