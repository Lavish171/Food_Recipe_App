package com.example.foodrecipeapp.ui.fragmentsOfDetailsActivity.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.databinding.FragmentInstructionsBinding
import com.example.foodrecipeapp.models.Result
import com.example.foodrecipeapp.util.Contants
import kotlinx.android.synthetic.main.fragment_instructions.view.*


class InstructionsFragment : Fragment() {

    private lateinit var instructionsBinding: FragmentInstructionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        instructionsBinding= FragmentInstructionsBinding.bind(view)

        val args=arguments
        val myBundle: Result?=args?.getParcelable(Contants.RECIPE_RESULT_KEY)

        instructionsBinding.instructionsWebView.webViewClient=object:WebViewClient(){}
        val websiteUrl:String=myBundle!!.sourceUrl
        //load url requires string which is not nullable
        instructionsBinding.instructionsWebView.loadUrl(websiteUrl)
    }
}