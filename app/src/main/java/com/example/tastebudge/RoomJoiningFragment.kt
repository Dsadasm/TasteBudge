package com.example.tastebudge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.zxing.integration.android.IntentIntegrator

class RoomJoiningFragment : Fragment() {

    private var codeInput : EditText? = null
    private var buttonJoinViaCode : Button? = null
    private var buttonJoinViaScan : Button? = null

    private val scanContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val scanResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
        handleScanResult(scanResult)
    }

    // Camera permission request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startQrScan()
        } else {
            Toast.makeText(requireContext(), "Camera permission is required to scan QR codes", Toast.LENGTH_LONG).show()
        }
    }

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

        buttonJoinViaCode = view.findViewById<Button>(R.id.buttonJoinViaCode)
        buttonJoinViaCode!!.setOnClickListener { joinRoom() }

        buttonJoinViaScan = view.findViewById<Button>(R.id.buttonJoinViaScan)
        buttonJoinViaScan!!.setOnClickListener { scanQRCode() }
    }

    fun scanQRCode() {
        if (hasCameraPermission()) {
            startQrScan()
        } else {
            requestCameraPermission()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startQrScan() {
        // Use IntentIntegrator.forSupportFragment for fragments
        val integrator = IntentIntegrator.forSupportFragment(this)

        // Configure the scanner
        integrator.setPrompt("Scan a room QR code")
        integrator.setOrientationLocked(true)
        integrator.setBeepEnabled(true)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setCameraId(0) // Use back camera
        integrator.setTimeout(30000) // 30 seconds timeout

        // Start the scan
        scanContract.launch(integrator.createScanIntent())
    }

    private fun handleScanResult(scanResult: com.google.zxing.integration.android.IntentResult?) {
        if (scanResult != null) {
            if (scanResult.contents == null) {
                // Scan was cancelled
                Toast.makeText(requireContext(), "Scan cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // Scan was successful - get scanned content
                val scannedContent = scanResult.contents

                // Is it a 6-digit code?
                if (scannedContent.matches(Regex("\\d{6}"))) {
                    // Yes
                    codeInput?.setText(scannedContent)
                    joinRoom()
                } else {
                    // No
                    Toast.makeText(requireContext(), "Invalid QR code.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // this fun should be called with "Join Room" button is clicked
    fun joinRoom() {
        val insertedCode: String = codeInput!!.text.toString()

        // Validate the code is 6 digits
        if (insertedCode.matches(Regex("\\d{6}"))) {
            TasteBudgeManager.joinGame(insertedCode, codeInput!!)
        } else {
            Toast.makeText(requireContext(), "Please enter a valid 6-digit room code", Toast.LENGTH_SHORT).show()
        }
    }
}