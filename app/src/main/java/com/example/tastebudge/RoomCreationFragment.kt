package com.example.tastebudge

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import android.util.Log

class RoomCreationFragment : Fragment() {


    private var roomCodeText: TextView? = null
    private var qrCodeImage : ImageView? = null

    private var tasteBudgeGame : TasteBudgeGame? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_room_creation, container, false)

        val startRoomButton: Button = view.findViewById(R.id.buttonStartRoom)
        startRoomButton.setOnClickListener {

            // Change GameStatus to SUGGESTION
            TasteBudgeManager.fetchGame()   // make sure game is updated first
            tasteBudgeGame?.apply {
                this.gameStatus = GameStatus.SUGGESTION
                TasteBudgeManager.saveGame(this)
            }

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
        qrCodeImage = view.findViewById(R.id.qrCode)

        // Update UI
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            // Also update roomCode
            tasteBudgeGame?.apply {
                roomCodeText!!.text = roomCode
                displayQrCode(qrCodeImage!!, roomCode)
            }
        }
    }


    fun generateQrCode(roomCode : String, size : Int = 400) : Bitmap {
        try {
            val encoder = BarcodeEncoder()
            return encoder.encodeBitmap(roomCode, BarcodeFormat.QR_CODE, size, size)
        } catch (e: Exception) {
            e.printStackTrace()
            // Create a simple error bitmap
            return createBitmap(size, size).apply {
                eraseColor(Color.RED)
            }
        }
    }

    fun displayQrCode(imageView: ImageView, roomCode: String) {
        val qrBitmap = generateQrCode(roomCode)
        imageView.setImageBitmap(qrBitmap)
    }

}