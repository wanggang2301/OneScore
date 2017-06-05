package com.hhly.mlottery.adapter.core;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

	/**
	 * Used to hold the findViewById loading the view
	 * 用于保存findViewById加载过的view
	 */
	private final SparseArray<View> views;
	private View itemView;


	public BaseRecyclerViewHolder(View itemView) {
		super(itemView);
		views = new SparseArray<>();
		this.itemView = itemView;
	}

	/**
	 * Due to the findViewById performance too low
	 * The findViewById view will be cached, provide the findViewById next time the same view
	 * ViewHolder data.model for the View
	 * 由于findViewById性能过低
	 * findViewById过的view会被缓存下来，以供下次find相同view的时候
	 * ViewHolder模式 查找子View
	 *
	 * @param viewId viewId
	 * @param <T>    Test
	 * @return Test
	 */
	public <T extends View> T findViewById(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = itemView.findViewById(viewId);
			views.put(viewId,view);
		}
		return (T)view;
	}

	/**
	 * set the on item_event_half_finish click listener
	 * 设置Item的点击事件
	 *
	 * @param listener listener
	 * @param position position
	 */
	public void setOnItemClickListener(final OnItemClickListener listener,final int position) {
		if (listener == null) {
			this.itemView.setOnClickListener(null);
		} else {
			this.itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					listener.onItemClick(v,position);
				}
			});
		}
	}

	/**
	 * set the on item_event_half_finish long click listener
	 * 设置Item的长点击事件
	 *
	 * @param listener listener
	 * @param position position
	 */
	public void setOnItemLongClickListener(final OnItemLongClickListener listener,final int position) {
		if (listener == null) {
			this.itemView.setOnLongClickListener(null);
		} else {
			this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override public boolean onLongClick(View v) {
					listener.onItemLongClick(v,position);
					return true;
				}
			});
		}
	}

	/**
	 * the click listeners callback
	 * 点击事件回调
	 */
	public interface OnItemClickListener {
		/**
		 * on item_event_half_finish click call back
		 *
		 * @param convertView convertView
		 * @param position    position
		 */
		void onItemClick(View convertView,int position);
	}

	/**
	 * the long click listeners callback
	 * 长点击事件回调
	 */
	public interface OnItemLongClickListener {
		/**
		 * on item_event_half_finish long click call back
		 *
		 * @param convertView convertView
		 * @param position    position
		 * @return true false
		 */
		void onItemLongClick(View convertView,int position);
	}
}
