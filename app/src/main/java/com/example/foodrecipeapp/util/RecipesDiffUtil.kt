package com.example.foodrecipeapp.util
import androidx.recyclerview.widget.DiffUtil
import com.example.foodrecipeapp.models.Result

//doing this to adapt for the recipes modal class as well as extended ingredients modal class
class RecipesDiffUtil<T>(
   private val oldList:List<T>,
   private val newList:List<T>
):DiffUtil.Callback()

{
    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]===newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       //called only when areItemsTheSame returns the true
        return oldList[oldItemPosition]==newList[newItemPosition]
    }

}