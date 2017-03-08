package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.NewAnalyzeBean;
import com.hhly.mlottery.bean.footballDetails.PointList;

import org.w3c.dom.Text;

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
            if(position==1){
                holder.tv1.setText(list.get(position).getPoint1()+"");
                holder.tv2.setText(list.get(position).getPoint2());
                holder.tv3.setText(list.get(position).getPoint3());
                holder.tv4.setText(list.get(position).getPoint4());
                holder.tv5.setText(list.get(position).getPoint5());
                holder.tv6.setText(list.get(position).getPoint6());
            }

            if(position==0){
                if(list.get(position).getPoint1()!=null){
                    setScore(list.get(position).getPoint1(),holder.tv1);
                }
                if(list.get(position).getPoint2()!=null){
                    setScore(list.get(position).getPoint2(),holder.tv2);
                }
                if(list.get(position).getPoint3()!=null){
                    setScore(list.get(position).getPoint3(),holder.tv3);
                }
                if(list.get(position).getPoint4()!=null){
                    setScore(list.get(position).getPoint4(),holder.tv4);
                }
                if(list.get(position).getPoint5()!=null){
                    setScore(list.get(position).getPoint5(),holder.tv5);
                }
                if(list.get(position).getPoint6()!=null){
                    setScore(list.get(position).getPoint6(),holder.tv6);
                }
            }


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
            if(position==1){
                holder.tv1.setText(list.get(position).getPoint1()+"");
                holder.tv2.setText(list.get(position).getPoint2());
                holder.tv3.setText(list.get(position).getPoint3());
                holder.tv4.setText(list.get(position).getPoint4());
                holder.tv5.setText(list.get(position).getPoint5());
                holder.tv6.setText(list.get(position).getPoint6());
            }

            if(position==0){
                if(list.get(position).getPoint1()!=null){
                    setScore(list.get(position).getPoint1(),holder.tv1);
                }
                if(list.get(position).getPoint2()!=null){
                    setScore(list.get(position).getPoint2(),holder.tv2);
                }
                if(list.get(position).getPoint3()!=null){
                    setScore(list.get(position).getPoint3(),holder.tv3);
                }
                if(list.get(position).getPoint4()!=null){
                    setScore(list.get(position).getPoint4(),holder.tv4);
                }
                if(list.get(position).getPoint5()!=null){
                    setScore(list.get(position).getPoint5(),holder.tv5);
                }
                if(list.get(position).getPoint6()!=null){
                    setScore(list.get(position).getPoint6(),holder.tv6);
                }
            }
            //1 赢（大）2 输（小）0 走
            if(position==2){
                if(analyzeBean.getSizeTrend()!=null&&analyzeBean.getSizeTrend().getBattleHistory()!=null&&analyzeBean.getSizeTrend().getBattleHistory().getTotList().size()!=0){
                    List<NewAnalyzeBean.SizeTrendEntity.BattleHistoryEntity.TotListEntity> listLet=analyzeBean.getSizeTrend().getBattleHistory().getTotList();
                    for(int i=0;i<listLet.size();i++){
                        if(listLet.get(i).getTot()==0){ //第i场比赛 走
                            tvList.get(i).setText(mContext.getString(R.string.new_analyze_draw));
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.more_record));
                        }else if(listLet.get(i).getTot()==1){
                            tvList.get(i).setText(mContext.getString(R.string.basket_handicap_big));
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_win_color));
                        }else if(listLet.get(i).getTot()==2){
                            tvList.get(i).setText(mContext.getString(R.string.basket_handicap_small));
                            tvList.get(i).setTextColor(mContext.getResources().getColor(R.color.football_analyze_lose_color));
                        }
                    }
                }

            }
        }
        return convertView;
    }

    /**
     * 设置比分，主队比分标红
     * @param point
     * @param score
     */
    private void setScore(String point, TextView score) {
        String a[]=point.split("-");
        String text1="<font color='#f15353'><b>" +a[0]+ "</b></font> "+"<font color='#333333'><b>" +"- "+a[1]+ "</b></font> ";//第一个比分是主队
        String text2="<font color='#333333'><b>" +a[0]+" -"+ "</b></font> "+"<font color='#f15353'><b>" +a[1]+ "</b></font> ";//第二个比分是主队
        if(a[2].equals("1")){ //第一个比分是主队，即2-1 中 2需要标红
            score.setText(Html.fromHtml(text1));
        }else if(a[2].equals("2")){ //第二个比分是主队 即 2-1中 1需要标红
            score.setText(Html.fromHtml(text2));
        }
    }
     class ViewHolder{
        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
        private TextView tv5;
        private TextView tv6;
    }
}
