package com.example.foodrecipeapp.ui.fragmentsOfDetailsActivity.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.databinding.FragmentOverviewBinding
import com.example.foodrecipeapp.models.Result
import com.example.foodrecipeapp.util.Contants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    private lateinit var overviewBinding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overviewBinding= FragmentOverviewBinding.bind(view)

        val args=arguments
        val myBundle:Result?=args?.getParcelable(RECIPE_RESULT_KEY)

        overviewBinding.mainImageView.load(myBundle?.image)

        overviewBinding.overviewTextView.text=myBundle?.title
        overviewBinding.likesTextView.text=myBundle?.aggregateLikes.toString()
        overviewBinding.timeTextView.text=myBundle?.readyInMinutes.toString()
           myBundle?.summary.let {
               val summary=Jsoup.parse(it).text()
               view.summary_textView.text=summary
           }

        if(myBundle?.vegetarian==true)
        {
            overviewBinding.vegeterianImageView.setColorFilter(
                ContextCompat.getColor(requireContext(),R.color.green))

            overviewBinding.vegeterianTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))

        }

        if(myBundle?.vegan==true)
        {
            overviewBinding.veganImageView.setColorFilter(
                ContextCompat.getColor(requireContext(),R.color.green))

            overviewBinding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.glutenFree==true)
        {
            overviewBinding.glutenfreeImageView.setColorFilter(
                ContextCompat.getColor(requireContext(),R.color.green))

            overviewBinding.glutenfreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.dairyFree==true)
        {
            overviewBinding.dairyFreeImageView.setColorFilter(
                ContextCompat.getColor(requireContext(),R.color.green))

            overviewBinding.dairyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.veryHealthy==true)
        {
            overviewBinding.healthyImageView.setColorFilter(
                ContextCompat.getColor(requireContext(),R.color.green))

            overviewBinding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }


        if(myBundle?.cheap==true)
        {
            overviewBinding.cheapImageView.setColorFilter(
                ContextCompat.getColor(requireContext(),R.color.green))

            overviewBinding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }








    }
}