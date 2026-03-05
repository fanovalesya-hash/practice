package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var text by remember { mutableStateOf("Green") }
    var btnColor by remember { mutableStateOf(Color.Green) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Название цвета") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val colorName = text.trim().lowercase()

                if (colorName == "red")     btnColor = Color(0xFFFF0000)
                else if (colorName == "orange") btnColor = Color(0xFFFFA500)
                else if (colorName == "yellow") btnColor = Color(0xFFFFFF00)
                else if (colorName == "green")  btnColor = Color(0xFF00FF00)
                else if (colorName == "blue")   btnColor = Color(0xFF0000FF)
                else if (colorName == "indigo") btnColor = Color(0xFF4B0082)
                else if (colorName == "violet") btnColor = Color(0xFFEE82EE)
                else {
                    Log.e("ColorApp", "Такого цвета нет: $text")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = btnColor)
        ) {
            Text("Применить цвет", color = Color.White)
        }

        Spacer(Modifier.height(24.dp))

        Text("Примеры цветов", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorRow("Red",     Color(0xFFFF0000))
            ColorRow("Orange",  Color(0xFFFFA500))
            ColorRow("Yellow",  Color(0xFFFFFF00))
            ColorRow("Green",   Color(0xFF00FF00))
            ColorRow("Blue",    Color(0xFF0000FF))
            ColorRow("Indigo",  Color(0xFF4B0082))
            ColorRow("Violet",  Color(0xFFEE82EE))
        }
    }
}

@Composable
fun ColorRow(name: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = name,
            color = if (color.red + color.green + color.blue > 1.5f) Color.Black else Color.White
        )
    }
}