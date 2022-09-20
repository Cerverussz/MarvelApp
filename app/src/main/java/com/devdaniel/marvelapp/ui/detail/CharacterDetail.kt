package com.devdaniel.marvelapp.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.domain.model.CharacterDetail
import com.devdaniel.marvelapp.ui.components.MarvelSurface
import com.devdaniel.marvelapp.ui.components.backgroundWithOutCorners
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
private val ImageOverlap = 121.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val TitleHeightOffset = MinTitleOffset + GradientScroll
private val ExpandedImageSize = 200.dp
private val CollapsedImageSize = 100.dp
private val size_6Dp = 6.dp
private val size_10Dp = 10.dp
private val size_12Dp = 12.dp
private val size_16Dp = 16.dp
private val size_24Dp = 24.dp
private val size_25Dp = 25.dp
private val size_30Dp = 30.dp
private val size_36Dp = 36.dp
private const val ZERO_VALUE = 0
private const val ONE_VALUE = 1
private const val TWO_VALUE = 2
private const val THREE_VALUE = 3
private const val SEVEN_VALUE = 7
private const val ALPHA_03 = 0.35f
private const val MIN_SCREEN_WIDTH = 390.0f

@Composable
fun CharacterDetail(
    characterDetailViewModel: CharacterDetailViewModel,
    infoCharacter: InfoCharacter?,
    onBackPress: () -> Unit
) {
    val characterId = infoCharacter?.id ?: ZERO_VALUE
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
        val scroll = rememberScrollState(ZERO_VALUE)
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
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = ZERO_VALUE, y = offset.toInt())
            }
            .background(color = MaterialTheme.colors.primary)
            .fillMaxWidth()
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.padding(start = size_12Dp, end = size_12Dp)
        )
        Divider(color = black_1C1B1F.copy(alpha = ALPHA_03))
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
                            .padding(start = size_16Dp, end = size_16Dp, top = size_10Dp)
                    )
                    Spacer(Modifier.height(size_6Dp))
                    Text(
                        text = stringResource(id = R.string.title_character_detail_appearances),
                        style = Typography.h4.copy(color = MaterialTheme.colors.primaryVariant),
                        modifier = Modifier.padding(start = size_12Dp, end = size_12Dp)
                    )
                    Appearances(data)
                }
            }
        }
    }
}

@Composable
private fun Appearances(data: CharacterDetail) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val (
        textStyle: TextStyle,
        verticalPadding: Dp,
        horizontalPadding: Dp
    ) = getStyleToAppearanceView(screenWidth)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = size_16Dp, end = size_16Dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.backgroundWithOutCorners(
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                verticalPadding = verticalPadding,
                horizontalPadding = horizontalPadding
            )
        ) {
            Text(
                text = stringResource(R.string.title_character_comics),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = textStyle
            )
            Text(
                text = data.comicAvailable.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = textStyle
            )
        }

        Column(
            modifier = Modifier.backgroundWithOutCorners(
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                verticalPadding = verticalPadding,
                horizontalPadding = horizontalPadding
            )
        ) {
            Text(
                text = stringResource(R.string.title_character_series),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = textStyle
            )
            Text(
                text = data.seriesAvailable.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = textStyle
            )
        }

        Column(
            modifier = Modifier.backgroundWithOutCorners(
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                verticalPadding = verticalPadding,
                horizontalPadding = horizontalPadding
            )
        ) {
            Text(
                text = stringResource(R.string.title_character_stories),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = textStyle
            )
            Text(
                text = data.storiesAvailable.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = textStyle
            )
        }
    }
    Spacer(modifier = Modifier.height(ExpandedImageSize.times(THREE_VALUE)))
}

@Composable
private fun getStyleToAppearanceView(screenWidth: Dp) =
    if (screenWidth.value < MIN_SCREEN_WIDTH) {
        Triple(Typography.body2, size_30Dp, size_25Dp)
    } else {
        Triple(Typography.body1, size_12Dp, size_30Dp)
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
    val randomOne = Random.nextInt(ZERO_VALUE, SEVEN_VALUE)
    val randomTwo = Random.nextInt(ZERO_VALUE, SEVEN_VALUE)
    val firstColor = colors[randomOne]
    val secondColor = colors[randomTwo]
    Spacer(
        modifier = Modifier
            .height(CollapsedImageSize)
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
        (scrollProvider() / collapseRange).coerceIn(ZERO_VALUE.toFloat(), ONE_VALUE.toFloat())
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = Modifier.padding(horizontal = size_24Dp).then(Modifier.statusBarsPadding())
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
                contentDescription = stringResource(R.string.title_character_content_description),
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
        check(measurables.size == ONE_VALUE)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable =
            measurables[ZERO_VALUE].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / TWO_VALUE, // centered when expanded
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
            .padding(horizontal = size_16Dp, vertical = size_10Dp)
            .size(size_36Dp)
            .background(
                color = black_1C1B1F.copy(alpha = ALPHA_03),
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
    Box(modifier = Modifier.fillMaxSize().padding(64.dp)) {
        LottieAnimation(composition)
    }
}

@Composable
fun ErrorScreen(errorMessage: Int) {
    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colors.primary),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = stringResource(id = errorMessage),
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
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
