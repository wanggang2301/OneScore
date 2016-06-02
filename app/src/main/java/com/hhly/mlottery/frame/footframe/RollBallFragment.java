package com.hhly.mlottery.frame.footframe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.RollBallAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.adapter.decration.BorderDividerItemDecration;
import com.hhly.mlottery.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;

import static com.hhly.mlottery.util.Preconditions.checkNotNull;

public class RollBallFragment extends BaseFragment implements BaseRecyclerViewHolder.OnItemClickListener,
				BaseRecyclerViewHolder.OnItemLongClickListener {

	private static final String FRAGMENT_SAVED_STATE_KEY = RollBallFragment.class.getSimpleName();
	private static final String FRAGMENT_INDEX = "fragment_index";
	@Bind(R.id.recyclerview_roll) RecyclerView recyclerView;
	@Bind(R.id.titleContainer) LinearLayout titleContainer;

	private LinearLayoutManager layoutManager;
	private BorderDividerItemDecration dataDecration;
	private RollBallAdapter adapter;

	private boolean resestTheLifeCycle;
	private boolean loadingMoreData;

	public static RollBallFragment newInstance(int index) {
		Bundle bundle = new Bundle();
		bundle.putInt(FRAGMENT_INDEX,index);
		RollBallFragment fragment = new RollBallFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override protected int getLayoutId() {
		return R.layout.football_roll;
	}

	@Override protected void initViews(View self,Bundle savedInstanceState) {
		this.setupRecyclerView();
		this.setupAdapter();
	}

	@Override protected void initListeners() {
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			private boolean moveToDown = false;
			private boolean titleAnimator = false;

			@Override public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
				if (loadingMoreData) return;
				RecyclerView.LayoutManager lManager = recyclerView.getLayoutManager();
				if (lManager instanceof LinearLayoutManager) {
					LinearLayoutManager manager = (LinearLayoutManager)lManager;
					if (newState == RecyclerView.SCROLL_STATE_IDLE) {
						if (titleContainer.getVisibility() == View.VISIBLE) {
							RollBallFragment.this.setupTitleAnimations(titleContainer,-200,new AnimatorListenerAdapter() {
								@Override public void onAnimationEnd(Animator animation) {
									titleAnimator = false;
									titleContainer.setVisibility(View.GONE);
								}
							});
						}
						if (moveToDown && manager.findLastCompletelyVisibleItemPosition() == (manager.getItemCount() - 1)) {
							loadingMoreData = true;
							loadUserList();
						} else if (layoutManager.findFirstVisibleItemPosition() == 0) {
							if (!titleAnimator) {
								RollBallFragment.this.setupTitleAnimations(titleContainer,0,new AnimatorListenerAdapter() {
									@Override public void onAnimationEnd(Animator animation) {
										titleContainer.setVisibility(View.VISIBLE);
										titleAnimator = true;
									}
								});
							}
						}
					}
				}
			}

			@Override public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
				this.moveToDown = dy > 0;
			}
		});
	}

	@Override protected void initData() {
		ArrayList datas = new ArrayList();
		for (int i = 0; i < 10; i++) {
			datas.add(i + "");
		}
		this.feedAdapter(datas);
	}

	@Override protected void initInjector() {

	}

	@Override protected void mOnViewStateRestored(Bundle savedInstanceState) {
		String restoreData = (String)savedInstanceState.getSerializable(FRAGMENT_SAVED_STATE_KEY);
		resestTheLifeCycle = true;
	}

	@Override protected void mOnSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable(FRAGMENT_SAVED_STATE_KEY,"");
	}

	@Override public void onItemClick(View convertView,int position) {

	}

	@Override public void onItemLongClick(View convertView,int position) {

	}

	private void setupTitleAnimations(View titleView,int translationY,Animator.AnimatorListener animatorListener) {
		titleView.animate().translationY(translationY).setDuration(400).setListener(animatorListener).start();
	}

	private void loadUserList() {
		initData();
	}

	public void setupRecyclerView() {
		dataDecration = new BorderDividerItemDecration(
						getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
						getResources().getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans));
		recyclerView.addItemDecoration(dataDecration);
		layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
		recyclerView.setLayoutManager(layoutManager);
	}

	private void setupAdapter() {
		if (adapter == null) {
			adapter = new RollBallAdapter(getActivity());
			recyclerView.setAdapter(adapter);
			adapter.setOnItemClickListener(this);
			adapter.setOnItemLongClickListener(this);
		}
	}

	private void feedAdapter(ArrayList datas) {
		if (datas == null) return;
		checkNotNull(adapter,"adapter == null");

		if (loadingMoreData) {
			loadingMoreData = false;
			adapter.addAll(datas);
			//			adapter.dismissFooterViewLoading();
		} else {
			this.checkTheLifeCycleIsChanging(resestTheLifeCycle);
			adapter.setList(datas);
		}
		adapter.notifyDataSetChanged();
	}

	private void checkTheLifeCycleIsChanging(boolean resestTheLifeCycle) {
		if (resestTheLifeCycle) {
			this.resestTheLifeCycle = false;
			this.clearDecoration();
			recyclerView.setLayoutManager(layoutManager);
			recyclerView.addItemDecoration(dataDecration);
		}
	}

	private void clearDecoration() {
		this.recyclerView.removeItemDecoration(this.dataDecration);
	}

}
