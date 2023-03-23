package com.muhsanapps.materialdesign

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.google.android.material.datepicker.MaterialDatePicker
import com.muhsanapps.materialdesign.databinding.ActivityMainBinding
import com.muhsanapps.materialdesign.receivers.AirplaneModeChangeReceiver
import com.muhsanapps.materialdesign.services.NewService
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var imageUri : Uri
    lateinit var receiver: AirplaneModeChangeReceiver

    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: PromptInfo
    //var mainLayout: ConstraintLayout? = null


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Material DatePicker Button
        binding.btnPickDate.setOnClickListener {
            showDatePickerDialog()
        }
        // BroadCast Receiver Code
//        receiver = AirplaneModeChangeReceiver()
//        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
//            registerReceiver(receiver, it)
//        }

        // BiometricManager Code
       /* val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Toast.makeText(
                this,
                "Device Doesn't have fingerprint",
                Toast.LENGTH_SHORT
            ).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Toast.makeText(
                this,
                "Not Working",
                Toast.LENGTH_SHORT
            ).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Toast.makeText(
                this,
                "No fingerprint Assigned",
                Toast.LENGTH_SHORT
            ).show()
        }

        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this@MainActivity, executor, object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
//                Toast.makeText(this@MainActivity, "login Success", Toast.LENGTH_SHORT).show()
//                binding.mainLinearLayout.visibility = View.VISIBLE
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }


        })

        promptInfo = PromptInfo.Builder().setTitle("Material Design")
            .setDescription("Use FingerPrint")
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
*/




        /*
        // BroadCast Receiver testing
        val intent : Intent = intent
        var action : String = intent.action.toString()
        var type : String = intent.type.toString()

       if (Intent.ACTION_SEND.equals(action) && type!= null) {
            binding.tvDate.text
        }*/

        //Chips material
        binding.chip0.setOnCloseIconClickListener {
            binding.chip0.visibility = View.GONE
            Toast.makeText(applicationContext, "Cancel", Toast.LENGTH_SHORT).show()
        }
        binding.checkFromChip.setOnClickListener {
//            binding.chip1.isCheckable = true
//            binding.chip1.isCheckable = false
//            binding.chip1.chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.purple_200))

            if (binding.checkFromChip.isCheckable) {
                binding.checkFromChip.chipBackgroundColor =
                    ColorStateList.valueOf(resources.getColor(R.color.checkColor))
            } else {

//                val red =  ContextCompat.getColor(this, R.color.UnCheckColor)
//                binding.checkFromChip.chipBackgroundColor = red
//                binding.checkFromChip.chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.UnCheckColor))
//                binding.checkFromChip.text = "Please Check"
            }
        }
        binding.chip3.setOnClickListener {
            binding.chip3.chipBackgroundColor =
                ColorStateList.valueOf(resources.getColor(R.color.UnCheckColor))
        }

        //Editor Box
        binding.btnEmail.setOnClickListener {
            process()
            Toast.makeText(this@MainActivity, "Successful Thank you!", Toast.LENGTH_SHORT).show()
        }

        //how to access folder in android 11 in android studio _ specific folder permission android
        binding.takePermission.setOnClickListener {
            getFolderPermission()
        }

        //Radio Buttons
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.home)
                Toast.makeText(applicationContext, "Home", Toast.LENGTH_SHORT).show()

            if (checkedId == R.id.office)
                Toast.makeText(applicationContext, "Office", Toast.LENGTH_SHORT).show()

            if (checkedId == R.id.school)
                Toast.makeText(applicationContext, "School", Toast.LENGTH_SHORT).show()

            if (checkedId == R.id.shop)
                Toast.makeText(applicationContext, "Shop", Toast.LENGTH_SHORT).show()

            if (checkedId == R.id.party)
                Toast.makeText(applicationContext, "Party", Toast.LENGTH_SHORT).show()

        }

        // Service Button Code
        binding.startService.setOnClickListener {
            startService(Intent(this, NewService::class.java))
        }
        binding.stopService.setOnClickListener {
            stopService(Intent(this, NewService::class.java))
        }

        val cities =
            arrayOf("Kamber", "Rawalpindi", "Islamabad", "Lahore", "Karachi")// Create Array
        val adapter = ArrayAdapter(this@MainActivity, R.layout.drop_down_item, cities)

        binding.filledExposed.setAdapter(adapter)

        binding.filledExposed.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(
                this@MainActivity,
                "You Selected ${position.toString()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.scrollingBackground.start()

        // Take ScreenShot Code
        //val bitmap = getBitmapFromView(binding.mainLinearLayout)

        //
        imageUri = createImageUri()
       // storeBitmap(bitmap)

        // btnTakeScreenShot
        binding.takeScreenShot.setOnClickListener {
            val bitmap = getBitmapFromView(binding.mainLinearLayout)
            storeBitmap(bitmap)

        }
    }

    // create background image to save bitmap step 1
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bg = view.background
        bg.draw(canvas) // Show error
        view.draw(canvas)
        return bitmap
    }

    // Ass file proivder path
    private fun createImageUri(): Uri {
        val image  = File(applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            applicationContext,
            "com.muhsanapps.materialdesign.fileProvider",
            image)
    }
    // Store image output Stream Asset
    private fun storeBitmap(bitmap: Bitmap){
        val outputStream = applicationContext.contentResolver.openOutputStream(imageUri)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream!!.close()
    }

    // Only android 11 Code write
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getFolderPermission() {
        val storageManager = application.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent = storageManager.primaryStorageVolume.createOpenDocumentTreeIntent()
        val targetDirectory = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
        var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI") as Uri
        var scheme = uri.toString()
        scheme = scheme.replace("/root/", "/tree/")
        scheme += "%3A$targetDirectory"
        uri = Uri.parse(scheme)
        intent.putExtra("android.provider.extra.INITIAL_URI", uri)
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
        startActivityForResult(intent, 1234)
    }

    // Only android 11 Code write
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val treeUri = data?.data
            binding.tvPath.text = treeUri.toString()

            if (treeUri != null) {
                contentResolver.takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val fileDoc = DocumentFile.fromTreeUri(applicationContext, treeUri)
            }

        }
    }

    //Editor Box
    private fun process() {
        // Timer COde
        Toast.makeText(
            applicationContext,
            binding.editTextTextPersonName.text.toString(),
            Toast.LENGTH_SHORT
        ).show()
        binding.editTextTextPersonName.setText("")
        binding.progressBar.visibility = View.INVISIBLE

//        val thread: Thread = object : Thread() {
//            override fun run() {
//                try {
//                    Toast.makeText(this@MainActivity, " By BY By Muhsan Javed", Toast.LENGTH_SHORT).show()
//                    binding.progressBar.visibility = View.GONE
//                    sleep(1000)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        thread.start()
    }

    //showDatePickerDialog Function
    private fun showDatePickerDialog() {
        val materialDatePicker: MaterialDatePicker<*> =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").build()

        materialDatePicker.addOnPositiveButtonClickListener {
            binding.tvDate.text = "" + materialDatePicker.headerText
        }
        materialDatePicker.show(supportFragmentManager, "TAG")
    }

    override fun onStop() {
        super.onStop()
       // unregisterReceiver(receiver)
    }
}