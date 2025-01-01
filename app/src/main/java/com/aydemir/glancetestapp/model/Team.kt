package com.aydemir.glancetestapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Teams")
data class Team(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "position")
    val position: Int = 0,
    @ColumnInfo(name = "selected")
    var selected: Boolean = false,
    @ColumnInfo(name = "point")
    var point: Int = 0,
)
