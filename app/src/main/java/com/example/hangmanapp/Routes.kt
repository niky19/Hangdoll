package com.example.hangmanapp

sealed class Routes(val route: String) {

    object SplashScreen : Routes("splash_screen")
    object MenuScreen : Routes("menu_screen")
    object GameScreen : Routes("game_screen")
    object ResultScreen : Routes("result_screen")

}
