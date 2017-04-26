package com.hhly.mlottery.adapter.football.teaminfoadapter;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamHistoryMatchBean;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamInfoListBean;

import java.util.List;

/**
 * desc:足球球队信息适配器
 * Created by 107_tangrr on 2017/4/21 0021.
 */

public class FootTeamInfoLineupAdapter extends BaseQuickAdapter<FootTeamInfoListBean.PlayerBean> {

    public FootTeamInfoLineupAdapter(int layoutResId, List<FootTeamInfoListBean.PlayerBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, FootTeamInfoListBean.PlayerBean bean) {
        if (bean == null) {
            return;
        }
        holder.setText(R.id.tv_lineup_number, bean.getNo() == 0 ? "-" : String.valueOf(bean.getNo()));
        holder.setText(R.id.tv_lineup_name, bean.getName() == null ? "-" : bean.getName());
        holder.setText(R.id.tv_lineup_nation, bean.getNation() == null ? "-" : bean.getNation());
        holder.setText(R.id.tv_lineup_position, bean.getPosition() == null ? "-" : bean.getPosition());
        holder.setText(R.id.tv_lineup_birth, bean.getBirth() == null ? "-" : bean.getBirth());
    }
}
