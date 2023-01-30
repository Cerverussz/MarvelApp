package com.devdaniel.marvelapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devdaniel.marvelapp.ui.HomeActivity
import com.devdaniel.marvelapp.ui.theme.MarvelTheme
import com.devdaniel.marvelapp.ui.theme.black_1C1B1F

@ExperimentalAnimationApi
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelTheme {
                NavigateToHome()
            }
        }
    }

    @Composable
    fun NavigateToHome() {
        var buttonEnabled by remember {
            mutableStateOf(true)
        }


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                border = BorderStroke(2.dp, MaterialTheme.colors.secondary),
                enabled = buttonEnabled,
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = black_1C1B1F
                ),
                onClick = {
                    buttonEnabled = false
                    startActivity(
                        Intent(this@SplashActivity, HomeActivity::class.java)
                    )
                    finish()
                }) {
                Text("Let's go to the party")
            }
        }
    }
}