package com.example.hangmanapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun ResultScreen(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val result = backStackEntry?.arguments?.getString("result") ?: ""
    val difficulty = backStackEntry?.arguments?.getString("difficulty") ?: ""
    val secretWord = backStackEntry?.arguments?.getString("secretword") ?: ""

    val resultBackgroundImage = painterResource(id = R.drawable.resultbackground)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = resultBackgroundImage,
            contentDescription = "Result Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (winOrLose, button, playAgainButton, word) = createRefs()

            Text(text = result, modifier = Modifier.constrainAs(winOrLose) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(word.top)
            })

            Text(text = "The secret word was $secretWord", modifier = Modifier.constrainAs(word) {
                top.linkTo(winOrLose.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(button.top)
            })

            Button(onClick = { navController.navigate(Routes.MenuScreen.route) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(word.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(playAgainButton.top)
                    }
                    .padding(top = 8.dp)
                    .background(doll_blue)
                    .width(150.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )) {
                Text(
                    text = "Back to Menu",
                    fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                    fontSize = 16.sp
                )
            }

            Button(onClick = { navController.navigate("${Routes.GameScreen.route}/$difficulty") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .constrainAs(playAgainButton) {
                        top.linkTo(button.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = 8.dp)
                    .background(dark_text)
                    .width(150.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )) {
                Text(
                    text = "Play Again",
                    fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun ResultScreenPreview() {
    ResultScreen(navController = NavController(LocalContext.current))
}


