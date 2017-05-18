/**   
 * @Title: LeagueCup.java 
 * @Package com.bethena.hhly.item_live_text
 * @Description: 杯赛对象
 * @author chenml   
 * @date 2015-10-13 下午6:45:26 
 * @version V1.0   
 */
package com.hhly.mlottery.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class LeagueCup implements Parcelable {
	public final static int TYPE_TITLE = 1;//用于区分筛选
	public final static int TYPE_BLANK = 2;
	public final static int TYPE_CUP = 3;
	public final static int TYPE_BLANK_AFTER_CUP = 4;
	
	
	private int type;
	private List<String> thirdId;
	private String racename;
	private String raceId;
	private int count;
	private boolean hot;

	public LeagueCup(int type, List<String> thirdId, String racename, String raceId, int count, boolean hot) {
		this.type = type;
		this.thirdId = thirdId;
		this.racename = racename;
		this.raceId = raceId;
		this.count = count;
		this.hot = hot;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRaceId() {
		return raceId;
	}

	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}

	public boolean isHot() {
		return hot;
	}

	public void setHot(boolean hot) {
		this.hot = hot;
	}

	public LeagueCup() {// Gson 解析需要保存无参构造

	}

	public List<String> getThirdId() {
		return thirdId;
	}

	public void setThirdId(List<String> thirdId) {
		this.thirdId = thirdId;
	}

	public String getRacename() {
		return racename;
	}

	public void setRacename(String racename) {
		this.racename = racename;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(thirdId);
		dest.writeString(racename);
		dest.writeInt(count);
		dest.writeString(raceId);
		dest.writeByte((byte) (hot ? 1 : 0));
	}

	public static final Parcelable.Creator<LeagueCup> CREATOR = new Creator<LeagueCup>() {

		@Override
		public LeagueCup[] newArray(int size) {
			return new LeagueCup[size];
		}

		@Override
		public LeagueCup createFromParcel(Parcel source) {
			LeagueCup cup = new LeagueCup(source);

			return cup;
		}
	};

	public LeagueCup(Parcel in) {
		thirdId = in.readArrayList(ArrayList.class.getClassLoader());
		racename = in.readString();
		count = in.readInt();
		raceId = in.readString();
		hot = in.readByte() == 1 ? true : false;
	}
}
