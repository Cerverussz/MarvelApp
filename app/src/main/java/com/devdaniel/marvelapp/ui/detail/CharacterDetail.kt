package com.devdaniel.marvelapp.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.domain.model.CharacterDetail
import com.devdaniel.marvelapp.ui.components.MarvelSurface
import com.devdaniel.marvelapp.ui.model.InfoCharacter
import com.devdaniel.marvelapp.ui.theme.Typography
import com.devdaniel.marvelapp.ui.theme.black_1C1B1F
import com.devdaniel.marvelapp.ui.theme.blue_21005D
import com.devdaniel.marvelapp.ui.theme.blue_86f7fa
import com.devdaniel.marvelapp.ui.theme.purple_49454F
import com.devdaniel.marvelapp.ui.theme.purple_7057f5
import com.devdaniel.marvelapp.ui.theme.red_8C1D18
import com.devdaniel.marvelapp.ui.theme.red_B3261E
import com.devdaniel.marvelapp.ui.theme.rose_E8DEF8
import com.devdaniel.marvelapp.ui.theme.white
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

private val GradientScroll = 100.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val TitleHeightOffset = MinTitleOffset + GradientScroll
private val ExpandedImageSize = 200.dp
private val CollapsedImageSize = 100.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun CharacterDetail(
    characterDetailViewModel: CharacterDetailViewModel,
    infoCharacter: InfoCharacter?,
    onBackPress: () -> Unit
) {
    val characterId = infoCharacter?.id ?: 0
    LaunchedEffect(key1 = characterId) {
        characterDetailViewModel.getCharacterDetail(characterId)
    }
    val characterDetailState = characterDetailViewModel.characterDetailState.collectAsState()

    if (characterDetailState.value.isLoading) {
        LoadingScreen()
    }
    characterDetailState.value.errorMessage?.let { errorMessage ->
        ErrorScreen(errorMessage)
    }

    characterDetailState.value.data?.let { data ->
        CharacterDetailScreen(data, onBackPress)
    } /*?: infoCharacter?.let { CharacterDetailScreen(data = it) }*/
}

@Composable
private fun CharacterDetailScreen(data: CharacterDetail, onBackPress: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        CharacterInfo(data, scroll)
        CharacterTitle(data.name) { scroll.value }
        Image(imageUrl = data.thumbnail) {
            scroll.value
        }
        Back(onBackPress)
    }
}

@Composable
private fun CharacterTitle(name: String, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    val nameTwoLines = name.trim().split(" ")
    // TODO: ver como soluciono lo del nombre cuando es muy largo
    val characterName = if (nameTwoLines.count() > 1) {
        LocalContext.current.getString(
            R.string.title_character_detail_name,
            nameTwoLines[0],
            nameTwoLines[1]
        )
    } else {
        name
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = MaterialTheme.colors.primary)
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        )
        Divider(color = black_1C1B1F.copy(alpha = 0.35f))
    }
}

@Composable
fun CharacterInfo(data: CharacterDetail, scroll: ScrollState) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier
                .verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(TitleHeightOffset))
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.primary)
                ) {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(6.dp))
                    val description =
                        data.description.ifEmpty {
                            LocalContext.current.getString(
                                R.string.detail_placeholder
                            )
                        }
                    Text(
                        text = description,
                        style = Typography.subtitle2,
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = stringResource(id = R.string.title_character_detail_appearances),
                        style = Typography.h4.copy(color = MaterialTheme.colors.primaryVariant),
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Cyan)
                            .padding(start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = data.comicAvailable.toString())
                        Text(text = data.seriesAvailable.toString())
                        Text(text = data.storiesAvailable.toString())
                    }
                    Spacer(modifier = Modifier.height(ExpandedImageSize.times(3)))
                }
            }
        }
    }
}

@Composable
private fun Header() {
    val colors = listOf(
        purple_7057f5,
        blue_86f7fa,
        red_8C1D18,
        blue_21005D,
        rose_E8DEF8,
        white,
        red_B3261E,
        purple_49454F
    )
    val randomOne = Random.nextInt(0, 7)
    val randomTwo = Random.nextInt(0, 7)
    val firstColor = colors[randomOne]
    val secondColor = colors[randomTwo]
    Spacer(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(firstColor, secondColor))
            )
    )
}

@Composable
private fun Image(
    imageUrl: String,
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        MarvelSurface(
            color = Color.LightGray,
            shape = CircleShape,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Image Character Marvel",
                placeholder = painterResource(R.drawable.ic_broken_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Composable
private fun Back(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = black_1C1B1F.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            tint = white,
            contentDescription = "go to back"
        )
    }
}

@Composable
fun LoadingScreen() {
}

@Composable
fun ErrorScreen(errorMessage: Int) {
}

@Preview("default", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CharacterDetailPreview() {
    MaterialTheme {
        val data = CharacterDetail(
            id = 23,
            name = "Daniel",
            modified = "",
            description = "",
            thumbnail = "",
            comicAvailable = 3,
            seriesAvailable = 2,
            storiesAvailable = 11
        )
        CharacterDetailScreen(data = data) {}
    }
}
