package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class RestaurantSuggestionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_restaurant_suggestion, container, false)

        // Go to Restaurant Search screen
        val searchButton: Button = view.findViewById(R.id.searchButton)
        searchButton.setOnClickListener {
            val fragment = RestaurantSearchFragment()
            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container_view, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        // Go to Add Custom screen
        val customButton: Button = view.findViewById(R.id.customButton)
        customButton.setOnClickListener {
            val fragment = AddCustomRestaurant()
            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container_view, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        return view
    }
}