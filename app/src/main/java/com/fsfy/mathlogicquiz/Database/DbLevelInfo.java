package com.fsfy.mathlogicquiz.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "levelinfo")
public class DbLevelInfo {

    @PrimaryKey(autoGenerate = true)
    private int mid;

    @ColumnInfo(name = "levelid")
    private int LevelId;

    @ColumnInfo(name = "durum")
    private boolean durum; //0:kapalı 1:açık

    @ColumnInfo(name = "star_count")
    private int starCount;

    @ColumnInfo(name = "first_find_count")
    private int firstFindCount;

    @ColumnInfo(name = "total_trial_count")
    private int totalTrialCount;

    @ColumnInfo(name = "heart")
    private int heart;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getLevelId() {
        return LevelId;
    }

    public void setLevelId(int levelId) {
        LevelId = levelId;
    }

    public boolean isDurum() {
        return durum;
    }

    public void setDurum(boolean durum) {
        this.durum = durum;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getFirstFindCount() {
        return firstFindCount;
    }

    public void setFirstFindCount(int firstFindCount) {
        this.firstFindCount = firstFindCount;
    }

    public int getTotalTrialCount() {
        return totalTrialCount;
    }

    public void setTotalTrialCount(int totalTrialCount) {
        this.totalTrialCount = totalTrialCount;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }
}
