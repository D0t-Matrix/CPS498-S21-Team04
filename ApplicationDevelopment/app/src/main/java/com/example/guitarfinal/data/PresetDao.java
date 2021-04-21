//|---------------------------------------------------------------------------|
//|    CLASS    : PresetDao.java by Alex Gennero                              |
//|                                                                           |
//|    PURPOSE  : Marks the class as a Data Access Object                     |
//|                                                                           |
//|    NOTES    : Gets all of the presets and allows to insert or update      |
//|---------------------------------------------------------------------------|


package com.example.guitarfinal.data;


import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Dao;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PresetDao {

    @Query("SELECT * FROM presets")
    List<Preset> getAll();


    @Query("SELECT * FROM presets WHERE preset_name = :name")
    Preset findByName(String name);

    @Query("SELECT * FROM presets WHERE uid = :uid")
    Preset findById(int uid);


    @Insert
    void insertAll(Preset... presets);

    @Update
    void update(Preset preset);

}
