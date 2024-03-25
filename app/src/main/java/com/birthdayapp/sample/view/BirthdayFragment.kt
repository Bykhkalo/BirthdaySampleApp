package com.birthdayapp.sample.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.birthdayapp.sample.R
import com.birthdayapp.sample.core.extensions.lifecycle.autoCleared
import com.birthdayapp.sample.core.extensions.observe
import com.birthdayapp.sample.core.extensions.shareImageResource
import com.birthdayapp.sample.core.extensions.takeScreenshot
import com.birthdayapp.sample.core.utils.DateUtils
import com.birthdayapp.sample.core.utils.DateUtils.MONTHS_IN_ONE_YEAR
import com.birthdayapp.sample.data.model.PreviewType
import com.birthdayapp.sample.data.model.Profile
import com.birthdayapp.sample.databinding.FragmentBirthdayBinding
import com.birthdayapp.sample.navigation.MainNavigation
import com.birthdayapp.sample.navigation.MainNavigation.Companion.setImagePickerResultListener
import com.birthdayapp.sample.viewmodel.BirthdayViewModel
import com.birthdayapp.sample.viewmodel.BirthdayViewModel.Companion.DIGIT_MAX_VALUE
import com.birthdayapp.sample.viewmodel.BirthdayViewModel.Companion.DIGIT_MIN_VALUE
import com.birthdayapp.sample.viewmodel.BirthdayViewModel.Companion.MAX_SUPPORTED_AGE_DISPLAY_VALUE
import com.birthdayapp.sample.viewmodel.BirthdayViewModel.Companion.MIN_SUPPORTED_AGE_DISPLAY_VALUE
import com.birthdayapp.sample.viewmodel.BirthdayViewModel.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class BirthdayFragment: Fragment(R.layout.fragment_birthday) {

    private var binding: FragmentBirthdayBinding by autoCleared()
    private val viewModel: BirthdayViewModel by viewModel()
    private val navigation: MainNavigation by lazy { MainNavigation(findNavController()) }
    private val args: BirthdayFragmentArgs by navArgs()

    private val previewType: PreviewType by lazy { args.previewType }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBirthdayBinding.bind(view)

        setupViews()
        setupObservers()
    }

    private fun setupViews() = with(binding) {
        setupBackground()
        setupProfileImage()

        arrowBackImage.setOnClickListener {
            navigation.back()
        }
        shareButton.setOnClickListener {
            val screenshotBitmap = makeScreenshot()
            val uri = viewModel.saveScreenshot(screenshotBitmap)
            requireView().shareImageResource(uri)
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
        profileImage.filePath = profile.photoPath
        title.text = resources.getString(R.string.birthday_title, profile.name)

        val birthDate = profile.birthDate
        val months = DateUtils.monthsFrom(birthDate)
        val years = DateUtils.yearsFrom(birthDate)

        val agePeriodText = when {
            months in 1 .. MONTHS_IN_ONE_YEAR -> resources.getQuantityString(R.plurals.month, months)
            months > MONTHS_IN_ONE_YEAR ->  resources.getQuantityString(R.plurals.years, years)
            else -> resources.getQuantityString(R.plurals.years, years)
        }

        subtitle.text = agePeriodText

        val ageValue = if (months > MONTHS_IN_ONE_YEAR) years else months
        val firstDigit = ageValue / 10
        val lastDigit = ageValue % 10
        val targetFirstDigit: Int
        val targetLastDigit: Int

        when {
            ageValue > MAX_SUPPORTED_AGE_DISPLAY_VALUE -> {
                targetFirstDigit = DIGIT_MAX_VALUE
                targetLastDigit = DIGIT_MAX_VALUE
            }
            ageValue < MIN_SUPPORTED_AGE_DISPLAY_VALUE -> {
                targetFirstDigit = DIGIT_MIN_VALUE
                targetLastDigit = DIGIT_MIN_VALUE
            }
            else -> {
                targetFirstDigit = firstDigit
                targetLastDigit = lastDigit
            }
        }

        ageDigit1.setImageResource(viewModel.getDigitDrawableResId(targetFirstDigit))
        ageDigit2.setImageResource(viewModel.getDigitDrawableResId(targetLastDigit))
        ageDigit1.isVisible = targetFirstDigit > 0
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

    private fun setupBackground() = with(binding) {
        val backgroundColorId: Int
        val backgroundImageResId: Int

        when (previewType) {
            PreviewType.YELLOW -> {
                backgroundColorId = R.color.yellow_bg
                backgroundImageResId = R.drawable.bg_yellow
            }
            PreviewType.GREEN -> {
                backgroundColorId = R.color.green_bg
                backgroundImageResId = R.drawable.bg_green
            }
            PreviewType.BLUE -> {
                backgroundColorId = R.color.blue_bg
                backgroundImageResId = R.drawable.bg_blue

            }
        }

        container.setBackgroundResource(backgroundColorId)
        requireActivity().window.apply {
            val color = ContextCompat.getColor(requireContext(), backgroundColorId)
            statusBarColor = color
            navigationBarColor = color
        }
        backgroundImage.setImageResource(backgroundImageResId)
    }

    private fun setupProfileImage() {
        binding.profileImage.apply {
            previewType = this@BirthdayFragment.previewType
            setOnCameraClickListener {
                navigation.imagePickerDialog()
            }
        }
    }

    private fun setIsShareFeatureIgnoredViewsVisible(isVisible: Boolean) = with(binding) {
        shareButton.isVisible = isVisible
        arrowBackImage.isVisible = isVisible
        profileImage.isCameraButtonVisible = isVisible
    }

    private fun makeScreenshot(): Bitmap {
        setIsShareFeatureIgnoredViewsVisible(false)
        val bitmap = binding.root.takeScreenshot()
        setIsShareFeatureIgnoredViewsVisible(true)
        return bitmap
    }
}
