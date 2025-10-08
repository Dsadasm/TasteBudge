package com.example.tastebudge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


// I am not entirely sure how this works, but it is related to networking
// Following this tutorial: https://www.youtube.com/watch?v=7Af2UEiKt80
object TasteBudgeData {
    private var _tasteBudgeGame : MutableLiveData<TasteBudgeGame> = MutableLiveData()
    var tasteBudgeGame : LiveData<TasteBudgeGame> = _tasteBudgeGame

    var player : Player = Player()


    // Using firebase to "do networking"

    // Save the TasteBudgeGame to FireBase (so others can see changes)
    fun saveGame(game : TasteBudgeGame) {
        _tasteBudgeGame.postValue(game)

        Firebase.firestore.collection("TasteBudge")
            .document(game.roomCode)
            .set(game)

    }

    // Update data in this TasteBudgeGame
    fun fetchGame() {
        tasteBudgeGame.value?.apply {
            Firebase.firestore.collection("TasteBudge")
                .document(roomCode)
                .addSnapshotListener{ value, error ->
                    val game = value?.toObject(TasteBudgeGame::class.java)
                    _tasteBudgeGame.postValue(game)
                }

        }
    }

}