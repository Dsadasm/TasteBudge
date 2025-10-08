package com.example.tastebudge

import androidx.lifecycle.ViewModel

// Class that contains all the data of a TasteBudge Game
class TasteBudgeGame (
    // The room code as an Int and String respectively
    // Generate room code on class creation
    var roomCode : String = "-1",

    var gameStatus : GameStatus = GameStatus.WAITING,

    var playerList : MutableList<Player> = mutableListOf(),
    var hostPlayer : Player? = null
) {
    fun addPlayer(player : Player) {
        this.playerList.add(player)
    }
}


// enum Class of different GameStatus
enum class GameStatus {
    WAITING,        // before Host presses Ready
    SUGGESTION,     // during Restaurant Suggestion phase
    MATCHING,       // during Matching Game
    FINISHED        // Winner determined
}