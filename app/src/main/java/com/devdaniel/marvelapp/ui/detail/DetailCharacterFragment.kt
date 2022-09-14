package com.devdaniel.marvelapp.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.devdaniel.marvelapp.databinding.FragmentDetailCharacterBinding
import com.devdaniel.marvelapp.ui.utils.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCharacterFragment :
    BaseFragmentBinding<FragmentDetailCharacterBinding>(FragmentDetailCharacterBinding::inflate) {

    private val infoCharacter: DetailCharacterFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "${infoCharacter.infoCharacter?.id}", Toast.LENGTH_SHORT)
            .show()
    }
}
