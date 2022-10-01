package com.muhsanapps.materialdesign

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.google.android.material.datepicker.MaterialDatePicker
import com.muhsanapps.materialdesign.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Material DatePicker Button
        binding.btnPickDate.setOnClickListener {
            showDatePickerDialog();
        }

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
        binding.chip1.setOnClickListener {
//            binding.chip1.isCheckable = true
//            binding.chip1.isCheckable = false
//            binding.chip1.chipBackgroundColor = ColorStateList.valueOf(resources.getColor(R.color.purple_200))

            if (binding.chip1.isCheckable == true) {
                binding.chip1.chipBackgroundColor =
                    ColorStateList.valueOf(resources.getColor(R.color.purple_200))
            } else if (binding.chip1.isCheckable == false) {
                binding.chip1.chipBackgroundColor =
                    ColorStateList.valueOf(resources.getColor(R.color.white))
            }
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
        binding.radioGroup.setOnCheckedChangeListener{ group, checkedId->
            if (checkedId == R.id.home){ Toast.makeText(applicationContext, "Home", Toast.LENGTH_SHORT).show() }
            if (checkedId == R.id.office){Toast.makeText(applicationContext, "Office", Toast.LENGTH_SHORT).show()}
            if (checkedId == R.id.school){Toast.makeText(applicationContext, "School", Toast.LENGTH_SHORT).show()}
            if (checkedId == R.id.shop){Toast.makeText(applicationContext, "Shop", Toast.LENGTH_SHORT).show()}
            if (checkedId == R.id.party){Toast.makeText(applicationContext, "Party", Toast.LENGTH_SHORT).show()}
        }

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
            binding.tvDate.text = treeUri.toString()

            if (treeUri != null) {
                contentResolver.takePersistableUriPermission(
                    treeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val fileDoc = DocumentFile.fromTreeUri(applicationContext, treeUri)
            }

        }
    }

    //Editor Box
    private fun process() {
        Toast.makeText(
            applicationContext,
            binding.editTextTextPersonName.text.toString(),
            Toast.LENGTH_SHORT
        ).show()
        binding.editTextTextPersonName.setText("")
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
}