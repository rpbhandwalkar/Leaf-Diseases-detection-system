package com.example.leafprediction

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {


    val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
    private var Leaf: Bitmap? = null
    val IMAGE_REQUEST_CODE = 100

    // var image: Bitmap? = null
    var classifier: Classifier? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imgButton: ImageButton = findViewById(R.id.imgButton)
        val txtPrediction : EditText = findViewById(R.id.txtPrediction)
        txtPrediction.setKeyListener(null)

        Log.i("Main", "main activity")
        imgButton.isEnabled = false

        //TODO initialize the Classifier class object. This class will load the model and using its
        // method we will pass input to the model and get the output
        classifier = Classifier(assets, "leafpred.tflite", "leaf_labels.txt", 224)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            imgButton.isEnabled = true
        }
        //on button click
        imgButton.setOnClickListener {


            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            getResult.launch(intent)

        }

        val btnGallery: Button = findViewById(R.id.btnGallery)

        btnGallery.setOnClickListener {
            pickImageGallery()
        }
    }

    private fun checkPermissionForExternalStorage(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(this, "External storage permission needed", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun getTimeStamp(): String {
        val tsLong = System.currentTimeMillis() / 1000
        return tsLong.toString()
    }

    // Receiver
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()

        ) {
            Log.i("MainActivity", "Image clicked")
            if (it.resultCode == Activity.RESULT_OK) {
                Leaf = it.data?.extras?.get("data") as Bitmap

                genetratePrediction(Leaf!!)

                val ivDisplayImage: ImageView = findViewById(R.id.ivDisplayImage)
                ivDisplayImage.setImageBitmap(Leaf)

            }
            if (!checkPermissionForExternalStorage()) {
                requestPermissionForExternalStorage()
            }
            saveImage(Leaf)
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            val imgButton: ImageButton = findViewById(R.id.imgButton)
            imgButton.isEnabled = true
        }
    }

    private fun saveImage(image: Bitmap?): String {
        var savedImagePath: String? = null
        val imageFileName = "Leaf" + getTimeStamp() + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "/Leaf Prediction Image"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                image!!.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(savedImagePath)
            val contentUrl = Uri.fromFile(f)
            mediaScanIntent.data = contentUrl
            sendBroadcast(mediaScanIntent)
            Toast.makeText(
                this,
                "Image saved to Into folder: Leaf Prediction Image in gallery",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
        }

        return savedImagePath!!
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        openGallery.launch(intent)

    }

    // Receiver
    private val openGallery =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            Log.i("MainActivity", "Image selected")
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Uri? = it.data?.data

                var image: Bitmap = MediaStore.Images.Media.getBitmap(
                    getContentResolver(),
                    Uri.parse(data.toString())
                )


                val ivDisplayImage: ImageView = findViewById(R.id.ivDisplayImage)
                ivDisplayImage.setImageURI(data)
                genetratePrediction(image)
            }

        }


     fun genetratePrediction(bitmap: Bitmap) {

        val txtPrediction: EditText = findViewById(R.id.txtPrediction)
        var image = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        var res = classifier?.recognizeImage(image!!)
        for (r in res!!) {

            Toast.makeText(this, r.title + " with confidance: " + r.confidence, Toast.LENGTH_LONG)
                .show()
            txtPrediction.setText(r.title)

            break;

        }



        Log.i(
            "Prediction Body",
            "Exit-----------------------------------------------------------------------------------------------------"
        )
    }




}