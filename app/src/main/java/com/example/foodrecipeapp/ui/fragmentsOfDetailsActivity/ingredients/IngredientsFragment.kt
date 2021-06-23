package com.example.foodrecipeapp.ui.fragmentsOfDetailsActivity.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.adapters.IngredientsAdapter
import com.example.foodrecipeapp.databinding.FragmentIngredientsBinding
import com.example.foodrecipeapp.models.Result
import com.example.foodrecipeapp.util.Contants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsFragment : Fragment() {

    private lateinit var ingredientsBinding: FragmentIngredientsBinding
    private val mAdapter:IngredientsAdapter by lazy { IngredientsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredientsBinding= FragmentIngredientsBinding.bind(view)


        val args=arguments
        val myBundle: Result?=args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecyclerView(view)
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }

    }

    private fun setupRecyclerView(view:View)
    {
        view.ingredients_recycler_view.adapter=mAdapter
        view.ingredients_recycler_view.layoutManager=LinearLayoutManager(requireContext())

    }
}