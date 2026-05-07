package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun HistoryDetailScreen(
    calculationId: Long,
    viewModel: DepositViewModel,
    onBackClick: () -> Unit
) {
    // Загружаем данные по ID при открытии экрана
    LaunchedEffect(calculationId) {
        viewModel.loadCalculationById(calculationId)
    }

    val calculation = viewModel.selectedCalculation

    // Пока данные грузятся — показываем индикатор
    if (calculation == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Шапка: заголовок + кнопка "Назад"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Детали расчёта",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
        }

        // Дата расчёта
        Text(
            text = "Дата: ${formatDate(calculation.calculationDate, withTime = true)}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Карточка с полной информацией
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailRow("Стартовый взнос", formatMoney(calculation.initialAmount))
                DetailRow("Срок (мес.)", calculation.periodMonths.toString())
                DetailRow("Ставка (%)", "${calculation.interestRate.toInt()}%")
                DetailRow(
                    "Ежемес. пополнение",
                    calculation.monthlyTopUp?.let { formatMoney(it) } ?: "Не указано"
                )

                DetailRow("Итоговая сумма", formatMoney(calculation.finalAmount), isBold = true)
                DetailRow("Начисленные проценты", formatMoney(calculation.interestEarned), isBold = true)
            }
        }
    }
}

// Вспомогательный компонент для строки "Название — Значение"
@Composable
private fun DetailRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.End
        )
    }
}