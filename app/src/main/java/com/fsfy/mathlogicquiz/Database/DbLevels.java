package com.fsfy.mathlogicquiz.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "levels")
public class DbLevels {

    @PrimaryKey(autoGenerate = true)
    private int mid;

    @ColumnInfo(name = "name")
    private String Name;

    @ColumnInfo(name = "image")
    private String Image;

    @ColumnInfo(name = "solution")
    private String Solution;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getSolution() {
        return Solution;
    }

    public void setSolution(String solution) {
        Solution = solution;
    }
}
