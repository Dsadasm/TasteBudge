package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RoomJoiningFragment : Fragment() {

    private var codeInput : EditText? = null
    private var buttonJoinViaCode : Button? = null

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

        // Bind
        codeInput = view.findViewById<EditText>(R.id.codeInput)

        // Using on-click listener for JoinButton
        buttonJoinViaCode = view.findViewById<Button>(R.id.buttonJoinViaCode)
        buttonJoinViaCode!!.setOnClickListener { joinRoom() }

    }

    // this fun should be called with "Join Room" button is clicked
    fun joinRoom() {
        val insertedCode : String = codeInput!!.text.toString()
        TasteBudgeManager.joinGame(insertedCode, codeInput!!)
    }

}