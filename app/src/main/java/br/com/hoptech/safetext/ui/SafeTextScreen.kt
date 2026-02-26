package br.com.hoptech.safetext.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.hoptech.safetext.R
import br.com.hoptech.safetext.ui.components.PasskeySelector
import br.com.hoptech.safetext.ui.decrypt.DecryptTab
import br.com.hoptech.safetext.ui.encrypt.EncryptTab
import br.com.hoptech.safetext.ui.passkey.PasskeySheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafeTextScreen(viewModel: SafeTextViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val passkeys by viewModel.passkeys.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    val tabTitles = listOf(
        stringResource(R.string.tab_encrypt),
        stringResource(R.string.tab_decrypt),
    )

    // Feedback messages mapped to string resources
    val feedbackMessages = mapOf(
        "select_passkey_first" to stringResource(R.string.feedback_select_passkey),
        "enter_message_first" to stringResource(R.string.feedback_enter_message),
        "encrypted_copied" to stringResource(R.string.feedback_encrypted_copied),
        "encryption_failed" to stringResource(R.string.feedback_encryption_failed),
        "paste_message_first" to stringResource(R.string.feedback_paste_message),
        "decrypted_ok" to stringResource(R.string.feedback_decrypted),
        "decryption_failed" to stringResource(R.string.feedback_decryption_failed),
    )

    LaunchedEffect(uiState.feedbackMessage) {
        val key = uiState.feedbackMessage
        if (key.isNotEmpty()) {
            val message = feedbackMessages[key] ?: key
            snackbarHostState.showSnackbar(message)
            viewModel.clearFeedback()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { viewModel.showPasskeySheet() }) {
                        Icon(
                            Icons.Default.Key,
                            contentDescription = stringResource(R.string.manage_passkeys),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PasskeySelector(
                passkeys = passkeys,
                selected = uiState.selectedPasskey,
                onSelect = viewModel::selectPasskey,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (page) {
                    0 -> EncryptTab(
                        input = uiState.encryptInput,
                        output = uiState.encryptOutput,
                        onInputChanged = viewModel::onEncryptInputChanged,
                        onEncrypt = viewModel::encrypt,
                    )
                    1 -> DecryptTab(
                        input = uiState.decryptInput,
                        output = uiState.decryptOutput,
                        onInputChanged = viewModel::onDecryptInputChanged,
                        onDecrypt = viewModel::decrypt,
                    )
                }
            }
        }
    }

    if (uiState.showPasskeySheet) {
        PasskeySheet(
            passkeys = passkeys,
            newLabel = uiState.newPasskeyLabel,
            newValue = uiState.newPasskeyValue,
            onLabelChanged = viewModel::onNewPasskeyLabelChanged,
            onValueChanged = viewModel::onNewPasskeyValueChanged,
            onSuggest = viewModel::suggestPasskey,
            onAdd = viewModel::addPasskey,
            onDelete = viewModel::deletePasskey,
            onDismiss = viewModel::hidePasskeySheet,
        )
    }
}
