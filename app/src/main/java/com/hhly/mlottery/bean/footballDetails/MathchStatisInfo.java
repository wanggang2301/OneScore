package com.hhly.mlottery.bean.footballDetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus1 on 2016/1/21.
 */
public class MathchStatisInfo implements Parcelable {

    private int home_score = 0;

    private int home_half_score = 0;

    private int home_rc = 0;

    private int home_yc = 0;

    private int home_corner = 0;

    private int home_danger = 0;

    private int home_shoot_correct = 0;

    private int home_shoot_miss = 0;


    private int home_shoot_door = 0;

    private int home_rescue = 0;   //扑救

    private int home_away = 0; //越位

    private int home_free_kick = 0; //任意球

    private int home_foul = 0; //犯规

    private int home_lineOut = 0;


    private int guest_score = 0;

    private int guest_half_score = 0;


    private int guest_rc = 0;

    private int guest_yc = 0;

    private int guest_corner = 0;


    private int guest_danger = 0;

    private int guest_shoot_correct = 0;

    private int guest_shoot_miss = 0;

    private int guest_shoot_door = 0;


    private int guest_rescue = 0;   //扑救


    private int guest_away = 0; //越位

    private int guest_free_kick = 0; //任意球


    private int guest_foul = 0; //犯规

    private int guest_lineOut = 0; //界外球


    private int home_attack = 0;

    private int guest_attack = 0;

    public MathchStatisInfo(){}

    protected MathchStatisInfo(Parcel in) {
        home_score = in.readInt();
        home_half_score = in.readInt();
        home_rc = in.readInt();
        home_yc = in.readInt();
        home_corner = in.readInt();
        home_danger = in.readInt();
        home_shoot_correct = in.readInt();
        home_shoot_miss = in.readInt();
        home_shoot_door = in.readInt();
        home_rescue = in.readInt();
        home_away = in.readInt();
        home_free_kick = in.readInt();
        home_foul = in.readInt();
        home_lineOut = in.readInt();
        guest_score = in.readInt();
        guest_half_score = in.readInt();
        guest_rc = in.readInt();
        guest_yc = in.readInt();
        guest_corner = in.readInt();
        guest_danger = in.readInt();
        guest_shoot_correct = in.readInt();
        guest_shoot_miss = in.readInt();
        guest_shoot_door = in.readInt();
        guest_rescue = in.readInt();
        guest_away = in.readInt();
        guest_free_kick = in.readInt();
        guest_foul = in.readInt();
        guest_lineOut = in.readInt();
        home_attack = in.readInt();
        guest_attack = in.readInt();
    }

    public static final Creator<MathchStatisInfo> CREATOR = new Creator<MathchStatisInfo>() {
        @Override
        public MathchStatisInfo createFromParcel(Parcel in) {
            return new MathchStatisInfo(in);
        }

        @Override
        public MathchStatisInfo[] newArray(int size) {
            return new MathchStatisInfo[size];
        }
    };

    public int getHome_attack() {
        return home_attack;
    }

    public void setHome_attack(int home_attack) {
        this.home_attack = home_attack;
    }

    public int getGuest_attack() {
        return guest_attack;
    }

    public void setGuest_attack(int guest_attack) {
        this.guest_attack = guest_attack;
    }

    public int getHome_score() {
        return home_score;
    }

    public void setHome_score(int home_score) {
        this.home_score = home_score;
    }

    public int getHome_rc() {
        return home_rc;
    }

    public void setHome_rc(int home_rc) {
        this.home_rc = home_rc;
    }

    public int getHome_yc() {
        return home_yc;
    }

    public void setHome_yc(int home_yc) {
        this.home_yc = home_yc;
    }

    public int getHome_shoot_correct() {
        return home_shoot_correct;
    }

    public void setHome_shoot_correct(int home_shoot_correct) {
        this.home_shoot_correct = home_shoot_correct;
    }

    public int getHome_danger() {
        return home_danger;
    }

    public void setHome_danger(int home_danger) {
        this.home_danger = home_danger;
    }

    public int getHome_corner() {
        return home_corner;
    }

    public void setHome_corner(int home_corner) {
        this.home_corner = home_corner;
    }

    public int getHome_shoot_miss() {
        return home_shoot_miss;
    }

    public void setHome_shoot_miss(int home_shoot_miss) {
        this.home_shoot_miss = home_shoot_miss;
    }

    public int getGuest_score() {
        return guest_score;
    }

    public void setGuest_score(int guest_score) {
        this.guest_score = guest_score;
    }

    public int getGuest_rc() {
        return guest_rc;
    }

    public void setGuest_rc(int guest_rc) {
        this.guest_rc = guest_rc;
    }

    public int getGuest_danger() {
        return guest_danger;
    }

    public void setGuest_danger(int guest_danger) {
        this.guest_danger = guest_danger;
    }

    public int getGuest_yc() {
        return guest_yc;
    }

    public void setGuest_yc(int guest_yc) {
        this.guest_yc = guest_yc;
    }

    public int getGuest_corner() {
        return guest_corner;
    }

    public void setGuest_corner(int guest_corner) {
        this.guest_corner = guest_corner;
    }

    public int getGuest_shoot_correct() {
        return guest_shoot_correct;
    }

    public void setGuest_shoot_correct(int guest_shoot_correct) {
        this.guest_shoot_correct = guest_shoot_correct;
    }

    public int getGuest_shoot_miss() {
        return guest_shoot_miss;
    }

    public void setGuest_shoot_miss(int guest_shoot_miss) {
        this.guest_shoot_miss = guest_shoot_miss;
    }

    public int getHome_rescue() {
        return home_rescue;
    }

    public void setHome_rescue(int home_rescue) {
        this.home_rescue = home_rescue;
    }

    public int getHome_away() {
        return home_away;
    }

    public void setHome_away(int home_away) {
        this.home_away = home_away;
    }

    public int getHome_free_kick() {
        return home_free_kick;
    }

    public void setHome_free_kick(int home_free_kick) {
        this.home_free_kick = home_free_kick;
    }

    public int getGuest_rescue() {
        return guest_rescue;
    }

    public void setGuest_rescue(int guest_rescue) {
        this.guest_rescue = guest_rescue;
    }

    public int getGuest_away() {
        return guest_away;
    }

    public void setGuest_away(int guest_away) {
        this.guest_away = guest_away;
    }

    public int getGuest_free_kick() {
        return guest_free_kick;
    }

    public void setGuest_free_kick(int guest_free_kick) {
        this.guest_free_kick = guest_free_kick;
    }

    public int getHome_shoot_door() {
        return home_shoot_door;
    }

    public void setHome_shoot_door(int home_shoot_door) {
        this.home_shoot_door = home_shoot_door;
    }

    public int getGuest_shoot_door() {
        return guest_shoot_door;
    }

    public void setGuest_shoot_door(int guest_shoot_door) {
        this.guest_shoot_door = guest_shoot_door;
    }

    public int getHome_half_score() {
        return home_half_score;
    }

    public void setHome_half_score(int home_half_score) {
        this.home_half_score = home_half_score;
    }

    public int getGuest_half_score() {
        return guest_half_score;
    }

    public void setGuest_half_score(int guest_half_score) {
        this.guest_half_score = guest_half_score;
    }

    public int getHome_foul() {
        return home_foul;
    }

    public void setHome_foul(int home_foul) {
        this.home_foul = home_foul;
    }

    public int getHome_lineOut() {
        return home_lineOut;
    }

    public void setHome_lineOut(int home_lineOut) {
        this.home_lineOut = home_lineOut;
    }

    public int getGuest_lineOut() {
        return guest_lineOut;
    }

    public void setGuest_lineOut(int guest_lineOut) {
        this.guest_lineOut = guest_lineOut;
    }

    public int getGuest_foul() {
        return guest_foul;
    }

    public void setGuest_foul(int guest_foul) {
        this.guest_foul = guest_foul;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(home_score);
        parcel.writeInt(home_half_score);
        parcel.writeInt(home_rc);
        parcel.writeInt(home_yc);
        parcel.writeInt(home_corner);
        parcel.writeInt(home_danger);
        parcel.writeInt(home_shoot_correct);
        parcel.writeInt(home_shoot_miss);
        parcel.writeInt(home_shoot_door);
        parcel.writeInt(home_rescue);
        parcel.writeInt(home_away);
        parcel.writeInt(home_free_kick);
        parcel.writeInt(home_foul);
        parcel.writeInt(home_lineOut);
        parcel.writeInt(guest_score);
        parcel.writeInt(guest_half_score);
        parcel.writeInt(guest_rc);
        parcel.writeInt(guest_yc);
        parcel.writeInt(guest_corner);
        parcel.writeInt(guest_danger);
        parcel.writeInt(guest_shoot_correct);
        parcel.writeInt(guest_shoot_miss);
        parcel.writeInt(guest_shoot_door);
        parcel.writeInt(guest_rescue);
        parcel.writeInt(guest_away);
        parcel.writeInt(guest_free_kick);
        parcel.writeInt(guest_foul);
        parcel.writeInt(guest_lineOut);
        parcel.writeInt(home_attack);
        parcel.writeInt(guest_attack);
    }
}
