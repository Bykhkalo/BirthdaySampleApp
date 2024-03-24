package com.birthdayapp.sample.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.birthdayapp.sample.R
import com.birthdayapp.sample.databinding.ViewProfileImageBinding
import com.birthdayapp.sample.data.model.PreviewType
import com.bumptech.glide.Glide
import java.io.File

class ProfileImageView(context: Context, attributes: AttributeSet) :
    ConstraintLayout(context, attributes) {

    private var binding: ViewProfileImageBinding =
        ViewProfileImageBinding.inflate(LayoutInflater.from(context), this)

    private var previewType: PreviewType = PreviewType.BLUE
    private var onCameraButtonClick: (() -> Unit)? = null

    var filePath: String = ""
        set(value) {
            field = value
            loadImageFrom(value)
        }

    private val errorDrawable: Drawable?
        get() {
            val resId = when (previewType) {
                PreviewType.YELLOW -> R.drawable.default_place_holder_yellow
                PreviewType.GREEN ->  R.drawable.default_place_holder_green
                PreviewType.BLUE ->  R.drawable.default_place_holder_blue
            }

            return ContextCompat.getDrawable(context, resId)
        }

    init {
        setPreviewType(previewType)

        val stroke = resources.getDimension(R.dimen.profile_image_default_stroke).toInt()

        // Obtain the view size for radius calculation
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {

                // Set radius for proper camera button placement
                val layoutParams = binding.cameraButton.layoutParams as LayoutParams
                layoutParams.circleRadius = (width - stroke) / 2
                binding.cameraButton.layoutParams = layoutParams

                viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })

        binding.cameraButton.setOnClickListener {
            onCameraButtonClick?.invoke()
        }
    }

    fun setOnCameraClickListener(onClick: (() -> Unit)?) {
        this.onCameraButtonClick = onClick
    }

    private fun loadImageFrom(filePath: String) = with(binding.userImage) {
        val file = File(filePath)
        Glide.with(this)
            .load(file)
            .into(this)
            .onLoadFailed(errorDrawable)
    }

    fun setPreviewType(previewType: PreviewType) {
        when (previewType) {
            PreviewType.YELLOW -> {
                binding.apply {
                    userImage.setImageResource(R.drawable.default_place_holder_yellow)
                    cameraButton.setImageResource(R.drawable.camera_icon_yellow)
                    userImage.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.yellow_primary
                        )
                    )
                }
            }
            PreviewType.GREEN -> {
                binding.apply {
                    userImage.setImageResource(R.drawable.default_place_holder_green)
                    cameraButton.setImageResource(R.drawable.camera_icon_green)
                    userImage.strokeColor =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_primary))
                }
            }
            PreviewType.BLUE -> {
                binding.apply {
                    userImage.setImageResource(R.drawable.default_place_holder_blue)
                    cameraButton.setImageResource(R.drawable.camera_icon_blue)
                    userImage.strokeColor =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.blue_primary))
                }
            }
        }
    }
}