package com.example.hangmanapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun ResultScreen(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val result = backStackEntry?.arguments?.getString("result") ?: ""
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title, button) = createRefs()

        Text(
            text = result,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Button(
            onClick = { navController.navigate(Routes.MenuScreen.route) },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(title.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text(text = "Back to Menu")
        }
    }
}