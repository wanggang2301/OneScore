package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.custom.CustomListAdapter;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomFristBean;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomListBean;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomSecondBean;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.LoadMoreRecyclerView;
import com.umeng.message.UmengRegistrar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yixq on 2016/12/5.
 * mail：yixq@13322.com
 * describe: 定制列表页
 */

public class CustomListActivity extends BaseActivity {

    private LinearLayoutManager mLinearLayoutManager;
    private List mFirstData;
    private CustomListAdapter mAdapter;
    private LoadMoreRecyclerView mCustomListRecycle;

    private CustomFocusClickListener mCustomFocusClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_activity);

        initView();
        initData();
    }
    // 定义关注监听
    public interface CustomFocusClickListener {
        void FocusOnClick(View view, String dataId , CustomFristBean firstData);
        void FocusOnClick(View view, String dataId , CustomSecondBean secondData);
    }
    private void initView(){
        TextView mCustomTitle = (TextView)findViewById(R.id.public_txt_title);
        mCustomTitle.setText("定制列表");

        TextView mCustomText = (TextView) findViewById(R.id.tv_right);
        mCustomText.setVisibility(View.VISIBLE);
        mCustomText.setText("完成");
        mCustomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendAllId();
                L.d("yxq123456","点击完成");
                Toast.makeText(mContext, "发送ID", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CustomListActivity.this, CustomActivity.class));
                finish();
            }
        });

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);


        mCustomListRecycle = (LoadMoreRecyclerView)findViewById(R.id.custom_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCustomListRecycle.setLayoutManager(mLinearLayoutManager);

    }

    private void initData(){

        String url = "http://192.168.10.242:8181/mlottery/core/basketballCommonMacth.findHotLeagueAndTeamConcern.do";

        Map<String , String> map = new HashMap();
        map.put("userId" , "hhly90522");
        map.put("deviceId" , "868048029263480");


        VolleyContentFast.requestJsonByGet(url, map ,new VolleyContentFast.ResponseSuccessListener<CustomListBean>() {
            @Override
            public void onResponse(CustomListBean json) {

                mFirstData = new ArrayList();

                mFirstData = json.getConcernLeagueAndTeam().getLeagueConcerns();

                fucusEvent();
                mAdapter = new CustomListAdapter(getApplicationContext() , mFirstData);
                mCustomListRecycle.setAdapter(mAdapter);

                mAdapter.setmFocus(mCustomFocusClickListener);

                setOnItemClick(mFirstData);

                addSecondTier();

////                {"1":[1,3,5,9],"2":[67]}"
//                List<CustomFristBean> allList = new ArrayList<CustomFristBean>();
//                allList = mFirstData;




//                L.d("yxq_1214" , mFirstData.size() + " type1 " + data.get(0).getFirstType());
//                L.d("yxq_1214" , mFirstData.size() + " type2 " + data.get(0).getTeamConcerns().get(0).getSecondType());

                L.d("yxq_1214" , mFirstData.size() + " mFirstData " + mFirstData.size());
//                L.d("yxq_1214" , mFirstData.size() + " data " + data.size());

                Toast.makeText(mContext, "请求成功", Toast.LENGTH_SHORT).show();
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
            }
        },CustomListBean.class);

//        fucusEvent();
//        mFirstData = new ArrayList();
////        addData();
//        mAdapter = new CustomListAdapter(getApplicationContext() , mFirstData);
//        mCustomListRecycle.setAdapter(mAdapter);
//
//        mAdapter.setmFocus(mCustomFocusClickListener);
//
//        setOnItemClick(mFirstData);
//
//        addSecondTier();
//        test();

    }

    private void sendAllId(){

        /***********************这里有Bug 啊~！！！！！！*************************/
        List<Map<String , String[]>> sendList = new ArrayList<>();
//        Map<String , String[]> sendMap = new HashMap<>();
        for (Object firstData : mFirstData) {
            if (firstData instanceof CustomFristBean) {
                CustomFristBean currData = (CustomFristBean)firstData;
                StringBuffer teamId = new StringBuffer();

                boolean isfouce = false;
                for (CustomSecondBean secondData : currData.getTeamConcerns()) {
                    if(secondData.isConcern()){
                        isfouce = true;
                        if ("".equals(teamId.toString())) {
                            teamId.append(secondData.getTeamId());
                        }else{
                            teamId.append("," + secondData.getTeamId());
                        }
                    }
                }
                if (isfouce) {
                    String[] arrTeamId = teamId.toString().split("[,]");

                    Map<String , String[]> sendMap = new HashMap<>();
                    sendMap.put(currData.getLeagueId() , arrTeamId);

                    sendList.add(sendMap);

                    L.d("yxq123456","teamId ==" + Arrays.toString(arrTeamId));
                }
            }
        }
        L.d("yxq123456","sendList.size ==" + sendList.size());

    }
    private void addSecondTier(){
        /**
         * 记录添加中间层（日期层）的位置
         */
        int addSecondIndex = 1;
        for (int i = 0; i < mFirstData.size(); i++) {
//        for (int i = 0; i < 1; i++) {
            if (mFirstData.get(i) instanceof CustomFristBean) {
                CustomFristBean firstData = (CustomFristBean) mFirstData.get(i);

                mAdapter.addAllChild(firstData.getTeamConcerns() , addSecondIndex);
                addSecondIndex += (firstData.getTeamConcerns().size() + 1);
            }
        }
    }

    private void setOnItemClick(final List<?> mData){
        mAdapter.setOnItemClickLitener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position , ImageView isOpen) {

                if (mData.get(position) instanceof CustomFristBean) { //判断点击的是不是 中间层 0
                    CustomFristBean parent = (CustomFristBean) mData.get(position);
                    if ((position + 1) == mData.size()) {//判断是否为最后一个元素

                        if (isOpen != null) {
                            isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                        }

                        mAdapter.addAllChild(parent.getTeamConcerns(), position + 1);
                    } else {
                        if (mData.get(position + 1) instanceof CustomFristBean) {//为折叠状态需要添加儿子
                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_2);
                            }
                            mAdapter.addAllChild(parent.getTeamConcerns(), position + 1);
                        } else if (mData.get(position + 1) instanceof CustomSecondBean) {//为展开状态需要删除儿子
                            if (isOpen != null) {
                                isOpen.setBackgroundResource(R.mipmap.iconfont_xiala_1);
                            }
                            mAdapter.deleteAllChild(position + 1, parent.getTeamConcerns().size());
                        }
                    }
                }else{// 否则为最内层（赛事层）比赛的点击事件这里写
                    // TODO***************************************************
                }
            }
        });
    }

    public void addAll (List<CustomFristBean> alldata){
        for (CustomFristBean data : alldata) {
            mFirstData.add(data);
        }
    }


    public void fucusEvent(){
//        mCustomFocusClickListener = new CustomFocusClickListener() {
//            @Override
//            public void FocusOnClick(View view, String data , boolean tag) {
//
//                boolean isFucus = (boolean)view.getTag();
//
//                if (!isFucus) {// 未选中 --> 选中
//                    addId(data);
//                    view.setTag(false);
//                    tag = false;
//                }else{ //选中 --> 未选中
//                    deletaId(data);
//                    view.setTag(true);
//                    tag = true;
//                }
//                mAdapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(), "yxq== " + data, Toast.LENGTH_SHORT).show();
//            }
//        };
        mCustomFocusClickListener = new CustomFocusClickListener() {
            @Override
            public void FocusOnClick(View view, String dataId, CustomFristBean firstData) {
                boolean isFucus = (boolean)view.getTag();

                if (!isFucus) {// 未选中 --> 选中
                    addId(dataId);
                    view.setTag(false);
                    firstData.setConcern(false);
                }else{ //选中 --> 未选中
                    deletaId(dataId);
                    view.setTag(true);
                    firstData.setConcern(true);
                }
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "yxq== " + dataId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void FocusOnClick(View view, String dataId, CustomSecondBean secondData) {
                boolean isFucus = (boolean)view.getTag();

                if (!isFucus) {// 未选中 --> 选中
                    addId(dataId);
                    view.setTag(false);
                    secondData.setConcern(false);
                }else{ //选中 --> 未选中
                    deletaId(dataId);
                    view.setTag(true);
                    secondData.setConcern(true);
                }
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "yxq== " + dataId, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void addId(String thirdid){
        String fucusId = PreferenceUtil.getString("custom_focus_ids" , "");

        if (fucusId.equals("")) {

            PreferenceUtil.commitString("custom_focus_ids" , thirdid);
        }else{

            PreferenceUtil.commitString("custom_focus_ids" , fucusId + "," + thirdid);
        }
    }

    private void deletaId(String thirdid){
        String fucusId = PreferenceUtil.getString("custom_focus_ids" , "");

        String[] arrId = fucusId.split("[,]");

        StringBuffer mSbuf = new StringBuffer();
        for (String id : arrId) {
            if (!id.equals(thirdid)) {
                if ("".equals(mSbuf.toString())) {
                    mSbuf.append(id);
                }else{
                    mSbuf.append("," + id);
                }
            }
        }
        PreferenceUtil.commitString("custom_focus_ids" , mSbuf.toString());
    }
}
