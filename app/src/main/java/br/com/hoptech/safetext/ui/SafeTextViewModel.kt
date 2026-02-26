package br.com.hoptech.safetext.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.hoptech.safetext.SafeTextApplication
import br.com.hoptech.safetext.crypto.CryptoEngine
import br.com.hoptech.safetext.crypto.WordListProvider
import br.com.hoptech.safetext.data.PasskeyEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val selectedPasskey: PasskeyEntity? = null,
    val encryptInput: String = "",
    val encryptOutput: String = "",
    val decryptInput: String = "",
    val decryptOutput: String = "",
    val feedbackMessage: String = "",
    val showPasskeySheet: Boolean = false,
    val newPasskeyLabel: String = "",
    val newPasskeyValue: String = "",
)

class SafeTextViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as SafeTextApplication
    private val repository = app.repository
    private val wordListProvider = WordListProvider(application)

    val passkeys: StateFlow<List<PasskeyEntity>> = repository.passkeys
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun selectPasskey(passkey: PasskeyEntity) {
        _uiState.update { it.copy(selectedPasskey = passkey) }
    }

    fun onEncryptInputChanged(text: String) {
        _uiState.update { it.copy(encryptInput = text) }
    }

    fun onDecryptInputChanged(text: String) {
        _uiState.update { it.copy(decryptInput = text) }
    }

    fun encrypt(): String? {
        val state = _uiState.value
        val passkey = state.selectedPasskey
        if (passkey == null) {
            _uiState.update { it.copy(feedbackMessage = "select_passkey_first") }
            return null
        }
        if (state.encryptInput.isBlank()) {
            _uiState.update { it.copy(feedbackMessage = "enter_message_first") }
            return null
        }
        return try {
            val encrypted = CryptoEngine.encrypt(state.encryptInput, passkey.passkey)
            _uiState.update {
                it.copy(encryptOutput = encrypted, feedbackMessage = "encrypted_copied")
            }
            encrypted
        } catch (e: Exception) {
            _uiState.update { it.copy(feedbackMessage = "encryption_failed") }
            null
        }
    }

    fun decrypt() {
        val state = _uiState.value
        val passkey = state.selectedPasskey
        if (passkey == null) {
            _uiState.update { it.copy(feedbackMessage = "select_passkey_first") }
            return
        }
        if (state.decryptInput.isBlank()) {
            _uiState.update { it.copy(feedbackMessage = "paste_message_first") }
            return
        }
        try {
            val decrypted = CryptoEngine.decrypt(state.decryptInput.trim(), passkey.passkey)
            _uiState.update { it.copy(decryptOutput = decrypted, feedbackMessage = "decrypted_ok") }
        } catch (_: Exception) {
            _uiState.update {
                it.copy(decryptOutput = "", feedbackMessage = "decryption_failed")
            }
        }
    }

    fun clearFeedback() {
        _uiState.update { it.copy(feedbackMessage = "") }
    }

    // Passkey sheet actions

    fun showPasskeySheet() {
        _uiState.update { it.copy(showPasskeySheet = true) }
    }

    fun hidePasskeySheet() {
        _uiState.update {
            it.copy(showPasskeySheet = false, newPasskeyLabel = "", newPasskeyValue = "")
        }
    }

    fun onNewPasskeyLabelChanged(text: String) {
        _uiState.update { it.copy(newPasskeyLabel = text) }
    }

    fun onNewPasskeyValueChanged(text: String) {
        _uiState.update { it.copy(newPasskeyValue = text) }
    }

    fun suggestPasskey() {
        val suggestion = wordListProvider.generate()
        _uiState.update { it.copy(newPasskeyValue = suggestion) }
    }

    fun addPasskey() {
        val state = _uiState.value
        if (state.newPasskeyLabel.isBlank() || state.newPasskeyValue.isBlank()) return
        viewModelScope.launch {
            repository.add(state.newPasskeyLabel.trim(), state.newPasskeyValue.trim())
            _uiState.update { it.copy(newPasskeyLabel = "", newPasskeyValue = "") }
        }
    }

    fun deletePasskey(entity: PasskeyEntity) {
        viewModelScope.launch {
            repository.delete(entity)
            _uiState.update {
                if (it.selectedPasskey?.id == entity.id) it.copy(selectedPasskey = null) else it
            }
        }
    }
}
