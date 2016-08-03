package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.NewAnalyzeBean;
import com.hhly.mlottery.bean.footballDetails.PointList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madongyun 155 on 2016/7/20 15:37.
 * <p/>
 * 描述：其实一开始写这个Adapter我是拒绝的。但是没想到别的好办法。
 */
public class AnalyzeAsiaAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private List<?extends PointList> mPointList;
    private NewAnalyzeBean analyzeBean;

    public AnalyzeAsiaAdapter(Context mContext, List<? extends PointList> mPointList,NewAnalyzeBean analyzeBean) {
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
        if(mPointList==null){
            mPointList=new ArrayList<>();
        }
        this.mPointList = mPointList;
        this.analyzeBean=analyzeBean;
    }

    @Override
    public int getCount() {
        return mPointList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPointList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_analyze_let,parent,false);
            holder=new ViewHolder();

            holder.tv1= (TextView) convertView.findViewById(R.id.item_point1);
            holder.tv2= (TextView) convertView.findViewById(R.id.item_point2);
            holder.tv3= (TextView) convertView.findViewById(R.id.item_point3);
            holder.tv4= (TextView) convertView.findViewById(R.id.item_point4);
            holder.tv5= (TextView) convertView.findViewById(R.id.item_point5);
            holder.tv6= (TextView) convertView.findViewById(R.id.item_point6);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        if(mPointList.get(position) instanceof NewAnalyzeBean.AsiaTrendEntity.BattleHistoryEntity.PointListEntity){
            List<NewAnalyzeBean.AsiaTrendEntity.BattleHistoryEntity.PointListEntity>list= (List<NewAnalyzeBean.AsiaTrendEntity.BattleHistoryEntity.PointListEntity>) mPointList;
            List<TextView> tvList=new ArrayList<>();
            tvList.add(holder.tv1);
            tvList.add(holder.tv2);
            tvList.add(holder.tv3);
            tvList.add(holder.tv4);
            tvList.add(holder.tv5);
            tvList.add(holder.tv6);
            for(int i=0;i<tvList.size();i++){
                tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_default_color));
            }
            holder.tv1.setText(list.get(position).getPoint1()+"");
            holder.tv2.setText(list.get(position).getPoint2());
            holder.tv3.setText(list.get(position).getPoint3());
            holder.tv4.setText(list.get(position).getPoint4());
            holder.tv5.setText(list.get(position).getPoint5());
            holder.tv6.setText(list.get(position).getPoint6());
            //1 赢（大）2 输（小）0 走
            if(position==2){
                if(analyzeBean.getAsiaTrend().getBattleHistory()!=null&&analyzeBean.getAsiaTrend().getBattleHistory().getLetList().size()!=0){
                    List<NewAnalyzeBean.AsiaTrendEntity.BattleHistoryEntity.LetListEntity> listLet=analyzeBean.getAsiaTrend().getBattleHistory().getLetList();
                    for(int i=0;i<listLet.size();i++){
                        if(listLet.get(i).getLet()==0){ //第i场比赛 走
                            tvList.get(i).setText(mContext.getString(R.string.new_analyze_draw));
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.more_record));
                        }else if(listLet.get(i).getLet()==1){
                            tvList.get(i).setText(mContext.getString(R.string.new_analyze_win));
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_win_color));
                        }else if(listLet.get(i).getLet()==2){
                            tvList.get(i).setText(mContext.getString(R.string.new_analyze_lose));
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_lose_color));
                        }
                    }
                }
            }
        }
        else { //大小球走势
            List<NewAnalyzeBean.SizeTrendEntity.BattleHistoryEntity.PointListEntity>list= (List<NewAnalyzeBean.SizeTrendEntity.BattleHistoryEntity.PointListEntity>) mPointList;
            List<TextView> tvList=new ArrayList<>();
            tvList.add(holder.tv1);
            tvList.add(holder.tv2);
            tvList.add(holder.tv3);
            tvList.add(holder.tv4);
            tvList.add(holder.tv5);
            tvList.add(holder.tv6);
            for(int i=0;i<tvList.size();i++){
                tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_default_color));
            }
            holder.tv1.setText(list.get(position).getPoint1()+"");
            holder.tv2.setText(list.get(position).getPoint2());
            holder.tv3.setText(list.get(position).getPoint3());
            holder.tv4.setText(list.get(position).getPoint4());
            holder.tv5.setText(list.get(position).getPoint5());
            holder.tv6.setText(list.get(position).getPoint6());
            //1 赢（大）2 输（小）0 走
            if(position==2){
                if(analyzeBean.getSizeTrend()!=null&&analyzeBean.getSizeTrend().getBattleHistory()!=null&&analyzeBean.getSizeTrend().getBattleHistory().getTotList().size()!=0){
                    List<NewAnalyzeBean.SizeTrendEntity.BattleHistoryEntity.TotListEntity> listLet=analyzeBean.getSizeTrend().getBattleHistory().getTotList();
                    for(int i=0;i<listLet.size();i++){
                        if(listLet.get(i).getTot()==0){ //第i场比赛 走
                            tvList.get(i).setText("走");
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.more_record));
                        }else if(listLet.get(i).getTot()==1){
                            tvList.get(i).setText("大");
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_win_color));
                        }else if(listLet.get(i).getTot()==2){
                            tvList.get(i).setText("小");
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_lose_color));
                        }
                    }
                }

            }
        }
        return convertView;
    }

    static class ViewHolder{
        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;
    }
}
