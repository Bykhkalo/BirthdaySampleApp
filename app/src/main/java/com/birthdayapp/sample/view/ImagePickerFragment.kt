package com.birthdayapp.sample.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.birthdayapp.sample.R
import com.birthdayapp.sample.core.extensions.lifecycle.autoCleared
import com.birthdayapp.sample.core.extensions.safeObserveLiveEvent
import com.birthdayapp.sample.databinding.FragmentDialogImagePickerBinding
import com.birthdayapp.sample.navigation.MainNavigation.Companion.setImagePickerPermissionsResult
import com.birthdayapp.sample.viewmodel.ImagePickerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagePickerFragment : BottomSheetDialogFragment() {

    private var binding: FragmentDialogImagePickerBinding by autoCleared()
    private val viewModel: ImagePickerViewModel by viewModel()

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            if (isSaved) {
                viewModel.onCameraImagePicked()
            } else {
                viewModel.onImagePickerLauncherError()
            }
        }

    private val pickPictureLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri == null) {
                viewModel.onImagePickerLauncherError()
            } else {
                viewModel.onGalleryImagePicked(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogImagePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupViews() = with(binding) {
        cameraButton.setOnClickListener {
            val imageUri = viewModel.getCameraImageUri()

            if (imageUri == null) {
                viewModel.onImagePickerLauncherError()
            } else {
                takePictureLauncher.launch(imageUri)
            }
        }

        galleryButton.setOnClickListener {
            pickPictureLauncher.launch(IMAGE_PICKER_LAUNCHER_INPUT)
        }
    }

    private fun setupObservers() {
        safeObserveLiveEvent(viewModel.event, ::onEvent)
    }

    private fun onEvent(event: ImagePickerViewModel.Event) = when (event) {
        ImagePickerViewModel.Event.ImagePicked -> {
            setImagePickerPermissionsResult(
                isPicked = true
            )
            dismiss()
        }

        ImagePickerViewModel.Event.ImagePickingError -> {
            onImageNotPicked()
        }
    }

    private fun onImageNotPicked() {
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.error_image_not_picked),
            Toast.LENGTH_LONG
        ).show()

        setImagePickerPermissionsResult(
            isPicked = false
        )
        dismiss()
    }

    companion object {
        private const val IMAGE_PICKER_LAUNCHER_INPUT = "image/*"
    }
}
