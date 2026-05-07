package ci.nsu.mobile.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.model.DepositCalculation
import ci.nsu.mobile.main.viewmodel.DepositViewModel

@Composable
fun HistoryScreen(
    viewModel: DepositViewModel,
    onBackClick: () -> Unit,
    onCalculationClick: (Long) -> Unit
) {
    // Загружаем историю при первом показе экрана
    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Заголовок + кнопка "Назад"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "История расчётов",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Если история пуста
        if (viewModel.historyList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "История пуста — сделайте первый расчёт!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Список расчётов
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.historyList) { calculation ->
                    HistoryItem(
                        calculation = calculation,
                        onClick = { onCalculationClick(calculation.id) }
                    )
                }
            }
        }
    }
}

// Элемент списка: одна карточка с краткой информацией
@Composable
private fun HistoryItem(
    calculation: DepositCalculation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Дата расчёта
            Text(
                text = formatDate(calculation.calculationDate, withTime = true),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Сумма и срок
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Взнос: ${formatMoney(calculation.initialAmount)}")
                    Text("Срок: ${calculation.periodMonths} мес.")
                }
                // Итоговая сумма (выделена)
                Text(
                    text = formatMoney(calculation.finalAmount),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
        }
    }
}