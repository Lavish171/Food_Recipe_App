package com.example.foodrecipeapp.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.databinding.RecipesBottomSheetBinding
import com.example.foodrecipeapp.util.Contants.Companion.DEFAULT_DIET_TYPE
import com.example.foodrecipeapp.util.Contants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodrecipeapp.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var recipesBottomSheetBinding: RecipesBottomSheetBinding

    //this are the global varibles,will store the text of the selected type diet and meal and their id as well
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recipes_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipesBottomSheetBinding = RecipesBottomSheetBinding.bind(view)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)


        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            //immediately apply the selected values using live data observing continously
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            //update the chip with the new data
            updateChip(value.selectedMealTypeId,recipesBottomSheetBinding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId,recipesBottomSheetBinding.dietTypeChipGroup)
        })

        recipesBottomSheetBinding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            //storing the text and id of the meal type
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)

            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        recipesBottomSheetBinding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
             //storing the text and id of the diet type
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId

        }


        //-->>>>Implementing the logic on the apply button
        recipesBottomSheetBinding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )

            val action=RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)


        }

    }

    private fun updateChip(chipId:Int,chipGroup:ChipGroup)
    {
        //if has stored some data before to datastore the chip id would not be zero
        if(chipId!=0)
        {
            try
            {
                chipGroup.findViewById<Chip>(chipId).isChecked=true
            }
            catch (e:Exception)
            {
                Log.d("RecipesBottomSheet",e.message.toString())
            }
        }
    }

}