package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.viewmodel.DepositViewModel


@Composable
fun ResultScreen(
    viewModel: DepositViewModel,
    onToStartClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Результат расчёта",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        // Карточка с результатами
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                ResultRow("Стартовый взнос:", formatMoney(viewModel.initialAmount.toDoubleOrNull() ?: 0.0))
                ResultRow("Срок вклада:", "${viewModel.periodMonths} мес.")
                ResultRow("Ставка:", "${viewModel.selectedRate?.toInt()}%")

                // Ежемесячное пополнение (может быть пустым)
                val topUpVal = viewModel.monthlyTopUp.toDoubleOrNull()
                ResultRow("Ежемес. пополнение:", if (topUpVal != null) formatMoney(topUpVal) else "Не указано")

                Divider()

                // Итоговые суммы (выделяем жирным)
                ResultRow(
                    "Итоговая сумма:",
                    formatMoney(viewModel.resultFinalAmount ?: 0.0),
                    isBold = true,
                    color = MaterialTheme.colorScheme.primary
                )
                ResultRow(
                    "Начислено %:",
                    formatMoney(viewModel.resultTotalInterest ?: 0.0),
                    isBold = true,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // Ошибка (на всякий случай)
        viewModel.errorMessage?.let { error ->
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = onToStartClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }
            Button(
                onClick = onSaveClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Сохранить")
            }
        }
    }
}

// Компонент для одной строки результата
@Composable
private fun ResultRow(label: String, value: String, isBold: Boolean = false, color: androidx.compose.ui.graphics.Color? = null) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = color ?: MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        )
    }
}