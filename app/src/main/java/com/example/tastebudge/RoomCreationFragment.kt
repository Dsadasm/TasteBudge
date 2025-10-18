package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class RoomCreationFragment : Fragment() {


    private var roomCodeText: TextView? = null

    private var tasteBudgeGame : TasteBudgeGame? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_room_creation, container, false)

        val startRoomButton: Button = view.findViewById(R.id.buttonStartRoom)
        startRoomButton.setOnClickListener {
            // Go to Restaurant Search screen
            val fragment = RestaurantSuggestionFragment()
            val ft = parentFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container_view, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        return view
    }

    // apparently bindings must be made after view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // binding
        roomCodeText = view.findViewById<TextView>(R.id.textCode)

        // Update UI
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            // Also update roomCode
            tasteBudgeGame?.apply {
                roomCodeText!!.text = roomCode
            }
        }

    }

}