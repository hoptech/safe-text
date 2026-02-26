package br.com.hoptech.safetext.ui.passkey

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.hoptech.safetext.R
import br.com.hoptech.safetext.data.PasskeyEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasskeySheet(
    passkeys: List<PasskeyEntity>,
    newLabel: String,
    newValue: String,
    onLabelChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit,
    onSuggest: () -> Unit,
    onAdd: () -> Unit,
    onDelete: (PasskeyEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
        ) {
            Text(
                text = stringResource(R.string.manage_passkeys),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(16.dp))

            // Add new passkey section
            OutlinedTextField(
                value = newLabel,
                onValueChange = onLabelChanged,
                label = { Text(stringResource(R.string.passkey_label_hint)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = newValue,
                onValueChange = onValueChanged,
                label = { Text(stringResource(R.string.passkey_value_hint)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onSuggest) {
                    Text(stringResource(R.string.suggest))
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = onAdd,
                    enabled = newLabel.isNotBlank() && newValue.isNotBlank(),
                ) {
                    Text(stringResource(R.string.add_passkey))
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            // Existing passkeys list
            if (passkeys.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_passkeys),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            } else {
                LazyColumn {
                    items(passkeys, key = { it.id }) { passkey ->
                        PasskeyListItem(
                            passkey = passkey,
                            onDelete = { onDelete(passkey) },
                        )
                    }
                }
            }
        }
    }
}
