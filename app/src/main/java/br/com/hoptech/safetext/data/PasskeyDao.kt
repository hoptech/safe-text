package br.com.hoptech.safetext.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PasskeyDao {

    @Query("SELECT * FROM passkeys ORDER BY label ASC")
    fun getAll(): Flow<List<PasskeyEntity>>

    @Insert
    suspend fun insert(passkey: PasskeyEntity): Long

    @Delete
    suspend fun delete(passkey: PasskeyEntity)
}
