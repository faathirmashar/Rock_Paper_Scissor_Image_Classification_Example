package id.sharekom.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class MainActivity : AppCompatActivity(){
    var imageView: ImageView? = null
    var imageuri: Bitmap? = null
    var buclassify: Button? = null
    var classitext: TextView? = null

    var tfLiteHelper: TFLiteHelper? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById<View>(R.id.image) as ImageView
        buclassify = findViewById<View>(R.id.classify) as Button
        classitext = findViewById<View>(R.id.classifytext) as TextView
        tfLiteHelper = TFLiteHelper(this)
        tfLiteHelper!!.init()
        imageView!!.setOnClickListener(selectImageListener)
        buclassify!!.setOnClickListener(classifyImageListtener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            bitmap = data.extras?.get("data") as Bitmap
            try {
//                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageuri)
                imageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    var selectImageListener = View.OnClickListener {
//        val SELECT_TYPE = "image/*"
//        val SELECT_PICTURE = "Select Picture"
//        val intent = Intent()
//        intent.type = SELECT_TYPE
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, SELECT_PICTURE), 12)

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 12)
            }
        }
    }

    var classifyImageListtener = View.OnClickListener {
        if (bitmap != null) {
            tfLiteHelper!!.classifyImage(bitmap!!)
            setLabel(tfLiteHelper!!.showresult())
        }
    }

    fun setLabel(entries: List<String?>?) {
        classitext!!.text = ""
        for (entry in entries!!) {
            classitext!!.append(entry)
        }
    }
}