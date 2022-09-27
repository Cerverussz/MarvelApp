package com.devdaniel.marvelapp.ui.characters

import android.animation.ValueAnimator.INFINITE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devdaniel.marvelapp.R
import com.devdaniel.marvelapp.databinding.FragmentCharactersBinding
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.ui.mappers.toCharacterPresentation
import com.devdaniel.marvelapp.ui.utils.BaseFragmentBinding
import com.devdaniel.marvelapp.util.extension.hide
import com.devdaniel.marvelapp.util.extension.observeFlows
import com.devdaniel.marvelapp.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment :
    BaseFragmentBinding<FragmentCharactersBinding>(FragmentCharactersBinding::inflate) {

    private val charactersViewModel: CharactersViewModel by viewModels()

    private val charactersAdapter by lazy { CharactersAdapter(::navigateToCharacterDetail) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCollectors()
    }

    override fun onResume() {
        super.onResume()
        charactersViewModel.getCharacters()
    }

    private fun setupCollectors() {
        observeFlows { coroutineScope ->
            coroutineScope.launch {
                charactersViewModel.charactersState.collect { state ->
                    when (state) {
                        CharactersState.Loading -> {
                            showLoader()
                        }
                        is CharactersState.CharactersSuccess -> {
                            charactersAdapter.submitList(state.characters)
                            showRecyclerView()
                        }
                        is CharactersState.CharactersError -> {
                            getLocalCharacters {
                                hideAllViews()
                                showScreenError(state.errorMessage)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getLocalCharacters(closure: () -> Unit = {}) {
        charactersViewModel.localCharacters.collect { characters ->
            if (characters.isNotEmpty()) {
                charactersAdapter.submitList(characters)
                showRecyclerView()
            } else {
                closure.invoke()
            }
        }
    }

    private fun showScreenError(errorMessage: Int?) {
        with(binding) {
            includeScreenError.txtErrorTitle.text =
                getString(errorMessage ?: R.string.not_found_error)
            includeScreenError.root.show()
        }
    }

    private fun showRecyclerView() {
        with(binding) {
            includeScreenError.root.hide()
            lavLoader.hide()
            rcvCharacters.show()
        }
    }

    private fun hideAllViews() {
        with(binding) {
            rcvCharacters.hide()
            lavLoader.hide()
        }
    }

    private fun showLoader() {
        with(binding) {
            includeScreenError.root.hide()
            rcvCharacters.hide()
        }
        with(binding.lavLoader) {
            repeatMode = INFINITE
            playAnimation()
            show()
        }
    }

    private fun setupRecyclerView() {
        with(binding.rcvCharacters) {
            adapter = charactersAdapter
            layoutManager =
                StaggeredGridLayoutManager(SPAN_COLUMNS, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun navigateToCharacterDetail(character: Character) {
        val action =
            CharactersFragmentDirections.actionCharactersFragmentToCharacterDetailFragment(
                character.toCharacterPresentation()
            )
        findNavController().navigate(action)
    }
}

private const val SPAN_COLUMNS = 2
