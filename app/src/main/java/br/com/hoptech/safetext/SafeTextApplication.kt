package br.com.hoptech.safetext

import android.app.Application
import androidx.room.Room
import br.com.hoptech.safetext.data.PasskeyRepository
import br.com.hoptech.safetext.data.SafeTextDatabase

class SafeTextApplication : Application() {

    lateinit var database: SafeTextDatabase
        private set

    lateinit var repository: PasskeyRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            SafeTextDatabase::class.java,
            "safetext.db"
        ).build()
        repository = PasskeyRepository(database.passkeyDao())
    }
}
