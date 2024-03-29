//|---------------------------------------------------------------------------|
//|    CLASS    : Preset.java by Alex Gennero                                 |
//|                                                                           |
//|    PURPOSE  : All of the data that is going to be stored in the presets.  |
//|                                                                           |
//|---------------------------------------------------------------------------|

package com.example.guitarfinal.data;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "presets")
public class Preset {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "preset_name")
    public String presetName;

    @ColumnInfo
    public boolean channel1;

    @ColumnInfo
    public boolean channel2;

    @ColumnInfo
    public boolean channel3;

    @ColumnInfo
    public boolean channel4;

    @ColumnInfo
    public boolean channel5;

    @ColumnInfo
    public boolean channel6;

    @ColumnInfo
    public boolean channel7;

    @ColumnInfo
    public boolean channel8;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] picture;

}
