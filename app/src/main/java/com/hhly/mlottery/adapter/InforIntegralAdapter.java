package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballsecond.IntegralBean;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * Created by Administrator on 2016/4/4 0004.
 */
public class InforIntegralAdapter extends CommonAdapter<IntegralBean.LangueScoreBean.ListBean> {

    private final Context mContext;
    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;
    private final DisplayImageOptions options;
    List<IntegralBean.LangueScoreBean.ListBean> childDataList;
    public InforIntegralAdapter(Context context,List<IntegralBean.LangueScoreBean.ListBean> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
        childDataList=datas;
        System.out.print("datas+====="+datas);
               options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.basket_default)   //默认图片
                .showImageForEmptyUri(R.mipmap.basket_default)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.basket_default)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).build();
        universalImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);

    }


//List<List<IntegralBean.LangueScoreBean.ListBean>>
    public void convert(ViewHolder holder, IntegralBean.LangueScoreBean.ListBean leaTeamBean) {
//        for (int c=0;c<childDataList.size();c++ ){
//            for (int j=0;j<c;j++){
//        childDataList=
//                System.out.println(">>>leaTeamBean.get(j).getName()"+leaTeamBean.get(j).getName());
//
////            }
////        }

//        for(int i= 0;i<childDataList.size();i++){
//            System.out.println(">>>>>>");
//            //holder.setText(R.id.integral_round,leaTeamBean.get(i).getRound());
//            //holder.setText(R.id.integral_round,leaTeamBean.get(i).getRound());
//            // holder.setText(R.id.integral_winorloss,leaTeamBean.getWin()+"/"+leaTeamBean.getEqu()+"/"+leaTeamBean.getFail());
//            //holder.setText(R.id.integral_goalorloss,leaTeamBean.getGoal()+"/"+leaTeamBean.getLoss());
//           // holder.setText(R.id.integral_abs,leaTeamBean.getAbs());
//           // holder.setText(R.id.integral_score,leaTeamBean.getScore());
//            //名次
//           // holder.setText(R.id.football_integral_tv,leaTeamBean.get(i).get);
//            //球队名称
//            holder.setText(R.id.integral_team_name, leaTeamBean.get(i).getName());
//            //队徽
//            //holder.setText(R.id.integral_team_imag);
//            //场次
//            holder.setText(R.id.integral_round, leaTeamBean.get(i).getRound());
//            //胜
//            holder.setText(R.id.integral_win, leaTeamBean.get(i).getWin());
//            //平
//            holder.setText(R.id.integral_equ, leaTeamBean.get(i).getEqu());
//            //负
//            holder.setText(R.id.integral_fail, leaTeamBean.get(i).getFail());
//            //得
//            holder.setText(R.id.integral_goal, leaTeamBean.get(i).getGoal());
//            //失
//            holder.setText(R.id.integral_loss,leaTeamBean.get(i).getLoss());
//            //净胜
//            holder.setText(R.id.integral_abs, leaTeamBean.get(i).getAbs());
//            //得分
//            holder.setText(R.id.integral_score, leaTeamBean.get(i).getScore());
//
//        }


     // ImageView  teamPic= (ImageView) holder.getConvertView().findViewById(R.id.football_integral_imag);
      //获取队图标
        // String url=matchBean.getGuestLogoUrl();
        //加载图片
        //universalImageLoader.displayImage(url,teamPic,options);

      //  holder.setText(R.id.integral_name,leaTeamBean.getName());

    }
}
