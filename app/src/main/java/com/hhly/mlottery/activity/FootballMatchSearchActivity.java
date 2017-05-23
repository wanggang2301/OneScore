package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.FootballMatchSearchAdapter;
import com.hhly.mlottery.bean.BallMatchItemsBean;
import com.hhly.mlottery.bean.FootballSearchBean;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.FootballMatchSearchService;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yuely198 on 2017/3/23.
 * 足球比分列表搜索
 */

public class FootballMatchSearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String LEAGUEID = "league";
    private FootballMatchSearchAdapter basketballInforSerachAdapter;
    private EditText et_keyword;
    private RecyclerView mTv_result;
    private TextView mNo_serach_tv;
    private static final String SEARCHKEYWORD = "searchKeyword";
    private ImageView mSearch_iv_delete;

    private FocusMatchClickListener mFocusClickListener;// 关注点击事件

    public static final int REQUEST_DETAIL_CODE = 0x12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_match_layout);
        initView();
        //  http://m.13322.com/mlottery/core/footballEInfo.vagueSearch.do?lang=zh&teamName=a&timeZone=8

        RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(BaseURLs.NEW_URL_API_HOST)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(new Gson()))
                .build();

        final FootballMatchSearchService service = retrofit.create(FootballMatchSearchService.class);
        //保存数据源
        RxTextView.textChanges(et_keyword)
                // 上面的对 tv_result 的操作需要在主线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .debounce(600, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        // 清空搜索出来的结构
                        //tv_result.setText("");
                        //当 EditText 中文字大于0的时候
                        if (charSequence.length() > 0) {
                            //有数据显示删除键
                            mSearch_iv_delete.setVisibility(View.VISIBLE);
                            mNo_serach_tv.setVisibility(View.VISIBLE);
                            mNo_serach_tv.setText(R.string.find_search);
                            return true;
                        } else {
                            mNo_serach_tv.setVisibility(View.GONE);
                            if (basketballInforSerachAdapter != null) {
                                //无搜索  隐藏删除键
                                mSearch_iv_delete.setVisibility(View.GONE);
                                basketballInforSerachAdapter.clearData();
                            }
                            return false;
                        }
//                        return charSequence.length() > 0;
                    }
                })
                .map(new Func1<CharSequence, CharSequence>() {
                    @Override
                    public CharSequence call(CharSequence charSequence) {
                        Observable<FootballSearchBean> observable =
                                service.searchProdcut(VolleyContentFast.returenLanguage(), charSequence.toString());
                        observable.subscribeOn(Schedulers.io())
                                //将 data 转换成 ArrayList<ArrayList<String>>
                                .map(new Func1<FootballSearchBean, List<BallMatchItemsBean>>() {
                                    @Override
                                    public List<BallMatchItemsBean> call(FootballSearchBean data) {

                                        return data.getBallMatchItems();
                                    }
                                })

                                .filter(new Func1<List<BallMatchItemsBean>, Boolean>() {
                                    @Override
                                    public Boolean call(List<BallMatchItemsBean> resultListBeen) {

                                        return resultListBeen.size() >= 0;
                                    }

                                })

                                .observeOn(AndroidSchedulers.mainThread())

                                .subscribe(new Observer<List<BallMatchItemsBean>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mNo_serach_tv.setText(R.string.network_suck);
                                    }

                                    @Override
                                    public void onNext(List<BallMatchItemsBean> leagueBeen) {
                                        //设置适配数据
                                        if (leagueBeen != null) {
                                            if (leagueBeen.isEmpty()) {
                                                //搜索详情
                                                //UiUtils.toast(MyApp.getInstance(), "找不到相关信息!");
                                                mNo_serach_tv.setText(R.string.not_find_search);
                                                mTv_result.setVisibility(View.GONE);
                                                mNo_serach_tv.setVisibility(View.VISIBLE);

                                            } else {
                                                mTv_result.setVisibility(View.VISIBLE);
                                                mNo_serach_tv.setVisibility(View.GONE);
                                                showpop(leagueBeen, et_keyword.getText().toString());
                                            }
                                            // showpop(resultListBeen);
                                        }

                                    }
                                });
                        return charSequence;
                    }
                })
                .subscribe();

    }

    private void initView() {
        //搜索框
        et_keyword = (EditText) findViewById(R.id.et_keyword);
        et_keyword.setHint(R.string.please_hint_team);
        //数据返回显示
        mTv_result = (RecyclerView) findViewById(R.id.tv_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mTv_result.setLayoutManager(layoutManager);
        //无数据显示
        mNo_serach_tv = (TextView) findViewById(R.id.no_serach_tv);
        //删除键
        mSearch_iv_delete = (ImageView) findViewById(R.id.search_iv_delete);
        mSearch_iv_delete.setOnClickListener(this);

        //返回
        findViewById(R.id.search_btn_back).setOnClickListener(this);


        mFocusClickListener = new FocusMatchClickListener() {// 关注按钮事件
            @Override
            public void onClick(View view, String third) {


                boolean isCheck = (Boolean) view.getTag();// 检查之前是否被选中
                if (!isCheck) {// 插入数据
                    FocusUtils.addFocusId(third);
                    ((ImageView) view).setImageResource(R.mipmap.football_focus);
                    view.setTag(true);
                } else {// 删除
                    FocusUtils.deleteFocusId(third);
                    ((ImageView) view).setImageResource(R.mipmap.football_nomal);
                    view.setTag(false);
                }
            }
        };
    }


    private void initEvent(final List<BallMatchItemsBean> resultListBeen) {

    }

    /* * 数据处理 */

    private void showpop(final List<BallMatchItemsBean> resultListBeen, String et_keyword) {
        basketballInforSerachAdapter = new FootballMatchSearchAdapter(getApplicationContext(), resultListBeen, "http://pic.13322.com/icons/teams/100/", ".png");
        basketballInforSerachAdapter.setmFocusMatchClickListener(mFocusClickListener);
        mTv_result.setAdapter(basketballInforSerachAdapter);
        basketballInforSerachAdapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

               if (HandMatchId.handId(mContext, data)) {


                   String thirdId = data;
                   Intent intent = new Intent(mContext, FootballMatchDetailActivity.class);
                   intent.putExtra("thirdId", thirdId);
                   intent.putExtra("currentFragmentId", 1);
                   startActivity(intent);
                   //startActivityForResult(intent, REQUEST_DETAIL_CODE);
               }
            }
        });
        basketballInforSerachAdapter.notifyDataSetChanged();
       // resultListBeen.clear();
        //点击监听
        initEvent(resultListBeen);
    }


    /* * 点击事件  * */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_btn_back:
                finish();
                break;


            case R.id.search_iv_delete:

                et_keyword.setText("");

                break;
            default:
                break;


        }
    }

}
