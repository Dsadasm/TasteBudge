package com.example.tastebudge

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


// I am not entirely sure how this works, but it is related to networking
// Following this tutorial: https://www.youtube.com/watch?v=7Af2UEiKt80
object TasteBudgeManager {

    private var _tasteBudgeGame : MutableLiveData<TasteBudgeGame> = MutableLiveData()
    var tasteBudgeGame : LiveData<TasteBudgeGame> = _tasteBudgeGame

    var user : User = User()


    // Using firebase to "do networking"

    // Save the TasteBudgeGame to FireBase (so others can see changes)
    fun saveGame(game: TasteBudgeGame) {
        _tasteBudgeGame.postValue(game)

        Firebase.firestore.collection("TasteBudge")
            .document(game.roomCode)
            .set(game)
            .addOnFailureListener { exception ->
                Log.w("TasteBudgeManger", "Listen failed.", exception)
            }

    }

    // Generates a unique room code, then uses saveGame
    fun createGame() {
        var isCodeUnique : Boolean = false
        var uniqueCode : String = "-1"
        uniqueCode = (100000 .. 999999).random().toString()

        // Check with firebase database to see if code is unique, if not generate another one
        // I can't quite figure this out right now ill do it later

        // Use saveGame to create a new TasteBudgeGame
        TasteBudgeManager.saveGame(
            TasteBudgeGame(
                roomCode = uniqueCode,  // if generate existing roomcode, regenerate
                gameStatus = GameStatus.WAITING,

                // set self as host,  add self to playerList
                hostUser = TasteBudgeManager.user,
                userList = mutableListOf(TasteBudgeManager.user)
            )
        )
    }

    // Function for joining game. Only used in RoomJoiningFragment
    fun joinGame(insertedCode : String, codeInput : EditText) {
        // Access Firebase to check if the insertedCode is valid
        Firebase.firestore.collection("TasteBudge")
            .document(insertedCode)
            .get()
            .addOnSuccessListener {
                val game = it?.toObject(TasteBudgeGame::class.java)
                if (game == null) {
                    codeInput.setError("Please enter valid game ID")
                } else {
                    // Correct room code
                    game.addUser(TasteBudgeManager.user)
                    TasteBudgeManager.saveGame(game)

                    // Should switch to another fragment
                }
            }
    }



    // Update data in this TasteBudgeGame
    fun fetchGame() {
        tasteBudgeGame.value?.apply {
            Firebase.firestore.collection("TasteBudge")
                .document(roomCode)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w("TasteBudgeManger", "Listen failed.", error)
                    }
                    val game = value?.toObject(TasteBudgeGame::class.java)
                    _tasteBudgeGame.postValue(game)
                }

        }
    }

}