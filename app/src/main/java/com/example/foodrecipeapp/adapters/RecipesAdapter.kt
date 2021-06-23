package com.example.foodrecipeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipeapp.databinding.RecipesRowLayoutBinding
import com.example.foodrecipeapp.models.FoodRecipe
import com.example.foodrecipeapp.models.Result
import com.example.foodrecipeapp.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipes= emptyList<Result>()
    class MyViewHolder(private val binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        //passing root element the row layout
            fun bind(result: Result)
            {
                binding.result=result
                //will update the layout if there is any change in the data
                binding.executePendingBindings()
            }

            companion object
            {
                fun from(parent:ViewGroup):MyViewHolder
                {
                    val layoutInflater=LayoutInflater.from(parent.context)
                    val binding=RecipesRowLayoutBinding.inflate(layoutInflater,parent,false)
                    return MyViewHolder(binding)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe=recipes[position]
        holder.bind(currentRecipe)

    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    //calling this function from recipes fragment and passing new data everytime
    fun setData(newData:FoodRecipe)
    {
        //passing both old and new list respectively
        val recipesDiffUtil=RecipesDiffUtil(recipes,newData.results)
        //calculating the diff b/w old and new list
        val diffUtilResult=DiffUtil.calculateDiff(recipesDiffUtil)
        //storing the result in empty list
        recipes=newData.results
        //notifying the recycler view adapter to look for any change
        //in the data and update the view according to new data
        //notifyDataSetChanged()

        diffUtilResult.dispatchUpdatesTo(this)
    }
}