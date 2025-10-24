package com.example.tastebudge

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresPermission
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {

    private var tasteBudgeGame : TasteBudgeGame? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_menu, container, false)

        // i am not fully sure how this works
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
        }

        // Always create a user first
        TasteBudgeManager.user = User()

        val buttonCreateRoom: Button = view.findViewById(R.id.buttonCreateRoom)
        buttonCreateRoom.setOnClickListener {

            // Create a new TasteBudgeGame
            TasteBudgeManager.createGame()

            val fragment: RoomCreationFragment = RoomCreationFragment()

            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container_view, fragment)
            ft.addToBackStack(null)
            ft.commit()

        }

        val buttonJoinRoom: Button = view.findViewById(R.id.buttonJoinRoom)
        buttonJoinRoom.setOnClickListener {
            // Join room
            val fragment: RoomJoiningFragment = RoomJoiningFragment()
            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container_view, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        // Test
//        lifecycleScope.launch {
//            RetrofitClient.yelpApiService.searchRestaurantsByLocation(
//                location = "Hong Kong",
//                radius = 4000,
//                limit = 20
//            )
//        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        val locationManager = LocationManager(context)
//        locationManager.requestPermissions(this)

        // Test
//        val locationManager = LocationManager(context)
//
//        locationManager.requestPermissions(this)
//        lifecycleScope.launch {
//            val location: Location? = locationManager.getLastLocation()
//            Log.i("Test", "Latitude: ${location?.latitude} Longitude: ${location?.longitude}")
//        }
    }
}