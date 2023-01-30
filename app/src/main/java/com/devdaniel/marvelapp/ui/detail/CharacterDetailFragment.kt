package com.devdaniel.marvelapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devdaniel.marvelapp.ui.theme.MarvelTheme
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
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MarvelTheme {
                    CharacterDetail(
                        characterDetailViewModel = characterDetailViewModel,
                        infoCharacter = infoCharacter.infoCharacter,
                        onBackPress = {
                            findNavController().popBackStack()
                        },
                        navigation = { comic ->
                            val action =
                                CharacterDetailFragmentDirections.actionCharacterDetailFragmentToInfoComicFragment(
                                    comic
                                )
                            findNavController().navigate(action)
                        }
                    )
                }
            }
        }
    }
}
