package com.app.stockmanagement.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.app.stockmanagement.R
import com.app.stockmanagement.util.UseCaseHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

open class BaseCameraFragment : DialogFragment() {


    protected lateinit var cameraController: LifecycleCameraController
    protected var barcode: Barcode? = null
    private lateinit var pendingCallback: UseCaseHandler<Barcode>
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scanBarCode()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showSettingsRedirectDialog()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.camera_permission_is_required_to_scan),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraController = LifecycleCameraController(requireContext())
    }

    private fun showSettingsRedirectDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.perimssion_requred_body))
            .setPositiveButton(getString(R.string.settings)) { _, _ ->
                val intent =
                    android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
                        }
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showCameraRationaleDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.camera_permission_needed))
            .setMessage(getString(R.string.camera_permission_needed_body))
            .setPositiveButton(getString(R.string.allow)) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    protected fun checkForPermissionAndScanBarCode(callback: UseCaseHandler<Barcode>) {
        this.pendingCallback = callback
        val permission = Manifest.permission.CAMERA
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                scanBarCode()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                showCameraRationaleDialog()
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }


    private fun scanBarCode() {
        val context = requireContext()
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
            )
            .build()
        val barcodeScanner = BarcodeScanning.getClient(options)

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                CameraController.IMAGE_ANALYSIS,
                ContextCompat.getMainExecutor(context)
            ) { result ->
                val barcodeResults = result.getValue(barcodeScanner)
                if (!barcodeResults.isNullOrEmpty()) {
                    val barcode = barcodeResults[0]
                    Toast.makeText(context, getString(R.string.barcode_scanned), Toast.LENGTH_SHORT)
                        .show()
                    this.pendingCallback.onSuccess(barcode)
                }
            }
        )

        cameraController.bindToLifecycle(viewLifecycleOwner)

    }

}
