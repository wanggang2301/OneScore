package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.custom.CustomMyDataAdapter;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineDataBean;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineFirstDataBean;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineScondDataBean;
import com.hhly.mlottery.bean.custombean.CustomMineBean.CustomMineThirdDataBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yixq on 2016/12/5.
 * mail：yixq@13322.com
 * describe: 我的定制页
 */

public class CustomActivity extends BaseWebSocketActivity {


    private LoadMoreRecyclerView mCustomRecycle;
    private LinearLayoutManager mLinearLayoutManager;
    private CustomMyDataAdapter mAdapter;
    private List mFirstData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 推送  必须定义在 super.onCreate前面，否则 订阅不成功
         */
//        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.snooker");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_activity);

        initView();
        initData();
    }

    private void initView(){

        TextView mCustomTitle = (TextView)findViewById(R.id.public_txt_title);
        mCustomTitle.setText("我的定制");

        TextView mCustomText = (TextView) findViewById(R.id.tv_right);
        mCustomText.setVisibility(View.VISIBLE);
        mCustomText.setText("编辑");
        mCustomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomActivity.this, CustomListActivity.class));
//                finish();
            }
        });

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);


        mCustomRecycle = (LoadMoreRecyclerView)findViewById(R.id.custom_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCustomRecycle.setLayoutManager(mLinearLayoutManager);
    }

    private void initData(){

        String url = "http://192.168.10.242:8181/mlottery/core/basketballCommonMacth.findBasketballMyConcernMatch.do" ;
//                "lang=zh&userId=13714102745&deviceId=21126FC4-DAF0-40DC-AF5C-1AD33EFB5F67";

        Map<String , String> map = new HashMap<>();
        map.put("userId" , "13714102745");
        map.put("deviceId" , "21126FC4-DAF0-40DC-AF5C-1AD33EFB5F67");

        VolleyContentFast.requestJsonByGet(url, map ,new VolleyContentFast.ResponseSuccessListener<CustomMineDataBean>() {
            @Override
            public void onResponse(CustomMineDataBean jsonData) {

                mFirstData = new ArrayList<>();
                mFirstData = jsonData.getData().getLeagueItem();

                mAdapter = new CustomMyDataAdapter(getApplicationContext() , mFirstData);
                setOnItemClick(mFirstData);
                mCustomRecycle.setAdapter(mAdapter);


                /**必须先添加中间层，再添加最内层 顺序不可逆*/
                addSecondTier();
                addThirdTier();
                /**必须先添加中间层，再添加最内层 顺序不可逆*/


            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        },CustomMineDataBean.class);


//        mFirstData = new ArrayList<>();
//        mAdapter = new CustomMyDataAdapter(getApplicationContext() , mFirstData);
//        setOnItemClick(mFirstData);
//        addData();
//        mCustomRecycle.setAdapter(mAdapter);
//
//
//        /**必须先添加中间层，再添加最内层 顺序不可逆*/
//        addSecondTier();
//        addThirdTier(mFirstData);
//        /**必须先添加中间层，再添加最内层 顺序不可逆*/
    }

    /**
     * 添加中间层数据（日期层）
     */
    private void addSecondTier(){
        /**
         * 记录添加中间层（日期层）的位置
         */
        int addSecondIndex = 1;
        for (int i = 0; i < mFirstData.size(); i++) {
            if (mFirstData.get(i) instanceof CustomMineFirstDataBean) {
                CustomMineFirstDataBean firstData = (CustomMineFirstDataBean) mFirstData.get(i);
                mAdapter.addAllChild(firstData.getMatchData() , addSecondIndex);
                addSecondIndex += (firstData.getMatchData().size() + 1);
            }
        }
    }

    /**
     * 添加最内层数据（比赛层）
     */
    private void addThirdTier(){
        /**
         * 添加最内层的位置
         */
        int addThirdIndex = 1;
        /**
         * 遍历时是否是最外层，是最外层时设为 1 ，添加子项时位置往后一位
         */
        int isGround = 0;

        for (int i = 0; i < mFirstData.size(); i++) {

            if (mFirstData.get(i) instanceof CustomMineScondDataBean) {

                CustomMineScondDataBean secondData = (CustomMineScondDataBean)mFirstData.get(i);

                mAdapter.addAllChild(secondData.getMatchItems() , (addThirdIndex + isGround));

                addThirdIndex += (secondData.getMatchItems().size()+1 + isGround);
                L.d("yxq22222", " x == " + addThirdIndex + " y == " + isGround + " size == " + secondData.getMatchItems().size());

                isGround=0;
            }
            if (mFirstData.get(i) instanceof CustomMineFirstDataBean) {
//                isGround = 1;
                isGround += 1; /** += 防止出现中间有某联赛下有无数据情况 添加数据时位置不对情况 */
            }
        }
    }


    private void setOnItemClick(final List<?> mData){

        mAdapter.setOnItemClickLitener(new CustomMyDataAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position , ImageView isOpen) {

                if (mData.get(position) instanceof CustomMineFirstDataBean) {//判断点击的是不是最外层 0

                    CustomMineFirstDataBean firstData = (CustomMineFirstDataBean) mData.get(position);
                    if (mData.size() == (position + 1)) {
                        //如果是最后一项

                        if (isOpen != null) {
                            isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                        }

                        mAdapter.addAllChild(firstData.getMatchData() , position + 1);
                    }else{
                        if (mData.get(position + 1) instanceof CustomMineFirstDataBean) {//折叠状态

                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                            }

                            mAdapter.addAllChild(firstData.getMatchData() , position + 1);
                        }else{
                            /**
                             * 记录子层数据（赛事层），若日期层打开状态需要连比赛层一并删除
                             */
                            int thirdDataSize =  0;
//                            L.d("yxq==========>>> " , "外层 size " + a);
                            for (CustomMineScondDataBean data : firstData.getMatchData()) {
                                if (data.isUnfold()) {
                                    thirdDataSize += data.getMatchItems().size();
                                    L.d("yxq==========>>> " , "a+size " + data.getMatchItems().size());
                                    L.d("yxq==========>>> " , "a+过程 " + thirdDataSize);

                                    data.setUnfold(false);// 遍历过后状态改为 false （折叠状态） ps:防止二次点击状态冲突
                                }
                            }
                            L.d("yxq==========>>> " , "删除 " + (thirdDataSize+firstData.getMatchData().size()));
                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_1);
                            }

                            mAdapter.deleteAllChild(position + 1,thirdDataSize + firstData.getMatchData().size());//删除 二级item 和 已展开的三级item 条数
                        }
                    }

                }else if (mData.get(position) instanceof CustomMineScondDataBean) { //判断点击的是不是 中间层 0
                    CustomMineScondDataBean parent = (CustomMineScondDataBean) mData.get(position);
                    if ((position + 1) == mData.size()) {//判断是否为最后一个元素
                        parent.setUnfold(true);

                        if (isOpen != null) {
                            isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                        }

                        mAdapter.addAllChild(parent.getMatchItems(), position + 1);
                    } else {
                        if (mData.get(position + 1) instanceof CustomMineFirstDataBean || mData.get(position + 1) instanceof CustomMineScondDataBean) {//为折叠状态需要添加儿子
                            parent.setUnfold(true);

                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                            }

                            mAdapter.addAllChild(parent.getMatchItems(), position + 1);
                        } else if (mData.get(position + 1) instanceof CustomMineThirdDataBean) {//为展开状态需要删除儿子
                            parent.setUnfold(false);

                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_1);
                            }

                            mAdapter.deleteAllChild(position + 1, parent.getMatchItems().size());
                        }
                    }
                }else{// 否则为最内层（赛事层）比赛的点击事件这里写
                    // TODO***************************************************
                }
            }
        });
    }

    /*****************************************以下推送方法**********************************************/

    @Override
    protected void onTextResult(String text) {

    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {

    }
    /*****************************************以上推送方法**********************************************/
}
