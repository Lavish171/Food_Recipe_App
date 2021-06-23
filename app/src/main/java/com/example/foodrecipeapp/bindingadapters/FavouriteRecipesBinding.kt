package com.example.foodrecipeapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.datastore.preferences.protobuf.Internal
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipeapp.adapters.FavouriteRecipesAdapter
import com.example.foodrecipeapp.data.database.entities.FavouritesEntity
import org.w3c.dom.Text

class FavouriteRecipesBinding {

    companion object
    {
        //setData will refer to the adapter
        @BindingAdapter("viewVisibility","setData",requireAll = false)
        @JvmStatic
        fun setDataViewVisibility(view: View,
                                  favouritesEntity: List<FavouritesEntity>?,
                                  mAdapter:FavouriteRecipesAdapter?)
        {
            //to check if fav table is empty or not
            //if empty,then show the hidden views

            if(favouritesEntity.isNullOrEmpty())
            {
                when(view)
                {
                    is ImageView->
                    {
                        view.visibility=View.VISIBLE
                    }
                    is TextView->
                    {
                        view.visibility=View.VISIBLE
                    }
                    is RecyclerView->
                    {
                        view.visibility=View.INVISIBLE
                    }

                }
            }
            else
            {
                when(view)
                {
                    is ImageView->
                    {
                        view.visibility=View.INVISIBLE
                    }
                    is TextView->
                    {
                        view.visibility=View.INVISIBLE
                    }
                    is RecyclerView->
                    {
                        view.visibility=View.VISIBLE
                        mAdapter?.setData(favouritesEntity)
                    }

                }
            }
        }
    }
}