package com.example.hangmanapp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = "animation"
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        navController.popBackStack()
        navController.navigate(Routes.MenuScreen.route)
    }
    Splash(alphaAnim.value)
}

@Composable
fun Splash(alphaAnim: Float) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            alpha = alphaAnim,
            modifier = Modifier.size(220.dp) // Change the value to your desired size
        )
        AnimatedText(
            text = "hangdoll.",
            fontSize = 28.sp,
            fontFamily = FontFamily(Font(resId = R.font.itim_regular))
        )
    }
}

@Composable
fun AnimatedText(text: String, fontSize: TextUnit, fontFamily: FontFamily) {
    var index by remember { mutableStateOf(0) }

    val animatedIndex by animateIntAsState(
        targetValue = index,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = ""
    )

    LaunchedEffect(key1 = text) {
        while (index < text.length) {
            delay(100) // delay between each character
            index++
        }
    }

    Text(text = text.substring(0, animatedIndex), fontFamily = fontFamily, fontSize = fontSize)
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    Splash(1f)
}