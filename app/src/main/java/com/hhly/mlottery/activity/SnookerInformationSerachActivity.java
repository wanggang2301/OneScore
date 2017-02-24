package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.SnookerInforSerachAdapter;
import com.hhly.mlottery.bean.basket.infomation.SnookerPlayerBean;
import com.hhly.mlottery.callback.SnookerSearchService;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
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

public class SnookerInformationSerachActivity extends BaseActivity implements View.OnClickListener {


    /**
     * @author wangg
     * @desc 斯洛克排名搜索
     * @date 2017/02/22
     */

    private static final String LEAGUEID = "league";
    private SnookerInforSerachAdapter snookerInforSerachAdapter;
    private EditText et_keyword;
    private ListView mTv_result;
    private TextView mNo_serach_tv;
    private static final String SEARCHKEYWORD = "searchKeyword";
    private ImageView mSearch_iv_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serach_layout);
        initView();


        RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(BaseURLs.NEW_URL_API_HOST)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(new Gson()))
                .build();

        final SnookerSearchService service = retrofit.create(SnookerSearchService.class);

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
                            if (snookerInforSerachAdapter != null) {
                                //无搜索  隐藏删除键
                                mSearch_iv_delete.setVisibility(View.GONE);
                                snookerInforSerachAdapter.clearData();
                            }
                            return false;
                        }
                    }
                })
                .map(new Func1<CharSequence, CharSequence>() {
                    @Override
                    public CharSequence call(CharSequence charSequence) {
                        Observable<SnookerPlayerBean> observable = service.searchSnookerPlayer(VolleyContentFast.returenLanguage(), charSequence.toString());
                        observable.subscribeOn(Schedulers.io())
                                //将 data 转换成 ArrayList<ArrayList<String>>
                                .map(new Func1<SnookerPlayerBean, List<SnookerPlayerBean.WorldRankingListBean>>() {
                                    @Override
                                    public List<SnookerPlayerBean.WorldRankingListBean> call(SnookerPlayerBean data) {

                                        L.d("wang_sn", data.getResult() + "");

                                        return data.getWorldRankingList();
                                    }
                                })

                                .filter(new Func1<List<SnookerPlayerBean.WorldRankingListBean>, Boolean>() {
                                    @Override
                                    public Boolean call(List<SnookerPlayerBean.WorldRankingListBean> worldRankingListBeen) {

                                        return worldRankingListBeen.size() >= 0;
                                    }

                                })

                                .observeOn(AndroidSchedulers.mainThread())

                                .subscribe(new Observer<List<SnookerPlayerBean.WorldRankingListBean>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mNo_serach_tv.setText(R.string.network_suck);
                                    }

                                    @Override
                                    public void onNext(List<SnookerPlayerBean.WorldRankingListBean> worldRankingListBeen) {
                                        //设置适配数据
                                        if (worldRankingListBeen != null) {
                                            if (worldRankingListBeen.isEmpty()) {
                                                //搜索详情
                                                //UiUtils.toast(MyApp.getInstance(), "找不到相关信息!");
                                                mNo_serach_tv.setText(R.string.not_find_search);
                                                mTv_result.setVisibility(View.GONE);
                                                mNo_serach_tv.setVisibility(View.VISIBLE);

                                            } else {
                                                mTv_result.setVisibility(View.VISIBLE);
                                                mNo_serach_tv.setVisibility(View.GONE);
                                                showpop(worldRankingListBeen, et_keyword.getText().toString());
                                            }
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
        et_keyword.setHint(getApplicationContext().getResources().getString(R.string.sn_player_search));
        //数据返回显示
        mTv_result = (ListView) findViewById(R.id.tv_result);
        //无数据显示
        mNo_serach_tv = (TextView) findViewById(R.id.no_serach_tv);
        //删除键
        mSearch_iv_delete = (ImageView) findViewById(R.id.search_iv_delete);
        mSearch_iv_delete.setOnClickListener(this);

        //返回
        findViewById(R.id.search_btn_back).setOnClickListener(this);
    }


    private void initEvent(final List<SnookerPlayerBean.WorldRankingListBean> worldRankingListBeen) {

        mTv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != worldRankingListBeen.get(position).getPlayerId() && !worldRankingListBeen.get(position).getPlayerId().isEmpty()) {
                    Intent intent = new Intent(SnookerInformationSerachActivity.this, SnookerPlayerInfoActivity.class);
                    intent.putExtra("playerId", worldRankingListBeen.get(position).getPlayerId());//传递球员ID
                    L.d("resulistBeen=====================" + worldRankingListBeen.get(position).toString());
                    startActivity(intent);
                }
            }
        });
    }

    /* * 数据处理 */

    private void showpop(final List<SnookerPlayerBean.WorldRankingListBean> worldRankingListBeen, String et_keyword) {
        snookerInforSerachAdapter = new SnookerInforSerachAdapter(getApplicationContext(), worldRankingListBeen, et_keyword);
        mTv_result.setAdapter(snookerInforSerachAdapter);
        snookerInforSerachAdapter.notifyDataSetChanged();
        //点击监听
        initEvent(worldRankingListBeen);
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
