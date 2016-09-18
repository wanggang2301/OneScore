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
import com.hhly.mlottery.adapter.BasketballInforSerachAdapter;
import com.hhly.mlottery.bean.BasketSerach;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:  模糊搜索
 * @data: 2016/8/9 18:15
 */

public class BasketballInformationSerachActivity extends BaseActivity implements  View.OnClickListener {

    private static final String LEAGUEID = "league";
    private BasketballInforSerachAdapter basketballInforSerachAdapter;
    private EditText et_keyword;
    private ListView mTv_result;
    private TextView mNo_serach_tv;
    private static final String SEARCHKEYWORD= "searchKeyword";
    private ImageView mSearch_iv_delete;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serach_layout);
        initView();
        //ButterKnife.bind(this);
        //BaseURLs.NEW_URL_API_HOST

        RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(BaseURLs.NEW_URL_API_HOST)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(new Gson()))
                .build();

        final SearchService service = retrofit.create(SearchService.class);
        //保存数据源
        RxTextView.textChanges(et_keyword)
                // 上面的对 tv_result 的操作需要在主线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .debounce(600, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override public Boolean call(CharSequence charSequence) {
                        // 清空搜索出来的结构
                        //tv_result.setText("");
                        //当 EditText 中文字大于0的时候
                        if(charSequence.length() > 0){
                            //有数据显示删除键
                            mSearch_iv_delete.setVisibility(View.VISIBLE);
                            mNo_serach_tv.setVisibility(View.VISIBLE);
                            mNo_serach_tv.setText(R.string.find_search);
                            return true;
                        }else{
                            if (basketballInforSerachAdapter!=null){
                                //无搜索  隐藏删除键
                                mSearch_iv_delete.setVisibility(View.GONE);
                                basketballInforSerachAdapter.clearData();
                            }

                            return false;
                        }
//                        return charSequence.length() > 0;
                    }
                })
                .switchMap(new Func1<CharSequence, rx.Observable<BasketSerach>>() {
                    @Override public rx.Observable<BasketSerach> call(CharSequence charSequence) {
                        // 搜索
                        return service.searchProdcut(VolleyContentFast.returenLanguage(), charSequence.toString());
                    }
                })
                // .retryWhen(new RetryWithConnectivityIncremental(BasketballInformationSerachActivity.this, 5, 15, TimeUnit.MILLISECONDS))
                // 网络操作在io线程
                .subscribeOn(Schedulers.io())
                //将 data 转换成 ArrayList<ArrayList<String>>
                .map(new Func1<BasketSerach, List<LeagueBean>>() {
                    @Override public List<LeagueBean> call(BasketSerach data) {

                        return data.resultList;
                    }
                })

                .filter(new Func1<List<LeagueBean>, Boolean>() {
                    @Override
                    public Boolean call(List<LeagueBean> resultListBeen) {

                        return  resultListBeen.size() >= 0;
                    }

                })

                // 发生错误后不要调用 onError，而是转到 onErrorResumeNext
   /*             .onErrorResumeNext((Observable<? extends List<BasketSerach.ResultListBean>>) new Func1<Throwable, Observable<String>>() {
            @Override public Observable<String> call(Throwable throwable) {
                return Observable.just("error result");
            }
        })*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LeagueBean>>() {
                    @Override
                    public void call(List<LeagueBean> resultListBeen) {
                        //设置适配数据
                        if (resultListBeen != null) {
                            if (resultListBeen.isEmpty()) {
                                //搜索详情
                                //UiUtils.toast(MyApp.getInstance(), "找不到相关信息!");
                                mNo_serach_tv.setText(R.string.not_find_search);
                                mTv_result.setVisibility(View.GONE);
                                mNo_serach_tv.setVisibility(View.VISIBLE);

                            } else {
                                mTv_result.setVisibility(View.VISIBLE);
                                mNo_serach_tv.setVisibility(View.GONE);
                                showpop(resultListBeen, et_keyword.getText().toString());
                            }
                            // showpop(resultListBeen);
                        }
                    }
                });
    }

    private void initView() {
        //搜索框
        et_keyword = (EditText) findViewById(R.id.et_keyword);
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


    private void initEvent(final List<LeagueBean> resultListBeen) {

        mTv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( null!=resultListBeen.get(position).getLeagueId()&&!resultListBeen.get(position).getLeagueId().isEmpty()) {
                    Intent intent = new Intent(BasketballInformationSerachActivity.this, BasketballDatabaseDetailsActivity.class);
                    intent.putExtra(LEAGUEID, resultListBeen.get(position));//传递联赛ID
                    System.out.println("resulistBeen====================="+resultListBeen.get(position).toString());
                    startActivity(intent);
                }
            }
        });
    }

    /* * 数据处理 */

    private void showpop(final List<LeagueBean> resultListBeen, String et_keyword) {
        basketballInforSerachAdapter=new BasketballInforSerachAdapter(getApplicationContext(),resultListBeen,et_keyword);
        mTv_result.setAdapter(basketballInforSerachAdapter);
        basketballInforSerachAdapter.notifyDataSetChanged();
        //点击监听
        initEvent(resultListBeen);
    }


    /* * 点击事件  * */

    @Override
    public void onClick(View v) {
        switch (v.getId()){

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

    interface SearchService {
        //@GET("/sug") Observable<Data> searchProdcut(@Query("code") String code, @Query("q") String keyword);
        @GET(BaseURLs.FUZZYSEARCH) rx.Observable<BasketSerach> searchProdcut(@Query("lang") String code, @Query(SEARCHKEYWORD) String keyword);
    }


}
