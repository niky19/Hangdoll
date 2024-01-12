package com.example.hangmanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hangmanapp.ui.theme.HangmanAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HangmanAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.SplashScreen.route
                    ) {
                        composable(Routes.SplashScreen.route) { SplashScreen(navController) }

                        composable(Routes.MenuScreen.route) { MenuScreen(navController) }
                        composable("game_screen/{difficulty}") { backStackEntry ->
                            val difficulty = backStackEntry.arguments?.getString("difficulty")
                            GameScreen(navController)

                        }
                        composable("result_screen/{result}") { backStackEntry ->
                            val result = backStackEntry.arguments?.getString("result")
                            ResultScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

