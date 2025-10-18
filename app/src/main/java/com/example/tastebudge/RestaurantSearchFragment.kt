package com.example.tastebudge

import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class RestaurantSearchFragment : Fragment() {
    private lateinit var restaurantListView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_restaurant_search, container, false)

        restaurantListView = view.findViewById(R.id.restaurantList)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Update the restaurantListView to show nearby restaurant
        val locationManager = LocationManager(context)
        lifecycleScope.launch {
            val location: Location? = locationManager.getLastLocation()
            val restaurants: List<Restaurant>? = RestaurantManager.searchRestaurants(location?.latitude, location?.longitude)
            restaurantListView.layoutManager = LinearLayoutManager(context)
            if (restaurants != null) {
                restaurantListView.adapter = MyAdapter(restaurants)
            }
        }
    }
}
