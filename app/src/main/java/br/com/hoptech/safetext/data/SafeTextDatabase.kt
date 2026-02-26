package br.com.hoptech.safetext.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PasskeyEntity::class], version = 1)
abstract class SafeTextDatabase : RoomDatabase() {
    abstract fun passkeyDao(): PasskeyDao
}
