package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddCustomRestaurant : Fragment() {
    private var tasteBudgeGame : TasteBudgeGame? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_custom_restaurant, container, false)
        TasteBudgeManager.fetchGame()
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
        }

        // Create custom restaurant
        val createButton: Button = view.findViewById(R.id.createButton)
        val nameInsert: EditText = view.findViewById(R.id.nameInsert)
        val priceInsert: EditText = view.findViewById(R.id.priceInsert)
        val ratingInsert: EditText = view.findViewById(R.id.ratingInsert)
        createButton.setOnClickListener {
            val name: String = nameInsert.text.toString()
            val price: String = priceInsert.text.toString()
            val rating: Float? = ratingInsert.text.toString().toFloatOrNull()

            // Price needs to be all $
            if (!price.all{it == '$'}) {
                Toast
                    .makeText(requireContext(),
                        "Price needs to indicated with only dollar sign",
                        Toast.LENGTH_SHORT)
                    .show()
            }
            // Rating 1-5
            else if (rating != null && (rating < 0 || rating > 5)) {
                Toast
                    .makeText(requireContext(),
                        "Rating needs to be in the range of 1 to 5",
                        Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                // Add custom restaurant
                if (rating != null) {
                    val restaurant = Restaurant(rating = rating.toDouble(), price = price, name = name)
                    tasteBudgeGame?.apply {
                        this.restaurantList.add(restaurant)
                        TasteBudgeManager.saveGame(this)
                        showNotifyRestaurantDialog(restaurant)
                    }
                }
            }
        }

        // Go back to previous screen
        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack();
        }

        return view
    }

    // Show a dialog to notify user add restaurant to session
    private fun showNotifyRestaurantDialog(restaurant: Restaurant) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(restaurant.name)
            .setMessage("Restaurant Added")
            .setPositiveButton("Yes") {dialog, which ->
                // Do nothing
            }
            .show()
    }

}