package br.com.hoptech.safetext.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passkeys")
data class PasskeyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val passkey: String,
)
