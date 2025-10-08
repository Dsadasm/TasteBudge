package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RoomJoiningFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_joining, container, false)
    }

    // Delete if un-needed
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    // this fun should be called with "Join Room" button is clicked
    fun joinRoom() {
        val insertedCode = ""     // bind this to the field where the code should be inserted
                                    // (this does not exist right now)
        if (insertedCode.isEmpty()) {
            // some kind of error message e.g. "Please enter game ID"
            return
        }

        // Access Firebase to check if the insertedCode is valid
        Firebase.firestore.collection("TasteBudge")
            .document(insertedCode)
            .get()
            .addOnSuccessListener {
                val game = it?.toObject(TasteBudgeGame::class.java)
                if (game == null) {
                    // some kind of error message e.g. "Please enter valid game ID"
                    val placeholder = "placeholder"
                } else {
                    // Correct room code
                    game.addPlayer(TasteBudgeData.player)
                    TasteBudgeData.saveGame(game)
                }
            }
    }

}