package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by A on 2016/1/12.
 */
public class BasketFiltrateAdapter extends SimpleAdapter{

    private LayoutInflater inflater;

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item_share. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item_share.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public BasketFiltrateAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }



//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            Holder holder = null;
//            if (null == convertView)
//            {
//                convertView = createChildrenView();
//                holder = new Holder();
//
//                convertView.setTag(holder);
//            }
//            else
//            {
//                holder = (Holder) convertView.getTag();
//            }
//            // 点击改变选中listItem的背景色
//            if (clickTemp == position) {
//                convertView.setBackgroundResource(R.drawable.check_in_gdv_bg_s);
//            } else {
//                convertView.setBackgroundColor(Color.TRANSPARENT);
//            }}
//    }








}
