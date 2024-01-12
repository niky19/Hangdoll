@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hangmanapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController

val doll_green = Color(0xFF405339)
val doll_blue = Color(0xFF7EAFBB)
val dark_text = Color(0xFF412525)
val light_text = Color(0xFFEDEDED)
val doll_lilac = Color(0xFFAF7F99)


@Composable
fun MenuScreen(navController: NavController) {
    var selectedDifficulty by remember { mutableStateOf("") }
    val backgroundImage = painterResource(id = R.drawable.menubackground)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundImage,
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds // This will make the image scale to fill the entire screen
        )
        ConstraintLayout {
            val (spacer, logo, slogan, mode, buttons) = createRefs()
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Transparent)
                .constrainAs(spacer) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(logo.top)
                })
            Image(
                modifier = Modifier
                    .size(175.dp)
                    .padding(top = 16.dp)
                    .constrainAs(logo) {
                        top.linkTo(spacer.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(mode.top)
                    },
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "HangdollLogo",
            )
            Text(text = " hangdoll",
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                textAlign = TextAlign.Center, // This will center align the text
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .constrainAs(slogan) {
                        top.linkTo(logo.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(mode.top)
                    })

            MyDropDownMenu(selectedDifficulty = selectedDifficulty,
                onDifficultySelected = { selectedDifficulty = it },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .constrainAs(mode) {
                        top.linkTo(slogan.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(buttons.top)
                    })

            MyButtons(
                selectedDifficulty = selectedDifficulty, navController = navController,
                modifier = Modifier.constrainAs(buttons) {
                    top.linkTo(mode.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropDownMenu(
    selectedDifficulty: String,
    onDifficultySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val difficulties = listOf("Casual", "Hardcore")

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        OutlinedTextField(
            value = selectedDifficulty,
            onValueChange = {},

            enabled = false,
            readOnly = true,
            modifier = Modifier
                .clickable { expanded = true }
                .width(150.dp)
                .height(65.dp),
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(
                    text = " Game mode   ▽",
                    color = dark_text,
                    fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                    fontSize = 16.sp
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = doll_lilac,

                ),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(150.dp)
        ) {
            difficulties.forEach { difficulty ->
                DropdownMenuItem(text = {
                    Text(
                        text = difficulty, color = Color.Black, textAlign = TextAlign.Center
                    )
                }, onClick = {
                    expanded = false
                    onDifficultySelected(difficulty)
                })
            }
        }
    }
}

@Composable
fun MyButtons(navController: NavController, modifier: Modifier, selectedDifficulty: String) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    HowToPlayDialiog(show = showDialog, onDismiss = { showDialog = false })
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (selectedDifficulty.isNotEmpty() && selectedDifficulty != "") {
                    navController.navigate("game_screen/$selectedDifficulty")
                } else Toast.makeText(context, "Please select a game mode", Toast.LENGTH_SHORT)
                    .show()
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(top = 8.dp) // Reduced padding here
                .background(Color.Transparent)
                .width(150.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = doll_blue,
            )
        ) {
            Text(
                text = "Play game",
                fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                fontSize = 16.sp,
                color = dark_text
            )
        }
        Button(
            onClick = { showDialog = true },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(top = 8.dp) // Reduced padding here
                .background(Color.Transparent)
                .width(150.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = doll_green, // Set the button color here
            )
        ) {
            Text(
                text = "How to play",
                fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                fontSize = 16.sp,
                color = light_text
            )
        }
    }
}

@Composable
fun HowToPlayDialiog(show: Boolean, onDismiss: () -> Unit) {
    val dialogBackgroundImage = painterResource(id = R.drawable.dialogbackground)
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Image(
                painter = dialogBackgroundImage,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds // This will make the image scale to fill the entire screen
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .height(300.dp)
            ) {

                Text(
                    text = """
                    Discover the hidden word before the drawing of the hang doll man is completed!
                    
                    Instructions:
                    ♡ Choose the letter with the buttons below.
                    ♡ Each correct letter will reveal its position in the word.
                    ♡ Each incorrect letter will advance the hangman drawing.
                    
                    Clues:
                    ♡ You have 2 clues each game. Press the icon to reveal a letter. Use them wisely!
                    
                    Game Modes:
                    ♡ Casual: no time, clues available and short, common words
                    ♡ Hardcore: Limited time, no clues, and medium length words.
                    
                    All words are related to retro and vintage culture from the 1920s to 1960s.
                """.trimIndent(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(resId = R.font.itim_regular)),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .fillMaxWidth()


                )


            }
        }
    }
}

@Preview
@Composable
fun PreviewMenuScreen() {
    MenuScreen(navController = NavController(LocalContext.current))
}

