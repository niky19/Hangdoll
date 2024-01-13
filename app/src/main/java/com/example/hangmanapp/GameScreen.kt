package com.example.hangmanapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.delay
import java.util.Locale


@Composable
fun GameScreen(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val difficulty = backStackEntry?.arguments?.getString("difficulty") ?: ""
    var result = backStackEntry?.arguments?.getString("result") ?: ""
    var secretWord by remember { mutableStateOf("") }
    var hashedSecretWord by remember { mutableStateOf("") }
    var usedLetters by remember { mutableStateOf("") }
    var clickedLetter by remember { mutableStateOf("") }
    var imageChanger by remember { mutableStateOf(0) }
    val clickedLetters by remember { mutableStateOf(mutableStateListOf<String>()) }
    var clueCount by remember { mutableStateOf(0) }
    var remainingSeconds by remember { mutableStateOf(30.toLong()) }

    var hangdollImage by remember {
        mutableStateOf(
            listOf(
                R.drawable.hang0,
                R.drawable.hang1,
                R.drawable.hang2,
                R.drawable.hang3,
                R.drawable.hang4,
                R.drawable.hang5,
                R.drawable.hang6
            )
        )
    }
    var currentImage by remember { mutableStateOf(hangdollImage[0]) }
    var attemptsLeft by remember { mutableStateOf(6) }
    LaunchedEffect(remainingSeconds, difficulty) {
        if (difficulty == "Hardcore") {
            if (remainingSeconds == 0.toLong()) {
                result = "You Lost!"
                navToResultScreen(navController, result, secretWord, difficulty)
            }
            while (remainingSeconds > 0) {
                delay(1000L) // Espera un segundo
                remainingSeconds-- // Disminuye el contador
            }
        }
    }


    LaunchedEffect(difficulty) {
        secretWord = getRandomWord(difficulty)
        hashedSecretWord = hashWord(secretWord)

    }

    LaunchedEffect(clickedLetter, imageChanger) {
        hashedSecretWord = revealLetter(
            clickedLetter,
            secretWord,
            hashedSecretWord,
        )
        clickedLetters.add(clickedLetter)

        if (!secretWord.contains(clickedLetter)) {
            usedLetters += clickedLetter
            attemptsLeft--
            currentImage = hangdollImage[usedLetters.length]
            if (attemptsLeft == 0) {
                result = "You Lost!"
                navToResultScreen(navController, result, secretWord, difficulty)

            }
        }
    }

    LaunchedEffect(hashedSecretWord) {
        if (hashedSecretWord != "" && hashedSecretWord == secretWord) {
            result = "You Won!"
            navToResultScreen(navController, result, secretWord, difficulty)

        }
    }
    val gameBackgroundImage = painterResource(id = R.drawable.gamebackground)

    Box(modifier = Modifier.fillMaxSize()) {
        // Draw the background image
        Image(
            painter = gameBackgroundImage,
            contentDescription = "Game Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (logo, specs, errorCounterText, image, correctLettersBox, keyboardBox, spacer, myButton) = createRefs()
            Text(text = "hangdoll.",
                fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                modifier = Modifier.constrainAs(logo) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Text(text = "Game mode: $difficulty",
                fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                modifier = Modifier.constrainAs(specs) {
                    top.linkTo(logo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(errorCounterText.top)
                })

            Text(text = "Attempts left $attemptsLeft",
                fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                modifier = Modifier.constrainAs(errorCounterText) {
                    top.linkTo(specs.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(image.top)
                })
            if (difficulty == "Hardcore") {
                TimerScreen(timerValue = remainingSeconds)
            }

            Image(painter = painterResource(id = currentImage),
                contentDescription = "Game Image",
                modifier = Modifier
                    .size(250.dp)
                    .constrainAs(image) {
                        top.linkTo(errorCounterText.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(correctLettersBox.top, margin = 16.dp)
                    })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.Transparent)
                    .constrainAs(correctLettersBox) {
                        top.linkTo(image.bottom)
                        start.linkTo(correctLettersBox.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(keyboardBox.top, margin = 16.dp)
                    },
            ) {

                Text(
                    text = hashedSecretWord,
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center,
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(resId = R.font.itim_regular))
                )


            }
            Button(

                onClick = {
                    if (clueCount < 2) {
                        val char = getUnrevealedChar(secretWord, hashedSecretWord)
                        clickedLetter = char
                        clueCount++
                    }
                },
                enabled = difficulty != "Hardcore",
                modifier = Modifier
                    .size(50.dp)
                    .constrainAs(myButton) {
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end, margin = 22.dp)
                    }) {
                Text("\uD83D\uDCA1")
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.Transparent)
                    .constrainAs(keyboardBox) {
                        top.linkTo(correctLettersBox.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    }, contentAlignment = Alignment.Center
            ) {
                Keyboard(onLetterClick = { clickedLetter = it }, clickedLetters)

            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color.Transparent)
                .constrainAs(spacer) {
                    top.linkTo(keyboardBox.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })


        }

    }
}

fun getUnrevealedChar(secretWord: String, hashedSecretWord: String): String {
    println(secretWord)
    println(hashedSecretWord)
    var randomIndex = (0..secretWord.lastIndex).random()

    if (hashedSecretWord[randomIndex] != '_') {
        println(hashedSecretWord[randomIndex])
        return getUnrevealedChar(secretWord, hashedSecretWord)
    }
    return secretWord[randomIndex].toString().lowercase()
}


fun revealLetter(
    clickedLetter: String,
    secretWord: String,
    hashedSecretWord: String,
): String {

    var updatedHashedWord = ""
    for (i in secretWord.indices) {
        if (clickedLetter == secretWord[i].toString().lowercase()) {
            updatedHashedWord += if (i === 0) clickedLetter.uppercase(Locale.ROOT) else clickedLetter
        } else {
            updatedHashedWord += hashedSecretWord[i].toString()
        }
    }
    return updatedHashedWord
}


fun navToResultScreen(
    navController: NavController, ressult: String, secretWord: String, difficulty: String
) {
    navController.popBackStack()
    navController.navigate("result_screen/$ressult/$difficulty/$secretWord")
}


fun getRandomWord(difficulty: String): String {
    val easyWordList = listOf(
        Word("Gloves", "Fashion"),
        Word("Hat", "Fashion"),
        Word("Suit", "Fashion"),
        Word("Jazz", "Music"),
        Word("Jukebox", "Music"),
        Word("Radio", "Music"),
        Word("Cinema", "Entertainment"),
        Word("Circus", "Entertainment"),
        Word("Theatre", "Entertainment"),
        Word("ArtDeco", "Art"),
        Word("Picasso", "Art"),
        Word("Modernism", "Art"),


        )
    val hardWordList = listOf(
        Word("Chanel", "Celebrities"),
        Word("Monroe", "Celebrities"),
        Word("Chaplin", "Celebrities"),
        Word("PinUp", "Fashion"),
        Word("Country", "Music"),
        Word("Rock", "Music"),
        Word("Paparazzi", "Entertainment"),
        Word("Hollywood", "Entertainment"),
        Word("FilmNoir", "Entertainment"),
        Word("Warhol", "Art"),
        Word("Haberdashery", "Jobs"),
        Word("Housewife", "Jobs"),
        Word("Surrealism", "Art"),
    )

    return when (difficulty) {
        "Casual" -> easyWordList.random().name
        "Hardcore" -> hardWordList.random().name
        else -> ""
    }

}

fun hashWord(word: String): String {
    var hashedWord = ""
    for (i in word.indices) {
        hashedWord += "_"
    }
    return hashedWord

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Keyboard(onLetterClick: (String) -> Unit, clickedLetters: MutableList<String>) {
    val myLetters = listOf(
        "a",
        "b",
        "c",
        "d",
        "e",
        "f",
        "g",
        "h",
        "i",
        "j",
        "k",
        "l",
        "m",
        "n",
        "o",
        "p",
        "q",
        "r",
        "s",
        "t",
        "u",
        "v",
        "w",
        "x",
        "y",
        "z"
    )

    FlowRow(
        modifier = Modifier.padding(2.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        myLetters.forEach { letter ->
            Button(
                onClick = {
                    onLetterClick(letter)
                    clickedLetters.add(letter)
                }, modifier = Modifier.padding(2.dp), enabled = !clickedLetters.contains(letter)
            ) {
                Text(
                    text = letter,
                    modifier = Modifier.wrapContentWidth(),
                    fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                    color = Color(0xFFC7BBBB),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TimerScreen(
    timerValue: Long,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (logo, specs, errorCounterText, image, correctLettersBox, keyboardBox, spacer, myButton, text) = createRefs()

        Text(text = timerValue.toString(),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
            modifier = Modifier.constrainAs(text) {
                top.linkTo(correctLettersBox.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(keyboardBox.top)
            })
    }
}


@Preview
@Composable
fun PreviewGameScreen() {
    GameScreen(navController = NavController(LocalContext.current))
}