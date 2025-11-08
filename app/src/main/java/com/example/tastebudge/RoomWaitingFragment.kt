package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class RoomWaitingFragment : Fragment() {

    private var roomCodeText : TextView? = null

    private var roomCodeString : String? = null
    private var tasteBudgeGame : TasteBudgeGame? = null

    private var gameStatusListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_waiting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind
        roomCodeText = view.findViewById<EditText>(R.id.roomCodeText)
        // Update UI
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            // Also update roomCode
            tasteBudgeGame?.apply {
                roomCodeString = "Room Code: $roomCode"
                roomCodeText!!.text = roomCodeString

                // Check current status
                setupFirestoreListener(roomCode)
            }
        }
    }

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
                GameStatus.SUGGESTION -> {
                    val fragment = RestaurantSuggestionFragment()
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
        roomCodeText = null
    }

}