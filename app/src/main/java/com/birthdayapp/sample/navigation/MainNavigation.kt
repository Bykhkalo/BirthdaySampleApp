package com.birthdayapp.sample.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.birthdayapp.sample.R
import com.birthdayapp.sample.data.model.PreviewType

class MainNavigation(private val navController: NavController) {

    fun back() {
        navController.navigateUp()
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStack(resId: Int, include: Boolean) {
        navController.popBackStack(resId, include)
    }

    fun birthdayOverview() {
        navController.navigate(
            ID_BIRTH_OVERVIEW,
            bundleOf(
                KEY_BIRTHDAY_OVERVIEW_PREVIEW_TYPE to PreviewType.entries.random()
            ),
            navOptions = NavOptions.Builder()
                .setEnterAnim(androidx.appcompat.R.anim.abc_fade_in)
                .setExitAnim(androidx.appcompat.R.anim.abc_fade_out)
                .setPopEnterAnim(androidx.appcompat.R.anim.abc_fade_in)
                .setPopExitAnim(androidx.appcompat.R.anim.abc_fade_out)
                .build()
        )
    }

    fun imagePickerDialog() {
        navController.navigate(ID_IMAGE_PICKER)
    }

    companion object {
        val ID_IMAGE_PICKER = R.id.imagePickerFragment
        val ID_BIRTH_OVERVIEW = R.id.birthdayFragment

        private const val KEY_BIRTHDAY_OVERVIEW_PREVIEW_TYPE = "previewType"
        private const val RESULT_KEY_IMAGE_PICKER_DIALOG = "DATA_KEY_IS_IMAGE_PICKED"
        private const val DATA_KEY_IS_IMAGE_PICKED = "RESULT_KEY_IMAGE_PICKER_DIALOG"

        fun Fragment.setImagePickerResultListener(resultAction: (Boolean) -> Unit) =
            setFragmentResultListener(RESULT_KEY_IMAGE_PICKER_DIALOG) { _, bundle ->
                resultAction(
                    bundle.getBoolean(DATA_KEY_IS_IMAGE_PICKED),
                )
            }

        fun Fragment.setImagePickerPermissionsResult(isPicked: Boolean) {
            setFragmentResult(
                RESULT_KEY_IMAGE_PICKER_DIALOG,
                bundleOf(
                    DATA_KEY_IS_IMAGE_PICKED to isPicked,
                )
            )
        }
    }
}
