package com.example.guitarfinal.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Preset.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PresetDao presetDao();

}
