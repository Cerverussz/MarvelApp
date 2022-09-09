package com.devdaniel.marvelapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.devdaniel.marvelapp.databinding.FragmentFirstBinding

class FirstFragment : BaseFragmentBinding<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonFirst.setOnClickListener {
            Toast.makeText(requireContext(), "Hola", Toast.LENGTH_SHORT).show()
        }
    }
}
