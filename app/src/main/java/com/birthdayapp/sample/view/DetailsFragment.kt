package com.birthdayapp.sample.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.birthdayapp.sample.R
import com.birthdayapp.sample.core.extensions.lifecycle.autoCleared
import com.birthdayapp.sample.core.extensions.observe
import com.birthdayapp.sample.core.extensions.setMultiClickProtectedOnClickListener
import com.birthdayapp.sample.data.model.Profile
import com.birthdayapp.sample.databinding.FragmentDetailsBinding
import com.birthdayapp.sample.navigation.MainNavigation
import com.birthdayapp.sample.navigation.MainNavigation.Companion.setImagePickerResultListener
import com.birthdayapp.sample.viewmodel.DetailsViewModel
import com.birthdayapp.sample.viewmodel.DetailsViewModel.State
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding by autoCleared()
    private val viewModel: DetailsViewModel by viewModel()
    private val navigation: MainNavigation by lazy { MainNavigation(findNavController()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        setupViews()
        setupObservers()
    }

    private fun setupViews() = with(binding) {
        setupStatusAndNavigationBar()
        setupNameEdit()
        setupDatePicker()

        profileImage.setOnCameraClickListener {
            navigation.imagePickerDialog()
        }

        birthdayButton.setOnClickListener {
            navigation.birthdayOverview()
        }
    }

    private fun setupObservers() {
        observe(viewModel.state, ::onStateChanged)
        setImagePickerResultListener { isImagePicked ->
            if (isImagePicked) {
                viewModel.loadContent()
            }
        }
    }

    private fun onStateChanged(state: State) = when (state) {
        is State.Content -> {
            hideLoading()
            showContent(state.profile)
        }

        is State.Loading -> {
            showLoading()

        }

        is State.LoadingError -> {
            onLoadingError()
        }
    }

    private fun showContent(profile: Profile) = with(binding) {
        val name = profile.name
        val formattedDate: String? = viewModel.getFormattedBirthDate(profile.birthDate)

        if (nameEditText.text.toString() != name) {
            nameEditText.setTextKeepState(name)
        }

        dateEditText.setText(formattedDate)
        profileImage.filePath = profile.photoPath
        birthdayButton.isEnabled = !formattedDate.isNullOrEmpty() && name.isNotEmpty()
    }

    private fun showLoading() {
        binding.loadingView.isVisible = true
    }

    private fun hideLoading() {
        binding.loadingView.isVisible = false
    }

    private fun onLoadingError() {
        hideLoading()
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.error_profile_loading),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setupStatusAndNavigationBar() {
        requireActivity().window.apply {
            val color = ContextCompat.getColor(requireContext(), R.color.blue_primary)
            statusBarColor = color
            navigationBarColor = color
        }
    }

    private fun setupNameEdit() = with(binding) {
        val timeoutHandler = Handler(Looper.getMainLooper())
        val onTypingStoppedRunnable = Runnable {
            viewModel.setBabyName(nameEditText.text.toString())
        }

        nameEditText.doAfterTextChanged {
            timeoutHandler.removeCallbacks(onTypingStoppedRunnable)
            timeoutHandler.postDelayed(onTypingStoppedRunnable, TYPING_TIMEOUT_MILLIS)
        }
    }

    private fun setupDatePicker() {
        binding.dateEditText.apply {
            inputType = InputType.TYPE_NULL
            keyListener = null
            setMultiClickProtectedOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_birth_date)
                        .build().apply {
                            addOnPositiveButtonClickListener {
                                viewModel.setBabyBirthday(it)
                            }
                        }

                datePicker.show(childFragmentManager, TAG_DATE_PICKER_TRANSACTION)
            }
        }
    }

    companion object {
        private const val TYPING_TIMEOUT_MILLIS = 600L
        private const val TAG_DATE_PICKER_TRANSACTION = "TAG_DATE_PICKER_TRANSACTION"
    }
}
