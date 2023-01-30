package com.devdaniel.marvelapp.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.domain.model.InfoComics
import com.devdaniel.marvelapp.ui.model.InfoCharacter
import com.devdaniel.marvelapp.ui.theme.Typography
import com.devdaniel.marvelapp.ui.theme.black_1C1B1F
import com.devdaniel.marvelapp.ui.theme.purple_4F378B
import com.devdaniel.marvelapp.ui.theme.rose_CCC2DC
import com.devdaniel.marvelapp.ui.theme.white

private val size_10Dp = 10.dp
private val size_12Dp = 12.dp
private val size_16Dp = 16.dp
private val size_36Dp = 36.dp
private const val ZERO_VALUE = 0
private const val ALPHA_03 = 0.35f

@Composable
fun CharacterDetail(
    characterDetailViewModel: CharacterDetailViewModel,
    infoCharacter: InfoCharacter?,
    onBackPress: () -> Unit,
    navigation: (item: InfoComics) -> Unit
) {
    val characterId = infoCharacter?.id ?: ZERO_VALUE
    LaunchedEffect(key1 = characterId) {
        characterDetailViewModel.getComicsCharacterDetail(characterId)
    }
    val comics = characterDetailViewModel.comicCharacterDetailState.collectAsState()

    if (comics.value.isLoading) {
        LoadingScreen()
    }

    comics.value.errorMessage?.let { message ->
        if (comics.value.isError) {
            ErrorScreen(message)
        }
    }

    comics.value.data?.let { data ->
        CharacterDetailScreen(data.infoComics, onBackPress, navigation)
    }
}

@Composable
private fun CharacterDetailScreen(
    comics: List<InfoComics>,
    onBackPress: () -> Unit,
    navigation: (item: InfoComics) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Back(onBackPress)
            Text(
                text = stringResource(id = R.string.title_character_comics),
                style = Typography.h4.copy(color = MaterialTheme.colors.primaryVariant),
                modifier = Modifier.padding(start = size_12Dp, end = size_12Dp)
            )
        }
        CharacterInfo(comics, navigation)
    }
}

@Composable
fun CharacterInfo(comics: List<InfoComics>, navigation: (item: InfoComics) -> Unit) {
    Column(modifier = Modifier.background(color = black_1C1B1F)) {
        Appearances(comics, navigation)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Appearances(comics: List<InfoComics>, navigation: (item: InfoComics) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(comics) { item: InfoComics ->
            Card(
                border = BorderStroke(4.dp, color = black_1C1B1F),
                onClick = {
                          navigation.invoke(item)
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.thumbnail)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.title_character_content_description),
                        placeholder = painterResource(R.drawable.ic_broken_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("comicImage"),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(size_10Dp)) {
                        Text(
                            text = item.titleComic,
                            style = MaterialTheme.typography.body1,
                            color = rose_CCC2DC,
                            modifier = Modifier.testTag("comicName")
                        )
                        Text(
                            text = manageDescription(item.description.orEmpty()),
                            style = MaterialTheme.typography.body1.copy(fontSize = 12.sp),
                            color = white
                        )
                    }
                }
            }
        }
    })
}

private fun manageDescription(description: String): String {
    return if (description.isNotEmpty() && description.count() >= 20) {
        description.take(20).plus("...")
    } else {
        description
    }
}

@Composable
private fun Back(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = size_16Dp, vertical = size_10Dp)
            .size(size_36Dp)
            .background(
                color = purple_4F378B.copy(alpha = ALPHA_03),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            tint = white,
            contentDescription = stringResource(R.string.title_character_back_content_description)
        )
    }
}

@Composable
fun LoadingScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ironman_loader))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp)
    ) {
        LottieAnimation(composition)
    }
}

@Composable
fun ErrorScreen(errorMessage: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(id = errorMessage),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = Typography.h4
        )
        Spacer(modifier = Modifier.height(size_36Dp))
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.hulk_angry),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(
                R.string.title_character_error_image_content_description
            )
        )
    }
}

@Preview(
    "default",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.CUSTOM_DEVICE
)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CharacterDetailPreview() {
    MaterialTheme {
        val comics = listOf(
            InfoComics(
                idComic = 123,
                description = "Test",
                titleComic = "Iron Man",
                "image"
            ),
            InfoComics(
                idComic = 123,
                description = "Test",
                titleComic = "Batman",
                "image"
            ),
            InfoComics(
                idComic = 123,
                description = "Test",
                titleComic = "Superman",
                "image"
            )
        )
        CharacterDetailScreen(comics, {}, {})
    }
}

@Preview(
    "default",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.CUSTOM_DEVICE
)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen(errorMessage = R.string.not_found_error)
}

@Device
object Devices {
    const val CUSTOM_DEVICE =
        "spec:id=reference_phone,shape=Normal,width=392,height=778,unit=dp,dpi=400"
}
