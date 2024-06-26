package com.birthdayapp.sample.core.delegate

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.birthdayapp.sample.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageFilesHandlingDelegate(
    private val appContext: Context
) {

    fun getUriForCamera(file: File): Uri {
        return FileProvider.getUriForFile(appContext, FILE_PROVIDER_AUTHORITY, file)
    }

    fun createImageFile(): File? {
        val formattedDate: String = getTimestamp()
        val fileNamePrefix = appContext.getString(R.string.photo_file_name_prefix, formattedDate)
        val storageDir: File? = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            fileNamePrefix,
            FILE_NAME_SUFFIX,
            storageDir
        )
    }

    fun getFileFrom(imageUri: Uri?): File? {
        if (imageUri == null) return null
        val fileToStoreImage = createImageFile() ?: return null
        val inputStream = appContext.contentResolver.openInputStream(imageUri) ?: return null
        val outputStream = fileToStoreImage.outputStream()

        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return fileToStoreImage
    }

    fun saveScreenshot(bitmap: Bitmap): Uri? {
        try {
            val imageFile = createImageFile() ?: return null
            val outputStream = FileOutputStream(imageFile)

            bitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_MAX_QUALITY, outputStream)
            outputStream.flush()
            outputStream.close()

            return FileProvider.getUriForFile(
                appContext,
                FILE_PROVIDER_AUTHORITY,
                imageFile
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getTimestamp(): String {
        return SimpleDateFormat(FILE_NAME_DATE_FORMAT_PATTERN, Locale.getDefault()).format(Date())
    }

    companion object {
        private const val FILE_NAME_DATE_FORMAT_PATTERN = "yyyyMMdd_HHmmss"
        private const val FILE_PROVIDER_AUTHORITY = "com.birthdayapp.sample.fileprovider"
        private const val FILE_NAME_SUFFIX = ".jpg"

        private const val BITMAP_MAX_QUALITY = 100
    }
}
