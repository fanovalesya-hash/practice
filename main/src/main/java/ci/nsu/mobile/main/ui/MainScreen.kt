package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(
    onCalculateClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заголовок приложения
        Text(
            text = "Расчёт вкладов",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(60.dp))

        // Кнопка "Рассчитать"
        Button(
            onClick = onCalculateClick,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Рассчитать", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "История расчётов"
        OutlinedButton(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("История расчётов", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Закрыть приложение"
        TextButton(
            onClick = onExitClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Закрыть приложение", color = MaterialTheme.colorScheme.error)
        }
    }
}