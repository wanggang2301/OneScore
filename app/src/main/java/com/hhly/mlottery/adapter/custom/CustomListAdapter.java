package com.hhly.mlottery.adapter.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CustomListActivity;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomFristBean;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomListBean;
import com.hhly.mlottery.bean.custombean.customlistdata.CustomSecondBean;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.List;

/**
 * Created by yixq on 2016/12/5.
 * mail：yixq@13322.com
 * describe:
 */

public class CustomListAdapter extends BaseQuickAdapter<CustomFristBean> {

    private Context mContext;
    private List mData;
    private String LEAGUE_SUFFIX = "_A";//联赛标记
    private String TEAM_SUFFIX = "_B";//球队标记

    public CustomListAdapter(Context context, List data) {
        super(R.layout.custom_list_league_item, data);
        this.mContext = context;
        this.mData = data;
    }

    public void setData(List data) {
        this.mData = data;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ImageView mIsOpen);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickLitener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    private CustomListActivity.CustomFocusClickListener mFocus; //关注监听回掉

    public void setmFocus(CustomListActivity.CustomFocusClickListener mFocus) {
        this.mFocus = mFocus;
    }
    public CustomListActivity.CustomDetailsClickListener mDetails;
    public void setmDetails(CustomListActivity.CustomDetailsClickListener mDetails){
        this.mDetails = mDetails;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null && mData.size() == 0) {
            return super.getItemViewType(position);
        }
        /**
         * 联赛类型
         */
        else if (mData.get(position) instanceof CustomFristBean) {
            return ((CustomFristBean) mData.get(position)).getFirstType();
        }
        /**
         * 球队类型
         */
        else if (mData.get(position) instanceof CustomSecondBean) {
            return ((CustomSecondBean) mData.get(position)).getSecondType();
        } else {
            /**
             * 都不属于 return super
             */
            return super.getItemViewType(position);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        BaseViewHolder holder = null;
        switch (viewType) {
            case 0:
                //联赛
                view = LayoutInflater.from(mContext).inflate(R.layout.custom_list_league_item, parent, false);
                holder = new ViewHolderLeague(view);
                break;
            case 1:
                //日期
                view = LayoutInflater.from(mContext).inflate(R.layout.custom_team_league_item, parent, false);
                holder = new ViewHolderTeamData(view);
                break;
        }
        return holder;
    }

    private boolean isAllCheck ;
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int positions) {

        if (mData == null) {
            return;
        }
        switch (getItemViewType(positions)) {
            case 0:
                final ViewHolderLeague viewHolderLeague = (ViewHolderLeague) holder;

                final CustomFristBean firstData = (CustomFristBean) mData.get(positions);

                final List<CustomSecondBean> currSecondData = firstData.getTeamConcerns();

                String logoUrl = firstData.getLeagueLogoPre() + firstData.getLeagueId() + firstData.getLeagueLogoSuff();
                if (mContext != null) {
                    ImageLoader.load(mContext, logoUrl, R.mipmap.iconfont_lanqiu2shixin).into(viewHolderLeague.mLeagueLogo);
                }

                if (firstData.getTeamConcerns() == null || firstData.getTeamConcerns().size() == 0) {
                    viewHolderLeague.mNoData.setVisibility(View.VISIBLE);
                    viewHolderLeague.mIsOpen.setVisibility(View.INVISIBLE);
                } else {
                    viewHolderLeague.mNoData.setVisibility(View.GONE);
                    viewHolderLeague.mIsOpen.setVisibility(View.VISIBLE);
                }
                if (firstData != null) {
                    viewHolderLeague.mLeagueName.setText(firstData.getLeagueName());
                    viewHolderLeague.mLeagueHotNum.setText(firstData.getLegueConcernNum());
                }else{
                    viewHolderLeague.mLeagueName.setText("");
                    viewHolderLeague.mLeagueHotNum.setText("");
                }

                if (onItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            onItemClickListener.onItemClick(holder.itemView, pos, viewHolderLeague.mIsOpen);
                        }
                    });
                }

                String focus_leagueids = PreferenceUtil.getString("custom_leagueId_focus_ids", "");
                String[] leagueIds = focus_leagueids.split("[,]");

                for (String id : leagueIds) {
                    if (id.equals(firstData.getLeagueId() + LEAGUE_SUFFIX)) {
                        viewHolderLeague.mLeagueStar.setBackgroundResource(R.mipmap.custom_focus);
                        viewHolderLeague.mLeagueStar.setTag(true);
                        firstData.setConcern(true);
                        break;
                    } else {
                        viewHolderLeague.mLeagueStar.setBackgroundResource(R.mipmap.custom_unfocus);
                        viewHolderLeague.mLeagueStar.setTag(false);
                        firstData.setConcern(false);
                    }
                }

                viewHolderLeague.mLeagueStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /** + "-A" 区分联赛 和球队 id相同的情况*/
                        mFocus.FocusOnClick(v, firstData.getLeagueId() + LEAGUE_SUFFIX, firstData , currSecondData);
                    }
                });
                viewHolderLeague.mLeagueName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDetails.DetailsOnClick(v, firstData.getLeagueId() + LEAGUE_SUFFIX, firstData);
                    }
                });
                break;
            case 1:
                final ViewHolderTeamData viewHolderDate = (ViewHolderTeamData) holder;

                final CustomSecondBean secondData = (CustomSecondBean) mData.get(positions);

                String tameLogoUrl = secondData.getTeamLogoPre() + secondData.getTeamId() + secondData.getTeamLogoSuff();
                if (mContext != null) {
                    ImageLoader.load(mContext, tameLogoUrl, R.mipmap.iconfont_lanqiu2shixin).into(viewHolderDate.mTeamLogo);
                }
                if (secondData != null) {
                    viewHolderDate.mTeamName.setText(secondData.getTeamName());
                    viewHolderDate.mTeamHotNum.setText(secondData.getTeamConcernNum());
                }else{
                    viewHolderDate.mTeamName.setText("");
                    viewHolderDate.mTeamHotNum.setText("");
                }

                if (onItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            onItemClickListener.onItemClick(holder.itemView, pos, null);
                        }
                    });
                }

                String focus_Dateids = PreferenceUtil.getString("custom_team_focus_ids", "");
                String[] dateids = focus_Dateids.split("[,]");
                for (String id : dateids) {
                    if (id.equals(secondData.getTeamId() + TEAM_SUFFIX)) {
                        viewHolderDate.mTeamStar.setBackgroundResource(R.mipmap.custom_focus);
                        viewHolderDate.mTeamStar.setTag(true);
                        secondData.setConcern(true);
                        break;
                    } else {
                        viewHolderDate.mTeamStar.setBackgroundResource(R.mipmap.custom_unfocus);
                        viewHolderDate.mTeamStar.setTag(false);
                        secondData.setConcern(false);
                    }
                }
                viewHolderDate.mTeamStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /** + "-B" 区分联赛 和球队 id相同的情况*/
                        mFocus.FocusOnClick(v, secondData.getTeamId() + TEAM_SUFFIX , secondData);
                    }
                });
                viewHolderDate.mTeamName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDetails.DetailsOnClick(v, secondData.getTeamId() + TEAM_SUFFIX , secondData);
                    }
                });
                break;
        }
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }

    /**
     * 添加所有child
     *
     * @param lists
     * @param position
     */
    public void addAllChild(List<?> lists, int position) {
        mData.addAll(position, lists);
        notifyItemRangeInserted(position, lists.size());
    }

    /**
     * 删除所有child
     *
     * @param position
     * @param itemnum
     */
    public void deleteAllChild(int position, int itemnum) {
        for (int i = 0; i < itemnum; i++) {
            mData.remove(position);
        }
        notifyItemRangeRemoved(position, itemnum);
    }


    /**
     * 球队holder
     */
    class ViewHolderTeamData extends BaseViewHolder {
        ImageView mTeamLogo;
        TextView mTeamName;
        TextView mTeamHotNum;
        ImageView mTeamStar;

        public ViewHolderTeamData(View itemView) {
            super(itemView);

            mTeamLogo = (ImageView) itemView.findViewById(R.id.custom_team_logo);
            mTeamName = (TextView) itemView.findViewById(R.id.custom_team_name);
            mTeamHotNum = (TextView) itemView.findViewById(R.id.custom_team_hot_num);
            mTeamStar = (ImageView) itemView.findViewById(R.id.custom_team_star);
        }
    }

    /**
     * 联赛holder
     */
    public class ViewHolderLeague extends BaseViewHolder {

        ImageView mLeagueLogo;
        TextView mLeagueName;
        TextView mLeagueHotNum;
        ImageView mIsOpen;
        ImageView mLeagueStar;
        TextView mNoData;

        public ViewHolderLeague(View itemView) {
            super(itemView);

            mIsOpen = (ImageView) itemView.findViewById(R.id.custom_item_isopen);
            mLeagueLogo = (ImageView) itemView.findViewById(R.id.custom_league_list_logo);
            mLeagueName = (TextView) itemView.findViewById(R.id.custom_league_name);
            mLeagueHotNum = (TextView) itemView.findViewById(R.id.custom_league_hot_num);
            mLeagueStar = (ImageView) itemView.findViewById(R.id.custom_league_star);
            mNoData = (TextView) itemView.findViewById(R.id.custon_league_nodata);


        }
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomFristBean customFristBean) {
    }
}
