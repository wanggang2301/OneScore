package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.BasketballInforSerachAdapter;
import com.hhly.mlottery.bean.BasketSerach;
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

    private static final String LEAGUEID = "leagueId";
    private BasketballInforSerachAdapter basketballInforSerachAdapter;
    private EditText et_keyword;
    private ListView mTv_result;
    private TextView mNo_serach_tv;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serach_layout);
        initView();
        //ButterKnife.bind(this);


        RestAdapter retrofit = new RestAdapter.Builder().setEndpoint("http://192.168.31.43:8888")
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
                        return charSequence.length() > 0;
                    }
                })
                .switchMap(new Func1<CharSequence, rx.Observable<BasketSerach>>() {
                    @Override public rx.Observable<BasketSerach> call(CharSequence charSequence) {
                        // 搜索
                        return service.searchProdcut("zh", charSequence.toString());
                    }
                })
//                .retryWhen(new RetryWithConnectivityIncremental(MainActivity.this, 5, 15, TimeUnit.MILLISECONDS))
                // 网络操作在io线程
                .subscribeOn(Schedulers.io())
                //将 data 转换成 ArrayList<ArrayList<String>>
                .map(new Func1<BasketSerach, List<BasketSerach.ResultListBean>>() {
                    @Override public List<BasketSerach.ResultListBean> call(BasketSerach data) {
                        return data.resultList;
                    }
                })

                .filter(new Func1<List<BasketSerach.ResultListBean>, Boolean>() {
                    @Override
                    public Boolean call(List<BasketSerach.ResultListBean> resultListBeen) {

                        return  resultListBeen.size() >= 0;
                    }

                })

                // 发生错误后不要调用 onError，而是转到 onErrorResumeNext
           /*     .onErrorResumeNext((Func1<Throwable, ? extends Observable<? extends List<BasketSerach.ResultListBean>>>) new Func1<Throwable, Observable<? extends String>>() {
                    @Override public Observable<? extends String> call(Throwable throwable) {
                        return Observable.just("error result");
                    }
                })*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BasketSerach.ResultListBean>>() {
                    @Override
                    public void call(List<BasketSerach.ResultListBean> resultListBeen) {
                        //设置适配数据

                        if(resultListBeen.isEmpty()){
                            //搜索详情
                            //UiUtils.toast(MyApp.getInstance(), "找不到相关信息!");
                            mTv_result.setVisibility(View.GONE);
                            mNo_serach_tv.setVisibility(View.VISIBLE);

                         }else{
                            mTv_result.setVisibility(View.VISIBLE);
                            mNo_serach_tv.setVisibility(View.GONE);
                            showpop(resultListBeen,et_keyword.getText().toString());
                        }
                       // showpop(resultListBeen);
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
        //返回
        findViewById(R.id.search_btn_back).setOnClickListener(this);
    }


    private void initEvent(final List<BasketSerach.ResultListBean> resultListBeen) {

        mTv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(BasketballInformationSerachActivity.this, BasketballDatabaseDetailsActivity.class);
                intent.putExtra(LEAGUEID, resultListBeen.get(position).leagueId);//传递联赛ID
                startActivity(intent);
            }
        });
    }

    /* * 数据处理 */

    private void showpop(final List<BasketSerach.ResultListBean> resultListBeen, String et_keyword) {
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

            default:
                break;



        }
    }

    interface SearchService {
        //@GET("/sug") Observable<Data> searchProdcut(@Query("code") String code, @Query("q") String keyword);
        @GET("/mlottery/core/basketballData.fuzzySearch.do") rx.Observable<BasketSerach> searchProdcut(@Query("lang") String code, @Query("searchKeyword") String keyword);
    }


}
