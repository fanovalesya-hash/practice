package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun HistoryDetailScreen(
    calculationId: Long,
    viewModel: DepositViewModel,
    onBackClick: () -> Unit
) {
    // Загружаем данные сразу при открытии экрана
    LaunchedEffect(calculationId) {
        viewModel.loadCalculationById(calculationId)
    }

    val calculation = viewModel.selectedCalculation

    // Пока данные грузятся, показываем индикатор
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
        // Шапка
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
                Icon(
                    androidx.compose.material.icons.Icons.Default.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        }

        Text(
            text = "Дата: ${formatDate(calculation.calculationDate)}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Карточка с полной информацией
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailRow("Стартовый взнос", String.format("%.2f ₽", calculation.initialAmount))
                DetailRow("Срок (мес.)", calculation.periodMonths.toString())
                DetailRow("Ставка (%)", calculation.interestRate.toString())
                DetailRow("Ежемес. пополнение", calculation.monthlyTopUp?.let { String.format("%.2f ₽", it) } ?: "Не указано")

                Divider()

                DetailRow("Итоговая сумма", String.format("%.2f ₽", calculation.finalAmount), isBold = true)
                DetailRow("Начисленные проценты", String.format("%.2f ₽", calculation.interestEarned), isBold = true)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.End
        )
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
}