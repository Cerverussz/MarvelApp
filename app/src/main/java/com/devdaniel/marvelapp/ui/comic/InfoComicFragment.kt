package com.devdaniel.marvelapp.ui.comic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.ui.theme.MarvelTheme
import com.devdaniel.marvelapp.ui.theme.purple_4F378B
import com.devdaniel.marvelapp.ui.theme.white
import dagger.hilt.android.AndroidEntryPoint

private val size_10Dp = 10.dp
private val size_16Dp = 16.dp
private val size_36Dp = 36.dp
private const val ALPHA_03 = 0.35f

@AndroidEntryPoint
class InfoComicFragment : Fragment() {

    private val infoComic: InfoComicFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MarvelTheme {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Box {
                            val configuration = LocalConfiguration.current
                            val screenHeight = configuration.screenHeightDp.dp / 2
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(infoComic.infoComics.thumbnail)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.title_character_content_description),
                                placeholder = painterResource(R.drawable.ic_broken_image),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(screenHeight)
                                    .testTag("imageCharacter"),
                                contentScale = ContentScale.FillHeight
                            )
                            Back {
                                findNavController().popBackStack()
                            }
                        }

                        Column(modifier = Modifier.padding(size_10Dp)) {
                            Text(
                                text = infoComic.infoComics.titleComic,
                                color = white,
                                style = MaterialTheme.typography.h6,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = infoComic.infoComics.description
                                    ?: stringResource(id = R.string.detail_placeholder),
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
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
}