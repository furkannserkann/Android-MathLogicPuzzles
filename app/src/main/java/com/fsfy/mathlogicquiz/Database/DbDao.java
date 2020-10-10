package com.fsfy.mathlogicquiz.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DbDao {

    @Insert
    public void addLevels(DbLevels dbLevels);

    @Query("select * from levels")
    public List<DbLevels> getLevels();

    @Query("select * from levels where name=:name limit 1")
    public DbLevels getLevelsByName(String name);

    @Query("select * from levels order by mid desc limit 1")
    public DbLevels getLevelLast();

    @Query("delete from levels")
    public void deleteLevels();

    @Delete
    public void deleteLevels(DbLevels dbLevels);

    @Update
    public void updateLevels(DbLevels dbLevels);

    @Insert
    public void addLevelInfo(DbLevelInfo dbLevelInfo);

    @Query("select * from levelinfo where levelid=:levelId limit 1")
    public DbLevelInfo getLevelInfo(int levelId);

    @Query("select * from levelinfo where durum=1 order by levelid desc limit 1")
    public DbLevelInfo getLevelInfoLast();

    @Update
    public void updateLevelInfo(DbLevelInfo dbLevelInfo);

    @Query("delete from levelinfo")
    public void deleteLevelInfo();
}
