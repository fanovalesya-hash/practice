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
import ci.nsu.mobile.main.ui.HistoryDetailScreen
import ci.nsu.mobile.main.ui.HistoryScreen
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

    // ✅ СОЗДАЕМ VIEWMODEL ОДИН РАЗ ЗДЕСЬ
    val viewModel: DepositViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Main) {

        composable(Screen.Main) {
            MainScreen(
                onCalculateClick = { navController.navigate(Screen.Step1) },
                onHistoryClick = { navController.navigate(Screen.History) },
                onExitClick = { (context as? Activity)?.finish() }
            )
        }

        // ✅ ПЕРЕДАЕМ ТОТ ЖЕ viewModel
        composable(Screen.Step1) {
            Step1Screen(
                viewModel = viewModel,
                onBackClick = { navController.navigate(Screen.Main) { popUpTo(0) } },
                onNextClick = {
                    if (viewModel.initialAmount.isNotBlank() && viewModel.periodMonths.isNotBlank()) {
                        navController.navigate(Screen.Step2)
                    } else {
                        viewModel.errorMessage = "Заполните все поля!"
                    }
                }
            )
        }

        // ✅ ПЕРЕДАЕМ ТОТ ЖЕ viewModel
        composable(Screen.Step2) {
            Step2Screen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onCalculateClick = {
                    viewModel.calculateResult()
                    if (viewModel.errorMessage == null) {
                        navController.navigate(Screen.Result)
                    }
                }
            )
        }

        // ✅ ПЕРЕДАЕМ ТОТ ЖЕ viewModel
        composable(Screen.Result) {
            ResultScreen(
                viewModel = viewModel,
                onToStartClick = {
                    navController.navigate(Screen.Main) { popUpTo(0) { inclusive = true } }
                    viewModel.errorMessage = null
                },
                onSaveClick = {
                    viewModel.saveCalculation()
                    navController.navigate(Screen.Main) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        // История и детали (тоже передаем viewModel)
        composable(Screen.History) {
            HistoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onCalculationClick = { id -> navController.navigate("history_detail/$id") }
            )
        }

        composable(Screen.HistoryDetail) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            if (id != null) {
                HistoryDetailScreen(
                    calculationId = id,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
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