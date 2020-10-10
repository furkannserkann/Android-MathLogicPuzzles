package com.fsfy.mathlogicquiz.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {DbLevels.class, DbLevelInfo.class}, version = 2)
public abstract class DBase extends RoomDatabase {

    public abstract DbDao dbDao();

}
