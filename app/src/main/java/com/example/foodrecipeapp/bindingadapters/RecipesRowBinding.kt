package com.example.foodrecipeapp.bindingadapters
//implement function for Recipes Row binding
//implemnted HTML parser function for recipe summary using Jsoup library
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.models.Result
import com.example.foodrecipeapp.ui.fragments.recipes.RecipesFragmentDirections
import com.example.foodrecipeapp.ui.fragments.recipes.bottomsheet.RecipesBottomSheetDirections
import org.jsoup.Jsoup
import java.io.FileDescriptor
import java.lang.Exception

class RecipesRowBinding {
    companion object
    {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result:Result)
        {
            Log.d("onRecipeClickListener","Called")
           recipeRowLayout.setOnClickListener {
               try
               {
                 val action=RecipesFragmentDirections.
                 actionRecipesFragmentToDetailsActivity(result)

                   recipeRowLayout.findNavController().navigate(action)
               }catch (e:Exception)
               {
                   Log.d("onRecipeClickListener",e.toString())
               }
           }
        }

        @BindingAdapter("LoadTheImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView,imageUrl:String)
        {
            imageView.load(imageUrl)
            {
                crossfade(600)
                //for those image which are not cached properly.
                error(R.drawable.ic_error_placeholder)
            }
        }
        @BindingAdapter("setTheNumberOfLikes")
        //making our function static
        @JvmStatic
        fun setNumberOfLikes(textView:TextView,likes:Int)
        {
            //this function will be automatically called from  textView of heart  of  recipe_low_layout.xml
            textView.text=likes.toString()
        }

        @BindingAdapter("setTheNumberOfMinutes")
        //making our function static
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView,minutes:Int)
        {
            textView.text=minutes.toString()
        }

        //used view since we need to apply this at both textview and imageview
        @BindingAdapter("applyTheVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan:Boolean)
        {
           if(vegan)
           {
               when(view)
               {
                   is TextView->
                   {
                       view.setTextColor(
                           ContextCompat.getColor(
                               view.context,
                               R.color.green
                           )
                       )
                   }

                   is ImageView->
                   {
                       view.setColorFilter(
                           ContextCompat.getColor(
                               view.context,
                               R.color.green
                           )
                       )
                   }
               }
           }
        }

        //removing the html tags in recipes_row_layout recipes items
        @BindingAdapter("parseHtml")
        @JvmStatic
       fun parseHtml(textView: TextView,description:String?)
       {
           if(description!=null)
           {
              val desc =Jsoup.parse(description).text()
               textView.text=desc
           }
       }
    }
}