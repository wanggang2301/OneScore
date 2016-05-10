package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiDetailsActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.oddfragment.CPIOddsFragment;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.widget.CpiListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tjl on 2016年4月14日 12:27:10
 * 新版指数（亚盘，大小，欧赔）CPIRecyclerViewAdapter
 */
public class CPIRecyclerViewAdapter extends RecyclerView.Adapter<CPIRecyclerViewAdapter.CPIViewHolder>{

    private List<NewOddsInfo.AllInfoBean> mAllInfoBean;
    private List<NewOddsInfo.AllInfoBean.ComListBean> mComListBean;
    private Context context;
    //cardview里面listview的适配器
    private CardViewListAdapter cardViewListAdapter;
    private NewOddsInfo.AllInfoBean.MatchInfoBean mMatchInfoBean;
    //标记是哪个类型“亚盘，大小，欧赔
    private String stType;
    public CPIRecyclerViewAdapter(List<NewOddsInfo.AllInfoBean> mAllInfoBean, Context context,String stType) {
        this.mAllInfoBean = mAllInfoBean;
        this.context=context;
        this.stType=stType;
    }


    //自定义ViewHolder类
    static class CPIViewHolder extends RecyclerView.ViewHolder{

        CardView cpi_item_cardview;//cardview
        CpiListView item_cpi_odds_listview;//cardview里面list
        TextView cpi_item_leagueName_txt;//联赛名称
        TextView cpi_item_time_txt;//比赛时间
        TextView cpi_scoreAndName_txt;//比分

        TextView cpi_item_home_txt;//主队
        TextView cpi_item_odds_txt;//盘口
        TextView cpi_item_guest_txt;//客队

        public CPIViewHolder(final View itemView) {
            super(itemView);
            cpi_item_cardview= (CardView) itemView.findViewById(R.id.cpi_item_cardview);
            item_cpi_odds_listview= (CpiListView) itemView.findViewById(R.id.item_cpi_odds_list);
            cpi_item_leagueName_txt= (TextView) itemView.findViewById(R.id.cpi_item_leagueName_txt);
            cpi_item_time_txt= (TextView) itemView.findViewById(R.id.cpi_item_time_txt);
            cpi_scoreAndName_txt= (TextView) itemView.findViewById(R.id.cpi_scoreAndName_txt);

            cpi_item_home_txt= (TextView) itemView.findViewById(R.id.cpi_item_home_txt);
            cpi_item_odds_txt= (TextView) itemView.findViewById(R.id.cpi_item_odds_txt);
            cpi_item_guest_txt= (TextView) itemView.findViewById(R.id.cpi_item_guest_txt);

        }


    }
    @Override
    public CPIViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_cpi_odds,viewGroup,false);
        CPIViewHolder nvh=new CPIViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(CPIViewHolder cpiViewHolder, final int position) {
        if("plate".equals(stType)){
            cpiViewHolder.cpi_item_home_txt.setText(R.string.odd_home_txt);
            cpiViewHolder.cpi_item_odds_txt.setText(R.string.odd_dish_txt);
            cpiViewHolder.cpi_item_guest_txt.setText( R.string.odd_guest_txt);
        }else if("big".equals(stType)){
            cpiViewHolder.cpi_item_home_txt.setText( R.string.odd_home_big_txt);
            cpiViewHolder.cpi_item_odds_txt.setText( R.string.odd_dish_txt);
            cpiViewHolder.cpi_item_guest_txt.setText( R.string.odd_guest_big_txt);
        }else if("op".equals(stType)){
            cpiViewHolder.cpi_item_home_txt.setText( R.string.odd_home_op_txt);
            cpiViewHolder.cpi_item_odds_txt.setText( R.string.odd_dish_op_txt);
            cpiViewHolder.cpi_item_guest_txt.setText( R.string.odd_guest_op_txt);
        }
        mMatchInfoBean = mAllInfoBean.get(position).getMatchInfo();

        //获得联赛名称
        cpiViewHolder.cpi_item_leagueName_txt.setText(mAllInfoBean.get(position).getLeagueName());
        //比赛时间
        cpiViewHolder.cpi_item_time_txt.setText(mMatchInfoBean.getOpenTime());
        //比赛的主客队名称和比分
        if("0".equals(mMatchInfoBean.getMatchState())){
            //未开赛
         cpiViewHolder.cpi_scoreAndName_txt.setText(mMatchInfoBean.getMatchHomeName()+
                 "\t"+"VS"+"\t"+mMatchInfoBean.getMatchGuestName());
        }else{ //开赛
            cpiViewHolder.cpi_scoreAndName_txt.setText(Html.fromHtml(mMatchInfoBean.getMatchHomeName()+
                    "\t"+"<font color=#ff0000>"+mMatchInfoBean.getMatchResult()+"</font>"+"\t"+mMatchInfoBean.getMatchGuestName()));
        }
//        mComListBean=mAllInfoBean.get(position).getComList();
        cardViewListAdapter=new CardViewListAdapter(context,mAllInfoBean.get(position).getComList());
        cpiViewHolder.item_cpi_odds_listview.setAdapter(cardViewListAdapter);
        new CpiListView(context).setListViewHeightBasedOnChildren(cpiViewHolder.item_cpi_odds_listview);

        cpiViewHolder.item_cpi_odds_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positionNunber, long id) {

                List<Map<String, String>> obList = new ArrayList<>();// 建立一个数组存储listview上显示的数据

//                    System.out.println(">>>>>>ewewe****"+ mAllInfoBean.get(position).getLeagueName());//这个是对的

//                UiUtils.toast(context, "" + mAllInfoBean.get(position).getComList().size());//这个是对的
                    for (int m = 0; m < mAllInfoBean.get(position).getComList().size(); m++) {
                        Map<String, String> obMap = new HashMap<>();
                        obMap.put("id", mAllInfoBean.get(position).getComList().get(m).getComId());
                        obMap.put("name", mAllInfoBean.get(position).getComList().get(m).getComName());
                        obMap.put("thirdid", mAllInfoBean.get(position).getMatchInfo().getMatchId());
                        obList.add(obMap);
                    }
                //点击指数页面，传值给详情界面
                Intent intent = new Intent(context, CpiDetailsActivity.class);
                intent.putExtra("obListEntity", (Serializable)obList);
                intent.putExtra("comId",mAllInfoBean.get(position).getComList().get(positionNunber).getComId());
                intent.putExtra("positionNunber",positionNunber+"");
                intent.putExtra("stType",stType);
                //如果未开赛
                if("0".equals(mAllInfoBean.get(position).getMatchInfo().getMatchState())){
                    intent.putExtra("PKScore",mAllInfoBean.get(position).getMatchInfo().getMatchHomeName()
                            +"\t"+"VS"+"\t"+mAllInfoBean.get(position).getMatchInfo().getMatchGuestName());
                }else{//否则开赛了
                    intent.putExtra("PKScore",mAllInfoBean.get(position).getMatchInfo().getMatchHomeName()
                            +"\t"+mAllInfoBean.get(position).getMatchInfo().getMatchResult()+"\t"+mAllInfoBean.get(position).getMatchInfo().getMatchGuestName());
                }
                context.startActivity(intent);


            }
        });
        //为 cardView设置点击事件
        cpiViewHolder.cpi_item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FootballMatchDetailActivity.class);
                intent.putExtra("thirdId", mAllInfoBean.get(position).getMatchInfo().getMatchId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAllInfoBean.size();
    }

    /**
     * 清除数据
     */
    public void clearData() {
        // clear the data
        mAllInfoBean.clear();
        cardViewListAdapter.cardViewclearData();
        notifyDataSetChanged();
    }

}
