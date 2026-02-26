package br.com.hoptech.safetext.ui.decrypt

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.hoptech.safetext.R

@Composable
fun DecryptTab(
    input: String,
    output: String,
    onInputChanged: (String) -> Unit,
    onDecrypt: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = onInputChanged,
            label = { Text(stringResource(R.string.message_to_decrypt)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onDecrypt,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.decrypt))
        }

        if (output.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.decrypted_output),
                style = MaterialTheme.typography.labelMedium,
            )
            Spacer(Modifier.height(4.dp))
            SelectionContainer {
                Text(
                    text = output,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
