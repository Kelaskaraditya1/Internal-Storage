package com.starkindustries.internalstorage.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.starkindustries.internalstorage.R
import com.starkindustries.internalstorage.databinding.ActivityPdfViewerBinding

class PdfViewerActivity : AppCompatActivity() {
    lateinit var binding:ActivityPdfViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pdf_viewer)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_pdf_viewer)

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*") // Set the MIME type to audio files
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val launchpdf = registerForActivityResult(ActivityResultContracts.GetContent())
        {
                uri->
            uri.let {
                binding.pdfView.fromUri(it)
                    .spacing(12)
                    .defaultPage(0)
                    .enableAnnotationRendering(false)
                    .enableDoubletap(true)
                    .load()
                binding.pdfView.fitToWidth()
                binding.pdfView.useBestQuality(true)
                Toast.makeText(applicationContext, "filename:"+getFileName(it!!), Toast.LENGTH_SHORT).show()
            }
        }
        binding.openPdfButton.setOnClickListener()
        {
            launchpdf.launch("application/pdf")

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && it.moveToFirst()) {
                    result = it.getString(nameIndex)
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK)
        {
            if(requestCode==101)
            {

            }
        }
    }
}