package com.devdaniel.marvelapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devdaniel.marvelapp.ui.HomeActivity
import com.devdaniel.marvelapp.ui.theme.MarvelTheme
import com.devdaniel.marvelapp.ui.theme.black_1C1B1F
import java.util.Timer
import kotlin.concurrent.timerTask

@ExperimentalAnimationApi
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelTheme {
                TextChange()
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun TextChange() {
        var text by remember {
            mutableStateOf(original)
        }
        var buttonEnabled by remember {
            mutableStateOf(true)
        }

        val time = 1000

        val blur = remember { Animatable(0f) }

        LaunchedEffect(text) {
            blur.animateTo(30f, tween(time / 2, easing = LinearEasing))
            blur.animateTo(0f, tween(time / 2, easing = LinearEasing))
        }


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            MetaContainer(
                modifier = Modifier
                    .animateContentSize()
                    .clipToBounds()
                    .fillMaxWidth(),
                cutoff = .2f
            ) {
                MetaEntity(
                    modifier = Modifier.fillMaxWidth(),
                    blur = blur.value,
                    metaContent = {
                        AnimatedContent(
                            targetState = text,
                            modifier = Modifier
                                .fillMaxWidth(),
                            transitionSpec = {
                                fadeIn(tween(time, easing = LinearEasing)) + expandVertically(
                                    tween(
                                        time,
                                        easing = LinearEasing
                                    ), expandFrom = Alignment.CenterVertically
                                ) with fadeOut(
                                    tween(
                                        time,
                                        easing = LinearEasing
                                    )
                                ) + shrinkVertically(
                                    tween(
                                        time,
                                        easing = LinearEasing
                                    ), shrinkTowards = Alignment.CenterVertically
                                )
                            }
                        ) { text ->
                            Text(
                                text,
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 50.sp,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.primaryVariant,
                            )
                        }
                    }) {}
            }
            Button(
                border = BorderStroke(2.dp, MaterialTheme.colors.secondary),
                enabled = buttonEnabled,
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = black_1C1B1F
                ),
                onClick = {
                    buttonEnabled = false
                    text = if (text == original) {
                        translated
                    } else {
                        original
                    }
                    startHomeActivity()
                }) {
                Text("Let's go to the party")
            }
        }
    }

    private fun startHomeActivity() {
        Timer().schedule(timerTask {
            startActivity(
                Intent(this@SplashActivity, HomeActivity::class.java)
            )
            finish()
        }, LAUNCH_DELAY)
    }
}

private const val original = "Hello!!!"
private const val translated = "Loading..."
private const val LAUNCH_DELAY = 3000L