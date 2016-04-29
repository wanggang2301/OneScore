package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.widget.CpiListView;

import java.util.List;

/**
 * Created by tjl on 2016年4月14日 12:27:10
 * 新版指数（亚盘，大小，欧赔）CPIRecyclerViewAdapter
 */
public class CPIRecyclerViewAdapter extends RecyclerView.Adapter<CPIRecyclerViewAdapter.CPIViewHolder>{

    private List<Object> newses;
    private Context context;

    public CPIRecyclerViewAdapter(List<Object> newses, Context context) {
        this.newses = newses;
        this.context=context;
    }


    //自定义ViewHolder类
    static class CPIViewHolder extends RecyclerView.ViewHolder{

        CardView cpi_item_cardview;//cardview
        CpiListView item_cpi_odds_listview;//cardview里面list

        public CPIViewHolder(final View itemView) {
            super(itemView);
            cpi_item_cardview= (CardView) itemView.findViewById(R.id.cpi_item_cardview);

            item_cpi_odds_listview= (CpiListView) itemView.findViewById(R.id.item_cpi_odds_list);

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
        final int j=position;

//        cpiViewHolder.news_photo.setImageResource(newses.get(position).getPhotoId());
//        cpiViewHolder.news_title.setText(newses.get(position).getTitle());
//        cpiViewHolder.news_desc.setText(newses.get(position).getDesc());
//            for (int i=0;i<newses.get(position).getmMapList().size();i++){
////                System.out.println(">>>"+newses.get(position).getmMapList());
//                for (int k=0;k<newses.get(position).getmMapList().get(i).size();k++){
//                    System.out.println(">>>"+newses.get(position).getmMapList().get(k).get("date"));
//                }
//            }
//        SimpleAdapter companyAdapter = new SimpleAdapter(context,  newses.get(position).getmMapList(), R.layout.item_dialog_layout, new String[]{"date"}, new int[]{R.id.dialog_txt_cancel_list});
//        cpiViewHolder.item_cpi_odds_listview.setAdapter(companyAdapter);
//        new CpiListView(context).setListViewHeightBasedOnChildren(cpiViewHolder.item_cpi_odds_listview);

//        cpiViewHolder.btn_share_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
//                Toast.makeText(context,"tjl"+newses.get(position).getmMapList().get(p).get("date"),Toast.LENGTH_SHORT).show();
//            }
//        });
        //为 cardView设置点击事件
//        cpiViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context,NewsActivity.class);
//                intent.putExtra("News",newses.get(j));
//                context.startActivity(intent);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return newses.size();
    }
}
