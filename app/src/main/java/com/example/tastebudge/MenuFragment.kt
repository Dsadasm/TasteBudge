package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class MenuFragment : Fragment() {

    private var tasteBudgeGame : TasteBudgeGame? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_menu, container, false)

        // i am not fully sure how this works
        TasteBudgeData.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
        }


        val buttonCreateRoom: Button = view.findViewById(R.id.buttonCreateRoom)
        buttonCreateRoom.setOnClickListener {

            // Create a "player"
            TasteBudgeData.player = Player()

            // Create a new TasteBudgeGame
            TasteBudgeData.saveGame(
                TasteBudgeGame(
                    roomCode = (100000 .. 999999).random().toString(),
                    gameStatus = GameStatus.WAITING,

                    // set self as host,  add self to playerList
                    hostPlayer = TasteBudgeData.player,
                    playerList = mutableListOf(TasteBudgeData.player)
                )
            )

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

        return view
    }
}