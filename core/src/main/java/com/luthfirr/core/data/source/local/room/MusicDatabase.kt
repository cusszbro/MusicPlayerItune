package com.luthfirr.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luthfirr.core.data.source.local.entity.MusicEntity

@Database(entities = [MusicEntity::class], version = 1, exportSchema = false)
abstract class MusicDatabase: RoomDatabase() {

    abstract fun musicDao(): MusicDao

}