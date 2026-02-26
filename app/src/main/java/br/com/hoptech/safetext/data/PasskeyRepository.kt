package br.com.hoptech.safetext.data

import kotlinx.coroutines.flow.Flow

class PasskeyRepository(private val dao: PasskeyDao) {

    val passkeys: Flow<List<PasskeyEntity>> = dao.getAll()

    suspend fun add(label: String, passkey: String): Long {
        return dao.insert(PasskeyEntity(label = label, passkey = passkey))
    }

    suspend fun delete(entity: PasskeyEntity) {
        dao.delete(entity)
    }
}
