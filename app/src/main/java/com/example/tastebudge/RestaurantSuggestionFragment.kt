package com.example.tastebudge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class RestaurantSuggestionFragment : Fragment() {
    private var tasteBudgeGame : TasteBudgeGame? = null
    private lateinit var adapter: RestaurantListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_restaurant_suggestion, container, false)

        // Fetch restaurant from current session
        TasteBudgeManager.fetchGame()
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            tasteBudgeGame?.apply {
                adapter = RestaurantListViewAdapter(this.restaurantList) { restaurant ->
                    showRemoveRestaurantDialog(restaurant)
                }
                val restaurantListView: RecyclerView = view.findViewById(R.id.restaurantList)
                restaurantListView.layoutManager = LinearLayoutManager(requireContext())
                restaurantListView.adapter = adapter
            }
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

        // Go to Matching screen
        val readyButton: Button = view.findViewById(R.id.readyButton)

        // Only visible to host (NOT YET TESTED)
        TasteBudgeManager.fetchGame()
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            tasteBudgeGame?.apply {
                if (hostUserID == TasteBudgeManager.user.userID) {
                    readyButton.visibility = View.VISIBLE
                } else {
                    readyButton.visibility = View.INVISIBLE
                }
            }
        }

        readyButton.setOnClickListener {
            // Change GameStatus to MATCHING
            TasteBudgeManager.fetchGame()   // make sure game is updated first
            tasteBudgeGame?.apply {
                this.gameStatus = GameStatus.MATCHING
                TasteBudgeManager.saveGame(this)
            }

            val fragment = MatchingFragment()
            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container_view, fragment)
            ft.commit()
        }

        return view
    }

    // Show a dialog to let user remove restaurant from session
    private fun showRemoveRestaurantDialog(restaurant: Restaurant) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(restaurant.name)
            .setMessage("Remove restaurant?")
            .setPositiveButton("Yes") {dialog, which ->
                // Remove restaurant from current session
                tasteBudgeGame?.apply {
                    this.restaurantList.remove(restaurant)
                    TasteBudgeManager.saveGame(this)
                }
            }
            .setNegativeButton("No") {dialog, which ->
                // Do nothing
            }
            .show()
    }
}