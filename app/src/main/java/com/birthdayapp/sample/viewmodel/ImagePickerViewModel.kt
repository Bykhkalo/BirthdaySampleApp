package com.birthdayapp.sample.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.birthdayapp.sample.core.delegate.ImageFilesHandlingDelegate
import com.birthdayapp.sample.core.extensions.LiveEvent
import com.birthdayapp.sample.core.extensions.MutableLiveEvent
import com.birthdayapp.sample.core.extensions.post
import com.birthdayapp.sample.domain.profile.usecase.SetProfilePhotoPathUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class ImagePickerViewModel(
    private val imageFilesHandlingDelegate: ImageFilesHandlingDelegate,
    private val setProfilePhotoPathUseCase: SetProfilePhotoPathUseCase,
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    private val mutableEvent = MutableLiveEvent<Event>()
    val event: LiveEvent<Event> = mutableEvent

    private val cameraImageFile: File? by lazy { imageFilesHandlingDelegate.createImageFile() }

    sealed class Event {
        data object ImagePicked : Event()
        data object ImagePickingError : Event()
    }

    fun onGalleryImagePicked(imageUri: Uri) = launch {
        setProfilePhotoPathUseCase.invoke(imageUri).fold(
            onSuccess = {
                mutableEvent.post(Event.ImagePicked)
            },
            onFailure = {
                mutableEvent.post(Event.ImagePickingError)
            }
        )
    }

    fun onCameraImagePicked() = launch {
        val filePath = cameraImageFile?.absolutePath
        if (filePath == null) {
            mutableEvent.post(Event.ImagePickingError)
            return@launch
        }

        setProfilePhotoPathUseCase.invoke(filePath).fold(
            onSuccess = {
                mutableEvent.post(Event.ImagePicked)
            },
            onFailure = {
                mutableEvent.post(Event.ImagePickingError)
            }
        )
    }

    fun getCameraImageUri(): Uri? {
        val file = cameraImageFile
        if (file == null) {
            mutableEvent.post(Event.ImagePickingError)
            return null
        }

        return imageFilesHandlingDelegate.getUriForCamera(file)
    }

    fun onImagePickerLauncherError() {
        mutableEvent.post(Event.ImagePickingError)
    }
}
