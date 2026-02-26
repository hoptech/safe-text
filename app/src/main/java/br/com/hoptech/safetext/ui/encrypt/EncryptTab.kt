package br.com.hoptech.safetext.ui.encrypt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import br.com.hoptech.safetext.R

@Composable
fun EncryptTab(
    input: String,
    output: String,
    onInputChanged: (String) -> Unit,
    onEncrypt: () -> String?,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = onInputChanged,
            label = { Text(stringResource(R.string.message_to_encrypt)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                val result = onEncrypt()
                if (result != null) {
                    clipboardManager.setText(AnnotatedString(result))
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.encrypt_and_copy))
        }

        if (output.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.encrypted_output),
                style = MaterialTheme.typography.labelMedium,
            )
            Spacer(Modifier.height(4.dp))
            SelectionContainer {
                Text(
                    text = output,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
