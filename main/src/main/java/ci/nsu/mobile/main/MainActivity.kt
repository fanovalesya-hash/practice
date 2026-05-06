package ci.nsu.mobile.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.MainScreen
import ci.nsu.mobile.main.ui.ResultScreen
import ci.nsu.mobile.main.ui.Screen
import ci.nsu.mobile.main.ui.Step1Screen
import ci.nsu.mobile.main.ui.Step2Screen
import ci.nsu.mobile.main.ui.theme.DepositCalculatorTheme
import ci.nsu.mobile.main.viewmodel.DepositViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DepositCalculatorTheme {
                DepositApp()
            }
        }
    }
}

@Composable
fun DepositApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Screen.Main) {

        composable(Screen.Main) {
            MainScreen(
                onCalculateClick = { navController.navigate(Screen.Step1) },
                onHistoryClick = { navController.navigate(Screen.History) },
                onExitClick = { (context as? Activity)?.finish() }
            )
        }

        composable(Screen.Step1) {
            val viewModel: DepositViewModel = viewModel()
            Step1Screen(
                viewModel = viewModel,
                onBackClick = { navController.navigate(Screen.Main) { popUpTo(0) } },
                onNextClick = {
                    if (viewModel.initialAmount.isNotBlank() && viewModel.periodMonths.isNotBlank()) {
                        navController.navigate(Screen.Step2)
                    } else {
                        viewModel.setError("Заполните все поля!")
                    }
                }
            )
        }
        // Экран "Шаг 2"
        composable(Screen.Step2) {
            val viewModel: DepositViewModel = viewModel()
            Step2Screen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }, // Возврат на Step1
                onCalculateClick = {
                    viewModel.calculateResult() // Запускаем расчёт
                    if (viewModel.errorMessage == null) {
                        navController.navigate(Screen.Result) // Переходим к результату только если нет ошибок
                    }
                }
            )
        }
        composable(Screen.Result) {
            val viewModel: DepositViewModel = viewModel()
            ResultScreen(
                viewModel = viewModel,
                onToStartClick = {
                    // Идём в начало и очищаем всю историю навигации
                    navController.navigate(Screen.Main) {
                        popUpTo(0) { inclusive = true }
                    }
                    // Сбрасываем ошибку при переходе
                    viewModel.errorMessage = null
                },
                onSaveClick = {
                    viewModel.saveCalculation()
                    // Можно показать сообщение об успехе (пока просто переходим в начало)
                    navController.navigate(Screen.Main) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DepositCalculatorTheme {
        MainScreen(
            onCalculateClick = {},
            onHistoryClick = {},
            onExitClick = {}
        )
    }
}