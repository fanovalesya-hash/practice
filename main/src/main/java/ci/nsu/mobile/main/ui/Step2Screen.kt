package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Screen(
    viewModel: DepositViewModel,
    onBackClick: () -> Unit,
    onCalculateClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Дополнительные параметры", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(24.dp))

        //ВЫБОР СТАВКИ
        val months = viewModel.periodMonths.toIntOrNull()

        // Определяем доступные ставки и текст для отображения
        val (rateOptions, displayText) = when {
            months == null -> listOf("Укажите срок на шаге 1") to "—"
            months < 6 -> listOf("15%") to "15%"
            months in 6..11 -> listOf("10%") to "10%"
            else -> listOf("5%") to "5%"
        }

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = viewModel.selectedRate?.let { "${it.toInt()}%" } ?: displayText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Процентная ставка") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                rateOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.selectedRate = option.removeSuffix("%").toDoubleOrNull()
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //ПОПОЛНЕНИЕ
        OutlinedTextField(
            value = viewModel.monthlyTopUp,
            onValueChange = { viewModel.updateMonthlyTopUp(it) },
            label = { Text("Ежемесячное пополнение (необязательно)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        viewModel.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f)) { Text("Назад") }
            Button(onClick = onCalculateClick, modifier = Modifier.weight(1f)) { Text("Рассчитать") }
        }
    }
}