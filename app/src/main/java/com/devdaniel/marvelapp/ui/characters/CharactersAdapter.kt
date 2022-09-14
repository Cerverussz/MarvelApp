package com.devdaniel.marvelapp.ui.characters

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.devdaniel.marvelapp.databinding.ItemCharacterBinding
import com.devdaniel.marvelapp.domain.model.Character
import com.devdaniel.marvelapp.ui.utils.BaseAdapter
import com.devdaniel.marvelapp.ui.utils.BaseViewHolder
import com.devdaniel.marvelapp.util.extension.isOddOrEven
import com.devdaniel.marvelapp.util.extension.loadImageFromUrl
import com.devdaniel.marvelapp.util.extension.viewBinding

class CharactersAdapter(private val onCharacterClicked: (Character) -> Unit) :
    BaseAdapter<Character, ItemCharacterBinding, CharactersAdapter.CharacterViewHolder>(
        areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem }

    ) {

    inner class CharacterViewHolder(viewBinding: ItemCharacterBinding) :
        BaseViewHolder<ItemCharacterBinding, Character>(viewBinding) {

        init {
            with(binding) {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val character = getItem(position)
                        onCharacterClicked.invoke(character)
                    }
                }
            }
        }

        override fun bind(item: Character) {
            with(binding) {
                txtCharacterName.text = item.name
                imvCharacter.loadImageFromUrl(item.thumbnail)
                if (isOddOrEven(adapterPosition)) {
                    setDimensionRatio("H,1:2")
                } else {
                    setDimensionRatio("H,1:1")
                }
            }
        }

        private fun ItemCharacterBinding.setDimensionRatio(ratio: String) {
            imvCharacter.updateLayoutParams<ConstraintLayout.LayoutParams> {
                dimensionRatio = ratio
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = parent.viewBinding(ItemCharacterBinding::inflate, false)
        return CharacterViewHolder(binding)
    }
}
