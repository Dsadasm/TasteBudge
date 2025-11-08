package com.example.tastebudge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class EndFragment : Fragment() {
    private var tasteBudgeGame : TasteBudgeGame? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RestaurantListViewAdapter
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_end, container, false)
        backBtn = view.findViewById(R.id.back_btn)

        // Fetch restaurant from current session
        TasteBudgeManager.fetchGame()

        // Observe should allow real time update so no need wait for others
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            tasteBudgeGame?.apply {
                val winners: List<Restaurant> = getWinners(this.restaurantList)
                adapter = RestaurantListViewAdapter(winners)
                recyclerView = view.findViewById(R.id.restaurantList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            }
        }

        // Go back to main menu
        backBtn.setOnClickListener {
            val fragment = MenuFragment()
            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container_view, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        return view
    }

    private fun getWinners(restaurantList: List<Restaurant>): List<Restaurant> {
        // Get the max score
        var maxScore: Int = 0
        for (restaurant in restaurantList) {
            if (restaurant.score > maxScore) {
                maxScore = restaurant.score
            }
        }

        return restaurantList.filter { it.score == maxScore }
    }
}