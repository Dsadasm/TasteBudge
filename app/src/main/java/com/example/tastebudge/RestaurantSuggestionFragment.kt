package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RestaurantSuggestionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_restaurant_suggestion, container, false)

        // Todo: Fetch restaurant from current session
        val restaurantListView: RecyclerView = view.findViewById(R.id.restaurantList)
        restaurantListView.adapter = RestaurantListViewAdapter(emptyList()) { restaurant ->
            showRemoveRestaurantDialog(restaurant)
        }

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

    private fun showRemoveRestaurantDialog(restaurant: Restaurant) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(restaurant.name)
            .setMessage("Add restaurant?")
            .setPositiveButton("Yes") {dialog, which ->
                // Todo: Remove restaurant from current session
            }
            .setNegativeButton("No") {dialog, which ->
                // Do nothing
            }
            .show()
    }
}