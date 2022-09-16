package com.devdaniel.marvelapp.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.devdaniel.marvelapp.ui.theme.blue_86f7fa
import com.devdaniel.marvelapp.ui.theme.purple_7057f5
import kotlin.math.max
import kotlin.math.min

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
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
        CharacterInfo(data) { scroll.value }
        Image(imageUrl = data.thumbnail) {
            scroll.value
        }
        Back(onBackPress)
    }
}

@Composable
fun CharacterInfo(data: CharacterDetail, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(8.dp)
        )
        Text(
            text = data.name,
            color = colorResource(id = R.color.md_theme_light_inversePrimary),
            style = Typography.h6
        )
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(purple_7057f5, blue_86f7fa))
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
            tint = MaterialTheme.colors.primary,
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

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun CharacterDetailPreview() {
    MaterialTheme {
        val data = CharacterDetail(
            id = 0,
            name = "Daniel",
            modified = "",
            description = "",
            thumbnail = "",
            comicAvailable = 0,
            seriesAvailable = 0,
            storiesAvailable = 0
        )
        CharacterDetailScreen(data = data) {}
    }
}
