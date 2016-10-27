package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.InforListData;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import java.util.List;


/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description:  资料库第一列表数据adapter
 * @data: 2016/3/30 11:40
 */
public class InformationDataAdapter extends CommonAdapter<InforListData.LeagueListBean> {


    private final Context mContext;

    private ImageView mTeam_icon;
    private int layoutId;
    public InformationDataAdapter(Context context, List<InforListData.LeagueListBean> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
        this.mDatas=datas;
        this.layoutId=layoutId;


    }



    public void updateDatas(List<InforListData.LeagueListBean> datas) {
        this.mDatas = datas;
    }


    //获取子布局控件
    @Override
    public void convert(ViewHolder holder, InforListData.LeagueListBean listDatas) {


          int leagueId=listDatas.getLid();
          String logoUrl="http://pic.13322.com/icons/league/"+leagueId+".png";
          mTeam_icon = (ImageView) holder.getConvertView().findViewById(R.id.football_logo_img);
         //加载图片
        ImageLoader.load(mContext,logoUrl,R.mipmap.live_default).into(mTeam_icon);

          //获取联赛名称
          holder.setText(R.id.football_item_team, listDatas.getName());


    }
}
