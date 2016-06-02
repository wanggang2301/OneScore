package com.hhly.mlottery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;

import static com.hhly.mlottery.util.Preconditions.checkNotNull;

public class RollBallAdapter extends BaseRecyclerViewAdapter {

	public static final int VIEW_TYPE_DEFAULT = 1;

	private Context context;

	public RollBallAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override public int[] getItemLayouts() {
		return new int[] {R.layout.item_football_roll};
	}

	@Override public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder,int position) {
		int itemViewType = getRecycleViewItemType(position);
		switch (itemViewType) {
		case VIEW_TYPE_DEFAULT:
			this.bindDefaultView(viewHolder,position);
			break;
		}
	}

	@Override public int getRecycleViewItemType(int position) {
		return VIEW_TYPE_DEFAULT;
	}

	@SuppressLint("SetTextI18n") private void bindDefaultView(BaseRecyclerViewHolder viewHolder,int position) {
		//		RollballModel data = getItemByPosition(position);
		String data = getItemByPosition(position);
		checkNotNull(data,"data == null");

		TextView titleContainer = viewHolder.findViewById(R.id.titleContainer);
		TextView tvTournament = viewHolder.findViewById(R.id.tvTournament);
		TextView tvScoreUp = viewHolder.findViewById(R.id.tvScoreUp);
		TextView tvTime = viewHolder.findViewById(R.id.tvTime);
		TextView tvSpedingTime = viewHolder.findViewById(R.id.tvSpedingTime);
		ImageView ivItemPositionControl = viewHolder.findViewById(R.id.ivItemPositionControl);
		TextView tvScoreDown = viewHolder.findViewById(R.id.tvScoreDown);
		TextView tvTeamUp = viewHolder.findViewById(R.id.tvTeamUp);
		TextView tvTeamUPScore1 = viewHolder.findViewById(R.id.tvTeamUPScore1);
		TextView tvTeamUpScore2 = viewHolder.findViewById(R.id.tvTeamUpScore2);
		TextView tvSpace = viewHolder.findViewById(R.id.tvSpace);
		TextView tvMiddleScore1 = viewHolder.findViewById(R.id.tvMiddleScore1);
		TextView tvMiddleScore2 = viewHolder.findViewById(R.id.tvMiddleScore2);
		TextView tvTeamDown = viewHolder.findViewById(R.id.tvTeamDown);
		TextView tvTeamDownScore1 = viewHolder.findViewById(R.id.tvTeamDownScore1);
		TextView tvTeamDownScore2 = viewHolder.findViewById(R.id.tvTeamDownScore2);
		TextView tvTeamUp_YA = viewHolder.findViewById(R.id.tvTeamUp_YA);
		TextView tvTeamMiddle_YA = viewHolder.findViewById(R.id.tvTeamMiddle_YA);
		TextView tvTeamDown_YA = viewHolder.findViewById(R.id.tvTeamDown_YA);
		TextView tvTeamUp_DA = viewHolder.findViewById(R.id.tvTeamUp_DA);
		TextView tvTeamMiddle_DA = viewHolder.findViewById(R.id.tvTeamMiddle_DA);
		TextView tvTeamDown_DA = viewHolder.findViewById(R.id.tvTeamDown_DA);
		TextView tvTeamUp_EU = viewHolder.findViewById(R.id.tvTeamUp_EU);
		TextView tvTeamMiddle_EU = viewHolder.findViewById(R.id.tvTeamMiddle_EU);
		TextView tvTeamDown_EU = viewHolder.findViewById(R.id.tvTeamDown_EU);

	}
}
