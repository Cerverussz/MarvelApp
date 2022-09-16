package com.devdaniel.marvelapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.devdaniel.marvelapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {

    private val characterDetailViewModel by viewModels<CharacterDetailViewModel>()

    private val infoCharacter: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val characterId = infoCharacter.infoCharacter?.id ?: 0
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    val characterDetailState by characterDetailViewModel.characterDetailState.collectAsState(
                        initial = CharacterDetailState(isLoading = false)
                    )
                    LaunchedEffect(key1 = characterDetailState.data?.id) {
                        characterDetailViewModel.getCharacterDetail(characterId)
                    }
                    if (characterDetailState.isLoading) {
                        println("daniel--- loading")
                    }
                    characterDetailState.errorMessage?.let {
                        println("daniel--- errorMessage")
                    }

                    characterDetailState.data?.let { data ->
                        Text(
                            text = data.name,
                            color = colorResource(id = R.color.md_theme_light_onPrimary)
                        )
                    }
                }
            }
        }
    }
}
