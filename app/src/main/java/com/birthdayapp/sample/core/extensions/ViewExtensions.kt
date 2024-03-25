package com.birthdayapp.sample.core.extensions

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.birthdayapp.sample.R


private const val DELAY_MILLIS = 150L
private const val MIME_TYPE_IMAGE = "image/jpeg"

fun View.setMultiClickProtectedOnClickListener(action: (() -> Unit)?) {
    if (action == null) {
        setOnClickListener(null)
        return
    }

    val reEnableButtonRunnable = Runnable {
        isEnabled = true
    }

    setOnClickListener {
        isEnabled = false
        action.invoke()
        postDelayed(reEnableButtonRunnable, DELAY_MILLIS)
    }
}

fun View.takeScreenshot(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    if (background != null) {
        background.draw(canvas)
    } else {
        canvas.drawColor(Color.WHITE)
    }

    draw(canvas)
    return bitmap
}

fun View.shareImageResource(uri: Uri?) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = MIME_TYPE_IMAGE
        putExtra(Intent.EXTRA_STREAM, uri)
    }

    startActivity(
        context,
        Intent.createChooser(intent, resources.getString(R.string.intent_chooser_text)),
        null
    )
}
