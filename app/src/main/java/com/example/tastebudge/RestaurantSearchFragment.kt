package com.example.tastebudge

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class RestaurantSearchFragment : Fragment() {
    private var tasteBudgeGame : TasteBudgeGame? = null
    private lateinit var locationManager: LocationManager

    private lateinit var restaurantListView: RecyclerView
    private lateinit var adapter: RestaurantListViewAdapter

    // For detecting GameStatus change
    private var gameStatusListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_restaurant_search, container, false)
        locationManager = LocationManager(requireContext())
        restaurantListView = view.findViewById(R.id.restaurantList)
        TasteBudgeManager.fetchGame()
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
        }

        // Ask for location permission
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            showNearbyRestaurant()
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        // Search by name (term)
        val searchButton: Button = view.findViewById(R.id.searchButton)
        val searchText: EditText = view.findViewById(R.id.searchText)
        searchButton.setOnClickListener {
            val term : String = searchText.text.toString()
            lifecycleScope.launch {
                val restaurants: List<Restaurant>? = RestaurantManager.searchRestaurantsByTerm(term)
                if (restaurants != null) {
                    adapter.updateData(restaurants)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // For checking status change
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            tasteBudgeGame?.apply {
                // Check current status
                setupFirestoreListener(roomCode)
            }
        }
    }

    // Show a dialog to let user add restaurant to session
    private fun showAddRestaurantDialog(restaurant: Restaurant) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(restaurant.name)
            .setMessage("Add restaurant?")
            .setPositiveButton("Yes") {dialog, which ->
                // Add restaurant to current session

                tasteBudgeGame?.apply {
                    this.restaurantList.add(restaurant)
                    TasteBudgeManager.saveGame(this)
                }
            }
            .setNegativeButton("No") {dialog, which ->
                // Do nothing
            }
            .show()
    }

    // Update the restaurantListView to show nearby restaurant
    private fun showNearbyRestaurant() {
        lifecycleScope.launch {
            val location: Location? = locationManager.getLastLocation()
            val restaurants: List<Restaurant>? =
                RestaurantManager.searchRestaurants(
                    location?.latitude,
                    location?.longitude
                )
            restaurantListView.layoutManager = LinearLayoutManager(requireContext())
            if (restaurants != null) {
                adapter = RestaurantListViewAdapter(restaurants) { restaurant ->
                    showAddRestaurantDialog(restaurant)
                }
                restaurantListView.adapter = adapter
            }
        }
    }

    // Procedure for "warping": Detect GameStatus change (by host) and follow

    private fun setupFirestoreListener(roomCode: String) {
        // Remove existing listener first
        gameStatusListener?.remove()

        val db = Firebase.firestore

        // Listen for changes to the game document
        gameStatusListener = db.collection("TasteBudge")
            .document(roomCode)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_SHORT).show()
                    }
                    return@addSnapshotListener
                }

                snapshot?.let { document ->
                    if (document.exists()) {
                        val newStatusString = document.getString("gameStatus")
                        newStatusString?.let { status ->
                            try {
                                val newStatus = GameStatus.valueOf(status)

                                // Check if status actually changed to avoid unnecessary navigation
                                if (newStatus != tasteBudgeGame?.gameStatus) {
                                    handleGameStatusChange(newStatus)
                                }
                            } catch (e: IllegalArgumentException) {
                            }
                        }
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Room not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun handleGameStatusChange(newStatus: GameStatus) {
        activity?.runOnUiThread {
            // Update local game status first
            tasteBudgeGame?.gameStatus = newStatus

            when (newStatus) {
                GameStatus.MATCHING -> {
                    val fragment = MatchingFragment()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                else -> {
                    // WAITING or other status - no navigation needed
                }
            }
        }
    }
    private fun cleanupFirestoreListener() {
        gameStatusListener?.remove()
        gameStatusListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanupFirestoreListener()
    }
}
