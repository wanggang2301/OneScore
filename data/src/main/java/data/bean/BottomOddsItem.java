package data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wang gang
 * @date 2016/6/8 11:24
 * @des ${}
 */
public class BottomOddsItem implements Parcelable {

    private String left;
    private String middle;
    private String right;

    private String leftUp;

    private String middleUp;

    private String rightUp;

    public BottomOddsItem(String left, String middle, String right, String leftUp, String middleUp, String rightUp) {
        this.left = left;
        this.middle = middle;
        this.right = right;
        this.leftUp = leftUp;
        this.middleUp = middleUp;
        this.rightUp = rightUp;
    }


    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getLeftUp() {
        return leftUp;
    }

    public void setLeftUp(String leftUp) {
        this.leftUp = leftUp;
    }

    public String getMiddleUp() {
        return middleUp;
    }

    public void setMiddleUp(String middleUp) {
        this.middleUp = middleUp;
    }

    public String getRightUp() {
        return rightUp;
    }

    public void setRightUp(String rightUp) {
        this.rightUp = rightUp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.left);
        dest.writeString(this.middle);
        dest.writeString(this.right);
        dest.writeString(this.leftUp);
        dest.writeString(this.middleUp);
        dest.writeString(this.rightUp);
    }

    public BottomOddsItem() {
    }

    protected BottomOddsItem(Parcel in) {
        this.left = in.readString();
        this.middle = in.readString();
        this.right = in.readString();
        this.leftUp = in.readString();
        this.middleUp = in.readString();
        this.rightUp = in.readString();
    }

    public static final Creator<BottomOddsItem> CREATOR = new Creator<BottomOddsItem>() {
        @Override
        public BottomOddsItem createFromParcel(Parcel source) {
            return new BottomOddsItem(source);
        }

        @Override
        public BottomOddsItem[] newArray(int size) {
            return new BottomOddsItem[size];
        }
    };
}
